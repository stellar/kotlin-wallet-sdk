package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.sdk.responses.AccountResponse

/**
 * Get account's native (XLM) balance.
 *
 * @param account Account response object
 *
 * @return account's available balance
 */
fun accountAvailableNativeBalance(account: AccountResponse): String {
  val nativeAmount = account.balances.find { it.assetType == "native" }!!.balance.toBigDecimal()
  val reservedBalance = accountReservedBalance(account).toBigDecimal()
  val availableAmount = nativeAmount.minus(reservedBalance)

  return if (availableAmount <= BigDecimal(0)) {
    return "0"
  } else availableAmount.toPlainString()
}
