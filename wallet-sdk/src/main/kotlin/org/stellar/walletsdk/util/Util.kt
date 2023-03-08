package org.stellar.walletsdk.util

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.AnchorInteractiveFlowNotSupported
import org.stellar.walletsdk.exception.IncorrectTransactionStatusException
import org.stellar.walletsdk.toml.TomlInfo

internal object Util {
  internal fun String.isHex(): Boolean {
    return this.toBigIntegerOrNull(16) != null
  }

  internal suspend inline fun <reified T> HttpClient.anchorGet(
    info: TomlInfo,
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    val urlBuilder =
      URLBuilder(
        info.services.sep24?.transferServerSep24 ?: throw AnchorInteractiveFlowNotSupported
      )
    urlBuilder.urlBlock()
    return this.get(urlBuilder.build()) {
        if (authToken != null) {
          headers { append(HttpHeaders.Authorization, "Bearer $authToken") }
        }
      }
      .body()
  }

  internal suspend inline fun <reified Req, reified Resp> HttpClient.postJson(
    url: String,
    requestBody: Req,
    authToken: AuthToken? = null,
    block: HttpRequestBuilder.() -> Unit = {}
  ): Resp {
    return this.post(url) {
        contentType(ContentType.Application.Json)
        setBody(requestBody)
        if (authToken != null) {
          headers { append(HttpHeaders.Authorization, "Bearer $authToken") }
        }
        block()
      }
      .body()
  }
}

fun AnchorTransaction.requireStatus(requiredStatus: TransactionStatus) {
  if (this.status != requiredStatus) {
    throw IncorrectTransactionStatusException(this, requiredStatus)
  }
}
