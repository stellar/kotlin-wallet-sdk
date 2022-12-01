package org.stellar.walletsdk

import kotlinx.serialization.Serializable
import org.stellar.sdk.AbstractTransaction
import org.stellar.sdk.KeyPair
import org.stellar.sdk.requests.RequestBuilder

data class AccountInfo(
  val publicKey: String,
  val assets: List<FormattedAsset>,
  val liquidityPools: List<FormattedLiquidityPool>,
  val reservedNativeBalance: String,
)

data class AccountSigner(val address: String, val weight: Int)

/**
 * Account weights threshold
 * @param low Low threshold weight
 * @param medium Medium threshold weight
 * @param high High threshold weight
 */
data class AccountThreshold(val low: Int, val medium: Int, val high: Int)

enum class AnchorTransactionType(val type: String) {
  DEPOSIT("deposit"),
  WITHDRAW("withdrawal")
}

enum class AssetType(val type: String) {
  NATIVE("native"),
  ALPHANUM_4("credit_alphanum4"),
  ALPHANUM_12("credit_alphanum12"),
  LIQUIDITY_POOL("liquidity_pool_shares")
}

data class CachedAsset(
  val id: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val amount: String?
)

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

data class FormattedBalances(
  val assets: MutableList<FormattedAsset>,
  val liquidityPools: MutableList<FormattedLiquidityPool>
)

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

data class LiquidityPoolInfo(
  val totalTrustlines: Long,
  val totalShares: String,
  val reserves: MutableList<LiquidityPoolReserve>
)

data class LiquidityPoolReserve(
  val id: String,
  val assetCode: String,
  val assetIssuer: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val amount: String,
)

data class NativeAssetDefault(
  val id: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val assetCode: String,
  val assetIssuer: String,
)

enum class StellarTomlField(val text: String) {
  SIGNING_KEY("SIGNING_KEY"),
  TRANSFER_SERVER_SEP0024("TRANSFER_SERVER_SEP0024"),
  WEB_AUTH_ENDPOINT("WEB_AUTH_ENDPOINT")
}

data class WalletAsset(val id: String, val code: String, val issuer: String)

data class WalletOperation<T>(
  val id: String,
  val date: String,
  val amount: String,
  val account: String,
  val asset: List<WalletAsset>,
  val type: WalletOperationType,
  val rawOperation: T,
)

enum class WalletOperationType() {
  SEND,
  RECEIVE,
  DEPOSIT,
  WITHDRAW,
  SWAP,
  OTHER
}

@JvmInline
value class AccountKeypair(val keyPair: KeyPair) {
  val address: String
    get() = keyPair.accountId

  val secretKey: String
    get() = keyPair.secretSeed.concatToString()
}

fun AbstractTransaction.sign(keyPair: AccountKeypair) {
  this.sign(keyPair.keyPair)
}
