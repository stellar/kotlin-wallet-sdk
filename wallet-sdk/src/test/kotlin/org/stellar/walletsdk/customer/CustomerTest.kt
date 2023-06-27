package org.stellar.walletsdk.customer

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.walletsdk.*
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.json.toJson

internal class CustomerTest : SuspendTest() {
  private val cfg = TestWallet.cfg

  private fun getToken(): AuthToken {
    return runBlocking {
      Auth(
          cfg,
          webAuthEndpoint = AUTH_ENDPOINT,
          homeDomain = AUTH_HOME_DOMAIN,
          cfg.app.defaultClient
        )
        .authenticate(ADDRESS_ACTIVE)
    }
  }

  @Test
  fun `get customer by id and type`() {
    val token = getToken()
    assertNotNull(token)

    val testCustomerId = "1"
    val testCustomerType = "sep31-receiver"
    val testCustomerPayload =
      """{
   "id": "$testCustomerId",
   "status": "ACCEPTED",
   "provided_fields": {
      "first_name": {
         "description": "The customer's first name",
         "type": "string",
         "status": "ACCEPTED"
      },
      "last_name": {
         "description": "The customer's last name",
         "type": "string",
         "status": "ACCEPTED"
      },
      "email_address": {
         "description": "The customer's email address",
         "type": "string",
         "status": "ACCEPTED"
      }
   }
}"""

    val customer = runBlocking {
      val mockEngine = MockEngine { request ->
        respond(
          content = ByteReadChannel(testCustomerPayload),
          status = HttpStatusCode.Created,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      val httpClient = HttpClient(mockEngine) { install(ContentNegotiation) { json() } }

      Customer(token, AUTH_HOME_DOMAIN, httpClient).getByIdAndType(testCustomerId, testCustomerType)
    }

    assertNotNull(customer)
    assertEquals(testCustomerId, customer.id)
  }

  @Test
  fun `add customer`() {
    val token = getToken()
    assertNotNull(token)

    val testCustomerType = "sep31-receiver"
    val expectedCustomer = AddCustomerResponse("1")
    val testCreateSep9Payload =
      mapOf(
        "first_name" to "John",
        "last_name" to "Doe",
        "email_address" to "jonhdoe@email.com",
      )

    val customer = runBlocking {
      val mockEngine = MockEngine { request ->
        respond(
          content = ByteReadChannel(expectedCustomer.toJson()),
          status = HttpStatusCode.Created,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      val httpClient = HttpClient(mockEngine) { install(ContentNegotiation) { json() } }

      Customer(token, AUTH_HOME_DOMAIN, httpClient)
        .add(sep9Info = testCreateSep9Payload, type = testCustomerType)
    }

    assertNotNull(customer)
    assertEquals(expectedCustomer.id, customer.id)
  }

  @Test
  fun `update customer`() {
    val token = getToken()
    assertNotNull(token)

    val testCustomerType = "sep31-receiver"
    val testSep9Payload =
      mapOf("first_name" to "Allie", "last_name" to "Grater", "email_address" to "allie@email.com")
    val expectedCustomer = AddCustomerResponse("1")

    val customer = runBlocking {
      val mockEngine = MockEngine { request ->
        respond(
          content = ByteReadChannel(expectedCustomer.toJson()),
          status = HttpStatusCode.Created,
          headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
      }

      val httpClient = HttpClient(mockEngine) { install(ContentNegotiation) { json() } }

      Customer(token, AUTH_HOME_DOMAIN, httpClient)
        .add(sep9Info = testSep9Payload, type = testCustomerType)
    }

    assertNotNull(customer)
    assertEquals(expectedCustomer.id, customer.id)
  }

  @Test
  fun `delete customer`() {
    val token = getToken()
    assertNotNull(token)

    val testAccount = "test-account"

    runBlocking {
      val mockEngine = MockEngine { request ->
        respond(
          content = "",
          status = HttpStatusCode.OK,
        )
      }

      val httpClient = HttpClient(mockEngine) { install(ContentNegotiation) { json() } }

      assertDoesNotThrow {
        runBlocking { Customer(token, AUTH_HOME_DOMAIN, httpClient).delete(testAccount) }
      }
    }
  }
}
