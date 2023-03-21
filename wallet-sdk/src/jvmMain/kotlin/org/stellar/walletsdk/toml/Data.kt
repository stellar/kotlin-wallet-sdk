package org.stellar.walletsdk.toml

import io.ktor.http.*
import org.stellar.sdk.Network
import org.stellar.walletsdk.exception.ValidationException
import shadow.com.google.gson.annotations.SerializedName

actual data class TomlInfo(
  @SerializedName("VERSION") actual val version: String?,
  @SerializedName("NETWORK_PASSPHRASE") actual val networkPassphrase: String?,
  @SerializedName("FEDERATION_SERVER") actual val federationServer: String?,
  @SerializedName("AUTH_SERVER") actual val authServer: String?,
  @SerializedName("TRANSFER_SERVER") actual val transferServer: String?,
  @SerializedName("TRANSFER_SERVER_SEP0024") actual val transferServerSep24: String?,
  @SerializedName("KYC_SERVER") actual val kycServer: String?,
  @SerializedName("WEB_AUTH_ENDPOINT") actual val webAuthEndpoint: String?,
  @SerializedName("SIGNING_KEY") actual val signingKey: String?,
  @SerializedName("HORIZON_URL") actual val horizonUrl: String?,
  @SerializedName("ACCOUNTS") actual val accounts: List<String>?,
  @SerializedName("URI_REQUEST_SIGNING_KEY") actual val uriRequestSigningKey: String?,
  @SerializedName("DIRECT_PAYMENT_SERVER") actual val directPaymentServer: String?,
  @SerializedName("ANCHOR_QUOTE_SERVER") actual val anchorQuoteServer: String?,
  @SerializedName("DOCUMENTATION") actual val documentation: InfoDocumentation?,
  @SerializedName("PRINCIPALS") actual val principals: List<InfoContact>?,
  @SerializedName("CURRENCIES") actual val currencies: List<InfoCurrency>?,
  @SerializedName("VALIDATORS") actual val validators: List<InfoValidator>?
) {
  // Supported services (SEPs)
  private val hasAuth = webAuthEndpoint != null && signingKey != null
  actual val services: InfoServices =
    InfoServices(
      sep6 =
        if (transferServer != null) {
          Sep6(transferServer, anchorQuoteServer)
        } else {
          null
        },
      sep10 =
        if (hasAuth) {
          Sep10(webAuthEndpoint.toString(), signingKey.toString())
        } else {
          null
        },
      sep24 =
        if (transferServerSep24 != null) {
          Sep24(transferServerSep24, hasAuth)
        } else {
          null
        },
      sep31 =
        if (directPaymentServer != null) {
          Sep31(directPaymentServer, hasAuth, kycServer, anchorQuoteServer)
        } else {
          null
        }
    )

  actual fun validate(network: Network) {
    if (network != Network.PUBLIC) {
      return
    }

    requireSecure("TRANSFER_SERVER", transferServer)
    requireSecure("TRANSFER_SERVER_SEP0024", transferServerSep24)
    requireSecure("FEDERATION_SERVER", federationServer)
    requireSecure("AUTH_SERVER", authServer)
    requireSecure("KYC_SERVER", kycServer)
    requireSecure("WEB_AUTH_ENDPOINT", webAuthEndpoint)
    requireSecure("DIRECT_PAYMENT_SERVER", directPaymentServer)
    requireSecure("ANCHOR_QUOTE_SERVER", anchorQuoteServer)
  }

  private fun requireSecure(name: String, url: String?) {
    if (url != null && URLBuilder(url).protocol.name != URLProtocol.HTTPS.name) {
      throw ValidationException(
        "TOML file contains url using http protocol for $name: $url. Http urls are prohibited " +
          "in production environment. Please notify anchor owner."
      )
    }
  }
}
