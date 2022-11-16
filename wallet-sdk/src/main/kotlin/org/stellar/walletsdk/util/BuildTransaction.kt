package org.stellar.walletsdk.util

import kotlinx.coroutines.coroutineScope
import org.stellar.sdk.Network
import org.stellar.sdk.Operation
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.AccountNotFoundException

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
 * @throws [AccountNotFoundException] when source account is not found
 */
suspend fun buildTransaction(
  sourceAddress: String,
  maxBaseFeeInStroops: Int,
  server: Server,
  network: Network,
  operations: List<Operation>
): Transaction = coroutineScope {
  val transactionBuilder =
    createTransactionBuilder(
      sourceAddress = sourceAddress,
      maxBaseFeeInStroops = maxBaseFeeInStroops,
      server = server,
      network = network
    )

  transactionBuilder.addOperations(operations).build()
}
