package org.stellar.walletsdk

import org.stellar.walletsdk.js.WalletJS

@JsExport
actual data class StellarConfiguration(
  actual val network: Network,
  val horizonUrl: String,
  val maxBaseFeeStroops: Int = 100,
) {
  actual companion object {
    actual val Testnet =
      StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org")
  }
}

internal actual val defaultBase64Decoder: Base64Decoder = { TODO("") }

@JsExport
fun testnetWallet(): WalletJS {
  return WalletJS(StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org"))
}
