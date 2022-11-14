package org.stellar.walletsdk

import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.jupiter.api.*
import org.stellar.sdk.Network
import org.stellar.sdk.Server

internal class AnchorTest {
  private val server = Server(HORIZON_URL)
  private val network = Network(NETWORK_PASSPHRASE)
  private val anchor = Anchor(server, network, AUTH_HOME_DOMAIN)

  // NOTE: Tests are running on live test network for SRT asset

  @Nested
  @DisplayName("getInfo")
  inner class GetInfo {
    @Test
    fun `fetches TOML for home domain`() {
      assertDoesNotThrow { runBlocking { anchor.getInfo() } }
    }

    @Test
    fun `throws exception if TOML is not found`() {
      val anchorInvalid = Anchor(server, network, ADDRESS_ACTIVE)

      assertThrows<Exception> { runBlocking { anchorInvalid.getInfo() } }
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
        val authToken =
          anchor.getAuthToken(
            accountAddress = ADDRESS_ACTIVE,
            walletSigner = InProcessWalletSigner()
          )

        anchor.getInteractiveDeposit(
          accountAddress = ADDRESS_ACTIVE,
          assetCode = ANCHOR_ASSET_CODE,
          homeDomain = ANCHOR_HOME_DOMAIN,
          authToken = authToken,
        )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }

    @Test
    fun `get interactive deposit URL with different funds account`() {
      val depositResponse = runBlocking {
        val authToken =
          anchor.getAuthToken(
            accountAddress = ADDRESS_ACTIVE,
            walletSigner = InProcessWalletSigner()
          )

        anchor.getInteractiveDeposit(
          accountAddress = ADDRESS_ACTIVE,
          fundsAccountAddress = ADDRESS_ACTIVE_TWO,
          assetCode = ANCHOR_ASSET_CODE,
          homeDomain = ANCHOR_HOME_DOMAIN,
          authToken = authToken,
        )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }
  }

  @Nested
  @DisplayName("getInteractiveWithdrawal")
  inner class GetInteractiveWithdrawal {
    @Test
    fun `get interactive withdrawal URL`() {
      val depositResponse = runBlocking {
        val authToken =
          anchor.getAuthToken(
            accountAddress = ADDRESS_ACTIVE,
            walletSigner = InProcessWalletSigner()
          )

        anchor.getInteractiveWithdrawal(
          accountAddress = ADDRESS_ACTIVE,
          assetCode = ANCHOR_ASSET_CODE,
          homeDomain = ANCHOR_HOME_DOMAIN,
          authToken = authToken,
        )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }

    @Test
    fun `get interactive withdrawal URL with different funds account`() {
      val depositResponse = runBlocking {
        val authToken =
          anchor.getAuthToken(
            accountAddress = ADDRESS_ACTIVE,
            walletSigner = InProcessWalletSigner()
          )

        anchor.getInteractiveWithdrawal(
          accountAddress = ADDRESS_ACTIVE,
          fundsAccountAddress = ADDRESS_ACTIVE_TWO,
          assetCode = ANCHOR_ASSET_CODE,
          homeDomain = ANCHOR_HOME_DOMAIN,
          authToken = authToken,
        )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }
  }
}
