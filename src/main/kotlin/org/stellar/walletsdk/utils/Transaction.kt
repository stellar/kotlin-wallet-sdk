package org.stellar.walletsdk.utils

import org.stellar.sdk.Network
import org.stellar.sdk.Operation
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.responses.AccountResponse

class Transaction {
  companion object {
    fun buildTransaction(
      sourceAddress: String,
      server: Server,
      network: Network,
      operations: List<Operation>
    ): Transaction {
      val sourceAccount: AccountResponse?

      try {
        sourceAccount = server.accounts().account(sourceAddress)
      } catch (e: Error) {
        throw Error("Source account was not found")
      }

      // TODO: add memo
      // TODO: update max fee
      // TODO: add time bounds

      return Transaction.Builder(sourceAccount, network)
        .addOperations(operations)
        .setBaseFee(500)
        .setTimeout(180)
        .build()
    }
  }
}
