package org.stellar.walletsdk.util

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.exception.AnchorRequestException
import org.stellar.walletsdk.util.Util.postJson

@Serializable internal data class DummyRequest(val foo: String)

@Serializable internal data class DummyResponse(val bar: String)

internal class FromJsonOrErrorTest {

  private fun clientReturning(
    body: String,
    status: HttpStatusCode = HttpStatusCode.OK
  ): HttpClient {
    val engine = MockEngine { _ ->
      respond(
        content = ByteReadChannel(body),
        status = status,
        headers = headersOf(HttpHeaders.ContentType, "application/json")
      )
    }
    return HttpClient(engine) { install(ContentNegotiation) { json() } }
  }

  @Test
  fun `exception message does not include raw response body when JSON is malformed`() {
    val sensitiveToken = "eyJ.VALID_TOKEN_PAYLOAD.SIGNATURE"
    val malformedBody = """{"token": "$sensitiveToken", "malformed_field": }"""
    val client = clientReturning(malformedBody)

    val ex =
      assertFailsWith<AnchorRequestException> {
        runBlocking {
          client.postJson<DummyRequest, DummyResponse>("https://example.com", DummyRequest("x"))
        }
      }

    val message = ex.message ?: ""
    assertFalse(
      message.contains(sensitiveToken),
      "exception message must not leak the response body; got: $message"
    )
    assertFalse(
      message.contains(malformedBody),
      "exception message must not leak the raw response body; got: $message"
    )
  }

  @Test
  fun `anchor error responses still surface the server-provided error`() {
    val errorBody = """{"error": "user not eligible"}"""
    val client = clientReturning(errorBody, HttpStatusCode.BadRequest)

    val ex =
      assertFailsWith<AnchorRequestException> {
        runBlocking {
          client.postJson<DummyRequest, DummyResponse>("https://example.com", DummyRequest("x"))
        }
      }

    assertTrue(
      (ex.message ?: "").contains("user not eligible"),
      "anchor error message should be propagated; got: ${ex.message}"
    )
  }
}
