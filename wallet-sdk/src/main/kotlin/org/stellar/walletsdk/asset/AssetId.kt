package org.stellar.walletsdk.asset

import mu.KotlinLogging
import org.stellar.sdk.Asset
import org.stellar.sdk.AssetTypeCreditAlphaNum
import org.stellar.sdk.AssetTypeNative
import org.stellar.sdk.AssetTypePoolShare

val log = KotlinLogging.logger {}

internal const val STELLAR_SCHEME = "stellar"
internal const val FIAT_SCHEME = "iso4217"

sealed interface AssetId {
  val id: String
  val scheme: String
  val sep38: String
    get() = "$scheme:$id"
}

sealed interface StellarAssetId : AssetId {
  override val scheme: String
    get() = STELLAR_SCHEME
}

data class IssuedAssetId(val code: String, val issuer: String) : StellarAssetId {
  override val id = "$code:$issuer"

  override fun toString() = sep38
}

object NativeAssetId : StellarAssetId {
  override val id = "native"
}

typealias XLM = NativeAssetId

@JvmInline
value class FiatAssetId(override val id: String) : AssetId {
  override val scheme: String
    get() = FIAT_SCHEME

  override fun toString() = sep38
}

internal fun StellarAssetId.toAsset(): Asset = Asset.create(this.id)

fun Asset.toAssetId(): StellarAssetId =
  when (this) {
    is AssetTypeNative -> NativeAssetId
    is AssetTypeCreditAlphaNum -> {
      IssuedAssetId(this.code, this.issuer)
    }
    is AssetTypePoolShare -> {
      log.warn { "Pool share is not supported by SDK yet" }

      IssuedAssetId("", "")
      // TODO: add this when we add support for liquidity pools
    }
    else -> {
      throw UnsupportedOperationException("Unknown asset type")
    }
  }
