package org.stellar.walletsdk.utils

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.helpers.objectFromJsonFile

@DisplayName("accountReservedBalance")
internal class AccountReservedBalanceTest {
  @Test
  fun `basic funded account`() {
    val account = objectFromJsonFile("account_basic.json", AccountResponse::class.java)
    val reservedBalance = accountReservedBalance(account)

    assertEquals(1.0, reservedBalance.toDouble())
  }

  @Test
  fun `more complex account`() {
    val account = objectFromJsonFile("account_full.json", AccountResponse::class.java)
    val reservedBalance = accountReservedBalance(account)

    assertEquals(6.5, reservedBalance.toDouble())
  }
}
