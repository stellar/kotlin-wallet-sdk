package org.stellar.walletsdk.util

import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.AccountNotEnoughBalanceException

/**
 * Helper to validate that the transaction's source account has enough native (XLM) balance to cover
 * the fees.
 *
 * @param transaction Transaction to validate
 * @param server Horizon [Server] instance
 *
 * @throws [AccountNotEnoughBalanceException] if there is not enough native balance
 */
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
