package org.stellar.walletsdk

import deezer.kustomexport.KustomExport
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlin.js.JsName
import org.stellar.walletsdk.anchor.Anchor
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.json.defaultJson
import org.stellar.walletsdk.recovery.Recovery

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 */
@KustomExport
class Wallet(
  stellarConfiguration: StellarConfiguration,
  applicationConfiguration: ApplicationConfiguration = ApplicationConfiguration()
) : Closeable {
  val cfg = Config(stellarConfiguration, applicationConfiguration)

  private val clients = mutableListOf(cfg.app.defaultClient)

  @JsName("deprecated")
  @Deprecated(
    "To be removed in 0.7",
    ReplaceWith("this.anchor(Url(\"https://\$homeDomain\"), httpClientConfig)", "io.ktor.http.Url")
  )
  fun anchor(homeDomain: String, httpClientConfig: ClientConfigFn? = null): Anchor {
    return anchor(Url("https://$homeDomain"), httpClientConfig)
  }

  fun anchor(url: Url, httpClientConfig: ClientConfigFn? = null): Anchor {
    return Anchor(cfg, url, getClient(httpClientConfig))
  }

  fun stellar(): Stellar {
    return Stellar(cfg)
  }

  fun recovery(httpClientConfig: ClientConfigFn? = null): Recovery {
    return Recovery(cfg, stellar(), getClient(httpClientConfig))
  }

  override fun close() {
    clients.forEach { it.close() }
  }

  @Suppress("UNCHECKED_CAST")
  private fun getClient(httpClientConfig: ClientConfigFn?): HttpClient {
    val httpClient =
      httpClientConfig?.run {
        cfg.app.defaultClient.config {
          (this as HttpClientConfig<ClientConfigType>).httpClientConfig()
        }
      }
    httpClient?.also { clients.add(it) }
    return httpClient ?: cfg.app.defaultClient
  }

  companion object {
    val Testnet =
      Wallet(
        StellarConfiguration.Testnet,
      )
  }
}

data class Config(val stellar: StellarConfiguration, val app: ApplicationConfiguration)

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
 * @property defaultClientConfig configuration for default client used across the app.
 */
data class ApplicationConfiguration(
  val defaultSigner: WalletSigner = WalletSigner.DefaultSigner(),
  val base64Decoder: Base64Decoder = defaultBase64Decoder,
  val defaultClientConfig: ClientConfigFn = {}
) {
  @Suppress("MaxLineLength")
  @Deprecated(
    "Can be configured using defaultClientConfig",
    replaceWith =
      ReplaceWith(
        "ApplicationConfiguration(defaultSigner, base64Decoder) { defaultRequest { url { protocol = URLProtocol.HTTP } } }",
        "io.ktor.client.plugins.defaultRequest",
        "io.ktor.http.URLProtocol"
      )
  )
  @JsName("deprecated")
  constructor(
    defaultSigner: WalletSigner = WalletSigner.DefaultSigner(),
    base64Decoder: Base64Decoder = defaultBase64Decoder,
    useHttp: Boolean
  ) : this(
    defaultSigner,
    base64Decoder,
    {
      if (useHttp) {
        defaultRequest { url { protocol = URLProtocol.HTTP } }
      }
    }
  )

  val defaultClient =
    HttpClient(Engine) {
      install(ContentNegotiation) { json(defaultJson) }
      defaultRequest { url { protocol = URLProtocol.HTTPS } }
      defaultClientConfig()
    }
}

typealias Base64Decoder = ((String) -> ByteArray)

internal typealias ClientConfig = HttpClientConfig<ClientConfigType>

internal typealias ClientConfigFn = (ClientConfig.() -> Unit)
