package org.stellar.walletsdk.utils

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.helpers.objectFromJsonFile

@DisplayName("accountAvailableNativeBalance")
internal class AccountAvailableNativeBalanceTest {
  @Test
  fun `basic funded account has 0 available balance`() {
    val account =
      objectFromJsonFile("src/test/resources/account_basic.json", AccountResponse::class.java)
    val availableBalance = accountAvailableNativeBalance(account)

    assertEquals("0", availableBalance)
  }

  @Test
  fun `more complex account`() {
    val account =
      objectFromJsonFile("src/test/resources/account_full.json", AccountResponse::class.java)
    val availableBalance = accountAvailableNativeBalance(account)

    assertEquals("36.3598684", availableBalance)
  }
}
