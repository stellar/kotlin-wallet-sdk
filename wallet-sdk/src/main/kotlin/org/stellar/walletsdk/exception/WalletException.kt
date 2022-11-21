package org.stellar.walletsdk.exception

import okhttp3.Response

sealed class WalletException : Exception {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

// TODO: add arguments for error code, response message, etc.
class NetworkRequestFailedException(
  rawResponse: Response,
  label: String? = "Request failed",
) : WalletException("$label: $rawResponse")

// TODO: delete when TOML is parsed
@Deprecated("To be removed")
class StellarTomlMissingFields(missingFields: List<String>) :
  WalletException("TOML configuration is missing: ${missingFields.joinToString(",")}")
