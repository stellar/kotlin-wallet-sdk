package org.stellar.walletsdk.toml

import io.ktor.http.*
import org.stellar.walletsdk.Network
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.exception.ValidationException

expect class TomlInfo {
  // Supported services (SEPs)
  val services: InfoServices
  val version: String?
  val networkPassphrase: String?
  val federationServer: String?
  val authServer: String?
  val transferServer: String?
  val transferServerSep24: String?
  val kycServer: String?
  val webAuthEndpoint: String?
  val signingKey: String?
  val horizonUrl: String?
  val accounts: List<String>?
  val uriRequestSigningKey: String?
  val directPaymentServer: String?
  val anchorQuoteServer: String?
  val documentation: InfoDocumentation?
  val principals: List<InfoContact>?
  val currencies: List<InfoCurrency>?
  val validators: List<InfoValidator>?

   fun validate(network: Network)
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
