package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.extension.availableNativeBalance
import org.stellar.walletsdk.helpers.stellarObjectFromJsonFile

@DisplayName("accountAvailableNativeBalance")
internal class AccountAvailableNativeBalanceTest {
  @Test
  fun `basic funded account has 0 available balance`() {
    val account = stellarObjectFromJsonFile<AccountResponse>("account_basic.json")
    val availableBalance = account.availableNativeBalance()

    assertEquals("0", availableBalance)
  }

  @Test
  fun `more complex account`() {
    val account = stellarObjectFromJsonFile<AccountResponse>("account_full.json")
    val availableBalance = account.availableNativeBalance()

    assertEquals("36.3598684", availableBalance)
  }
}
