package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import okhttp3.OkHttpClient
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.toml.StellarToml

@DisplayName("stellarToml")
internal class StellarTomlTest {
  val stellarToml = StellarToml("https", ADDRESS_ACTIVE.address, OkHttpClient())

  @Test
  fun `basic home domain`() {
    val tomlUrl = stellarToml.buildTomlUrl("test.org")

    assertEquals("https://test.org/.well-known/stellar.toml", tomlUrl)
  }

  @Test
  fun `home domain with subdomain`() {
    val tomlUrl = stellarToml.buildTomlUrl("sub.test.org")

    assertEquals("https://sub.test.org/.well-known/stellar.toml", tomlUrl)
  }

  @Test
  fun `home domain with port`() {
    val tomlUrl = stellarToml.buildTomlUrl("test.org:1234")

    assertEquals("https://test.org:1234/.well-known/stellar.toml", tomlUrl)
  }

  @Test
  fun `home domain with subdomain and port`() {
    val tomlUrl = stellarToml.buildTomlUrl("sub.test.org:1234")

    assertEquals("https://sub.test.org:1234/.well-known/stellar.toml", tomlUrl)
  }
}
