package org.stellar.walletsdk.exception

import okhttp3.Response
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.walletsdk.util.toJson

data class AnchorErrorResponse(val error: String)

sealed class WalletException : Exception {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

open class ServerRequestFailedException(response: Response) :
  WalletException("Anchor request failed") {
  private val errorResponse: AnchorErrorResponse = response.toJson()

  val errorCode = response.code
  override val message = errorResponse.error
}

class HorizonRequestFailedException(response: ErrorResponse) :
  WalletException("Horizon request failed") {
  val errorCode = response.code
  override val message = response.body ?: response.message
}

class AnchorRequestFailedException(response: Response) : ServerRequestFailedException(response)

// TODO: delete when TOML is parsed
@Deprecated("To be removed")
class StellarTomlMissingFields(missingFields: List<String>) :
  WalletException("TOML configuration is missing: ${missingFields.joinToString(",")}")
