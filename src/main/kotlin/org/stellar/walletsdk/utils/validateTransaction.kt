package org.stellar.walletsdk.utils

import java.io.IOException
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun validateTransaction(transaction: Transaction, server: Server) {
  val sourceAccount =
    try {
      server.accounts().account(transaction.sourceAccount)
    } catch (e: IOException) {
      throw Exception("Source account ${transaction.sourceAccount} does not exist")
    }

  val sourceAccountBalance = accountAvailableNativeBalance(sourceAccount).toBigDecimal()
  val transactionFees = stroopsToLumens(transaction.fee.toString()).toBigDecimal()

  if (sourceAccountBalance < transactionFees) {
    throw Exception(
      "Source account ${transaction.sourceAccount} does not have enough XLM balance to cover " +
        "${transactionFees.toPlainString()} XLM fees. Available balance ${sourceAccountBalance
                        .toPlainString()} XLM."
    )
  }
}
