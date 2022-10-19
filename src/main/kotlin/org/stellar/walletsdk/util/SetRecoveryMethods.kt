package org.stellar.walletsdk.util

import java.io.IOException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import org.stellar.walletsdk.*

suspend fun setRecoveryMethods(
  endpoint: String,
  authEndpoint: String,
  homeDomain: String,
  accountAddress: String,
  accountIdentity: List<RecoveryAccountIdentity>,
  walletSigner: WalletSigner
): String {
  val gson = GsonUtils.instance!!
  val okHttpClient = OkHttpClient()

  val authToken =
    Auth(
        accountAddress = accountAddress,
        authEndpoint = authEndpoint,
        homeDomain = homeDomain,
        walletSigner = walletSigner
      )
      .authenticate()

  val requestUrl = "$endpoint/accounts/$accountAddress"
  val request =
    OkHttpUtils.buildJsonPostRequest(
      requestUrl,
      RecoveryIdentities(identities = accountIdentity),
      authToken
    )

  return CoroutineScope(Dispatchers.IO)
    .async {
      okHttpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Request failed: $response")

        val jsonResponse = gson.fromJson(response.body!!.charStream(), RecoveryAccount::class.java)

        return@async getLatestRecoverySigner(jsonResponse.signers)
      }
    }
    .await()
}

fun getLatestRecoverySigner(signers: List<RecoveryAccountSigner>): String {
  if (signers.isEmpty()) {
    throw Exception("There are no signers on this recovery server")
  }

  return signers[0].key
}
