package org.stellar.walletsdk.anchor

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import mu.KotlinLogging
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.toml.StellarToml
import org.stellar.walletsdk.toml.TomlInfo
import org.stellar.walletsdk.util.*
import org.stellar.walletsdk.util.Util.anchorGet

private val log = KotlinLogging.logger {}

/** Build on/off ramps with anchors. */
class Anchor
internal constructor(
  private val cfg: Config,
  private val baseUrl: Url,
  private val httpClient: HttpClient
) {
  private val infoHolder = InfoHolder(cfg.stellar.network, baseUrl, httpClient)

  /**
   * Get anchor information from a TOML file.
   *
   * @return TOML file content
   */
  suspend fun getInfo(): TomlInfo {
    return infoHolder.getInfo()
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
      // Strip protocol
      baseUrl.toString().replace("${baseUrl.protocol.name}://", ""),
      httpClient
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
    return infoHolder.getServicesInfo()
  }

  /**
   * Creates new interactive flow for given anchor. It can be used for withdrawal or deposit.
   *
   * @return interactive flow service
   */
  fun interactive(): Interactive {
    return Interactive(this, httpClient)
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
    return get<AnchorTransactionStatusResponse>(authToken) {
        appendPathSegments("transaction")
        parameters.append("id", transactionId)
      }
      .transaction
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
    return get<AnchorAllTransactionsResponse>(authToken) {
        appendPathSegments("transactions")
        parameters.append("asset_code", asset.sep38)
      }
      .transactions
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

    // Add query params
    val queryParams = mutableMapOf<String, String>()
    queryParams["asset_code"] = assetId.sep38
    limit?.run { queryParams["limit"] = this.toString() }
    pagingId?.run { queryParams["paging_id"] = this }
    noOlderThan?.run { queryParams["no_older_than"] = this }
    lang?.run { queryParams["lang"] = this }

    val finalStatusList = listOf(TransactionStatus.COMPLETED, TransactionStatus.REFUNDED)

    log.debug {
      "Anchor account's formatted history request: asset = $assetId, authToken = " +
        "${authToken.prettify()}, limit = $limit, pagingId = $pagingId, noOlderThan = $noOlderThan, " +
        "lang = $lang"
    }

    val resp: AnchorAllTransactionsResponse =
      get(authToken) {
        appendPathSegments("transactions")
        queryParams.forEach { parameters.append(it.key, it.value) }
      }

    return resp.transactions.filter { finalStatusList.contains(it.status) }
  }

  private suspend inline fun <reified T> get(
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    return httpClient.anchorGet(getInfo(), authToken, urlBlock)
  }
}

private class InfoHolder(
  private val network: Network,
  private val baseUrl: Url,
  private val httpClient: HttpClient
) {
  // This 2 variables are lazy and shouldn't be used directly. Call getInfo() and getServiceInfo()
  // instead
  private lateinit var info: TomlInfo
  private lateinit var serviceInfo: AnchorServiceInfo

  suspend fun getInfo(): TomlInfo {
    if (!::info.isInitialized) {
      info = StellarToml.getToml(baseUrl, httpClient)
      info.validate(network)
    }

    return info
  }

  suspend fun getServicesInfo(): AnchorServiceInfo {
    if (!::serviceInfo.isInitialized) {
      serviceInfo = get { appendPathSegments("info") }
    }

    return serviceInfo
  }

  private suspend inline fun <reified T> get(
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    return httpClient.anchorGet(getInfo(), authToken, urlBlock)
  }
}
