package org.stellar.walletsdk.util

import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.AccountNotEnoughBalanceException

suspend fun validateSufficientBalance(transaction: Transaction, server: Server) {
  val sourceAccount = fetchAccount(transaction.sourceAccount, server)
  val sourceAccountBalance = accountAvailableNativeBalance(sourceAccount).toBigDecimal()
  val transactionFees = stroopsToLumens(transaction.fee.toString()).toBigDecimal()

  if (sourceAccountBalance < transactionFees) {
    throw AccountNotEnoughBalanceException(
      accountAddress = transaction.sourceAccount,
      accountBalance = sourceAccountBalance,
      transactionFees = transactionFees
    )
  }
}
