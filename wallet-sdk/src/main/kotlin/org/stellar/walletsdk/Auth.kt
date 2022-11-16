package org.stellar.walletsdk

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.util.GsonUtils
import org.stellar.walletsdk.util.OkHttpUtils

/**
 * Authenticate to an external server using
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md).
 *
 * @property accountAddress Stellar address of the account authenticating
 * @property webAuthEndpoint Authentication endpoint URL
 * @property homeDomain Domain hosting stellar.toml (
 * [SEP-1](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)) file
 * containing `WEB_AUTH_ENDPOINT` URL and `SIGNING_KEY`
 * @property memoId optional memo ID to distinguish the account
 * @property clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
 * @property networkPassPhrase Stellar network passphrase
 * @property walletSigner interface to define wallet client and domain (if using `clientDomain`)
 * signing methods
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class Auth(
  private val accountAddress: String,
  private val webAuthEndpoint: String,
  private val homeDomain: String,
  private val memoId: String? = null,
  private val clientDomain: String? = null,
  private val networkPassPhrase: String = Network.TESTNET.toString(),
  private val walletSigner: WalletSigner,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  private val gson = GsonUtils.instance!!

  data class ChallengeResponse(val transaction: String, val network_passphrase: String)
  data class AuthToken(val token: String)
  data class AuthTransaction(val transaction: String)

  /**
   * Authenticates to an external server.
   *
   * @return authentication token (JWT)
   *
   * @throws [InvalidMemoIdException] when memo ID is not valid
   * @throws [ClientDomainWithMemoException] when both client domain and memo ID provided
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [MissingTransactionException] when request JSON response does not contain `transaction`
   * @throws [NetworkMismatchException] when request JSON response network passphrase does not match
   * provided network passphrase
   * @throws [MissingTokenException] when request JSON response does not contain `token`
   */
  suspend fun authenticate(): String {
    val challengeTxn = challenge()
    val signedTxn = sign(challengeTxn)
    return getToken(signedTxn)
  }

  /**
   * Request transaction challenge from the auth endpoint.
   *
   * @return transaction as Base64 encoded TransactionEnvelope XDR string and network passphrase
   * from the auth endpoint
   *
   * @throws [InvalidMemoIdException] when memo ID is not valid
   * @throws [ClientDomainWithMemoException] when both client domain and memo ID provided
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [MissingTransactionException] when request JSON response does not contain `transaction`
   * @throws [NetworkMismatchException] when request JSON response network passphrase does not match
   * provided network passphrase
   */
  private suspend fun challenge(): ChallengeResponse {
    val endpoint = webAuthEndpoint.toHttpUrl()
    val authURL = HttpUrl.Builder().scheme("https").host(endpoint.host)

    // Add path segments, if there are any
    endpoint.pathSegments.forEach { authURL.addPathSegment(it) }

    // Add required query params
    authURL
      .addQueryParameter("account", accountAddress)
      .addQueryParameter("home_domain", homeDomain)

    if (!memoId.isNullOrBlank()) {
      if (memoId.toInt() < 0) {
        throw InvalidMemoIdException()
      }

      authURL.addQueryParameter("memo", memoId)
    }

    if (!clientDomain.isNullOrBlank()) {
      if (!memoId.isNullOrBlank()) {
        throw ClientDomainWithMemoException()
      }

      authURL.addQueryParameter("client_domain", clientDomain)
    }

    authURL.build()

    val request = OkHttpUtils.buildStringGetRequest(authURL.toString())

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) {
        throw NetworkRequestFailedException(response)
      }

      val jsonResponse: ChallengeResponse =
        gson.fromJson(response.body!!.charStream(), ChallengeResponse::class.java)

      if (jsonResponse.transaction.isBlank()) {
        throw MissingTransactionException()
      }

      if (jsonResponse.network_passphrase != networkPassPhrase) {
        throw NetworkMismatchException()
      }

      jsonResponse
    }
  }

  /**
   * Sign transaction with client account and, optionally, domain account using [WalletSigner]
   * methods.
   *
   * @param challengeResponse challenge transaction and network passphrase returned from the auth
   * endpoint
   *
   * @return signed transaction
   */
  private fun sign(challengeResponse: ChallengeResponse): Transaction {
    var challengeTxn =
      Transaction.fromEnvelopeXdr(
        challengeResponse.transaction,
        Network(challengeResponse.network_passphrase)
      ) as Transaction

    val clientDomainOperation =
      challengeTxn.operations.firstOrNull {
        it.toXdr().body?.manageDataOp?.dataName?.string64.toString() == "client_domain"
      }

    if (clientDomainOperation != null) {
      challengeTxn =
        walletSigner.signWithDomainAccount(
          challengeResponse.transaction,
          challengeResponse.network_passphrase
        )
    }

    walletSigner.signWithClientAccount(challengeTxn)

    return challengeTxn
  }

  /**
   * Send signed transaction to the auth endpoint to get JWT token.
   *
   * @param signedTransaction signed transaction
   *
   * @return transaction as Base64 encoded TransactionEnvelope XDR string
   *
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [MissingTokenException] when request JSON response does not contain `token`
   */
  private suspend fun getToken(signedTransaction: Transaction): String {
    val signedChallengeTxnXdr = signedTransaction.toEnvelopeXdrBase64()
    val tokenRequestParams = AuthTransaction(signedChallengeTxnXdr)
    val tokenRequest = OkHttpUtils.buildJsonPostRequest(webAuthEndpoint, tokenRequestParams)

    httpClient.newCall(tokenRequest).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      val jsonResponse: AuthToken =
        gson.fromJson(response.body!!.charStream(), AuthToken::class.java)

      if (jsonResponse.token.isBlank()) {
        throw MissingTokenException()
      }

      return jsonResponse.token
    }
  }
}
