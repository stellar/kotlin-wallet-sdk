package org.stellar.walletsdk
expect class StellarConfiguration {
  val network: Network
  companion object {
    val Testnet: StellarConfiguration
  }
}

internal expect val defaultBase64Decoder: Base64Decoder
