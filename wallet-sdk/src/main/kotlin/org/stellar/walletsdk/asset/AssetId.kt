package org.stellar.walletsdk.asset

import org.stellar.sdk.Asset

sealed interface AssetId {
  val id: String
  val scheme: String
  val sep38: String
    get() = "$scheme:$id"
}

sealed interface StellarAssetId : AssetId {
  override val scheme: String
    get() = "stellar"
}

data class IssuedAssetId(val code: String, val issuer: String) : StellarAssetId {
  override val id = "$code:$issuer"

  override fun toString() = sep38
}

object NativeAssetId : StellarAssetId {
  override val id = "native"
}

@JvmInline
value class FiatAssetId(override val id: String) : AssetId {
  override val scheme: String
    get() = "iso4217"

  override fun toString() = sep38
}

internal fun StellarAssetId.toAsset(): Asset = Asset.create(this.id)
