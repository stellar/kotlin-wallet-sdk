package org.stellar.walletsdk

import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.jupiter.api.*
import org.stellar.sdk.Network
import org.stellar.sdk.Server

internal class AnchorTest {
  private val server = Server(HORIZON_URL)
  private val network = Network(NETWORK_PASSPHRASE)
  private val anchor = Anchor(server, network)

  // NOTE: Tests are running on live test network for SRT asset

  @Nested
  @DisplayName("getInfo")
  inner class GetInfo {
    @Test
    fun `fetches TOML for asset issuer`() {
      assertDoesNotThrow { runBlocking { anchor.getInfo(ANCHOR_ASSET_ISSUER) } }
    }

    @Test
    fun `throws exception if TOML is not found`() {
      assertThrows<Exception> { runBlocking { anchor.getInfo(ADDRESS_ACTIVE) } }
    }
  }

  @Nested
  @DisplayName("getServicesInfo")
  inner class GetServicesInfo {
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

  @Nested
  @DisplayName("getInteractiveDeposit")
  inner class GetInteractiveDeposit {
    @Test
    fun `get interactive deposit URL`() {
      val depositResponse = runBlocking {
        anchor.getInteractiveDeposit(
          accountAddress = ADDRESS_ACTIVE,
          assetCode = ANCHOR_ASSET_CODE,
          assetIssuer = ANCHOR_ASSET_ISSUER,
          walletSigner = InProcessWalletSigner(),
        )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }
  }
}
