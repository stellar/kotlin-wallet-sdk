package org.stellar.walletsdk.auth

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.WalletSigner
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.OkHttpUtils
import org.stellar.walletsdk.util.SchemeUtil
import org.stellar.walletsdk.util.toJson

/**
 * Authenticate to an external server using
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md).
 *
 * @property webAuthEndpoint Authentication endpoint URL
 * @property homeDomain Domain hosting stellar.toml (
 * [SEP-1](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)) file
 * containing `WEB_AUTH_ENDPOINT` URL and `SIGNING_KEY`
 * @property network Stellar network
 * @property defaultSigner interface to define wallet client and domain (if using `clientDomain`)
 * signing methods
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class Auth(
  private val webAuthEndpoint: String,
  private val homeDomain: String,
  private val defaultSigner: WalletSigner,
  private val network: Network = Network.TESTNET,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  /**
   * Authenticates to an external server.
   *
   * @param accountAddress Stellar address of the account authenticating
   * @param walletSigner overriding [Auth.defaultSigner] to use in this authentication
   * @param memoId optional memo ID to distinguish the account
   * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
   *
   * @return authentication token (JWT)
   *
   * @throws [ValidationException] when some of the request arguments are not valid
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [InvalidResponseException] when JSON response is malformed
   */
  suspend fun authenticate(
    accountAddress: String,
    walletSigner: WalletSigner? = null,
    memoId: String? = null,
    clientDomain: String? = null
  ): String {
    val challengeTxn = challenge(accountAddress, memoId, clientDomain)
    val signedTxn = sign(challengeTxn, walletSigner ?: this.defaultSigner)
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
   * @throws [InvalidResponseException] when JSON response is malformed
   */
  private suspend fun challenge(
    accountAddress: String,
    memoId: String? = null,
    clientDomain: String? = null
  ): ChallengeResponse {
    val endpoint = webAuthEndpoint.toHttpUrl()
    val authURL = endpoint.newBuilder().scheme(SchemeUtil.scheme)

    // Add required query params
    authURL
      .addQueryParameter("account", accountAddress)
      .addQueryParameter("home_domain", homeDomain)

    if (!memoId.isNullOrBlank()) {
      if (memoId.toInt() < 0) {
        throw InvalidMemoIdException
      }

      authURL.addQueryParameter("memo", memoId)
    }

    if (!clientDomain.isNullOrBlank()) {
      if (!memoId.isNullOrBlank()) {
        throw ClientDomainWithMemoException
      }

      authURL.addQueryParameter("client_domain", clientDomain)
    }

    authURL.build()

    val request = OkHttpUtils.buildStringGetRequest(authURL.toString())

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) {
        throw NetworkRequestFailedException(response)
      }

      val jsonResponse: ChallengeResponse = response.toJson()

      if (jsonResponse.transaction.isBlank()) {
        throw MissingTransactionException
      }

      if (jsonResponse.network_passphrase != network.networkPassphrase) {
        throw NetworkMismatchException
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
  private fun sign(challengeResponse: ChallengeResponse, walletSigner: WalletSigner): Transaction {
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
    val tokenRequest = OkHttpUtils.makePostRequest(webAuthEndpoint, tokenRequestParams)

    httpClient.newCall(tokenRequest).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      val jsonResponse: AuthToken = response.toJson()

      if (jsonResponse.token.isBlank()) {
        throw MissingTokenException
      }

      return jsonResponse.token
    }
  }
}
