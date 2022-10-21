package org.stellar.walletsdk

const val BASE_RESERVE_MIN_COUNT = 2
const val BASE_RESERVE = 0.5

val XLM_ASSET_DEFAULTS =
  NativeAssetDefaults(
    id = "XLM:native",
    homeDomain = "Native",
    name = "XLM",
    // TODO: add XLM image
    imageUrl = null,
    assetCode = "XLM",
    assetIssuer = "Native",
  )
