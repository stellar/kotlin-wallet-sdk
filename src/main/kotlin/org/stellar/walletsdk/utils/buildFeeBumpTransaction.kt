package org.stellar.walletsdk.utils

import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun buildFeeBumpTransaction(
  feeAccount: String,
  innerTransaction: Transaction,
  maxFeeInStroops: Long,
  server: Server
): FeeBumpTransaction {
  try {
    server.accounts().account(feeAccount)
  } catch (e: Exception) {
    throw Error("Fee account was not found")
  }

  return FeeBumpTransaction.Builder(innerTransaction)
    .setBaseFee(maxFeeInStroops)
    .setFeeAccount(feeAccount)
    .build()
}
