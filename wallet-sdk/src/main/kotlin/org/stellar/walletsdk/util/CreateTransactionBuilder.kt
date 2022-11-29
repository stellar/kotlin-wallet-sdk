package org.stellar.walletsdk.util

import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.TransactionBuilder
import org.stellar.walletsdk.exception.AccountNotFoundException

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
 * @throws [AccountNotFoundException] when source account is not found
 */
suspend fun createTransactionBuilder(
  sourceAddress: String,
  maxBaseFeeInStroops: Int,
  server: Server,
  network: Network,
): TransactionBuilder {
  // TODO: accept AccountResponse as an argument to not re-fetch account
  val sourceAccount = fetchAccount(sourceAddress, server)

  // TODO: add memo
  // TODO: add time bounds

  return Transaction.Builder(sourceAccount, network).setBaseFee(maxBaseFeeInStroops).setTimeout(180)
}
