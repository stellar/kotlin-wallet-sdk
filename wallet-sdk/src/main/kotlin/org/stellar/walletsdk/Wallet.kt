package org.stellar.walletsdk

import java.util.*
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.anchor.Anchor
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.recovery.Recovery
import shadow.okhttp3.OkHttpClient

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 */
class Wallet(
  private val stellarConfiguration: StellarConfiguration,
  private val applicationConfiguration: ApplicationConfiguration
) {
  internal val cfg = Config(stellarConfiguration, applicationConfiguration)

  init {
    if (applicationConfiguration.useHttp && stellarConfiguration.network == Network.PUBLIC) {
      throw IllegalArgumentException("Using http is not allowed with main Stellar network (pubnet)")
    }
  }

  fun anchor(
    homeDomain: String,
    httpClient: okhttp3.OkHttpClient = okhttp3.OkHttpClient()
  ): Anchor {
    return Anchor(cfg, homeDomain, httpClient)
  }

  fun stellar(): Stellar {
    return Stellar(cfg)
  }

  fun recovery(httpClient: okhttp3.OkHttpClient = okhttp3.OkHttpClient()): Recovery {
    return Recovery(cfg, httpClient)
  }
}

data class StellarConfiguration(
  val network: Network,
  val horizonUrl: String,
  val maxBaseFeeStroops: UInt = 100u,
  val horizonClient: OkHttpClient? = null,
  val submitClient: OkHttpClient? = null
) {
  var server: Server =
    if (horizonClient != null) {
      if (submitClient == null) {
        throw IllegalArgumentException(
          "Horizon and submit client must be both initialized or set to null"
        )
      }
      Server(horizonUrl, horizonClient, submitClient)
    } else {
      Server(horizonUrl)
    }
    // Only used for tests
    internal set

  companion object {
    val Testnet = StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org")
  }
}

internal data class Config(val stellar: StellarConfiguration, val app: ApplicationConfiguration)

// TODO: provide a default WalletSignerImpl
/**
 * Application configuration
 *
 * @property defaultSigner default signer implementation to be used across application
 * @property base64Decoder Base64 decoder. Default [java.util.Base64] decoder works with Android API
 * 23+. To support Android API older than API 23, custom base64Decoder needs to be provided. For
 * example, `android.util.Base64`.
 * @property useHttp when enabled, switch from https to http scheme. Only allowed when network is
 * not [Network.PUBLIC] for security reasons
 * @constructor Create empty Application configuration
 */
data class ApplicationConfiguration(
  val defaultSigner: WalletSigner,
  val base64Decoder: Base64Decoder = defaultBase64Decoder,
  val useHttp: Boolean = false
)

typealias Base64Decoder = ((String) -> ByteArray)

internal val defaultBase64Decoder: Base64Decoder = { it: String -> Base64.getDecoder().decode(it) }

internal val Config.scheme: String
  get() {
    return if (this.app.useHttp) {
      "http"
    } else {
      "https"
    }
  }
