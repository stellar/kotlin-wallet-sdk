package org.stellar.walletsdk.anchor

import mu.KotlinLogging
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.scheme
import org.stellar.walletsdk.toml.StellarToml
import org.stellar.walletsdk.toml.TomlInfo
import org.stellar.walletsdk.toml.parseToml
import org.stellar.walletsdk.util.OkHttpUtils

private val log = KotlinLogging.logger {}

/**
 * Build on/off ramps with anchors.
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 * @property homeDomain home domain of the anchor
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
actual class Anchor
internal actual constructor(
  internal actual val cfg: Config,
  internal actual val homeDomain: String
) {
  private val server = cfg.stellar.server
  private val network = cfg.stellar.network
  private val httpClient = OkHttpClient()

  // This 2 variables are lazy and shouldn't be used directly. Call getInfo() and getServiceInfo()
  // instead
  private lateinit var info: TomlInfo
  private lateinit var serviceInfo: AnchorServiceInfo

  /**
   * Get anchor information from a TOML file.
   *
   * @return TOML file content
   */
  suspend fun getInfo(): TomlInfo {
    if (!::info.isInitialized) {
      val toml = StellarToml(cfg.scheme, homeDomain, httpClient)

      info = parseToml(toml.getToml())
    }

    return info
  }

  /**
   * Create new auth object to authenticate account with the anchor using SEP-10.
   *
   * @return auth object
   * @throws [AnchorAuthNotSupported] if SEP-10 is not configured
   */
  suspend fun auth(): Auth {
    return Auth(
      cfg,
      getInfo().services.sep10?.webAuthEndpoint ?: throw AnchorAuthNotSupported,
      homeDomain,
    )
  }

  /**
   * Available anchor services and information about them. For example, limits, currency, fees,
   * payment methods.
   *
   * @return a list of available anchor services
   * @throws [ServerRequestFailedException] if network request fails
   * @throws [InvalidAnchorServiceUrl] if provided service URL is not a valid URL
   */
  suspend fun getServicesInfo(): AnchorServiceInfo {
    if (!::serviceInfo.isInitialized) {
      val url =
        getInfo().services.sep24?.transferServerSep24?.toHttpUrl()
          ?: throw AnchorInteractiveFlowNotSupported

      val urlBuilder = HttpUrl.Builder().scheme(url.scheme).host(url.host).port(url.port)

      url.pathSegments.forEach { urlBuilder.addPathSegment(it) }
      urlBuilder.addPathSegment("info")

      val infoUrl = urlBuilder.build().toString()

      log.debug { "Anchor services /info request: serviceUrl = $url" }

      val request = Request.Builder().url(infoUrl).build()

      serviceInfo =
        httpClient.newCall(request).execute().use { response ->
          if (!response.isSuccessful) throw ServerRequestFailedException(response)

          response.toJson()
        }
    }

    return serviceInfo
  }

  /**
   * Creates new interactive flow for given anchor. It can be used for withdrawal or deposit.
   *
   * @return interactive flow service
   */
  fun interactive(): Interactive {
    return Interactive(homeDomain, this, cfg)
  }

  // TODO: is this for SEP-24 only?
  // TODO: handle extra fields
  /**
   * Get single transaction's current status and details.
   *
   * @param transactionId transaction ID
   * @param authToken auth token of the account authenticated with the anchor
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun getTransaction(transactionId: String, authToken: AuthToken): AnchorTransaction {
    val transferServerEndpoint =
      getInfo().services.sep24?.transferServerSep24 ?: throw AnchorInteractiveFlowNotSupported
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
   * @param asset target asset to query for
   * @param authToken auth token of the account authenticated with the anchor
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun getTransactionsForAsset(
    asset: AssetId,
    authToken: AuthToken
  ): List<AnchorTransaction> {
    val transferServerEndpoint =
      getInfo().services.sep24?.transferServerSep24 ?: throw AnchorInteractiveFlowNotSupported
    val endpointUrl = "$transferServerEndpoint/transactions?asset_code=${asset.sep38}"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    log.debug {
      "Anchor account's all transactions request: assetCode = ${asset.sep38}, authToken = ${authToken.prettify()}"
    }

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson<AnchorAllTransactionsResponse>().transactions
    }
  }

  /**
   * Get all successfully finished (either completed or refunded) account transactions for specified
   * asset. Optional field implementation depends on anchor.
   *
   * @param authToken auth token of the account authenticated with the anchor
   * @param assetId asset to make a query for
   * @param limit optional how many transactions to fetch
   * @param pagingId optional return transactions prior to this ID
   * @param noOlderThan optional return transactions starting on or after this date and time
   * @param lang optional language code specified using
   * [RFC 4646](https://www.rfc-editor.org/rfc/rfc4646), default is `en`
   * @return a list of formatted operations
   * @throws [ServerRequestFailedException] if network request fails
   * @throws [AssetNotSupportedException] if asset is not supported by the anchor
   */
  @Suppress("LongParameterList")
  suspend fun getHistory(
    assetId: AssetId,
    authToken: AuthToken,
    limit: Int? = null,
    pagingId: String? = null,
    noOlderThan: String? = null,
    lang: String? = null
  ): List<AnchorTransaction> {
    if (getInfo().currencies?.find { it.assetId == assetId } == null) {
      throw AssetNotSupportedException(assetId)
    }

    val transferServerEndpoint =
      getInfo().services.sep24?.transferServerSep24 ?: throw AnchorInteractiveFlowNotSupported
    val endpointHttpUrl = transferServerEndpoint.toHttpUrl()
    val endpointUrl = HttpUrl.Builder().scheme("https").host(endpointHttpUrl.host)

    // Add path segments, if there are any
    endpointHttpUrl.pathSegments.forEach { endpointUrl.addPathSegment(it) }

    // Add transactions path segment
    endpointUrl.addPathSegment("transactions")

    // Add query params
    val queryParams = mutableMapOf<String, String>()
    queryParams["asset_code"] = assetId.sep38
    limit?.run { queryParams["limit"] = this.toString() }
    pagingId?.run { queryParams["paging_id"] = this }
    noOlderThan?.run { queryParams["no_older_than"] = this }
    lang?.run { queryParams["lang"] = this }

    queryParams.forEach { endpointUrl.addQueryParameter(it.key, it.value) }

    val request = OkHttpUtils.buildStringGetRequest(endpointUrl.build().toString(), authToken)
    val finalStatusList = listOf(TransactionStatus.COMPLETED, TransactionStatus.REFUNDED)

    log.debug {
      "Anchor account's formatted history request: asset = $assetId, authToken = " +
        "${authToken.prettify()}, limit = $limit, pagingId = $pagingId, noOlderThan = $noOlderThan, " +
        "lang = $lang"
    }

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson<AnchorAllTransactionsResponse>().transactions.filter {
        finalStatusList.contains(it.status)
      }
    }
  }
}
