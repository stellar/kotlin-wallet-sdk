package org.stellar.walletsdk.exception

import okhttp3.Response
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.walletsdk.json.toJsonOrNull

open class ServerRequestFailedException(val response: Response) : WalletException("") {
  private val errorResponse = response.toJsonOrNull<AnchorErrorResponse>()

  val errorCode = response.code
  override val message = errorResponse?.error ?: "Anchor request failed"
}

class HorizonRequestFailedException(val response: ErrorResponse) :
  WalletException(response.body ?: response.message ?: "Horizon request failed") {
  val errorCode = response.code
}
