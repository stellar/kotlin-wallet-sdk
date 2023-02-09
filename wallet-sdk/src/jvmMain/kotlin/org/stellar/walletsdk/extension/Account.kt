package org.stellar.walletsdk.extension

import java.math.BigDecimal
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.BASE_RESERVE
import org.stellar.walletsdk.BASE_RESERVE_MIN_COUNT

/**
 * Get account's native (XLM) balance.
 *
 * @return account's available balance
 */
fun AccountResponse.availableNativeBalance(): String {
  val nativeAmount = balances.find { it.assetType == "native" }!!.balance.toBigDecimal()
  val reservedBalance = reservedBalance().toBigDecimal()
  val availableAmount = nativeAmount.minus(reservedBalance)

  return if (availableAmount <= BigDecimal(0)) {
    return "0"
  } else availableAmount.toPlainString()
}

@Suppress("MaxLineLength")
/**
 * Get account's base reserve in XLM.
 *
 * [Learn about Stellar account base reserve](https://developers.stellar.org/docs/fundamentals-and-concepts/stellar-data-structures/accounts#base-reserves-and-subentries)
 *
 * @return account's reserved balance
 */
fun AccountResponse.reservedBalance(): String {
  val subEntryCount = subentryCount.toBigDecimal()
  val numSponsoring = numSponsoring.toBigDecimal()
  val numSponsored = numSponsored.toBigDecimal()

  val sellingLiabilities =
    balances.find { it.assetType == "native" }?.sellingLiabilities?.get() ?: "0"

  //  (2 + numSubEntries + numSponsoring - numSponsored) * baseReserve + liabilities.selling
  return BigDecimal(BASE_RESERVE_MIN_COUNT)
    .plus(subEntryCount)
    .plus(numSponsoring)
    .minus(numSponsored)
    .times(BASE_RESERVE.toBigDecimal())
    .plus(sellingLiabilities.toBigDecimal())
    .toPlainString()
}
