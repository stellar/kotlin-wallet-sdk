package org.stellar.walletsdk.anchor

import mu.KotlinLogging
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.sdk.*
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.toAsset
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.*
import org.stellar.walletsdk.util.toJson

private val log = KotlinLogging.logger {}

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
      toml[StellarTomlField.WEB_AUTH_ENDPOINT.text].toString(),
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
   * @throws [ServerRequestFailedException] if network request fails
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

    log.debug { "Anchor services /info request: serviceUrl = $serviceUrl" }

    val request = Request.Builder().url(infoUrl).build()

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson<AnchorServiceInfo>()
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
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun getTransactionStatus(
    transactionId: String,
    authToken: String,
    toml: Map<String, Any>
  ): AnchorTransaction {
    val transferServerEndpoint = toml[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointUrl = "$transferServerEndpoint/transaction?id=$transactionId"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    log.debug { "Anchor account's transaction by ID request: transactionId = $transactionId" }

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson<AnchorTransactionStatusResponse>().transaction
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
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun getAllTransactionStatus(
    assetCode: String,
    authToken: String,
    toml: Map<String, Any>
  ): List<AnchorTransaction> {
    val transferServerEndpoint = toml[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointUrl = "$transferServerEndpoint/transactions?asset_code=$assetCode"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    log.debug {
      "Anchor account's all transactions request: assetCode = $assetCode, authToken = ${authToken.take(8)}"
    }

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson<AnchorAllTransactionsResponse>().transactions
    }
  }

  /**
   * Get account transactions for specified asset. Optional field implementation depends on anchor.
   *
   * @param assetCode asset's code
   * @param authToken auth token of the account authenticated with the anchor
   * @param toml Anchor's stellar.toml file containing `CURRENCIES` list of supported assets
   * @param limit optional how many transactions to fetch
   * @param pagingId optional return transactions prior to this ID
   * @param noOlderThan optional return transactions starting on or after this date and time
   * @param lang optional language code specified using
   * [RFC 4646](https://www.rfc-editor.org/rfc/rfc4646), default is `en`
   *
   * @return a list of formatted operations
   *
   * @throws [ServerRequestFailedException] if network request fails
   * @throws [AssetNotSupportedException] if asset is not supported by the anchor
   */
  suspend fun getHistory(
    assetId: IssuedAssetId,
    authToken: String,
    toml: Map<String, Any>,
    limit: Int? = null,
    pagingId: String? = null,
    noOlderThan: String? = null,
    lang: String? = null
  ): List<WalletOperation<AnchorTransaction>> {
    val anchorCurrency =
      ((toml as HashMap)["CURRENCIES"] as List<*>).filterIsInstance<HashMap<*, *>>().find {
        it["code"] == assetId.code
      }
        ?: throw AssetNotSupportedException(assetId)
    val asset = assetId.toAsset()

    val transferServerEndpoint = toml[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointHttpUrl = transferServerEndpoint.toHttpUrl()
    val endpointUrl = HttpUrl.Builder().scheme("https").host(endpointHttpUrl.host)

    // Add path segments, if there are any
    endpointHttpUrl.pathSegments.forEach { endpointUrl.addPathSegment(it) }

    // Add transactions path segment
    endpointUrl.addPathSegment("transactions")

    // Add query params
    val queryParams = mutableMapOf<String, String>()
    queryParams["asset_code"] = assetId.code
    limit?.run { queryParams["limit"] = this.toString() }
    pagingId?.run { queryParams["paging_id"] = this }
    noOlderThan?.run { queryParams["no_older_than"] = this }
    lang?.run { queryParams["lang"] = this }

    queryParams.forEach { endpointUrl.addQueryParameter(it.key, it.value) }

    val request = OkHttpUtils.buildStringGetRequest(endpointUrl.build().toString(), authToken)
    val finalStatusList = listOf("completed", "refunded")

    log.debug {
      "Anchor account's formatted history request: asset = $asset, authToken = " +
        "${authToken.take(8)}, limit = $limit, pagingId = $pagingId, noOlderThan = $noOlderThan, " +
        "lang = $lang"
    }

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response
        .toJson<AnchorAllTransactionsResponse>()
        .transactions
        .filter { finalStatusList.contains(it.status) }
        .map { formatAnchorTransaction(it, asset) }
    }
  }
}
