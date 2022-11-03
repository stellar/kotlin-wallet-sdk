package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.BASE_RESERVE
import org.stellar.walletsdk.BASE_RESERVE_MIN_COUNT

/**
 * Get account's base reserve in XLM.
 *
 * [Learn about Stellar account base reserve](https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/accounts#base-reserves-and-subentries)
 *
 * @param account Account response object
 *
 * @return account's reserved balance
 */
fun accountReservedBalance(account: AccountResponse): String {
  val subEntryCount = account.subentryCount.toBigDecimal()
  val numSponsoring = account.numSponsoring.toBigDecimal()
  val numSponsored = account.numSponsored.toBigDecimal()

  val sellingLiabilities =
    account.balances.find { it.assetType == "native" }?.sellingLiabilities?.get() ?: "0"

  //  (2 + numSubEntries + numSponsoring - numSponsored) * baseReserve + liabilities.selling
  return BigDecimal(BASE_RESERVE_MIN_COUNT)
    .plus(subEntryCount)
    .plus(numSponsoring)
    .minus(numSponsored)
    .times(BASE_RESERVE.toBigDecimal())
    .plus(sellingLiabilities.toBigDecimal())
    .toPlainString()
}
