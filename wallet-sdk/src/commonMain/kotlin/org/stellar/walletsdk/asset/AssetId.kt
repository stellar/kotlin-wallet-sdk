@file:JsExport

package org.stellar.walletsdk.asset

import kotlin.js.JsExport
import mu.KotlinLogging

private val log = KotlinLogging.logger {}

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

data class FiatAssetId(override val id: String) : AssetId {
  override val scheme: String
    get() = FIAT_SCHEME

  override fun toString() = sep38
}
