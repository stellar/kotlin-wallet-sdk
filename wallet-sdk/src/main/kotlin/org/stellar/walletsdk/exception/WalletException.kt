package org.stellar.walletsdk.exception

import okhttp3.Response
import org.stellar.walletsdk.util.GsonUtils

data class AnchorErrorResponse(val error: String)

sealed class WalletException : Exception {
  val gson = GsonUtils.instance!!

  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

open class ServerRequestFailedException(response: Response) :
  WalletException("Anchor request failed") {
  private val errorResponse: AnchorErrorResponse =
    gson.fromJson(response.body!!.charStream(), AnchorErrorResponse::class.java)

  val errorCode = response.code
  override val message = errorResponse.error
}

class AnchorRequestFailedException(response: Response) : ServerRequestFailedException(response)

// TODO: create HorizonRequestFailedException

// TODO: delete when TOML is parsed
@Deprecated("To be removed")
class StellarTomlMissingFields(missingFields: List<String>) :
  WalletException("TOML configuration is missing: ${missingFields.joinToString(",")}")
