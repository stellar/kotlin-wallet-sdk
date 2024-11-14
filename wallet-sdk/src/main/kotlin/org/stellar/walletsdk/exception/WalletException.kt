package org.stellar.walletsdk.exception

import kotlinx.serialization.Serializable
import org.stellar.sdk.exception.NetworkException

@Serializable data class AnchorErrorResponse(val error: String)

sealed class WalletException : Exception {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

class AnchorRequestException(message: String, cause: Exception) : WalletException(message, cause)

class HorizonRequestFailedException(val response: NetworkException) :
  WalletException(response.body ?: response.message ?: "Horizon request failed") {
  val errorCode: Int? = response.code
}
