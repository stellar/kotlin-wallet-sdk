package org.stellar.walletsdk.account

import io.mockk.every
import io.mockk.spyk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.ADDRESS_BASIC
import org.stellar.walletsdk.ADDRESS_FULL
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.TestWallet
import org.stellar.walletsdk.helpers.stellarObjectFromJsonFile

@DisplayName("getInfo")
internal class GetInfoTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val accounts = wallet.stellar().account()

  @Test
  fun `basic account info`() {
    val accountResponse = stellarObjectFromJsonFile<AccountResponse>("account_basic.json")

    every { server.accounts().account(ADDRESS_BASIC) } returns accountResponse

    val accountInfo = runBlocking { accounts.getInfo(ADDRESS_BASIC, server) }

    assertEquals(ADDRESS_BASIC, accountInfo.publicKey)
    assertEquals("1.0000000", accountInfo.reservedNativeBalance)
    assertEquals(1, accountInfo.assets.size)
    assertEquals(0, accountInfo.liquidityPools.size)
  }

  @Test
  fun `full account info`() {
    val accountResponse = stellarObjectFromJsonFile<AccountResponse>("account_full.json")

    every { server.accounts().account(ADDRESS_FULL) } returns accountResponse

    val accountInfo = runBlocking { accounts.getInfo(ADDRESS_FULL, server) }

    assertEquals(ADDRESS_FULL, accountInfo.publicKey)
    assertEquals("6.5000000", accountInfo.reservedNativeBalance)
    assertEquals(4, accountInfo.assets.size)
    assertEquals(0, accountInfo.liquidityPools.size)
  }
}
