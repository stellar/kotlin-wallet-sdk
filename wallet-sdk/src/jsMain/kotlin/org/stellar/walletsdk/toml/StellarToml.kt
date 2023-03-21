package org.stellar.walletsdk.toml

import io.ktor.client.*
import io.ktor.http.*

/**
 * [Stellar info file](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)
 * (also known as TOML file) methods.
 *
 * @property homeDomain Home domain where to find stellar.toml file
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
internal actual object StellarToml {
    /**
     * Get TOML file content.
     *
     * @return content of the TOML file
     */
    actual suspend fun getToml(
        baseURL: Url,
        httpClient: HttpClient
    ): TomlInfo {
        TODO("Not yet implemented")
    }
}