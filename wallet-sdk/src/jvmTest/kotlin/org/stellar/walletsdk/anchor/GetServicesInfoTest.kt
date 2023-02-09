package org.stellar.walletsdk.anchor

import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.AUTH_HOME_DOMAIN
import org.stellar.walletsdk.TestWallet

internal class GetServicesInfoTest {
  private val wallet = TestWallet

  @Test
  fun `fetches info endpoint data`() {
    runBlocking { wallet.anchor(Url("https://$AUTH_HOME_DOMAIN")).getServicesInfo() }
  }
}
