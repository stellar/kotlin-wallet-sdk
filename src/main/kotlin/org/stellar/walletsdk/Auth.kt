package org.stellar.walletsdk

import java.io.IOException
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.util.GsonUtils
import org.stellar.walletsdk.util.OkHttpUtils

class Auth(
  private val accountAddress: String,
  private val authEndpoint: String,
  private val homeDomain: String,
  private val clientDomain: String? = null,
  private val networkPassPhrase: String = Network.TESTNET.toString(),
  private val walletSigner: WalletSigner
) {
  private val gson = GsonUtils.instance!!
  private val okHttpClient = OkHttpClient()

  data class ChallengeResponse(val transaction: String, val network_passphrase: String)
  data class AuthToken(val token: String)
  data class AuthTransaction(val transaction: String)

  fun authenticate(): String {
    val challengeTxn = challenge()
    val signedTxn = sign(challengeTxn)
    return getToken(signedTxn)
  }

  private fun challenge(): ChallengeResponse {
    val endpoint = authEndpoint.toHttpUrl()
    val authURL = HttpUrl.Builder().scheme("https").host(endpoint.host)

    // Add path segments, if there are any
    endpoint.pathSegments.forEach { authURL.addPathSegment(it) }

    // Add required query params
    authURL
      .addQueryParameter("account", accountAddress)
      .addQueryParameter("home_domain", homeDomain)

    if (!clientDomain.isNullOrBlank()) {
      authURL.addQueryParameter("client_domain", clientDomain)
    }

    authURL.build()

    val request = OkHttpUtils.buildStringGetRequest(authURL.toString())

    okHttpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw IOException("Request failed: $response")

      val jsonResponse: ChallengeResponse =
        gson.fromJson(response.body!!.charStream(), ChallengeResponse::class.java)

      if (jsonResponse.transaction.isBlank()) {
        throw Exception("The response did not contain a transaction")
      }

      if (jsonResponse.network_passphrase != networkPassPhrase) {
        throw Exception("Networks don't match")
      }

      return jsonResponse
    }
  }

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

  private fun getToken(signedTransaction: Transaction): String {
    val signedChallengeTxnXdr = signedTransaction.toEnvelopeXdrBase64()
    val tokenRequestParams = AuthTransaction(signedChallengeTxnXdr)
    val tokenRequest = OkHttpUtils.buildJsonPostRequest(authEndpoint, tokenRequestParams)

    okHttpClient.newCall(tokenRequest).execute().use { response ->
      if (!response.isSuccessful) throw IOException("Request failed: $response")

      val jsonResponse: AuthToken =
        gson.fromJson(response.body!!.charStream(), AuthToken::class.java)

      if (jsonResponse.token.isBlank()) {
        throw Exception("Token was not returned")
      }

      return jsonResponse.token
    }
  }
}
