package org.stellar.walletsdk.util

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.stellar.sdk.Memo
import org.stellar.sdk.requests.RequestBuilder
import org.stellar.walletsdk.ApplicationConfiguration
import org.stellar.walletsdk.Order
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.AnchorInteractiveFlowNotSupported
import org.stellar.walletsdk.exception.IncorrectTransactionStatusException
import org.stellar.walletsdk.toml.TomlInfo
import org.stellar.walletsdk.util.Util.isHex

internal object Util {
  fun String.isHex(): Boolean {
    return this.toBigIntegerOrNull(16) != null
  }

  fun MemoType.makeMemo(s: String, cfg: ApplicationConfiguration): Memo {
    return when (this) {
      MemoType.TEXT -> Memo.text(s)
      MemoType.HASH -> hash(s, cfg)
      MemoType.ID -> Memo.id(s.toLong())
    }
  }

  private fun hash(s: String, cfg: ApplicationConfiguration): Memo {
    return if (s.isHex()) Memo.hash(s) else Memo.hash(cfg.base64Decoder(s))
  }

  val Order.builderEnum: RequestBuilder.Order
    get() {
      return when (this) {
        Order.ASC -> RequestBuilder.Order.ASC
        Order.DESC -> RequestBuilder.Order.DESC
      }
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
