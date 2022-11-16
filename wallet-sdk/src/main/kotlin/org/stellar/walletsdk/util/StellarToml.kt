package org.stellar.walletsdk.util

import kotlinx.coroutines.coroutineScope
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.sdk.Server
import org.stellar.walletsdk.NetworkRequestFailedException
import shadow.com.moandjiezana.toml.Toml

/**
 * [Stellar info file](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)
 * (also known as TOML file) methods.
 *
 * @property homeDomain Home domain where to find stellar.toml file
 * @property server Horizon [Server] instance
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class StellarToml(
  private val homeDomain: String,
  private val server: Server,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  /**
   * Get TOML file content.
   *
   * @return content of the TOML file
   */
  suspend fun getToml(): Map<String, Any> {
    val tomlUrl = buildTomlUrl(homeDomain)

    val request = Request.Builder().url(tomlUrl).build()

    return coroutineScope {
      httpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw NetworkRequestFailedException(response)

        val tomlContent = response.body!!.charStream()

        Toml().read(tomlContent).toMap()
      }
    }
  }

  /**
   * Build full URL with the path to Stellar TOML file.
   *
   * @param homeDomain home domain to use in the URL
   *
   * @return full URL of the TOML file
   */
  fun buildTomlUrl(homeDomain: String): String {
    // TODO: create a helper method to normalize url
    // TODO: handle localhost

    val tomlPath = ".well-known/stellar.toml"
    var scheme = "https"
    var host = homeDomain

    try {
      val url = homeDomain.toHttpUrl()
      scheme = url.scheme
      host = url.host
    } catch (e: Exception) {
      // Use defaults
    }

    return "$scheme://$host/$tomlPath"
  }
}
