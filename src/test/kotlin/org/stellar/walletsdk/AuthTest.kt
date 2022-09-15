package org.stellar.walletsdk

import java.io.IOException
import kotlin.test.assertNotNull
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.util.GsonUtils
import org.stellar.walletsdk.util.OkHttpUtils

internal class AuthTest {
  private fun signTxn(txn: Transaction): Transaction {
    txn.sign(KeyPair.fromSecretSeed(ADDRESS_ACTIVE_SECRET))
    return txn
  }

  // Sign client domain transaction on Demo Wallet backend
  private fun signTxClientDomain(
    transactionString: String,
    networkPassPhrase: String
  ): Transaction {
    val gson = GsonUtils.instance!!
    val okHttpClient = OkHttpClient()

    val clientDomainRequestParams = Auth.ChallengeResponse(transactionString, networkPassPhrase)

    val clientDomainRequest =
      OkHttpUtils.buildJsonPostRequest(AUTH_CLIENT_DOMAIN_URL, clientDomainRequestParams)

    okHttpClient.newCall(clientDomainRequest).execute().use { response ->
      if (!response.isSuccessful) throw IOException("Request failed: $response")

      val jsonResponse: Auth.ChallengeResponse =
        gson.fromJson(response.body!!.charStream(), Auth.ChallengeResponse::class.java)

      if (jsonResponse.transaction.isBlank()) {
        throw Exception("The response did not contain a transaction")
      }

      return Transaction.fromEnvelopeXdr(
        jsonResponse.transaction,
        Network(jsonResponse.network_passphrase)
      ) as Transaction
    }
  }

  // NOTE: making real network calls for now. Mocking SEP-10 server is tricky, so we will need to
  // spend more time on this later.

  @Test
  fun `auth token`() {
    val authToken =
      Auth(
          ADDRESS_ACTIVE,
          AUTH_ENDPOINT,
          AUTH_HOME_DOMAIN,
          signTransaction = ::signTxn,
        )
        .authenticate()

    assertNotNull(authToken)
  }

  @Test
  fun `auth token with client domain`() {
    val authToken =
      Auth(
          ADDRESS_ACTIVE,
          AUTH_ENDPOINT,
          AUTH_HOME_DOMAIN,
          signTransaction = ::signTxn,
          clientDomain = AUTH_CLIENT_DOMAIN,
          signClientDomainTransaction = ::signTxClientDomain
        )
        .authenticate()

    assertNotNull(authToken)
  }

  @Test
  fun `throw error if there is clientDomain without signClientDomainTransaction callback`() {
    assertThrows<Exception> {
      Auth(
          ADDRESS_ACTIVE,
          AUTH_ENDPOINT,
          AUTH_HOME_DOMAIN,
          signTransaction = ::signTxn,
          clientDomain = AUTH_CLIENT_DOMAIN,
        )
        .authenticate()
    }
  }
}
