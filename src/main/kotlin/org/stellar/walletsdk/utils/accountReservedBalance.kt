package org.stellar.walletsdk.utils

import java.math.BigDecimal
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.BASE_RESERVE
import org.stellar.walletsdk.BASE_RESERVE_MIN_COUNT

fun accountReservedBalance(account: AccountResponse): String {
  val subEntryCount = account.subentryCount.toBigDecimal()
  val numSponsoring = account.numSponsoring.toBigDecimal()
  val numSponsored = account.numSponsored.toBigDecimal()

  val buyingLiabilities =
    account.balances.find { it.assetType == "native" }?.buyingLiabilities?.get() ?: "0"

  return BigDecimal(BASE_RESERVE_MIN_COUNT)
    .plus(subEntryCount)
    .plus(numSponsoring)
    .minus(numSponsored)
    .times(BASE_RESERVE.toBigDecimal())
    .plus(buyingLiabilities.toBigDecimal())
    .toPlainString()
}
