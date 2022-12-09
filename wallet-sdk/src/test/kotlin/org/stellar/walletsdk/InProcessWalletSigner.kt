package org.stellar.walletsdk

import java.io.IOException
import okhttp3.OkHttpClient
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.auth.ChallengeResponse
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.util.OkHttpUtils

class InProcessWalletSigner : WalletSigner {
  override fun signWithClientAccount(txn: Transaction, address: String): Transaction {
    txn.sign(KeyPair.fromSecretSeed(ADDRESS_ACTIVE_SECRET))
    return txn
  }

  override fun signWithDomainAccount(
    transactionString: String,
    networkPassPhrase: String,
    address: String
  ): Transaction {
    val okHttpClient = OkHttpClient()

    val clientDomainRequestParams = ChallengeResponse(transactionString, networkPassPhrase)

    val clientDomainRequest =
      OkHttpUtils.makePostRequest(AUTH_CLIENT_DOMAIN_URL, clientDomainRequestParams)

    okHttpClient.newCall(clientDomainRequest).execute().use { response ->
      if (!response.isSuccessful) throw IOException("Request failed: $response")

      val jsonResponse = response.toJson<ChallengeResponse>()

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
