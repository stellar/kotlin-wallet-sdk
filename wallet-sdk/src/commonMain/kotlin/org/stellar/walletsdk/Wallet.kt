@file:JsExport

package org.stellar.walletsdk

import kotlin.js.JsExport
import org.stellar.walletsdk.anchor.Anchor
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.recovery.Recovery

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 */
class Wallet(
  stellarConfiguration: StellarConfiguration,
  applicationConfiguration: ApplicationConfiguration = ApplicationConfiguration()
) {
  internal val cfg = Config(stellarConfiguration, applicationConfiguration)

  init {
    if (applicationConfiguration.useHttp) {
      require(stellarConfiguration.isPublic()) {
        "Using http is not allowed with main Stellar network (pubnet)"
      }
    }
  }

  // TODO: make client configurable again
  fun anchor(homeDomain: String): Anchor {
    return Anchor(cfg, homeDomain)
  }

  fun stellar(): Stellar {
    return Stellar(cfg)
  }

  // TODO: make client configurable again
  fun recovery(): Recovery {
    return Recovery(cfg, stellar())
  }
}

internal data class Config(val stellar: StellarConfiguration, val app: ApplicationConfiguration)

// TODO: provide a default WalletSignerImpl
/**
 * Application configuration
 *
 * @constructor Create empty Application configuration
 * @property defaultSigner default signer implementation to be used across application
 * @property base64Decoder Base64 decoder. Default [java.util.Base64] decoder works with Android API
 * 23+. To support Android API older than API 23, custom base64Decoder needs to be provided. For
 * example, `android.util.Base64`.
 * @property useHttp when enabled, switch from https to http scheme. Only allowed when network is
 * not [Network.PUBLIC] for security reasons
 */
data class ApplicationConfiguration(
  val defaultSigner: WalletSigner = WalletSigner.Default,
  val base64Decoder: Base64Decoder = defaultBase64Decoder,
  val useHttp: Boolean = false
)

typealias Base64Decoder = ((String) -> ByteArray)

internal val Config.scheme: String
  get() {
    return if (this.app.useHttp) {
      "http"
    } else {
      "https"
    }
  }
