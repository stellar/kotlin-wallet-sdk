package org.stellar.walletsdk.anchor

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.walletsdk.AUTH_HOME_DOMAIN
import org.stellar.walletsdk.TestWallet

internal class GetServicesInfoTest {
  private val wallet = TestWallet

  @Test
  fun `fetches info endpoint data`() {
    assertDoesNotThrow { runBlocking { wallet.anchor(AUTH_HOME_DOMAIN).getServicesInfo() } }
  }
}
