package org.stellar.walletsdk

import java.util.*
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import shadow.okhttp3.OkHttpClient

actual data class StellarConfiguration(
  val network: Network,
  val horizonUrl: String,
  val maxBaseFeeStroops: UInt = 100u,
  val horizonClient: OkHttpClient? = null,
  val submitClient: OkHttpClient? = null
) {
  var server: Server =
    if (horizonClient != null) {
      requireNotNull(submitClient) {
        "Horizon and submit client must be both initialized or set to null"
      }
      Server(horizonUrl, horizonClient, submitClient)
    } else {
      Server(horizonUrl)
    }
    // Only used for tests
    internal set

  actual fun isPublic(): Boolean {
    return network == Network.PUBLIC
  }

  companion object {
    val Testnet = StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org")
  }
}

internal actual val defaultBase64Decoder: Base64Decoder = { Base64.getDecoder().decode(it) }
