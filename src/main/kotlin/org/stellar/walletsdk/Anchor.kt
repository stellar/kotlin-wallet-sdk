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
import org.stellar.walletsdk.util.OkHttpUtils
import org.stellar.walletsdk.util.StellarToml

//  TODO: move to Data
data class AnchorServiceAsset(
  val enabled: Boolean,
  val min_amount: Double,
  val max_amount: Double,
  val fee_fixed: Double,
  val fee_percent: Double
)

data class AnchorServiceFee(val enabled: Boolean)

data class AnchorServiceFeatures(val account_creation: Boolean, val claimable_balances: Boolean)

data class AnchorServiceInfo(
  val deposit: Map<String, AnchorServiceAsset>,
  val withdraw: Map<String, AnchorServiceAsset>,
  val fee: AnchorServiceFee,
  val features: AnchorServiceFeatures,
)

data class InteractiveResponse(
  val id: String,
  val url: String,
  val type: String,
)

enum class StellarTomlFields(val text: String) {
  SIGNING_KEY("SIGNING_KEY"),
  TRANSFER_SERVER_SEP0024("TRANSFER_SERVER_SEP0024"),
  WEB_AUTH_ENDPOINT("WEB_AUTH_ENDPOINT")
}

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
      throw Exception("Service URL is invalid", e)
    }
  }

  suspend fun getInteractiveDeposit(
    accountAddress: String,
    assetCode: String,
    assetIssuer: String,
    clientDomain: String? = null,
    memoId: String? = null,
    extraFields: Map<String, Any>? = null,
    walletSigner: WalletSigner,
  ): InteractiveResponse {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val sep24RequiredFields =
          listOf(
            StellarTomlFields.SIGNING_KEY.text,
            StellarTomlFields.TRANSFER_SERVER_SEP0024.text,
            StellarTomlFields.WEB_AUTH_ENDPOINT.text
          )
        val toml = StellarToml(stellarAddress = assetIssuer, server, httpClient)
        val tomlContent = toml.getToml()

        // TODO: check toml for required fields
        toml.hasFields(fields = sep24RequiredFields, tomlContent = tomlContent)

        val transferServerEndpoint =
          tomlContent[StellarTomlFields.TRANSFER_SERVER_SEP0024.text].toString()

        // TODO: check the /info endpoint for the asset deposit
        val serviceInfo = getServicesInfo(transferServerEndpoint)
        val depositAsset =
          serviceInfo.deposit[assetCode]
            ?: throw Exception("Asset $assetCode is not accepted for deposits")

        if (!depositAsset.enabled) {
          throw Exception("Asset $assetCode is not enabled for deposits")
        }

        val homeDomain = toml.getHomeDomain()

        // TODO: SEP-10 auth
        val auth =
          Auth(
            accountAddress = accountAddress,
            webAuthEndpoint = tomlContent[StellarTomlFields.WEB_AUTH_ENDPOINT.text].toString(),
            homeDomain = homeDomain,
            clientDomain = clientDomain,
            memoId = memoId,
            networkPassPhrase = network.networkPassphrase,
            walletSigner = walletSigner
          )
        val authToken = auth.authenticate()

        val requestParams = mutableMapOf<String, Any>()
        requestParams["account"] = accountAddress
        requestParams["asset_code"] = assetCode

        if (extraFields != null) {
          requestParams += extraFields
        }

        // TODO: get SEP-24 deposit link
        val gson = GsonUtils.instance!!
        val client = OkHttpClient()

        val requestUrl = "$transferServerEndpoint/transactions/deposit/interactive"
        val request = OkHttpUtils.buildJsonPostRequest(requestUrl, requestParams, authToken)

        client.newCall(request).execute().use { response ->
          if (!response.isSuccessful) throw NetworkRequestFailedException(response)

          return@async gson.fromJson(response.body!!.charStream(), InteractiveResponse::class.java)
        }
      }
      .await()
  }
}
