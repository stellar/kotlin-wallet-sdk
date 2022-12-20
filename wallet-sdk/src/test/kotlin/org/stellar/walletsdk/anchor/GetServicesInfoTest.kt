package org.stellar.walletsdk.anchor

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.stellar.walletsdk.ANCHOR_SERVICE_URL
import org.stellar.walletsdk.AUTH_HOME_DOMAIN
import org.stellar.walletsdk.TestWallet

@DisplayName("getServicesInfo")
internal class GetServicesInfoTest {
  private val wallet = TestWallet
  private val anchor = wallet.anchor(AUTH_HOME_DOMAIN)

  @Test
  fun `fetches info endpoint data`() {
    assertDoesNotThrow { runBlocking { anchor.getServicesInfo(ANCHOR_SERVICE_URL) } }
  }

  @Test
  fun `throws error if network request fails`() {
    assertThrows<Exception> { runBlocking { anchor.getServicesInfo("http://test.org") } }
  }

  @Test
  fun `throws error if service URL is not a valid URL`() {
    assertThrows<Exception> { runBlocking { anchor.getServicesInfo("test.org") } }
  }
}
