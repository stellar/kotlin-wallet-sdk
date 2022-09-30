package org.stellar.walletsdk.util

import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.TransactionBuilder

suspend fun createTransactionBuilder(
  sourceAddress: String,
  maxBaseFeeInStroops: Int,
  server: Server,
  network: Network,
): TransactionBuilder {
  val sourceAccount = fetchAccount(sourceAddress, server)

  // TODO: add memo
  // TODO: add time bounds

  return Transaction.Builder(sourceAccount, network).setBaseFee(maxBaseFeeInStroops).setTimeout(180)
}
