package org.stellar.walletsdk.util

import okhttp3.OkHttpClient
import org.stellar.sdk.Transaction
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.walletsdk.NetworkRequestFailedException
import org.stellar.walletsdk.RecoveryServerAuth

/**
 * Get a transaction signature from a recovery server for the account.
 *
 * @param transaction Transaction to be signed by the recovery server
 * @param accountAddress Stellar address of the account that the recovery server is signing for
 * @param recoveryServer Recovery server configuration
 * @param base64Decoder optional base64Decoder. Default `java.util.Base64` decoder works with
 * Android API 23+. To support Android API older than API 23, custom base64Decoder needs to be
 * provided. For example, `android.util.Base64`.
 *
 * @return decorated signature of the recovery server
 *
 * @throws [NetworkRequestFailedException] when request fails
 */
suspend fun getRecoveryServerTxnSignature(
  transaction: Transaction,
  accountAddress: String,
  recoveryServer: RecoveryServerAuth,
  base64Decoder: ((String) -> ByteArray)? = null
): DecoratedSignature {
  data class TransactionRequest(val transaction: String)
  data class AuthSignature(val signature: String)

  val gson = GsonUtils.instance!!
  val client = OkHttpClient()

  val requestUrl =
    "${recoveryServer.endpoint}/accounts/$accountAddress/sign/${recoveryServer.signerAddress}"
  val requestParams = TransactionRequest(transaction.toEnvelopeXdrBase64())
  val request =
    OkHttpUtils.buildJsonPostRequest(requestUrl, requestParams, recoveryServer.authToken)

  return client.newCall(request).execute().use { response ->
    if (!response.isSuccessful) throw NetworkRequestFailedException(response)

    val authResponse: AuthSignature =
      gson.fromJson(response.body!!.charStream(), AuthSignature::class.java)

    createDecoratedSignature(recoveryServer.signerAddress, authResponse.signature, base64Decoder)
  }
}
