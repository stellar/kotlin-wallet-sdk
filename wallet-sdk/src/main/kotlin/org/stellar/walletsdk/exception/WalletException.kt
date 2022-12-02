package org.stellar.walletsdk.exception

import okhttp3.Response
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.walletsdk.util.toJsonOrNull

data class AnchorErrorResponse(val error: String)

sealed class WalletException : Exception {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

open class ServerRequestFailedException(val response: Response) : WalletException("") {
  private val errorResponse = response.toJsonOrNull<AnchorErrorResponse>()

  val errorCode = response.code
  override val message = errorResponse?.error ?: "Anchor request failed"
}

class HorizonRequestFailedException(val response: ErrorResponse) :
  WalletException(response.body ?: response.message ?: "Horizon request failed") {
  val errorCode = response.code
}

// TODO: delete when TOML is parsed
@Deprecated("To be removed")
class StellarTomlMissingFields(missingFields: List<String>) :
  WalletException("TOML configuration is missing: ${missingFields.joinToString(",")}")
