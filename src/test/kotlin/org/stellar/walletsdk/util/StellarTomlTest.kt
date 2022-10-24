package org.stellar.walletsdk.util

import io.mockk.every
import io.mockk.spyk
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.helpers.objectFromJsonFile

internal class StellarTomlTest {
  private val server = spyk(Server(HORIZON_URL))
  val stellarToml = StellarToml(ADDRESS_ACTIVE, server)

  @Nested
  @DisplayName("getHomeDomain")
  inner class GetHomeDomain {
    @Test
    fun `home domain exists on account`() {
      val account = objectFromJsonFile("account_full.json", AccountResponse::class.java)

      every { server.accounts().account(ADDRESS_ACTIVE) } returns account

      val homeDomain = runBlocking { stellarToml.getHomeDomain() }

      assertEquals("test.org", homeDomain)
    }

    @Test
    fun `throws exception if home domain is not on the account`() {
      val account = objectFromJsonFile("account_basic.json", AccountResponse::class.java)

      every { server.accounts().account(ADDRESS_ACTIVE) } returns account

      assertThrows<Exception> { runBlocking { stellarToml.getHomeDomain() } }
    }
  }

  @Nested
  @DisplayName("buildTomlUrl")
  inner class BuildTomlUrl {
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

  @Nested
  @DisplayName("hasFields")
  inner class HasFields {
    private val tomlFields = mutableMapOf<String, String>()

    @Test
    fun `returns true if all fields exist`() {
      tomlFields["ONE"] = "one"
      tomlFields["TWO"] = "two"
      tomlFields["THREE"] = "three"

      val fieldsToCheck = listOf("ONE", "TWO")
      val hasAllFields = stellarToml.hasFields(fieldsToCheck, tomlFields)

      assertTrue(hasAllFields)
    }

    @Test
    fun `throws exception if any field is missing`() {
      tomlFields["ONE"] = "one"
      tomlFields["THREE"] = "three"

      val fieldsToCheck = listOf("ONE", "TWO")

      assertThrows<Exception> { stellarToml.hasFields(fieldsToCheck, tomlFields) }
    }
  }
}
