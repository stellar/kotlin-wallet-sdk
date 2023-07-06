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
  internal val infoHolder = InfoHolder(cfg.stellar.network, baseUrl, httpClient)

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
   * Creates new interactive flow for given anchor. It can be used for withdrawal or deposit.
   *
   * @return interactive flow service
   */
  fun interactive(): Interactive {
    return Interactive(this, httpClient)
  }

  /**
   * Get single transaction's current status and details.
   *
   * @param transactionId transaction ID
   * @param authToken auth token of the account authenticated with the anchor
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   */
  @Deprecated("Use interactive.getTransaction()", ReplaceWith("interactive().getTransaction(transactionId, authToken)"))
  suspend fun getTransaction(transactionId: String, authToken: AuthToken): AnchorTransaction {
    return interactive().getTransaction(transactionId, authToken)
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
  @Deprecated("Use interactive.getTransactionBy()",
    ReplaceWith("interactive().getTransactionBy(authToken, id, stellarTransactionId, externalTransactionId, lang)")
  )
  suspend fun getTransactionBy(
    authToken: AuthToken,
    id: String? = null,
    stellarTransactionId: String? = null,
    externalTransactionId: String? = null,
    lang: String? = null
  ): AnchorTransaction {
    return interactive().getTransactionBy(authToken, id, stellarTransactionId, externalTransactionId, lang)
  }

  /**
   * Get all account's transactions by specified asset. See SEP-24 specification for parameters
   *
   * @param asset target asset to query for
   * @param authToken auth token of the account authenticated with the anchor
   * @param noOlderThan The response should contain transactions starting on or after this date &
   * time.
   * @param limit The response should contain at most limit transactions
   * @param kind The kind of transaction that is desired
   * @param pagingId The response should contain transactions starting prior to this ID (exclusive)
   * @param lang Language to use
   * @return transaction object
   * @throws [AnchorInteractiveFlowNotSupported] if SEP-24 interactive flow is not configured
   */
  @Suppress("LongParameterList")
  @Deprecated("Use interactive.getTransactionsForAsset()",
    ReplaceWith("interactive().getTransactionsForAsset(asset, authToken, noOlderThan, limit, kind, pagingId, lang)")
  )
  suspend fun getTransactionsForAsset(
    asset: AssetId,
    authToken: AuthToken,
    noOlderThan: Instant? = null,
    limit: Int? = null,
    kind: TransactionKind? = null,
    pagingId: String? = null,
    lang: String? = null
  ): List<AnchorTransaction> {
    return interactive().getTransactionsForAsset(asset, authToken, noOlderThan, limit, kind, pagingId, lang)
  }

  @Deprecated("Use interactive.getHistory()",
    ReplaceWith("interactive().getHistory(assetId, authToken, limit, pagingId, noOlderThan, lang)")
  )
  @Suppress("LongParameterList")
  suspend fun getHistory(
    assetId: AssetId,
    authToken: AuthToken,
    limit: Int? = null,
    pagingId: String? = null,
    noOlderThan: String? = null,
    lang: String? = null
  ): List<AnchorTransaction> {
    return interactive().getHistory(assetId, authToken, limit, pagingId, noOlderThan, lang)
  }

  private suspend inline fun <reified T> get(
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    return httpClient.anchorGet(getInfo(), authToken, urlBlock)
  }
}

suspend fun Anchor.sep12(authToken: AuthToken): Sep12 {
  return this.customer(authToken)
}

fun Anchor.sep24(): Sep24 {
  return this.interactive()
}

typealias Sep24 = Interactive

typealias Sep12 = Customer

internal class InfoHolder(
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
