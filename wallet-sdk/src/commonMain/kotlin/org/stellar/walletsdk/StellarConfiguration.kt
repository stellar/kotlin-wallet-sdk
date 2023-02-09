package org.stellar.walletsdk
expect class StellarConfiguration {
  fun isPublic(): Boolean
}

internal expect val defaultBase64Decoder: Base64Decoder
