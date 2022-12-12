package org.stellar.walletsdk.anchor

import org.stellar.walletsdk.asset.IssuedAssetId

class Info(toml: Map<String, Any>) {
  // General information
  val version: String? = toml["VERSION"]?.toString()
  val networkPassphrase: String? = toml["NETWORK_PASSPHRASE"]?.toString()
  val federationServer: String? = toml["FEDERATION_SERVER"]?.toString()
  val authServer: String? = toml["AUTH_SERVER"]?.toString()
  val transferServer: String? = toml["TRANSFER_SERVER"]?.toString()
  val transferServerSep24: String? = toml["TRANSFER_SERVER_SEP0024"]?.toString()
  val kycServer: String? = toml["KYC_SERVER"]?.toString()
  val webAuthEndpoint: String? = toml["WEB_AUTH_ENDPOINT"]?.toString()
  val signingKey: String? = toml["SIGNING_KEY"]?.toString()
  val horizonUrl: String? = toml["HORIZON_URL"]?.toString()
  val accounts: List<String>? = (toml["ACCOUNTS"] as? List<*>)?.filterIsInstance<String>()
  val uriRequestSigningKey: String? = toml["URI_REQUEST_SIGNING_KEY"]?.toString()
  val directPaymentServer: String? = toml["DIRECT_PAYMENT_SERVER"]?.toString()
  val anchorQuoteServer: String? = toml["ANCHOR_QUOTE_SERVER"]?.toString()

  // Organization documentation
  private val infoDoc = (toml["DOCUMENTATION"] as? HashMap<*, *>)
  val documentation: InfoDocumentation =
    InfoDocumentation(
      orgName = infoDoc?.get("ORG_NAME")?.toString(),
      orgDba = infoDoc?.get("ORG_DBA")?.toString(),
      orgUrl = infoDoc?.get("ORG_URL")?.toString(),
      orgLogo = infoDoc?.get("ORG_LOGO")?.toString(),
      orgDescription = infoDoc?.get("ORG_DESCRIPTION")?.toString(),
      orgPhysicalAddress = infoDoc?.get("ORG_PHYSICAL_ADDRESS")?.toString(),
      orgPhysicalAddressAttestation = infoDoc?.get("ORG_PHYSICAL_ADDRESS_ATTESTATION")?.toString(),
      orgPhoneNumber = infoDoc?.get("ORG_PHONE_NUMBER")?.toString(),
      orgPhoneNumberAttestation = infoDoc?.get("ORG_PHONE_NUMBER_ATTESTATION")?.toString(),
      orgKeybase = infoDoc?.get("ORG_KEYBASE")?.toString(),
      orgTwitter = infoDoc?.get("ORG_TWITTER")?.toString(),
      orgGithub = infoDoc?.get("ORG_GITHUB")?.toString(),
      orgOfficialEmail = infoDoc?.get("ORG_OFFICIAL_EMAIL")?.toString(),
      orgSupportEmail = infoDoc?.get("ORG_SUPPORT_EMAIL")?.toString(),
      orgLicensingAuthority = infoDoc?.get("ORG_LICENSING_AUTHORITY")?.toString(),
      orgLicenseType = infoDoc?.get("ORG_LICENSE_TYPE")?.toString(),
      orgLicenseNumber = infoDoc?.get("ORG_LICENSE_NUMBER")?.toString(),
    )

  // Contact documentation
  private val infoPrincipalList =
    (toml["PRINCIPALS"] as? List<*>)?.filterIsInstance<HashMap<*, *>>()
  val principals: List<InfoContact>? =
    infoPrincipalList?.map {
      InfoContact(
        name = it["name"]?.toString(),
        email = it["email"]?.toString(),
        keybase = it["keybase"]?.toString(),
        telegram = it["telegram"]?.toString(),
        twitter = it["twitter"]?.toString(),
        github = it["github"]?.toString(),
        idPhotoHash = it["id_photo_hash"]?.toString(),
        verificationPhotoHash = it["verification_photo_hash"]?.toString(),
      )
    }

  // Currency documentation
  private val infoCurrencyList = (toml["CURRENCIES"] as? List<*>)?.filterIsInstance<HashMap<*, *>>()
  val currencies: List<InfoCurrency>? =
    infoCurrencyList?.map {
      val assetCode = it["code"]?.toString()
      val assetIssuer = it["issuer"]?.toString()

      InfoCurrency(
        assetId =
          if (assetCode != null && assetIssuer != null) {
            IssuedAssetId(it["code"].toString(), it["issuer"].toString())
          } else {
            null
          },
        code = assetCode,
        codeTemplate = it["code_template"]?.toString(),
        issuer = assetIssuer,
        status = it["status"]?.toString(),
        displayDecimals = it["display_decimals"]?.toString()?.toInt(),
        name = it["name"]?.toString(),
        desc = it["desc"]?.toString(),
        conditions = it["conditions"]?.toString(),
        image = it["image"]?.toString(),
        fixedNumber = it["fixed_number"]?.toString()?.toInt(),
        maxNumber = it["max_number"]?.toString()?.toInt(),
        isUnlimited = it["is_unlimited"]?.toString()?.toBoolean(),
        isAssetAnchored = it["is_asset_anchored"]?.toString()?.toBoolean(),
        anchorAssetType = it["anchor_asset_type"]?.toString(),
        anchorAsset = it["anchor_asset"]?.toString(),
        attestationOfReserve = it["attestation_of_reserve"]?.toString(),
        redemptionInstructions = it["redemption_instructions"]?.toString(),
        collateralAddresses = (it["collateral_addresses"] as? List<*>)?.filterIsInstance<String>(),
        collateralAddressMessages =
          (it["collateral_address_messages"] as? List<*>)?.filterIsInstance<String>(),
        collateralAddressSignatures =
          (it["collateral_address_signatures"] as? List<*>)?.filterIsInstance<String>(),
        regulated = it["regulated"]?.toString()?.toBoolean(),
        approvalServer = it["approval_server"]?.toString(),
        approvalCriteria = it["approval_criteria"]?.toString(),
      )
    }

  // Validator information
  val validators: List<InfoValidator>? =
    (toml["VALIDATORS"] as? List<*>)?.filterIsInstance<HashMap<*, *>>()?.map {
      InfoValidator(
        alias = it["ALIAS"].toString(),
        displayName = it["DISPLAY_NAME"].toString(),
        publicKey = it["PUBLIC_KEY"].toString(),
        host = it["HOST"].toString(),
        history = it["HISTORY"].toString()
      )
    }

  // Supported services (SEPs)
  private val hasAuth = webAuthEndpoint != null && signingKey != null
  val services: InfoServices? =
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
  val orgLicenseNumber: String?,
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
  val history: String?,
)

data class InfoServices(
  val sep6: Sep6?,
  // TODO: add SEP8
  val sep10: Sep10?,
  val sep24: Sep24?,
  val sep31: Sep31?
)

data class InfoCurrency(
  val assetId: IssuedAssetId?,
  val code: String?,
  val codeTemplate: String?,
  val issuer: String?,
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
