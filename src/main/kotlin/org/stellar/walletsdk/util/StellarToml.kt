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

// TODO: document
class StellarToml(
  private val stellarAddress: String,
  private val server: Server,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
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
