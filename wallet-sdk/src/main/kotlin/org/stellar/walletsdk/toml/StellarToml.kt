package org.stellar.walletsdk.toml

import com.moandjiezana.toml.Toml
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * [Stellar info file](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)
 * (also known as TOML file) methods.
 *
 * @property homeDomain Home domain where to find stellar.toml file
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
internal object StellarToml {
  /**
   * Get TOML file content.
   *
   * @return content of the TOML file
   */
  suspend fun getToml(baseURL: Url, httpClient: HttpClient): TomlInfo {
    val tomlContent =
      httpClient
        .get {
          accept(ContentType.Text.Any)
          url {
            takeFrom(baseURL)
            appendPathSegments(".well-known", "stellar.toml")
          }
        }
        .bodyAsText()

    return parseToml(Toml().read(tomlContent).toMap())
  }
}
