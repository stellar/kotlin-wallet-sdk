package org.stellar.walletsdk.js

import io.ktor.http.*
import kotlin.js.Promise
import kotlinx.coroutines.promise
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.toml.TomlInfo
import org.stellar.walletsdk.util.UtilJS.promise

@JsExport
@JsName("Anchor")
class AnchorJS internal constructor(private val anchor: Anchor) {

  fun getInfo(): Promise<TomlInfo> = promise { anchor.getInfo() }

  fun auth() = promise { AuthJS(anchor.auth()) }

  fun getServicesInfo(): Promise<AnchorServiceInfo> = promise { anchor.getServicesInfo() }

  fun interactive(): InteractiveJS {
    return InteractiveJS(anchor.interactive())
  }

  fun getTransaction(transactionId: String, authToken: AuthToken): Promise<AnchorTransaction> =
    promise {
      anchor.getTransaction(transactionId, authToken)
    }

  fun getTransactionsForAsset(
    asset: AssetId,
    authToken: AuthToken
  ): Promise<List<AnchorTransaction>> = promise { anchor.getTransactionsForAsset(asset, authToken) }

  @Suppress("LongParameterList")
  fun getHistory(
    assetId: AssetId,
    authToken: AuthToken,
    limit: Int? = null,
    pagingId: String? = null,
    noOlderThan: String? = null,
    lang: String? = null
  ): Promise<List<AnchorTransaction>> = promise {
    anchor.getHistory(assetId, authToken, limit, pagingId, noOlderThan, lang)
  }
}
