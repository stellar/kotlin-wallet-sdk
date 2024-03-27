package org.stellar.walletsdk.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.math.BigInteger
import java.util.*
import kotlinx.datetime.Clock
import mu.KotlinLogging
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.util.Util.authGetStringToken
import org.stellar.walletsdk.util.Util.postJson

private val log = KotlinLogging.logger {}

/**
 * Authenticate to an external server using
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md).
 */
class Sep10
internal constructor(
  private val cfg: Config,
  private val webAuthEndpoint: String,
  private val homeDomain: String,
  private val httpClient: HttpClient
) {
  /**
   * Authenticates to an external server.
   *
   * @param accountAddress Stellar address of the account authenticating
   * @param walletSigner overriding [Sep10.defaultSigner] to use in this authentication
   * @param memoId optional memo ID to distinguish the account
   * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
   * @return authentication token (JWT)
   * @throws [ValidationException] when some of the request arguments are not valid
   * @throws [InvalidResponseException] when JSON response is malformed
   */
  suspend fun authenticate(
    accountAddress: AccountKeyPair,
    walletSigner: WalletSigner? = null,
    memoId: ULong? = null,
    clientDomain: String? = null,
    authHeaderSigner: AuthHeaderSigner? = null
  ): AuthToken {
    val challengeTxn =
      challenge(
        accountAddress,
        memoId?.toString(),
        clientDomain ?: cfg.app.defaultClientDomain,
        authHeaderSigner
      )
    val signedTxn = sign(accountAddress, challengeTxn, walletSigner ?: cfg.app.defaultSigner)
    return getToken(signedTxn)
  }

  suspend fun authenticateBigInt(
    accountAddress: AccountKeyPair,
    walletSigner: WalletSigner? = null,
    memoId: BigInteger? = null,
    clientDomain: String? = null,
    authHeaderSigner: AuthHeaderSigner? = null
  ): AuthToken {
    val challengeTxn =
      challenge(
        accountAddress,
        memoId?.toString(),
        clientDomain ?: cfg.app.defaultClientDomain,
        authHeaderSigner
      )
    val signedTxn = sign(accountAddress, challengeTxn, walletSigner ?: cfg.app.defaultSigner)
    return getToken(signedTxn)
  }

  /**
   * Request transaction challenge from the auth endpoint.
   *
   * @return transaction as Base64 encoded TransactionEnvelope XDR string and network passphrase
   * from the auth endpoint
   * @throws [InvalidMemoIdException] when memo ID is not valid
   * @throws [ClientDomainWithMemoException] when both client domain and memo ID provided
   * @throws [InvalidResponseException] when JSON response is malformed
   */
  @Suppress("ThrowsCount")
  private suspend fun challenge(
    account: AccountKeyPair,
    memoId: String? = null,
    clientDomain: String? = null,
    authHeaderSigner: AuthHeaderSigner?
  ): ChallengeResponse {
    val url = URLBuilder(webAuthEndpoint)

    // Add required query params
    val parameters = mutableMapOf<String, String>()
    parameters["account"] = account.address
    parameters["home_domain"] = homeDomain

    if (memoId != null) {
      parameters["memo"] = memoId
    }

    if (!clientDomain.isNullOrBlank()) {
      parameters["client_domain"] = clientDomain
    }

    parameters.forEach { url.parameters.append(it.key, it.value) }

    log.debug {
      "Challenge request: account = $account, memo = $memoId, client_domain = $clientDomain"
    }

    val token =
      createAuthSignToken(account, webAuthEndpoint, parameters, clientDomain, authHeaderSigner)

    val jsonResponse =
      httpClient.authGetStringToken<ChallengeResponse>(url.build().toString(), token)

    if (jsonResponse.transaction.isBlank()) {
      throw MissingTransactionException
    }

    if (jsonResponse.networkPassphrase != cfg.stellar.network.networkPassphrase) {
      throw NetworkMismatchException
    }

    return jsonResponse
  }

  /**
   * Sign transaction with client account and, optionally, domain account using [WalletSigner]
   * methods.
   *
   * @param challengeResponse challenge transaction and network passphrase returned from the auth
   * endpoint
   * @return signed transaction
   */
  private suspend fun sign(
    account: AccountKeyPair,
    challengeResponse: ChallengeResponse,
    walletSigner: WalletSigner
  ): Transaction {
    var challengeTxn =
      Transaction.fromEnvelopeXdr(
        challengeResponse.transaction,
        Network(challengeResponse.networkPassphrase)
      ) as Transaction

    val clientDomainOperation =
      challengeTxn.operations.firstOrNull {
        it.toXdr().body?.manageDataOp?.dataName?.string64.toString() == "client_domain"
      }

    if (clientDomainOperation != null) {
      log.debug { "Authenticating with client domain" }

      challengeTxn =
        walletSigner.signWithDomainAccount(
          challengeResponse.transaction,
          challengeResponse.networkPassphrase,
          account
        )
    }

    walletSigner.signWithClientAccount(challengeTxn, account)

    return challengeTxn
  }

  /**
   * Send signed transaction to the auth endpoint to get JWT token.
   *
   * @param signedTransaction signed transaction
   * @return transaction as Base64 encoded TransactionEnvelope XDR string
   * @throws [MissingTokenException] when request JSON response does not contain `token`
   */
  private suspend fun getToken(signedTransaction: Transaction): AuthToken {
    val signedChallengeTxnXdr = signedTransaction.toEnvelopeXdrBase64()
    val tokenRequestParams = AuthTransaction(signedChallengeTxnXdr)

    val resp: AuthTokenResponse = httpClient.postJson(webAuthEndpoint, tokenRequestParams)

    if (resp.token.isBlank()) {
      throw MissingTokenException
    }

    val token = AuthToken.from(resp.token)

    if (token.expiresAt < Clock.System.now()) {
      throw ValidationException(
        "Auth token has already expired. Expiration time: ${token.expiresAt}"
      )
    }

    return token
  }
}

fun createAuthSignToken(
  account: AccountKeyPair,
  webAuthEndpoint: String,
  parameters: Map<String, String>,
  clientDomain: String? = null,
  authHeaderSigner: AuthHeaderSigner? = null
): String? {
  if (authHeaderSigner != null) {
    // For noncustodial issuer is unknown -> comes from SEP-1 toml file
    val issuer = if (clientDomain == null) account else null
    val claims = parameters.toMutableMap()
    claims["aud"] = webAuthEndpoint
    return authHeaderSigner.createToken(claims, clientDomain, issuer)
  }
  return null
}
