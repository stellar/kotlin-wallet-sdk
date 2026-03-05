package org.stellar.walletsdk

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import java.time.Duration
import java.util.*
import okhttp3.OkHttpClient
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.anchor.Anchor
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.json.defaultJson
import org.stellar.walletsdk.recovery.Recovery
import org.stellar.walletsdk.recovery.RecoveryServer
import org.stellar.walletsdk.recovery.RecoveryServerKey
import org.stellar.walletsdk.uri.Sep7
import org.stellar.walletsdk.uri.Sep7Base

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 */
class Wallet(
  stellarConfiguration: StellarConfiguration,
  applicationConfiguration: ApplicationConfiguration = ApplicationConfiguration()
) : Closeable {
  internal val cfg = Config(stellarConfiguration, applicationConfiguration)

  private val clients = mutableListOf(cfg.app.defaultClient)

  fun anchor(homeDomain: String, httpClientConfig: ClientConfigFn? = null): Anchor {
    val url = if (homeDomain.contains("://")) homeDomain else "https://$homeDomain"

    return Anchor(cfg, Url(url), getClient(httpClientConfig))
  }

  fun stellar(): Stellar {
    return Stellar(cfg)
  }

  fun recovery(
    servers: Map<RecoveryServerKey, RecoveryServer>,
    httpClientConfig: ClientConfigFn? = null
  ): Recovery {
    return Recovery(cfg, stellar(), getClient(httpClientConfig), servers)
  }

  /**
   * Access SEP-7 URI functionality for creating and parsing Stellar URIs.
   *
   * @return Sep7 object providing SEP-7 utilities
   */
  fun uri(): Sep7 {
    return Sep7
  }

  /**
   * Parse a SEP-7 URI string into the appropriate Sep7 object.
   *
   * @param uriString The SEP-7 URI string to parse
   * @return Sep7Base instance (either Sep7Pay or Sep7Tx)
   * @throws org.stellar.walletsdk.uri.Sep7InvalidUriError if the URI is invalid
   */
  fun parseSep7Uri(uriString: String): Sep7Base {
    return Sep7.parseUri(uriString)
  }

  override fun close() {
    clients.forEach { it.close() }
  }

  @Suppress("UNCHECKED_CAST")
  private fun getClient(httpClientConfig: ClientConfigFn?): HttpClient {
    val httpClient =
      httpClientConfig?.run {
        cfg.app.defaultClient.config { (this as HttpClientConfig<OkHttpConfig>).httpClientConfig() }
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

/**
 * Configuration for all Stellar-related activity.
 *
 * @constructor Create empty Stellar configuration
 * @property network network to be used
 * @property horizonUrl URL of the Horizons server.
 * @property baseFee default [base fee]
 * (https://developers.stellar.org/docs/encyclopedia/fees-surge-pricing-fee-strategies#network-fees-on-stellar)
 * to be used
 * @property defaultTimeout default transaction timeout
 * @property horizonClient optional HTTP client configuration to be used for Horizon calls.
 * @property submitClient optional HTTP client configuration to be used for transaction submission.
 */
data class StellarConfiguration(
  val network: Network,
  val horizonUrl: String,
  /**
   * [Default base fee]
   * (https://developers.stellar.org/docs/encyclopedia/fees-surge-pricing-fee-strategies#network-fees-on-stellar)
   */
  val baseFee: ULong = 100u,
  val defaultTimeout: Duration = Duration.ofMinutes(3),
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

  companion object {
    val Testnet = StellarConfiguration(Network.TESTNET, "https://horizon-testnet.stellar.org")
  }
}

internal data class Config(val stellar: StellarConfiguration, val app: ApplicationConfiguration)

/**
 * Application configuration
 *
 * @constructor Create empty Application configuration
 * @property defaultSigner default signer implementation to be used across application
 * @property defaultClientDomain default client_domain
 * @property defaultClientConfig configuration for default client used across the app.
 */
data class ApplicationConfiguration(
  val defaultSigner: WalletSigner = WalletSigner.DefaultSigner(),
  val defaultClientDomain: String? = null,
  val defaultClientConfig: ClientConfigFn = {}
) {
  companion object {
    val useHttp: ClientConfigFn = { defaultRequest { url { protocol = URLProtocol.HTTP } } }
  }

  val defaultClient =
    HttpClient(OkHttp) {
      install(ContentNegotiation) { json(defaultJson) }
      defaultRequest { url { protocol = URLProtocol.HTTPS } }
      expectSuccess = true
      defaultClientConfig()
    }
}

internal typealias ClientConfig = HttpClientConfig<OkHttpConfig>

internal typealias ClientConfigFn = (ClientConfig.() -> Unit)
