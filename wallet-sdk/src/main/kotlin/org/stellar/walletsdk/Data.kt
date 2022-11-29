package org.stellar.walletsdk

import org.stellar.sdk.AbstractTransaction
import org.stellar.sdk.KeyPair

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

data class AnchorServiceAsset(
  val enabled: Boolean,
  val min_amount: Double,
  val max_amount: Double,
  val fee_fixed: Double,
  val fee_percent: Double
)

data class AnchorServiceFeatures(val account_creation: Boolean, val claimable_balances: Boolean)

data class AnchorServiceFee(val enabled: Boolean)

data class AnchorServiceInfo(
  val deposit: Map<String, AnchorServiceAsset>,
  val withdraw: Map<String, AnchorServiceAsset>,
  val fee: AnchorServiceFee,
  val features: AnchorServiceFeatures,
)

data class AnchorTransaction(
  val id: String,
  val kind: String,
  val status: String,
  val more_info_url: String,
  val amount_in: String,
  val amount_out: String,
  val amount_fee: String,
  val started_at: String,
  val stellar_transaction_id: String,
  val from: String,
  val to: String,
)

data class AnchorTransactionStatusResponse(val transaction: AnchorTransaction)

data class AnchorAllTransactionsResponse(val transactions: List<AnchorTransaction>)

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

data class InteractiveFlowResponse(
  val id: String,
  val url: String,
  val type: String,
)

enum class InteractiveFlowType(val value: String) {
  DEPOSIT("deposit"),
  WITHDRAW("withdraw")
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

data class NativeAssetDefaults(
  val id: String,
  val homeDomain: String?,
  val name: String?,
  val imageUrl: String?,
  val assetCode: String,
  val assetIssuer: String,
)

enum class StellarTomlFields(val text: String) {
  SIGNING_KEY("SIGNING_KEY"),
  TRANSFER_SERVER_SEP0024("TRANSFER_SERVER_SEP0024"),
  WEB_AUTH_ENDPOINT("WEB_AUTH_ENDPOINT")
}

@JvmInline
value class AccountKeypair(val keyPair: KeyPair) {
  val publicKeyString: String
    get() = keyPair.accountId

  val secretKey: String
    get() = keyPair.secretSeed.concatToString()
}

fun AbstractTransaction.sign(keyPair: AccountKeypair) {
  this.sign(keyPair.keyPair)
}
