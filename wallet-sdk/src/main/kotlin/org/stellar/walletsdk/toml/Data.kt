package org.stellar.walletsdk.toml

import com.google.gson.annotations.SerializedName
import io.ktor.http.*
import org.stellar.sdk.Network
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.NativeAssetId
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.exception.ValidationException

data class TomlInfo(
  @SerializedName("VERSION") val version: String?,
  @SerializedName("NETWORK_PASSPHRASE") val networkPassphrase: String?,
  @SerializedName("FEDERATION_SERVER") val federationServer: String?,
  @SerializedName("AUTH_SERVER") val authServer: String?,
  @SerializedName("TRANSFER_SERVER") val transferServer: String?,
  @SerializedName("TRANSFER_SERVER_SEP0024") val transferServerSep24: String?,
  @SerializedName("KYC_SERVER") val kycServer: String?,
  @SerializedName("WEB_AUTH_ENDPOINT") val webAuthEndpoint: String?,
  @SerializedName("SIGNING_KEY") val signingKey: String?,
  @SerializedName("HORIZON_URL") val horizonUrl: String?,
  @SerializedName("ACCOUNTS") val accounts: List<String>?,
  @SerializedName("URI_REQUEST_SIGNING_KEY") val uriRequestSigningKey: String?,
  @SerializedName("DIRECT_PAYMENT_SERVER") val directPaymentServer: String?,
  @SerializedName("ANCHOR_QUOTE_SERVER") val anchorQuoteServer: String?,
  @SerializedName("DOCUMENTATION") val documentation: InfoDocumentation?,
  @SerializedName("PRINCIPALS") val principals: List<InfoContact>?,
  @SerializedName("CURRENCIES") val currencies: List<InfoCurrency>?,
  @SerializedName("VALIDATORS") val validators: List<InfoValidator>?
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
      sep7 =
        if (uriRequestSigningKey != null) {
          Sep7(uriRequestSigningKey)
        } else {
          null
        },
      sep10 =
        if (hasAuth) {
          Sep10(webAuthEndpoint.toString(), signingKey.toString())
        } else {
          null
        },
      sep12 =
        if (kycServer != null) {
          Sep12(kycServer.toString(), signingKey.toString())
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

data class InfoDocumentation(
  @SerializedName("ORG_NAME") val orgName: String?,
  @SerializedName("ORG_DBA") val orgDba: String?,
  @SerializedName("ORG_URL") val orgUrl: String?,
  @SerializedName("ORG_LOGO") val orgLogo: String?,
  @SerializedName("ORG_DESCRIPTION") val orgDescription: String?,
  @SerializedName("ORG_PHYSICAL_ADDRESS") val orgPhysicalAddress: String?,
  @SerializedName("ORG_PHYSICAL_ADDRESS_ATTESTATION") val orgPhysicalAddressAttestation: String?,
  @SerializedName("ORG_PHONE_NUMBER") val orgPhoneNumber: String?,
  @SerializedName("ORG_PHONE_NUMBER_ATTESTATION") val orgPhoneNumberAttestation: String?,
  @SerializedName("ORG_KEYBASE") val orgKeybase: String?,
  @SerializedName("ORG_TWITTER") val orgTwitter: String?,
  @SerializedName("ORG_GITHUB") val orgGithub: String?,
  @SerializedName("ORG_OFFICIAL_EMAIL") val orgOfficialEmail: String?,
  @SerializedName("ORG_SUPPORT_EMAIL") val orgSupportEmail: String?,
  @SerializedName("ORG_LICENSING_AUTHORITY") val orgLicensingAuthority: String?,
  @SerializedName("ORG_LICENSE_TYPE") val orgLicenseType: String?,
  @SerializedName("ORG_LICENSE_NUMBER") val orgLicenseNumber: String?
)

data class InfoContact(
  @SerializedName("name") val name: String?,
  @SerializedName("email") val email: String?,
  @SerializedName("keybase") val keybase: String?,
  @SerializedName("telegram") val telegram: String?,
  @SerializedName("twitter") val twitter: String?,
  @SerializedName("github") val github: String?,
  @SerializedName("id_photo_hash") val idPhotoHash: String?,
  @SerializedName("verification_photo_hash") val verificationPhotoHash: String?
)

data class InfoValidator(
  @SerializedName("ALIAS") val alias: String?,
  @SerializedName("DISPLAY_NAME") val displayName: String?,
  @SerializedName("PUBLIC_KEY") val publicKey: String?,
  @SerializedName("HOST") val host: String?,
  @SerializedName("HISTORY") val history: String?
)

data class InfoCurrency(
  @SerializedName("code") val code: String,
  @SerializedName("issuer") val issuer: String?,
  @SerializedName("code_template") val codeTemplate: String?,
  @SerializedName("status") val status: String?,
  @SerializedName("display_decimals") val displayDecimals: Int?,
  @SerializedName("name") val name: String?,
  @SerializedName("desc") val desc: String?,
  @SerializedName("conditions") val conditions: String?,
  @SerializedName("image") val image: String?,
  @SerializedName("fixed_number") val fixedNumber: Int?,
  @SerializedName("max_number") val maxNumber: Int?,
  @SerializedName("is_unlimited") val isUnlimited: Boolean?,
  @SerializedName("is_asset_anchored") val isAssetAnchored: Boolean?,
  @SerializedName("anchor_asset_type") val anchorAssetType: String?,
  @SerializedName("anchor_asset") val anchorAsset: String?,
  @SerializedName("attestation_of_reserve") val attestationOfReserve: String?,
  @SerializedName("redemption_instructions") val redemptionInstructions: String?,
  @SerializedName("collateral_addresses") val collateralAddresses: List<String>?,
  @SerializedName("collateral_address_messages") val collateralAddressMessages: List<String>?,
  @SerializedName("collateral_address_signatures") val collateralAddressSignatures: List<String>?,
  @SerializedName("regulated") val regulated: Boolean?,
  @SerializedName("approval_server") val approvalServer: String?,
  @SerializedName("approval_criteria") val approvalCriteria: String?
) {
  private val myCode = code
  private val myIssuer = issuer
  val assetId: StellarAssetId =
    when {
      myCode == "native" && myIssuer == null -> NativeAssetId
      myCode != "native" && myIssuer != null -> IssuedAssetId(myCode, myIssuer)
      else -> throw ValidationException("Invalid asset code and issuer pair: $myCode, $myIssuer")
    }
}

data class InfoServices(
  val sep6: Sep6?,
  val sep7: Sep7?,
  val sep10: Sep10?,
  val sep12: Sep12?,
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
 * [SEP-7](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0007.md): URI
 * scheme for Stellar transactions and operations.
 *
 * @property signingKey Stellar public address of the URI request signing key
 */
data class Sep7(val signingKey: String)

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
 * [SEP-12](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0012.md): Stellar
 * KYC endpoint.
 *
 * @property kycServer customer server endpoint
 * @property signingKey Stellar public address of the signing key
 */
data class Sep12(
  val kycServer: String,
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
