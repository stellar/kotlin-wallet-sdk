package org.stellar.walletsdk.utils

import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun buildFeeBumpTransaction(
  feeAccount: String,
  innerTransaction: Transaction,
  maxBaseFeeInStroops: Long,
  server: Server
): FeeBumpTransaction {
  fetchAccountFromAddress(feeAccount, server)

  return FeeBumpTransaction.Builder(innerTransaction)
    .setBaseFee(maxBaseFeeInStroops)
    .setFeeAccount(feeAccount)
    .build()
}
