package org.stellar.walletsdk.anchor

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.InvalidAnchorServiceUrl
import org.stellar.walletsdk.exception.NetworkRequestFailedException
import org.stellar.walletsdk.util.*

/**
 * Build on/off ramps with anchors.
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 * @property homeDomain home domain of the anchor
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class Anchor(
  private val server: Server,
  private val network: Network,
  private val homeDomain: String,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  private val gson = GsonUtils.instance!!

  /**
   * Get anchor information from a TOML file.
   *
   * @return TOML file content
   */
  suspend fun getInfo(): Map<String, Any> {
    val toml = StellarToml(homeDomain, server, httpClient)

    return toml.getToml()
  }

  /**
   * Create new auth object to authenticate account with the anchor using SEP-10.
   *
   * @param toml Anchor's stellar.toml file containing `WEB_AUTH_ENDPOINT`
   * @param walletSigner interface to define wallet client and domain (if using `clientDomain`)
   * signing methods
   *
   * @return auth object
   */
  suspend fun auth(
    toml: Map<String, Any>,
    walletSigner: WalletSigner,
  ): Auth {
    // TODO: get toml automatically
    // TODO: provide wallet signer as parameter to Anchor class
    return Auth(
      toml[StellarTomlFields.WEB_AUTH_ENDPOINT.text].toString(),
      homeDomain,
      walletSigner,
      network,
      httpClient
    )
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
    val url: HttpUrl

    try {
      url = serviceUrl.toHttpUrl()
    } catch (e: IllegalArgumentException) {
      throw InvalidAnchorServiceUrl(e)
    }

    val urlBuilder = HttpUrl.Builder().scheme(url.scheme).host(url.host).port(url.port)

    url.pathSegments.forEach { urlBuilder.addPathSegment(it) }
    urlBuilder.addPathSegment("info")

    val infoUrl = urlBuilder.build().toString()

    val request = Request.Builder().url(infoUrl).build()
    val gson = GsonUtils.instance!!

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      val infoResponse = response.body!!.charStream()
      gson.fromJson(infoResponse, AnchorServiceInfo::class.java)
    }
  }

  /**
   * Creates new interactive flow for given anchor. It can be used for withdrawal or deposit.
   *
   * @return interactive flow service
   */
  fun interactive(): Interactive {
    return Interactive(homeDomain, this, server, httpClient)
  }

  // TODO: is this for SEP-24 only?
  // TODO: handle extra fields
  /**
   * Get single transaction's current status and details.
   *
   * @param transactionId transaction ID
   * @param authToken auth token of the account authenticated with the anchor
   * @param toml Anchor's stellar.toml file containing `WEB_AUTH_ENDPOINT`
   *
   * @return transaction object
   *
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getTransactionStatus(
    transactionId: String,
    authToken: String,
    toml: Map<String, Any>
  ): AnchorTransaction {
    val transferServerEndpoint = toml[StellarTomlFields.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointUrl = "$transferServerEndpoint/transaction?id=$transactionId"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      gson
        .fromJson(response.body!!.charStream(), AnchorTransactionStatusResponse::class.java)
        .transaction
    }
  }

  // TODO: is this for SEP-24 only?
  // TODO: handle extra fields
  /**
   * Get all account's transactions by specified asset.
   *
   * @param assetCode asset's code
   * @param authToken auth token of the account authenticated with the anchor
   * @param toml Anchor's stellar.toml file containing `WEB_AUTH_ENDPOINT`
   *
   * @return transaction object
   *
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getAllTransactionStatus(
    assetCode: String,
    authToken: String,
    toml: Map<String, Any>
  ): List<AnchorTransaction> {
    val transferServerEndpoint = toml[StellarTomlFields.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointUrl = "$transferServerEndpoint/transactions?asset_code=$assetCode"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      gson
        .fromJson(response.body!!.charStream(), AnchorAllTransactionsResponse::class.java)
        .transactions
    }
  }
}
