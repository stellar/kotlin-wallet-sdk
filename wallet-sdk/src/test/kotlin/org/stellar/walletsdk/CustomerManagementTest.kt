package org.stellar.walletsdk

import io.ktor.client.plugins.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.walletsdk.anchor.auth
import org.stellar.walletsdk.anchor.customer
import org.stellar.walletsdk.customer.Sep12Status
import org.stellar.walletsdk.horizon.SigningKeyPair

class CustomerManagementTest {
  private val wallet = Wallet(StellarConfiguration.Testnet)
  private val anchor = wallet.anchor(AUTH_HOME_DOMAIN)
  val keypair =
    SigningKeyPair.fromSecret("SDGEPZ5QOQ24XGCVA274ZL43OKI6NND5CSR4XD4UH6QT3AA33P66FAJW")

  // NOTE: making real network calls for now. Mocking SEP-10 server is tricky, so we will need to
  // spend more time on this later.
  @Test
  fun `manage customer`() {
    runBlocking {
      val token = anchor.auth().authenticate(keypair)
      val customer = anchor.customer(token)
      val testCustomerType = "sep31-receiver"
      val testCustomerAccount = token.account
      val testCreateSep9Payload =
        mapOf(
          "first_name" to "John",
          "last_name" to "Doe",
          "email_address" to "jonhdoe@email.com",
        )
      val testUpdateSep9Payload =
        mapOf(
          "address" to "123 Washington Street",
          "city" to "San Francisco",
          "state_or_province" to "CA",
          "address_country_code" to "US",
          "clabe_number" to "1234",
          "bank_number" to "abcd",
          "bank_account_number" to "1234",
          "bank_account_type" to "checking"
        )

      val addCustomerResponse =
        customer.add(
          sep9Info = testCreateSep9Payload,
          type = testCustomerType,
        )
      assertNotNull(addCustomerResponse.id)

      var customerData = customer.get(id = addCustomerResponse.id, type = testCustomerType)
      assertNotNull(customerData)
      assertEquals(customerData.providedFields?.get("first_name")?.status, Sep12Status.ACCEPTED)
      assertEquals(customerData.providedFields?.get("last_name")?.status, Sep12Status.ACCEPTED)
      assertEquals(customerData.providedFields?.get("email_address")?.status, Sep12Status.ACCEPTED)
      assertNull(customerData.providedFields?.get("bank_number"))

      val updateCustomerResponse =
        customer.update(
          sep9Info = testUpdateSep9Payload,
          id = addCustomerResponse.id,
          type = testCustomerType,
        )
      assertNotNull(updateCustomerResponse.id)

      customerData = customer.get(addCustomerResponse.id, type = testCustomerType)
      assertNotNull(customerData)
      assertEquals(customerData.providedFields?.get("bank_number")?.status, Sep12Status.ACCEPTED)
      assertEquals(
        customerData.providedFields?.get("bank_account_number")?.status,
        Sep12Status.ACCEPTED
      )

      assertDoesNotThrow { runBlocking { customer.delete(testCustomerAccount) } }
      assertFailsWith<ClientRequestException> {
        runBlocking { customer.get(addCustomerResponse.id, type = testCustomerType) }
      }
      assertFailsWith<ClientRequestException> {
        runBlocking { customer.delete(testCustomerAccount) }
      }
    }
  }
}
