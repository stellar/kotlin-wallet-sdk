package org.stellar.walletsdk.toml

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.walletsdk.exception.ServerRequestFailedException
import shadow.com.moandjiezana.toml.Toml

/**
 * [Stellar info file](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)
 * (also known as TOML file) methods.
 *
 * @property homeDomain Home domain where to find stellar.toml file
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
internal class StellarToml(
  private val scheme: String,
  private val homeDomain: String,
  private val httpClient: OkHttpClient
) {
  /**
   * Get TOML file content.
   *
   * @return content of the TOML file
   */
  suspend fun getToml(): Map<String, Any> {
    val tomlUrl = buildTomlUrl(homeDomain)

    val request = Request.Builder().url(tomlUrl).build()

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      val tomlContent = response.body!!.charStream()

      Toml().read(tomlContent).toMap()
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

    val tomlPath = ".well-known/stellar.toml"
    var scheme = scheme
    var host = homeDomain

    @Suppress("SwallowedException", "TooGenericExceptionCaught")
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
