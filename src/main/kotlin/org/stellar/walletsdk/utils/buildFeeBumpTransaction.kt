package org.stellar.walletsdk.utils

import java.io.IOException
import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

fun buildFeeBumpTransaction(
  feeAccount: String,
  innerTransaction: Transaction,
  maxBaseFeeInStroops: Long,
  server: Server
): FeeBumpTransaction {
  try {
    server.accounts().account(feeAccount)
  } catch (e: IOException) {
    throw Exception("Fee account was not found")
  }

  return FeeBumpTransaction.Builder(innerTransaction)
    .setBaseFee(maxBaseFeeInStroops)
    .setFeeAccount(feeAccount)
    .build()
}
