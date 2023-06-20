package org.stellar.walletsdk

import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.walletsdk.horizon.SigningKeyPair

class CustomerManagementTest {
  private val wallet =
    Wallet(
      StellarConfiguration.Testnet,
      ApplicationConfiguration { defaultRequest { url { protocol = URLProtocol.HTTP } } }
    )
  private val anchor = wallet.anchor("https://$AUTH_HOME_DOMAIN")
  val keypair =
    SigningKeyPair.fromSecret("SDGEPZ5QOQ24XGCVA274ZL43OKI6NND5CSR4XD4UH6QT3AA33P66FAJW")

  @BeforeEach
  fun setup() {
    System.setProperty("IS_TEST_RUNNING", "true")
  }

  @AfterEach
  fun tearDown() {
    System.clearProperty("IS_TEST_RUNNING")
  }

  // NOTE: making real network calls for now. Mocking SEP-10 server is tricky, so we will need to
  // spend more time on this later.
  @Test
  fun `manage customer`() = runBlocking {
    val token = anchor.auth().authenticate(keypair)
    val customer = anchor.customer(token)
    val testCustomerType = "sep31-receiver"
    val testCustomerAccount = "GDZNFN6JRKKIN2HSV5IOMXPHNWB5EIK2EG4KZK5CQKSJWXSX3CMRJQ52"
    val testPayload =
      mapOf(
        "type" to testCustomerType,
        "first_name" to "John",
        "last_name" to "Doe",
        "address" to "123 Washington Street",
        "city" to "San Francisco",
        "state_or_province" to "CA",
        "address_country_code" to "US",
        "clabe_number" to "1234",
        "bank_number" to "abcd",
        "bank_account_number" to "1234",
        "bank_account_type" to "checking"
      )

    val addCustomerResponse = customer.add(testPayload)
    assertNotNull(addCustomerResponse.id)

    val customerData = customer.getById(addCustomerResponse.id, testCustomerType)
    assertNotNull(customerData)

    assertDoesNotThrow { runBlocking { customer.delete(testCustomerAccount) } }
  }
}
