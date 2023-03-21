@file:JsModule("stellar-sdk")
@file:JsNonModule
@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external.resolver

import kotlin.js.Promise

external open class StellarTomlResolver {
  interface StellarTomlResolveOptions {
    var allowHttp: Boolean?
      get() = definedExternally
      set(value) = definedExternally
    var timeout: Number?
      get() = definedExternally
      set(value) = definedExternally
  }
  interface Documentation {
    var ORG_NAME: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_DBA: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_URL: Url?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_PHONE_NUMBER: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_LOGO: Url?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_LICENSE_NUMBER: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_LICENSING_AUTHORITY: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_LICENSE_TYPE: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_DESCRIPTION: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_PHYSICAL_ADDRESS: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_PHYSICAL_ADDRESS_ATTESTATION: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_PHONE_NUMBER_ATTESTATION: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_OFFICIAL_EMAIL: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_SUPPORT_EMAIL: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_KEYBASE: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_TWITTER: String?
      get() = definedExternally
      set(value) = definedExternally
    var ORG_GITHUB: String?
      get() = definedExternally
      set(value) = definedExternally
    @nativeGetter operator fun get(key: String): Any?
    @nativeSetter operator fun set(key: String, value: Any)
  }
  interface Principal {
    var name: String
    var email: String
    var github: String?
      get() = definedExternally
      set(value) = definedExternally
    var keybase: String?
      get() = definedExternally
      set(value) = definedExternally
    var telegram: String?
      get() = definedExternally
      set(value) = definedExternally
    var twitter: String?
      get() = definedExternally
      set(value) = definedExternally
    var id_photo_hash: String?
      get() = definedExternally
      set(value) = definedExternally
    var verification_photo_hash: String?
      get() = definedExternally
      set(value) = definedExternally
    @nativeGetter operator fun get(key: String): Any?
    @nativeSetter operator fun set(key: String, value: Any)
  }
  interface Currency {
    var code: String?
      get() = definedExternally
      set(value) = definedExternally
    var code_template: String?
      get() = definedExternally
      set(value) = definedExternally
    var issuer: PublicKey?
      get() = definedExternally
      set(value) = definedExternally
    var display_decimals: Number?
      get() = definedExternally
      set(value) = definedExternally
    var status: String? /* "live" | "dead" | "test" | "private" */
      get() = definedExternally
      set(value) = definedExternally
    var name: String?
      get() = definedExternally
      set(value) = definedExternally
    var desc: String?
      get() = definedExternally
      set(value) = definedExternally
    var conditions: String?
      get() = definedExternally
      set(value) = definedExternally
    var fixed_number: Number?
      get() = definedExternally
      set(value) = definedExternally
    var max_number: Number?
      get() = definedExternally
      set(value) = definedExternally
    var is_asset_anchored: Boolean?
      get() = definedExternally
      set(value) = definedExternally
    var anchor_asset_type:
      String? /* "fiat" | "crypto" | "nft" | "stock" | "bond" | "commodity" | "realestate" | "other" */
      get() = definedExternally
      set(value) = definedExternally
    var anchor_asset: String?
      get() = definedExternally
      set(value) = definedExternally
    var attestation_of_reserve: Url?
      get() = definedExternally
      set(value) = definedExternally
    var attestation_of_reserve_amount: String?
      get() = definedExternally
      set(value) = definedExternally
    var attestation_of_reserve_last_audit: ISODateTime?
      get() = definedExternally
      set(value) = definedExternally
    var is_unlimited: Boolean?
      get() = definedExternally
      set(value) = definedExternally
    var redemption_instructions: String?
      get() = definedExternally
      set(value) = definedExternally
    var image: Url?
      get() = definedExternally
      set(value) = definedExternally
    var regulated: Boolean?
      get() = definedExternally
      set(value) = definedExternally
    var collateral_addresses: Array<String>?
      get() = definedExternally
      set(value) = definedExternally
    var collateral_address_messages: Array<String>?
      get() = definedExternally
      set(value) = definedExternally
    var collateral_address_signatures: Array<String>?
      get() = definedExternally
      set(value) = definedExternally
    var approval_server: Url?
      get() = definedExternally
      set(value) = definedExternally
    var approval_criteria: String?
      get() = definedExternally
      set(value) = definedExternally
    @nativeGetter operator fun get(key: String): Any?
    @nativeSetter operator fun set(key: String, value: Any)
  }
  interface Validator {
    var ALIAS: String?
      get() = definedExternally
      set(value) = definedExternally
    var DISPLAY_NAME: String?
      get() = definedExternally
      set(value) = definedExternally
    var PUBLIC_KEY: PublicKey?
      get() = definedExternally
      set(value) = definedExternally
    var HOST: String?
      get() = definedExternally
      set(value) = definedExternally
    var HISTORY: Url?
      get() = definedExternally
      set(value) = definedExternally
    @nativeGetter operator fun get(key: String): Any?
    @nativeSetter operator fun set(key: String, value: Any)
  }
  interface StellarToml {
    var VERSION: String?
      get() = definedExternally
      set(value) = definedExternally
    var ACCOUNTS: Array<PublicKey>?
      get() = definedExternally
      set(value) = definedExternally
    var NETWORK_PASSPHRASE: String?
      get() = definedExternally
      set(value) = definedExternally
    var TRANSFER_SERVER_SEP0024: Url?
      get() = definedExternally
      set(value) = definedExternally
    var TRANSFER_SERVER: Url?
      get() = definedExternally
      set(value) = definedExternally
    var KYC_SERVER: Url?
      get() = definedExternally
      set(value) = definedExternally
    var WEB_AUTH_ENDPOINT: Url?
      get() = definedExternally
      set(value) = definedExternally
    var FEDERATION_SERVER: Url?
      get() = definedExternally
      set(value) = definedExternally
    var SIGNING_KEY: PublicKey?
      get() = definedExternally
      set(value) = definedExternally
    var HORIZON_URL: Url?
      get() = definedExternally
      set(value) = definedExternally
    var URI_REQUEST_SIGNING_KEY: PublicKey?
      get() = definedExternally
      set(value) = definedExternally
    var DIRECT_PAYMENT_SERVER: Url?
      get() = definedExternally
      set(value) = definedExternally
    var ANCHOR_QUOTE_SERVER: Url?
      get() = definedExternally
      set(value) = definedExternally
    var DOCUMENTATION: Documentation?
      get() = definedExternally
      set(value) = definedExternally
    var PRINCIPALS: Array<Principal>?
      get() = definedExternally
      set(value) = definedExternally
    var CURRENCIES: Array<Currency>?
      get() = definedExternally
      set(value) = definedExternally
    var VALIDATORS: Array<Validator>?
      get() = definedExternally
      set(value) = definedExternally
    @nativeGetter operator fun get(key: String): Any?
    @nativeSetter operator fun set(key: String, value: Any)
  }

  companion object {
    fun resolve(
      domain: String,
      opts: StellarTomlResolveOptions = definedExternally
    ): Promise<StellarToml>
  }
}
