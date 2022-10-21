package org.stellar.walletsdk

data class AccountInfo(
  val publicKey: String,
  val assets: List<FormattedAsset>,
  val liquidityPools: List<FormattedLiquidityPool>,
  val reservedNativeBalance: String,
)

data class AccountSigner(val address: String, val weight: Int)

data class AccountThreshold(val low: Int, val medium: Int, val high: Int)

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

data class RecoveryServer(
  val endpoint: String,
  val authEndpoint: String,
  val stellarAddress: String,
  val homeDomain: String,
)

data class RecoveryServerAuth(
  val endpoint: String,
  val signerAddress: String,
  val authToken: String,
)

data class RecoveryAccount(
  val address: String,
  val identities: List<RecoveryAccountRole>,
  val signers: List<RecoveryAccountSigner>
)

data class RecoveryIdentities(val identities: List<RecoveryAccountIdentity>)

data class RecoveryAccountRole(val role: String, val authenticated: Boolean?)

data class RecoveryAccountSigner(val key: String)

data class RecoveryAccountAuthMethod(
  val type: String,
  val value: String,
)

data class RecoveryAccountIdentity(
  val role: String,
  val auth_methods: List<RecoveryAccountAuthMethod>,
)

data class SignerWeight(
  val master: Int,
  val recoveryServer: Int,
)
