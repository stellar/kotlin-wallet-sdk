package org.stellar.walletsdk.js

import org.stellar.walletsdk.anchor.Interactive
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.util.UtilJS.promise

@JsName("Interactive")
@JsExport
class InteractiveJS internal constructor(private val interactive: Interactive) {
  fun withdraw(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: typescript.Map<String>? = null,
    fundsAccountAddress: String? = null,
  ) = promise {
    var map: Map<String, String>? = null
    if (extraFields != null) {
      map = mutableMapOf()
      extraFields.forEach { value, key -> map[value] = key }
    }
    interactive.withdraw(accountAddress, assetId, authToken, map, fundsAccountAddress)
  }

  fun deposit(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: typescript.Map<String>? = null,
    fundsAccountAddress: String? = null,
  ) = promise {
    var map: Map<String, String>? = null
    if (extraFields != null) {
      map = mutableMapOf()
      extraFields.forEach { value, key -> map[value] = key }
    }
    interactive.deposit(accountAddress, assetId, authToken, map, fundsAccountAddress)
  }
}
