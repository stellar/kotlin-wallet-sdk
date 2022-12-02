package org.stellar.walletsdk.extension

import org.stellar.sdk.*
import org.stellar.walletsdk.exception.AccountNotEnoughBalanceException
import org.stellar.walletsdk.exception.HorizonRequestFailedException
import org.stellar.walletsdk.util.availableNativeBalance
import org.stellar.walletsdk.util.stroopsToLumens

/**
 * Helper to validate that the transaction's source account has enough native (XLM) balance to cover
 * the fees.
 *
 * @param server Horizon [Server] instance
 *
 * @throws [AccountNotEnoughBalanceException] if there is not enough native balance
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
suspend fun Transaction.validateSufficientBalance(server: Server) {
  val sourceAccount = server.accountByAddress(sourceAccount)
  val sourceAccountBalance = sourceAccount.availableNativeBalance().toBigDecimal()
  val transactionFees = stroopsToLumens(fee.toString()).toBigDecimal()

  if (sourceAccountBalance < transactionFees) {
    throw AccountNotEnoughBalanceException(
      accountAddress = sourceAccount.accountId,
      accountBalance = sourceAccountBalance,
      transactionFees = transactionFees
    )
  }
}

/**
 * Fee-bump a transaction to cover the fees (sponsoring) or adjust fees on a pre-authorized
 * transaction.
 *
 * [Learn about fee-bump transactions](https://developers.stellar.org/docs/encyclopedia/fee-bump-transactions)
 *
 * @param feeAccount Stellar address of the account covering the fees
 * @param innerTransaction Signed transaction to be covered
 * @param maxBaseFeeInStroops Maximum base fee in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop)
 * @param server Horizon [Server] instance
 *
 * @return fee-bumped transaction
 *
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
suspend fun buildFeeBumpTransaction(
  feeAccount: String,
  innerTransaction: Transaction,
  maxBaseFeeInStroops: Long,
  server: Server
): FeeBumpTransaction {
  // TODO: is an extra call needed here?
  server.accountByAddress(feeAccount)

  return FeeBumpTransaction.Builder(innerTransaction)
    .setBaseFee(maxBaseFeeInStroops)
    .setFeeAccount(feeAccount)
    .build()
}

/**
 * Build a transaction to be signed and submitted on the Stellar network.
 *
 * @param sourceAddress Stellar address of the account that originates the transaction. It also
 * provides the fee and sequence number for the transaction.
 * @param maxBaseFeeInStroops Maximum base fee in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop)
 * @param server Horizon [Server] instance
 * @param network Stellar [Network] instance
 * @param operations A list of operations in the transaction
 *
 * @return transaction
 *
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
@Deprecated("To be removed")
suspend fun buildTransaction(
  sourceAddress: String,
  maxBaseFeeInStroops: Int,
  server: Server,
  network: Network,
  operations: List<Operation>
): Transaction {
  val transactionBuilder =
    createTransactionBuilder(
      sourceAddress = sourceAddress,
      maxBaseFeeInStroops = maxBaseFeeInStroops,
      server = server,
      network = network
    )

  return transactionBuilder.addOperations(operations).build()
}

/**
 * Helper method to build a transaction.
 *
 * @param sourceAddress Stellar address of the account that originates the transaction. It also
 * provides the fee and sequence number for the transaction.
 * @param maxBaseFeeInStroops Maximum base fee in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop)
 * @param server Horizon [Server] instance
 * @param network Stellar [Network] instance
 *
 * @return transaction builder
 *
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
suspend fun createTransactionBuilder(
  sourceAddress: String,
  maxBaseFeeInStroops: Int,
  server: Server,
  network: Network,
): TransactionBuilder {
  // TODO: accept AccountResponse as an argument to not re-fetch account
  val sourceAccount = server.accountByAddress(sourceAddress)

  // TODO: add memo
  // TODO: add time bounds

  return Transaction.Builder(sourceAccount, network).setBaseFee(maxBaseFeeInStroops).setTimeout(180)
}
