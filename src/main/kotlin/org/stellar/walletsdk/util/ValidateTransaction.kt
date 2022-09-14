package org.stellar.walletsdk.util

import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun validateSufficientBalance(transaction: Transaction, server: Server) {
  val sourceAccount = fetchAccount(transaction.sourceAccount, server)
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
