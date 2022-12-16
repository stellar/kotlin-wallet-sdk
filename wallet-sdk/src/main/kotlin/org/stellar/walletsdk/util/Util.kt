package org.stellar.walletsdk.util

import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.exception.IncorrectTransactionStatusException

internal object Util {
  fun String.isHex(): Boolean {
    @Suppress("MagicNumber")
    return this.toBigIntegerOrNull(16) != null
  }
}

fun AnchorTransaction.requireStatus(requiredStatus: String) {
  if (this.status != requiredStatus) {
    throw IncorrectTransactionStatusException(this, requiredStatus)
  }
}
