package org.stellar.walletsdk.asset

import mu.KotlinLogging
import org.stellar.sdk.Asset
import org.stellar.sdk.AssetTypeCreditAlphaNum
import org.stellar.sdk.AssetTypeNative
import org.stellar.sdk.AssetTypePoolShare

private val log = KotlinLogging.logger {}

internal fun StellarAssetId.toAsset(): Asset = Asset.create(this.id)

fun Asset.toAssetId(): StellarAssetId =
  when (this) {
    is AssetTypeNative -> org.stellar.walletsdk.asset.NativeAssetId
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
