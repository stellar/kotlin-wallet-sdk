package org.stellar.walletsdk
actual class StellarConfiguration {
  actual companion object {
    actual val Testnet: StellarConfiguration
      get() = TODO("Not yet implemented")
  }

  actual val network: Network
    get() = TODO("Not yet implemented")
}

internal actual val defaultBase64Decoder: Base64Decoder = { TODO("") }
