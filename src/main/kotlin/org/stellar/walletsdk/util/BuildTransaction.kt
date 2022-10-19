package org.stellar.walletsdk.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.stellar.sdk.Network
import org.stellar.sdk.Operation
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

suspend fun buildTransaction(
  sourceAddress: String,
  maxBaseFeeInStroops: Int,
  server: Server,
  network: Network,
  operations: List<Operation>
): Transaction {
  return CoroutineScope(Dispatchers.IO)
    .async {
      val transactionBuilder =
        createTransactionBuilder(
          sourceAddress = sourceAddress,
          maxBaseFeeInStroops = maxBaseFeeInStroops,
          server = server,
          network = network
        )

      return@async transactionBuilder.addOperations(operations).build()
    }
    .await()
}
