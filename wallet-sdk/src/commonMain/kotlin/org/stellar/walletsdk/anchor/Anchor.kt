package org.stellar.walletsdk.anchor

import org.stellar.walletsdk.Config
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.toml.TomlInfo

/** Build on/off ramps with anchors. */
expect class Anchor internal constructor(
  cfg: Config,
  homeDomain: String,
) {
  internal val cfg: Config
  internal val homeDomain: String

  /**
   * Get anchor information from a TOML file.
   *
   * @return TOML file content
   */
  suspend fun getInfo(): TomlInfo

  /**
   * Create new auth object to authenticate account with the anchor using SEP-10.
   *
   * @return auth object
   * @throws [AnchorAuthNotSupported] if SEP-10 is not configured
   */
  suspend fun auth(): Auth

  /**
   * Available anchor services and information about them. For example, limits, currency, fees,
   * payment methods.
   *
   * @return a list of available anchor services
   * @throws [ServerRequestFailedException] if network request fails
   * @throws [InvalidAnchorServiceUrl] if provided service URL is not a valid URL
   */
  suspend fun getServicesInfo(): AnchorServiceInfo

  /**
   * Creates new interactive flow for given anchor. It can be used for withdrawal or deposit.
   *
   * @return interactive flow service
   */
  fun interactive(): Interactive

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
  suspend fun getTransaction(transactionId: String, authToken: AuthToken): AnchorTransaction

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
  suspend fun getTransactionsForAsset(asset: AssetId, authToken: AuthToken): List<AnchorTransaction>

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
  ): List<AnchorTransaction>
}