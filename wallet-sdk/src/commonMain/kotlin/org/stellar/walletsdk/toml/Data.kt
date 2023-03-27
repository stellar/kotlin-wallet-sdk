@file:JsExport

package org.stellar.walletsdk.toml

import io.ktor.http.*
import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import org.stellar.walletsdk.Network
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.exception.ValidationException
import org.stellar.walletsdk.isPublic

data class TomlInfo(
  @SerialName("VERSION") val version: String?,
  @SerialName("NETWORK_PASSPHRASE") val networkPassphrase: String?,
  @SerialName("FEDERATION_SERVER") val federationServer: String?,
  @SerialName("AUTH_SERVER") val authServer: String?,
  @SerialName("TRANSFER_SERVER") val transferServer: String?,
  @SerialName("TRANSFER_SERVER_SEP0024") val transferServerSep24: String?,
  @SerialName("KYC_SERVER") val kycServer: String?,
  @SerialName("WEB_AUTH_ENDPOINT") val webAuthEndpoint: String?,
  @SerialName("SIGNING_KEY") val signingKey: String?,
  @SerialName("HORIZON_URL") val horizonUrl: String?,
  @SerialName("ACCOUNTS") val accounts: List<String>?,
  @SerialName("URI_REQUEST_SIGNING_KEY") val uriRequestSigningKey: String?,
  @SerialName("DIRECT_PAYMENT_SERVER") val directPaymentServer: String?,
  @SerialName("ANCHOR_QUOTE_SERVER") val anchorQuoteServer: String?,
  @SerialName("DOCUMENTATION") val documentation: InfoDocumentation?,
  @SerialName("PRINCIPALS") val principals: List<InfoContact>?,
  @SerialName("CURRENCIES") val currencies: List<InfoCurrency>?,
  @SerialName("VALIDATORS") val validators: List<InfoValidator>?
) {
  // Supported services (SEPs)
  private val hasAuth = webAuthEndpoint != null && signingKey != null
  val services: InfoServices =
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

  fun validate(network: Network) {
    if (!network.isPublic()) {
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

data class InfoDocumentation(
  val orgName: String?,
  val orgDba: String?,
  val orgUrl: String?,
  val orgLogo: String?,
  val orgDescription: String?,
  val orgPhysicalAddress: String?,
  val orgPhysicalAddressAttestation: String?,
  val orgPhoneNumber: String?,
  val orgPhoneNumberAttestation: String?,
  val orgKeybase: String?,
  val orgTwitter: String?,
  val orgGithub: String?,
  val orgOfficialEmail: String?,
  val orgSupportEmail: String?,
  val orgLicensingAuthority: String?,
  val orgLicenseType: String?,
  val orgLicenseNumber: String?
)

data class InfoContact(
  val name: String?,
  val email: String?,
  val keybase: String?,
  val telegram: String?,
  val twitter: String?,
  val github: String?,
  val idPhotoHash: String?,
  val verificationPhotoHash: String?
)

data class InfoValidator(
  val alias: String?,
  val displayName: String?,
  val publicKey: String?,
  val host: String?,
  val history: String?
)

data class InfoCurrency(
  val code: String,
  val issuer: String,
  val codeTemplate: String?,
  val status: String?,
  val displayDecimals: Int?,
  val name: String?,
  val desc: String?,
  val conditions: String?,
  val image: String?,
  val fixedNumber: Int?,
  val maxNumber: Int?,
  val isUnlimited: Boolean?,
  val isAssetAnchored: Boolean?,
  val anchorAssetType: String?,
  val anchorAsset: String?,
  val attestationOfReserve: String?,
  val redemptionInstructions: String?,
  val collateralAddresses: List<String>?,
  val collateralAddressMessages: List<String>?,
  val collateralAddressSignatures: List<String>?,
  val regulated: Boolean?,
  val approvalServer: String?,
  val approvalCriteria: String?
) {
  private val myCode = code
  private val myIssuer = issuer
  val assetId: IssuedAssetId = IssuedAssetId(myCode, myIssuer)
}

data class InfoServices(
  val sep6: Sep6?,
  // TODO: add SEP8
  val sep10: Sep10?,
  val sep24: Sep24?,
  val sep31: Sep31?
)

/**
 * [SEP-6](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0006.md): Deposit
 * and withdrawal API.
 *
 * @property transferServer anchor's deposit and withdrawal server URL
 * @property anchorQuoteServer optional anchor's quote server URL if it supports
 * [SEP-38](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0038.md)
 */
data class Sep6(val transferServer: String, val anchorQuoteServer: String?)

/**
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md): Stellar
 * web authentication.
 *
 * @property webAuthEndpoint web auth URL
 * @property signingKey Stellar public address of the signing key
 */
data class Sep10(
  val webAuthEndpoint: String,
  val signingKey: String,
)

/**
 * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md):
 * Hosted/interactive deposit and withdrawal.
 *
 * @property transferServerSep24 anchor's deposit and withdrawal server URL
 * @property hasAuth anchor must support
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md)
 * authentication
 */
data class Sep24(val transferServerSep24: String, val hasAuth: Boolean)

/**
 * [SEP-31](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0031.md):
 * Cross-border payments API.
 *
 * @property directPaymentServer anchor's cross-border payments server URL
 * @property hasAuth anchor must support
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md)
 * authentication
 * @property kycServer optional anchor's KYC server URL
 * @property anchorQuoteServer optional anchor's quote server URL if it supports [SEP-38]
 * (https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0038.md)
 */
data class Sep31(
  val directPaymentServer: String,
  val hasAuth: Boolean,
  val kycServer: String?,
  val anchorQuoteServer: String?
)
