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

/**
 * Build on/off ramps with anchors.
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class Anchor(
  private val server: Server,
  private val network: Network,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  /**
   * Get anchor information from a TOML file.
   *
   * @param assetIssuer Stellar address of the asset issuer (anchor)
   *
   * @return TOML file content
   */
  suspend fun getInfo(assetIssuer: String): Map<String, Any> {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val toml = StellarToml(stellarAddress = assetIssuer, server, httpClient)

        return@async toml.getToml()
      }
      .await()
  }

  /**
   * Available anchor services and information about them. For example, limits, currency, fees,
   * payment methods.
   *
   * @param serviceUrl URL where `/info` endpoint is hosted
   *
   * @return a list of available anchor services
   *
   * @throws [NetworkRequestFailedException] if network request fails
   * @throws [InvalidAnchorServiceUrl] if provided service URL is not a valid URL
   */
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

  /**
   * Interactive deposit flow using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param accountAddress Stellar address of the account
   * @param assetCode Asset code to use
   * @param assetIssuer Asset issuer to use
   * @param memoId optional memo ID to distinguish the account
   * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
   * @param extraFields Additional information to pass to the anchor
   * @param walletSigner interface to define wallet client and domain (if using `clientDomain`)
   * signing methods
   *
   * @return response object from the anchor
   *
   * @throws [AssetNotAcceptedForDepositException] if asset is not accepted for deposits
   * @throws [AssetNotEnabledForDepositException] if asset is not enabled for deposits by the anchor
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getInteractiveDeposit(
    accountAddress: String,
    assetCode: String,
    assetIssuer: String,
    memoId: String? = null,
    clientDomain: String? = null,
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

  /**
   * Interactive withdrawal flow using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param accountAddress Stellar address of the account
   * @param assetCode Asset code to use
   * @param assetIssuer Asset issuer to use
   * @param memoId optional memo ID to distinguish the account
   * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
   * @param extraFields Additional information to pass to the anchor
   * @param walletSigner interface to define wallet client and domain (if using `clientDomain`)
   * signing methods
   *
   * @return response object from the anchor
   *
   * @throws [AssetNotAcceptedForWithdrawalException] if asset is not accepted for withdrawals
   * @throws [AssetNotEnabledForWithdrawalException] if asset is not enabled for withdrawals by the
   * anchor
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getInteractiveWithdrawal(
    accountAddress: String,
    assetCode: String,
    assetIssuer: String,
    memoId: String? = null,
    clientDomain: String? = null,
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
