package org.stellar.walletsdk.anchor

import io.ktor.client.*
import io.ktor.http.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Instant
import org.stellar.walletsdk.InteractiveFlowResponse
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.NativeAssetId
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.util.Util.anchorGet

/** Interactive flow for deposit and withdrawal using SEP-24. */
class Sep24
internal constructor(
  internal val anchor: Anchor,
  internal val httpClient: HttpClient,
) {
  /**
   * Initiates interactive withdrawal using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param withdrawalAccount The Stellar or muxed account the client will use as the source of the
   * withdrawal payment to the anchor. Defaults to the account authenticated via SEP-10 if not
   * specified.
   * @param assetId Stellar asset to deposit or withdraw
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   * @return response object from the anchor
   * @throws [AnchorAssetException] if asset was refused by the anchor
   */
  suspend fun withdraw(
    assetId: StellarAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>? = null,
    withdrawalAccount: String? = null,
  ): InteractiveFlowResponse {
    return flow(assetId, authToken, extraFields, withdrawalAccount, null, "withdraw") {
      when (assetId) {
        is IssuedAssetId -> it.withdraw[assetId.code]
        is NativeAssetId -> it.withdraw[NativeAssetId.id]
      }
    }
  }

  /**
   * Initiates interactive deposit using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param destinationAccount The Stellar or muxed account the client wants to use as the
   * destination of the payment sent by the anchor. Defaults to the account authenticated via SEP-10
   * if not specified.
   * @param assetId Stellar asset to deposit or withdraw
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   * @return response object from the anchor
   * @throws [AnchorAssetException] if asset was refused by the anchor
   */
  suspend fun deposit(
    assetId: StellarAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>? = null,
    destinationAccount: String? = null,
    destinationMemo: Pair<String, MemoType>? = null,
  ): InteractiveFlowResponse {
    return flow(assetId, authToken, extraFields, destinationAccount, destinationMemo, "deposit") {
      when (assetId) {
        is IssuedAssetId -> it.deposit[assetId.code]
        is NativeAssetId -> it.deposit[NativeAssetId.id]
      }
    }
  }

  /**
   * Available anchor services and information about them. For example, limits, currency, fees,
   * payment methods.
   *
   * @return a list of available anchor services
   * @throws [InvalidAnchorServiceUrl] if provided service URL is not a valid URL
   */
  suspend fun getServicesInfo(): AnchorServiceInfo {
    return anchor.infoHolder.getServicesInfo()
  }

  /**
   * Creates new transaction watcher
   *
   * @param pollDelay poll interval in which requests to the Anchor are being made.
   * @param channelSize size of the Coroutine [Channel]. See
   * [channel documentation](https://kotlinlang.org/docs/coroutines-and-channels.html#channels) for
   * more info about channel size configuration. Be default, unlimited channel is created.
   * @param exceptionHandler handler for exceptions. By default, [RetryExceptionHandler] is being
   * used.
   * @return new transaction watcher
   */
  fun watcher(
    pollDelay: Duration = 5.seconds,
    channelSize: Int = Channel.UNLIMITED,
    exceptionHandler: WalletExceptionHandler = RetryExceptionHandler()
  ): Watcher {
    return Watcher(anchor, pollDelay, channelSize, exceptionHandler)
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
   * time.
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

  private suspend inline fun <reified T> get(
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    return httpClient.anchorGet(anchor.sep1(), authToken, urlBlock)
  }
}
