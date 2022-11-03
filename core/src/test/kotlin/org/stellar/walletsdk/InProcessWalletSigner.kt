package org.stellar.walletsdk

import java.io.IOException
import okhttp3.OkHttpClient
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.util.GsonUtils
import org.stellar.walletsdk.util.OkHttpUtils

class InProcessWalletSigner : WalletSigner {
  override fun signWithClientAccount(txn: Transaction): Transaction {
    txn.sign(KeyPair.fromSecretSeed(ADDRESS_ACTIVE_SECRET))
    return txn
  }

  override fun signWithDomainAccount(
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
}
