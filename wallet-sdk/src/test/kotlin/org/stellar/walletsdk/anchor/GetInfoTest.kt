package org.stellar.walletsdk.anchor

import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.AUTH_HOME_DOMAIN
import org.stellar.walletsdk.TestWallet
import org.stellar.walletsdk.helpers.mapFromTomlFile
import org.stellar.walletsdk.toml.parseToml

@DisplayName("getInfo")
internal class GetInfoTest {
  private val wallet = TestWallet
  private val anchor = wallet.anchor(AUTH_HOME_DOMAIN)
  private val toml = parseToml(mapFromTomlFile("stellar.toml"))

  @Test
  fun `fetches TOML for home domain`() {
    assertDoesNotThrow { runBlocking { anchor.getInfo() } }
  }

  @Test
  fun `throws exception if TOML is not found`() {
    val anchorInvalid = wallet.anchor(ADDRESS_ACTIVE.address)

    assertThrows<Exception> { runBlocking { anchorInvalid.getInfo() } }
  }

  @Test
  fun `services should be defined`() {
    assertNotNull(toml.services)
  }

  @Test
  fun `currency should have assetId`() {
    val currency = toml.currencies?.get(0)

    assertNotNull(currency?.assetId)
  }
}
