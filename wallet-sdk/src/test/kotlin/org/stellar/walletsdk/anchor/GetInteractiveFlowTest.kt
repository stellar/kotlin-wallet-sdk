package org.stellar.walletsdk.anchor

import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.walletsdk.*
import org.stellar.walletsdk.helpers.mapFromTomlFile
import org.stellar.walletsdk.toml.parseToml

internal class GetInteractiveFlowTest {
  private val wallet = TestWallet
  private val anchor = wallet.anchor(AUTH_HOME_DOMAIN)
  private val toml = parseToml(mapFromTomlFile("stellar.toml"))

  @Test
  fun `get interactive deposit URL`() {
    val depositResponse = runBlocking {
      val authToken = anchor.auth().authenticate(ADDRESS_ACTIVE)

      anchor
        .interactive()
        .deposit(
          ASSET_SRT,
          authToken = authToken,
        )
    }

    assertDoesNotThrow { depositResponse.url.toHttpUrl() }
  }

  @Test
  @Disabled // disabled because the test anchor is currently not supporting destination accounts
  // other then the one from the auth token
  fun `get interactive deposit URL with different funds account`() {
    val depositResponse = runBlocking {
      val authToken = anchor.auth().authenticate(ADDRESS_ACTIVE)

      anchor
        .interactive()
        .deposit(
          destinationAccount = ADDRESS_ACTIVE_TWO.address,
          assetId = ASSET_SRT,
          authToken = authToken,
        )
    }

    assertDoesNotThrow { depositResponse.url.toHttpUrl() }
  }

  @Test
  fun `get interactive withdrawal URL`() {
    val depositResponse = runBlocking {
      val authToken = anchor.auth().authenticate(ADDRESS_ACTIVE)

      anchor
        .interactive()
        .withdraw(
          ASSET_SRT,
          authToken = authToken,
        )
    }

    assertDoesNotThrow { depositResponse.url.toHttpUrl() }
  }

  @Test
  @Disabled // disabled because the test anchor is currently not supporting withdrawal accounts
  // other then the one from the auth token
  fun `get interactive withdrawal URL with different funds account`() {
    val depositResponse = runBlocking {
      val authToken = anchor.auth().authenticate(ADDRESS_ACTIVE)

      anchor
        .interactive()
        .deposit(
          destinationAccount = ADDRESS_ACTIVE_TWO.address,
          assetId = ASSET_SRT,
          authToken = authToken,
        )
    }

    assertDoesNotThrow { depositResponse.url.toHttpUrl() }
  }
}
