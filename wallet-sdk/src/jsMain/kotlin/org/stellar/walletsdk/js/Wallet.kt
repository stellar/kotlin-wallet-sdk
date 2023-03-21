package org.stellar.walletsdk.js

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlin.js.JsName
import org.stellar.walletsdk.ApplicationConfiguration
import org.stellar.walletsdk.ClientConfigFn
import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.recovery.Recovery

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 */
@JsName("Wallet")
@JsExport
class WalletJS(
  stellarConfiguration: StellarConfiguration,
  applicationConfiguration: ApplicationConfiguration = ApplicationConfiguration()
) : Closeable {
  private val proxy = Wallet(stellarConfiguration, applicationConfiguration)

  fun anchor(url: String, httpClientConfig: ClientConfigFn? = null): AnchorJS {
    return AnchorJS(proxy.anchor(url, httpClientConfig))
  }

  fun stellar(): Stellar {
    return proxy.stellar()
  }

  fun recovery(httpClientConfig: ClientConfigFn? = null): Recovery {
    return proxy.recovery(httpClientConfig)
  }

  override fun close() {
    proxy.close()
  }

  companion object {
    val Testnet =
      WalletJS(
        StellarConfiguration.Testnet,
      )
  }
}
