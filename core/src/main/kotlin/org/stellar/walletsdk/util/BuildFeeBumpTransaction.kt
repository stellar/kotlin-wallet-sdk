package org.stellar.walletsdk.util

import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.AccountNotFoundException

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
 * @throws [AccountNotFoundException] when fee account is not found
 */
suspend fun buildFeeBumpTransaction(
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
