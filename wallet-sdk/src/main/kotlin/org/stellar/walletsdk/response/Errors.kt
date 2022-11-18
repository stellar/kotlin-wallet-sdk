package org.stellar.walletsdk.response

import kotlin.Exception

sealed interface Error {
  val message: String
}

sealed interface WalletError : Error

class InvalidStartingBalanceError : WalletError {
  override val message = "Starting balance must be at least 1 XLM for non-sponsored accounts"
}

@JvmInline
value class GenericError(val e: Exception) : Error {
  override val message: String
    get() = e.message ?: ""
}
