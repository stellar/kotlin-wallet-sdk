package org.stellar.walletsdk.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.sdk.Server
import org.stellar.walletsdk.NetworkRequestFailedException
import org.stellar.walletsdk.StellarTomlAddressMissingHomeDomain
import org.stellar.walletsdk.StellarTomlMissingFields
import shadow.com.moandjiezana.toml.Toml

/**
 * [Stellar info file](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)
 * (also known as TOML file) methods.
 *
 * @property stellarAddress Stellar address whose TOML file to fetch
 * @property server Horizon [Server] instance
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class StellarToml(
  private val stellarAddress: String,
  private val server: Server,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  /**
   * Get TOML file content.
   *
   * @return content of the TOML file
   */
  suspend fun getToml(): Map<String, Any> {
    val homeDomain = getHomeDomain()
    val tomlUrl = buildTomlUrl(homeDomain)

    val request = Request.Builder().url(tomlUrl).build()

    return CoroutineScope(Dispatchers.IO)
      .async {
        httpClient.newCall(request).execute().use { response ->
          if (!response.isSuccessful) throw NetworkRequestFailedException(response)

          val tomlContent = response.body!!.charStream()
          return@async Toml().read(tomlContent).toMap()
        }
      }
      .await()
  }

  /**
   * Get Stellar account's home domain, if it's configured.
   *
   * @return account's home domain
   *
   * @throws [StellarTomlAddressMissingHomeDomain] if account does not have home domain configured
   */
  suspend fun getHomeDomain(): String {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val account = fetchAccount(accountAddress = stellarAddress, server)

        if (account.homeDomain.isNullOrBlank()) {
          throw StellarTomlAddressMissingHomeDomain(stellarAddress)
        }

        return@async account.homeDomain
      }
      .await()
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

  /**
   * Check if a list of fields exist in the TOML file.
   *
   * @param fields a list of fields to check
   * @param tomlContent TOML file content
   *
   * @return `true` if all fields are found in the TOML file
   *
   * @throws [StellarTomlMissingFields] if there are any missing fields
   */
  fun hasFields(fields: List<String>, tomlContent: Map<String, Any>): Boolean {
    val missingFields = mutableListOf<String>()

    fields.forEach { field ->
      if (tomlContent[field] == null) {
        missingFields.add(field)
      }
    }

    if (missingFields.size > 0) {
      throw StellarTomlMissingFields(missingFields)
    }

    return true
  }
}
