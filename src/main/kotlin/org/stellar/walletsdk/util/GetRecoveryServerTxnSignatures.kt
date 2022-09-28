package org.stellar.walletsdk.util

import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import org.stellar.sdk.Transaction
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.walletsdk.RecoveryServerAuth

suspend fun getRecoveryServerTxnSignatures(
  transaction: Transaction,
  accountAddress: String,
  recoveryServer: RecoveryServerAuth,
  base64Decoder: ((String) -> ByteArray)? = null
): DecoratedSignature {
  data class TransactionRequest(val transaction: String)
  data class AuthSignature(val signature: String)

  val gson = GsonUtils.instance!!
  val client = OkHttpClient()

  return CoroutineScope(Dispatchers.IO)
    .async {
      val requestUrl =
        "${recoveryServer.endpoint}/accounts/$accountAddress/sign/${recoveryServer.signerAddress}"
      val requestParams = TransactionRequest(transaction.toEnvelopeXdrBase64())
      val request =
        OkHttpUtils.buildJsonPostRequest(requestUrl, requestParams, recoveryServer.authToken)

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Request failed: $response")

        val authResponse: AuthSignature =
          gson.fromJson(response.body!!.charStream(), AuthSignature::class.java)

        return@async createDecoratedSignature(
          recoveryServer.signerAddress,
          authResponse.signature,
          base64Decoder
        )
      }
    }
    .await()
}
