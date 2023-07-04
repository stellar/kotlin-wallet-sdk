package org.stellar.walletsdk

import kotlinx.serialization.Serializable
import org.stellar.sdk.requests.RequestBuilder
import org.stellar.walletsdk.asset.AssetId

@Deprecated("Formatted classes are to be removed")
data class AccountInfo(
  val publicKey: String,
  val assets: List<FormattedAsset>,
  val liquidityPools: List<FormattedLiquidityPool>,
  val reservedNativeBalance: String,
)

/**
 * Account weights threshold
 *
 * @param low Low threshold weight
 * @param medium Medium threshold weight
 * @param high High threshold weight
 */
data class AccountThreshold(val low: Int, val medium: Int, val high: Int)

@Deprecated("Formatted classes are to be removed")
enum class AssetType(val type: String) {
  NATIVE("native"),
  ALPHANUM_4("credit_alphanum4"),
  ALPHANUM_12("credit_alphanum12"),
  LIQUIDITY_POOL("liquidity_pool_shares")
}

@Deprecated("Formatted classes are to be removed")
data class CachedAsset(
  val id: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val amount: String?
)

@Deprecated("Formatted classes are to be removed")
data class FormattedAsset(
  val id: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val assetCode: String,
  val assetIssuer: String,
  val balance: String,
  val availableBalance: String,
  val buyingLiabilities: String,
  val sellingLiabilities: String
)

@Deprecated("Formatted classes are to be removed")
data class FormattedBalances(
  val assets: MutableList<FormattedAsset>,
  val liquidityPools: MutableList<FormattedLiquidityPool>
)

@Deprecated("Formatted classes are to be removed")
data class FormattedLiquidityPool(
  val id: String,
  val balance: String,
  val totalTrustlines: Long,
  val totalShares: String,
  val reserves: List<LiquidityPoolReserve>
)

@Serializable
data class InteractiveFlowResponse(
  val id: String,
  val url: String,
  val type: String,
)

enum class Order(internal val builderEnum: RequestBuilder.Order) {
  ASC(RequestBuilder.Order.ASC),
  DESC(RequestBuilder.Order.DESC)
}

@Deprecated("Formatted classes are to be removed")
data class LiquidityPoolInfo(
  val totalTrustlines: Long,
  val totalShares: String,
  val reserves: MutableList<LiquidityPoolReserve>
)

@Deprecated("Formatted classes are to be removed")
data class LiquidityPoolReserve(
  val id: String,
  val assetCode: String,
  val assetIssuer: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val amount: String,
)

@Deprecated("Formatted classes are to be removed")
data class NativeAssetDefault(
  val id: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val assetCode: String,
  val assetIssuer: String,
)

@Deprecated("Formatted classes are to be removed. Use rawOperation instead", replaceWith = ReplaceWith("this.rawOperation"))
data class WalletOperation<T>(
  val id: String,
  val date: String,
  val amount: String,
  val account: String,
  val asset: List<AssetId>,
  val type: WalletOperationType,
  val rawOperation: T,
)

@Deprecated("Formatted classes are to be removed")
enum class WalletOperationType {
  SEND,
  RECEIVE,
  DEPOSIT,
  WITHDRAW,
  SWAP,
  OTHER,
  ERROR
}
