package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.sdk.responses.AccountResponse

fun accountAvailableNativeBalance(account: AccountResponse): String {
  val nativeAmount = account.balances.find { it.assetType == "native" }!!.balance.toBigDecimal()
  val reservedBalance = accountReservedBalance(account).toBigDecimal()
  val availableAmount = nativeAmount.minus(reservedBalance)

  return if (availableAmount <= BigDecimal(0)) {
    return "0"
  } else availableAmount.toPlainString()
}
