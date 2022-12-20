package org.stellar.walletsdk

import java.io.IOException
import okhttp3.OkHttpClient
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.auth.ChallengeResponse
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.util.OkHttpUtils

class InProcessWalletSigner : WalletSigner {
  override fun signWithClientAccount(txn: Transaction, account: AccountKeyPair): Transaction {
    return (account as SigningKeyPair).sign(txn)
  }

  override fun signWithDomainAccount(
    transactionString: String,
    networkPassPhrase: String,
    account: AccountKeyPair
  ): Transaction {
    val okHttpClient = OkHttpClient()

    val clientDomainRequestParams = ChallengeResponse(transactionString, networkPassPhrase)

    val clientDomainRequest =
      OkHttpUtils.makePostRequest(AUTH_CLIENT_DOMAIN_URL, clientDomainRequestParams)

    okHttpClient.newCall(clientDomainRequest).execute().use { response ->
      if (!response.isSuccessful) throw IOException("Request failed: $response")

      val jsonResponse = response.toJson<ChallengeResponse>()

      @Suppress("TooGenericExceptionThrown")
      if (jsonResponse.transaction.isBlank()) {
        throw Exception("The response did not contain a transaction")
      }

      return Transaction.fromEnvelopeXdr(
        jsonResponse.transaction,
        Network(jsonResponse.networkPassphrase)
      ) as Transaction
    }
  }
}
