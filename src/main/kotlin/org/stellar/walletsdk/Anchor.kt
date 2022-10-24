package org.stellar.walletsdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.util.GsonUtils
import org.stellar.walletsdk.util.StellarToml
import org.stellar.walletsdk.util.interactiveFlow

// TODO: document
class Anchor(
  private val server: Server,
  private val network: Network,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  suspend fun getInfo(assetIssuer: String): Map<String, Any> {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val toml = StellarToml(stellarAddress = assetIssuer, server, httpClient)

        return@async toml.getToml()
      }
      .await()
  }

  //  Service info
  suspend fun getServicesInfo(serviceUrl: String): AnchorServiceInfo {
    try {
      val url = serviceUrl.toHttpUrl()
      val urlBuilder = HttpUrl.Builder().scheme(url.scheme).host(url.host).port(url.port)

      url.pathSegments.forEach { urlBuilder.addPathSegment(it) }
      urlBuilder.addPathSegment("info")

      val infoUrl = urlBuilder.build().toString()

      val request = Request.Builder().url(infoUrl).build()
      val gson = GsonUtils.instance!!

      return CoroutineScope(Dispatchers.IO)
        .async {
          httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw NetworkRequestFailedException(response)

            val infoResponse = response.body!!.charStream()
            return@async gson.fromJson(infoResponse, AnchorServiceInfo::class.java)
          }
        }
        .await()
    } catch (e: Exception) {
      throw InvalidAnchorServiceUrl(e)
    }
  }

  //  TODO: doc
  //  Interactive deposit
  suspend fun getInteractiveDeposit(
    accountAddress: String,
    assetCode: String,
    assetIssuer: String,
    clientDomain: String? = null,
    memoId: String? = null,
    extraFields: Map<String, Any>? = null,
    walletSigner: WalletSigner,
  ): InteractiveFlowResponse {
    return interactiveFlow(
      type = InteractiveFlowType.DEPOSIT,
      accountAddress = accountAddress,
      assetCode = assetCode,
      assetIssuer = assetIssuer,
      clientDomain = clientDomain,
      memoId = memoId,
      extraFields = extraFields,
      walletSigner = walletSigner,
      anchor = Anchor(server, network, httpClient),
      server = server,
      network = network,
      httpClient = httpClient
    )
  }

  //  TODO: doc
  //  Interactive withdrawal
  suspend fun getInteractiveWithdrawal(
    accountAddress: String,
    assetCode: String,
    assetIssuer: String,
    clientDomain: String? = null,
    memoId: String? = null,
    extraFields: Map<String, Any>? = null,
    walletSigner: WalletSigner,
  ): InteractiveFlowResponse {
    return interactiveFlow(
      type = InteractiveFlowType.WITHDRAW,
      accountAddress = accountAddress,
      assetCode = assetCode,
      assetIssuer = assetIssuer,
      clientDomain = clientDomain,
      memoId = memoId,
      extraFields = extraFields,
      walletSigner = walletSigner,
      anchor = Anchor(server, network, httpClient),
      server = server,
      network = network,
      httpClient = httpClient
    )
  }
}
