package org.stellar.walletsdk.util

import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.exception.IncorrectTransactionStatusException

internal object Util {
  fun String.isHex(): Boolean {
    return this.toBigIntegerOrNull(16) != null
  }
}

fun AnchorTransaction.requireStatus(requiredStatus: TransactionStatus) {
  if (this.status != requiredStatus) {
    throw IncorrectTransactionStatusException(this, requiredStatus)
  }
}
