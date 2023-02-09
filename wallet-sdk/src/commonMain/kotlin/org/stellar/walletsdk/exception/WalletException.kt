package org.stellar.walletsdk.exception

data class AnchorErrorResponse(val error: String)

open class WalletException : Exception {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

// TODO: delete when TOML is parsed
@Deprecated("To be removed")
class StellarTomlMissingFields(missingFields: List<String>) :
  WalletException("TOML configuration is missing: ${missingFields.joinToString(",")}")
