package org.stellar.walletsdk.toml

fun parseToml(toml: Map<String, Any>): TomlInfo {
  // Organization documentation
  val infoDoc = (toml["DOCUMENTATION"] as? HashMap<*, *>)
  val documentation =
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
  val infoPrincipalList = (toml["PRINCIPALS"] as? List<*>)?.filterIsInstance<HashMap<*, *>>()
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
  val infoCurrencyList = (toml["CURRENCIES"] as? List<*>)?.filterIsInstance<HashMap<*, *>>()
  val currencies: List<InfoCurrency>? =
    infoCurrencyList?.map {
      InfoCurrency(
        code = it["code"].toString(),
        codeTemplate = it["code_template"]?.toString(),
        issuer = it["issuer"].toString(),
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

  return TomlInfo(
    version = toml["VERSION"]?.toString(),
    networkPassphrase = toml["NETWORK_PASSPHRASE"]?.toString(),
    federationServer = toml["FEDERATION_SERVER"]?.toString(),
    authServer = toml["AUTH_SERVER"]?.toString(),
    transferServer = toml["TRANSFER_SERVER"]?.toString(),
    transferServerSep24 = toml["TRANSFER_SERVER_SEP0024"]?.toString(),
    kycServer = toml["KYC_SERVER"]?.toString(),
    webAuthEndpoint = toml["WEB_AUTH_ENDPOINT"]?.toString(),
    signingKey = toml["SIGNING_KEY"]?.toString(),
    horizonUrl = toml["HORIZON_URL"]?.toString(),
    accounts = (toml["ACCOUNTS"] as? List<*>)?.filterIsInstance<String>(),
    uriRequestSigningKey = toml["URI_REQUEST_SIGNING_KEY"]?.toString(),
    directPaymentServer = toml["DIRECT_PAYMENT_SERVER"]?.toString(),
    anchorQuoteServer = toml["ANCHOR_QUOTE_SERVER"]?.toString(),
    documentation = documentation,
    principals = principals,
    currencies = currencies,
    validators = validators
  )
}
