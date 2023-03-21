package org.stellar.walletsdk

import deezer.kustomexport.KustomExport

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

@KustomExport
fun testnetWallet(): Wallet {
  return Wallet(StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org"))
}
