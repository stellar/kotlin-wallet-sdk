package org.stellar.walletsdk.util

import org.stellar.sdk.Memo
import org.stellar.sdk.requests.RequestBuilder
import org.stellar.walletsdk.ApplicationConfiguration
import org.stellar.walletsdk.Order
import org.stellar.walletsdk.anchor.MemoType

internal object JvmUtil {
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
}
