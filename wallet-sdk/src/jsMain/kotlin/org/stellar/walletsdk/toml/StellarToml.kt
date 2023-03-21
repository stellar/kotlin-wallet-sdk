package org.stellar.walletsdk.toml

import external.resolver.StellarTomlResolver
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.await

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
  actual suspend fun getToml(baseURL: Url, httpClient: HttpClient): TomlInfo {
    // TODO: don't use SDK for that
    val url = baseURL.toString().replace("https://", "")

    val r = StellarTomlResolver.resolve(url).await()

    return TomlInfo(
      r.VERSION,
      r.NETWORK_PASSPHRASE,
      r.FEDERATION_SERVER,
      r.asDynamic().AUTH_SERVER as? String, // TODO
      r.TRANSFER_SERVER,
      r.TRANSFER_SERVER_SEP0024,
      r.KYC_SERVER,
      r.WEB_AUTH_ENDPOINT,
      r.SIGNING_KEY,
      r.HORIZON_URL,
      r.ACCOUNTS?.map { it },
      r.URI_REQUEST_SIGNING_KEY,
      r.DIRECT_PAYMENT_SERVER,
      r.ANCHOR_QUOTE_SERVER,
      r.DOCUMENTATION?.run {
        InfoDocumentation(
          ORG_NAME,
          ORG_DBA,
          ORG_URL,
          ORG_LOGO,
          ORG_DESCRIPTION,
          ORG_PHYSICAL_ADDRESS,
          ORG_PHYSICAL_ADDRESS_ATTESTATION,
          ORG_PHONE_NUMBER,
          ORG_PHONE_NUMBER_ATTESTATION,
          ORG_KEYBASE,
          ORG_TWITTER,
          ORG_GITHUB,
          ORG_OFFICIAL_EMAIL,
          ORG_SUPPORT_EMAIL,
          ORG_LICENSING_AUTHORITY,
          ORG_LICENSE_TYPE,
          ORG_LICENSE_NUMBER
        )
      },
      r.PRINCIPALS?.map {
        it.run {
          InfoContact(
            name,
            email,
            keybase,
            telegram,
            twitter,
            github,
            id_photo_hash,
            verification_photo_hash
          )
        }
      },
      r.CURRENCIES?.map {
        it.run {
          InfoCurrency(
            code!!,
            issuer!!,
            code_template,
            status,
            display_decimals?.toInt(),
            name,
            desc,
            conditions,
            image,
            fixed_number?.toInt(),
            max_number?.toInt(),
            is_unlimited,
            is_asset_anchored,
            anchor_asset_type,
            anchor_asset,
            attestation_of_reserve,
            redemption_instructions,
            collateral_addresses?.map { x -> x },
            collateral_address_messages?.map { x -> x },
            collateral_address_signatures?.map { x -> x },
            regulated,
            approval_server,
            approval_criteria
          )
        }
      },
      r.VALIDATORS?.map {
        it.run { InfoValidator(this.ALIAS, DISPLAY_NAME, PUBLIC_KEY, HOST, HISTORY) }
      }
    )
  }
}
