package org.stellar.walletsdk.utils

import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun validateTransaction(transaction: Transaction, server: Server) {
  val sourceAccount =
    try {
      server.accounts().account(transaction.sourceAccount)
    } catch (e: Exception) {
      throw Error("Source account ${transaction.sourceAccount} does not exist")
    }

  val sourceAccountBalance = accountAvailableNativeBalance(sourceAccount).toBigDecimal()
  val transactionFees = stroopsToLumens(transaction.fee.toString()).toBigDecimal()

  if (sourceAccountBalance < transactionFees) {
    throw Error(
      "Source account ${transaction.sourceAccount} does not have enough XLM balance to cover " +
        "${transactionFees.toPlainString()} XLM fees. Available balance ${sourceAccountBalance
                        .toPlainString()} XLM."
    )
  }
}
