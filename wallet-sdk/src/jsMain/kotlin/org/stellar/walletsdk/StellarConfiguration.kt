package org.stellar.walletsdk

actual data class StellarConfiguration(
  actual val network: Network,
  val horizonUrl: String,
  val maxBaseFeeStroops: UInt = 100u,
) {
  actual companion object {
    actual val Testnet =
      StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org")
  }
}

internal actual val defaultBase64Decoder: Base64Decoder = { TODO("") }
