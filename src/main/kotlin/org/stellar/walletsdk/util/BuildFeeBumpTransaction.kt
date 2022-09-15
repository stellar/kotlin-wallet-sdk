package org.stellar.walletsdk.util

import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun buildFeeBumpTransaction(
  feeAccount: String,
  innerTransaction: Transaction,
  maxBaseFeeInStroops: Long,
  server: Server
): FeeBumpTransaction {
  fetchAccount(feeAccount, server)

  return FeeBumpTransaction.Builder(innerTransaction)
    .setBaseFee(maxBaseFeeInStroops)
    .setFeeAccount(feeAccount)
    .build()
}
