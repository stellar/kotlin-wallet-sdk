package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.extension.reservedBalance
import org.stellar.walletsdk.helpers.stellarObjectFromJsonFile

@DisplayName("accountReservedBalance")
internal class AccountReservedBalanceTest {
  @Test
  fun `basic funded account`() {
    val account = stellarObjectFromJsonFile<AccountResponse>("account_basic.json")
    val reservedBalance = account.reservedBalance()

    assertEquals(1.0, reservedBalance.toDouble())
  }

  @Test
  fun `more complex account`() {
    val account = stellarObjectFromJsonFile<AccountResponse>("account_full.json")
    val reservedBalance = account.reservedBalance()

    assertEquals(6.5, reservedBalance.toDouble())
  }
}
