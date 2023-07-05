@file:Suppress("MaxLineLength")

package org.stellar.walletsdk.anchor

import io.ktor.client.*
import io.ktor.http.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Instant
import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.customer.Customer
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.toml.StellarToml
import org.stellar.walletsdk.toml.TomlInfo
import org.stellar.walletsdk.util.Util.anchorGet

private val log = KotlinLogging.logger {}

/** Build on/off ramps with anchors. */
@Suppress("TooManyFunctions")
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
   * Create new customer object to handle customer records with the anchor using SEP-12.
   *
   * @return customer object
   */
  suspend fun customer(token: AuthToken): Customer {
    val kycServer = getInfo().services.sep12?.kycServer ?: throw KYCServerNotFoundException()

    return Customer(token, kycServer, httpClient)
  }

  /**
   * Available anchor services and information about them. For example, limits, currency, fees,
   * payment methods.
   *
   * @return a list of available anchor services
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

  /**
   * Creates new transaction watcher
   *
   * @param pollDelay poll interval in which requests to the Anchor are being made.
   * @param channelSize size of the Coroutine [Channel]. See
   *   [channel documentation](https://kotlinlang.org/docs/coroutines-and-channels.html#channels)
   *   for more info about channel size configuration. Be default, unlimited channel is created.
   * @param exceptionHandler handler for exceptions. By default, [RetryExceptionHandler] is being
   *   used.
   * @return new transaction watcher
   */
  fun watcher(
    pollDelay: Duration = 5.seconds,
    channelSize: Int = Channel.UNLIMITED,
    exceptionHandler: WalletExceptionHandler = RetryExceptionHandler()
  ): Watcher {
    return Watcher(this, pollDelay, channelSize, exceptionHandler)
  }

  /**
   * Get single transaction's current status and details.
   *
   * @param transactionId transaction ID
   * @param authToken auth token of the account authenticated with the anchor
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   */
  suspend fun getTransaction(transactionId: String, authToken: AuthToken): AnchorTransaction {
    return get<AnchorTransactionStatusResponse>(authToken) {
        appendPathSegments("transaction")
        parameters.append("id", transactionId)
      }
      .transaction
  }

  /**
   * Get single transaction's current status and details. One of the [id], [stellarTransactionId],
   * [externalTransactionId] must be provided.
   *
   * @param id transaction ID
   * @param stellarTransactionId stellar transaction ID
   * @param externalTransactionId external transaction ID
   * @param authToken auth token of the account authenticated with the anchor
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   */
  suspend fun getTransactionBy(
    authToken: AuthToken,
    id: String? = null,
    stellarTransactionId: String? = null,
    externalTransactionId: String? = null,
    lang: String? = null
  ): AnchorTransaction {
    if (id == null && stellarTransactionId == null && externalTransactionId == null) {
      throw ValidationException(
        "One of id, stellarTransactionId or externalTransactionId is required."
      )
    }

    return get<AnchorTransactionStatusResponse>(authToken) {
        appendPathSegments("transaction")
        id?.apply { parameters.append("id", id) }
        stellarTransactionId?.apply {
          parameters.append("stellar_transaction_id", stellarTransactionId)
        }
        externalTransactionId?.apply {
          parameters.append("external_transaction_id", externalTransactionId)
        }
        lang?.apply { parameters.append("lang", lang) }
      }
      .transaction
  }

  /**
   * Get all account's transactions by specified asset. See SEP-24 specification for parameters
   *
   * @param asset target asset to query for
   * @param authToken auth token of the account authenticated with the anchor
   * @param noOlderThan The response should contain transactions starting on or after this date &
   *   time.
   * @param limit The response should contain at most limit transactions
   * @param kind The kind of transaction that is desired
   * @param pagingId The response should contain transactions starting prior to this ID (exclusive)
   * @param lang Language to use
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   */
  @Suppress("LongParameterList")
  suspend fun getTransactionsForAsset(
    asset: AssetId,
    authToken: AuthToken,
    noOlderThan: Instant? = null,
    limit: Int? = null,
    kind: TransactionKind? = null,
    pagingId: String? = null,
    lang: String? = null
  ): List<AnchorTransaction> {
    return get<AnchorAllTransactionsResponse>(authToken) {
        appendPathSegments("transactions")

        val code =
          when (asset) {
            is IssuedAssetId -> asset.code
            else -> asset.id
          }
        parameters.append("asset_code", code)

        noOlderThan?.run { parameters.append("no_longer_than", noOlderThan.toJson()) }
        limit?.run { parameters.append("limit", limit.toString()) }
        kind?.run { parameters.append("kind", kind.toString()) }
        pagingId?.run { parameters.append("paging_id", pagingId.toString()) }
        lang?.run { parameters.append("lang", lang.toString()) }
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
   *   [RFC 4646](https://www.rfc-editor.org/rfc/rfc4646), default is `en`
   * @return a list of formatted operations
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

fun Anchor.sep24(): Sep24 {
  return this.interactive()
}

typealias Sep24 = Interactive

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
