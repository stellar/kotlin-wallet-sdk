package org.stellar.walletsdk

import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.jupiter.api.*
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.anchor.Anchor
import org.stellar.walletsdk.helpers.mapFromTomlFile

internal class AnchorTest {
  private val server = Server(HORIZON_URL)
  private val network = Network(NETWORK_PASSPHRASE)
  private val anchor = Anchor(server, network, AUTH_HOME_DOMAIN)
  private val toml = mapFromTomlFile("stellar.toml")

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
          anchor
            .auth(toml = toml, walletSigner = InProcessWalletSigner())
            .authenticate(ADDRESS_ACTIVE)

        anchor
          .interactive()
          .deposit(
            accountAddress = ADDRESS_ACTIVE,
            SRT,
            authToken = authToken,
          )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }

    @Test
    fun `get interactive deposit URL with different funds account`() {
      val depositResponse = runBlocking {
        val authToken =
          anchor
            .auth(toml = toml, walletSigner = InProcessWalletSigner())
            .authenticate(ADDRESS_ACTIVE)

        anchor
          .interactive()
          .deposit(
            accountAddress = ADDRESS_ACTIVE,
            fundsAccountAddress = ADDRESS_ACTIVE_TWO,
            assetId = SRT,
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
          anchor
            .auth(toml = toml, walletSigner = InProcessWalletSigner())
            .authenticate(ADDRESS_ACTIVE)

        anchor
          .interactive()
          .withdraw(
            accountAddress = ADDRESS_ACTIVE,
            SRT,
            authToken = authToken,
          )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }

    @Test
    fun `get interactive withdrawal URL with different funds account`() {
      val depositResponse = runBlocking {
        val authToken =
          anchor
            .auth(toml = toml, walletSigner = InProcessWalletSigner())
            .authenticate(ADDRESS_ACTIVE)

        anchor
          .interactive()
          .deposit(
            accountAddress = ADDRESS_ACTIVE,
            fundsAccountAddress = ADDRESS_ACTIVE_TWO,
            assetId = SRT,
            authToken = authToken,
          )
      }

      assertDoesNotThrow { depositResponse.url.toHttpUrl() }
    }
  }
}
