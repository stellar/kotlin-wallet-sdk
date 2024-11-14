@file:Suppress("MaxLineLength")

package org.stellar.walletsdk.anchor

import io.ktor.client.*
import io.ktor.http.*
import org.stellar.sdk.Network
import org.stellar.walletsdk.*
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.auth.Sep10
import org.stellar.walletsdk.customer.Sep12
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.toml.StellarToml
import org.stellar.walletsdk.toml.TomlInfo
import org.stellar.walletsdk.util.Util.anchorGet

/** Build on/off ramps with anchors. */
@Suppress("TooManyFunctions")
class Anchor
internal constructor(
  private val cfg: Config,
  private val baseUrl: Url,
  private val httpClient: HttpClient
) {
  internal val infoHolder = InfoHolder(cfg.stellar.network, baseUrl, httpClient)

  /**
   * Get anchor information from a TOML file.
   *
   * @return TOML file content
   */
  suspend fun sep1(): TomlInfo {
    return infoHolder.getInfo()
  }

  /**
   * Create new auth object to authenticate account with the anchor using SEP-10.
   *
   * @return auth object
   * @throws [AnchorAuthNotSupported] if SEP-10 is not configured
   */
  suspend fun sep10(): Sep10 {
    return Sep10(
      cfg,
      sep1().services.sep10?.webAuthEndpoint ?: throw AnchorAuthNotSupported,
      // Strip protocol
      baseUrl.toString().replace("${baseUrl.protocol.name}://", ""),
      httpClient
    )
  }

  /**
   * Create new customer object to handle customer records with the anchor using SEP-12.
   *
   * @return customer object
   */
  suspend fun sep12(token: AuthToken): Sep12 {
    val kycServer = sep1().services.sep12?.kycServer ?: throw KYCServerNotFoundException()

    return Sep12(token, kycServer, httpClient)
  }

  /**
   * Creates new interactive flow for given anchor. It can be used for withdrawal or deposit.
   *
   * @return interactive flow service
   */
  fun sep24(): Sep24 {
    return Sep24(this, httpClient)
  }
}

typealias Auth = Sep10

typealias Customer = Sep12

typealias Interactive = Sep24

suspend fun Anchor.getInfo(): TomlInfo {
  return this.sep1()
}

suspend fun Anchor.auth(): Auth {
  return this.sep10()
}

suspend fun Anchor.customer(authToken: AuthToken): Customer {
  return this.sep12(authToken)
}

fun Anchor.interactive(): Interactive {
  return this.sep24()
}

internal class InfoHolder(
  private val network: Network,
  private val baseUrl: Url,
  private val httpClient: HttpClient
) {
  // This 2 variables are lazy and shouldn't be used directly. Call getInfo() and getServiceInfo()
  // instead
  private lateinit var info: TomlInfo
  private lateinit var serviceInfo: AnchorServiceInfo

  suspend fun getInfo(): TomlInfo {
    if (!::info.isInitialized) {
      info = StellarToml.getToml(baseUrl, httpClient)
      info.validate(network)
    }

    return info
  }

  suspend fun getServicesInfo(): AnchorServiceInfo {
    if (!::serviceInfo.isInitialized) {
      serviceInfo = get { appendPathSegments("info") }
    }

    return serviceInfo
  }

  private suspend inline fun <reified T> get(
    authToken: AuthToken? = null,
    urlBlock: URLBuilder.() -> Unit = {},
  ): T {
    return httpClient.anchorGet(getInfo(), authToken, urlBlock)
  }
}
