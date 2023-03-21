package org.stellar.walletsdk
expect class StellarConfiguration {
  companion object {
    val Testnet: StellarConfiguration
  }
}

internal expect val defaultBase64Decoder: Base64Decoder
