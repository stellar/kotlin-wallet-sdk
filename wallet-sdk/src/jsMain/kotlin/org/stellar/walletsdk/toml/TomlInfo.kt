package org.stellar.walletsdk.toml

import org.stellar.walletsdk.Network

actual class TomlInfo {
  actual val services: InfoServices
    get() = TODO("Not yet implemented")
  actual val version: String?
    get() = TODO("Not yet implemented")
  actual val networkPassphrase: String?
    get() = TODO("Not yet implemented")
  actual val federationServer: String?
    get() = TODO("Not yet implemented")
  actual val authServer: String?
    get() = TODO("Not yet implemented")
  actual val transferServer: String?
    get() = TODO("Not yet implemented")
  actual val transferServerSep24: String?
    get() = TODO("Not yet implemented")
  actual val kycServer: String?
    get() = TODO("Not yet implemented")
  actual val webAuthEndpoint: String?
    get() = TODO("Not yet implemented")
  actual val signingKey: String?
    get() = TODO("Not yet implemented")
  actual val horizonUrl: String?
    get() = TODO("Not yet implemented")
  actual val accounts: List<String>?
    get() = TODO("Not yet implemented")
  actual val uriRequestSigningKey: String?
    get() = TODO("Not yet implemented")
  actual val directPaymentServer: String?
    get() = TODO("Not yet implemented")
  actual val anchorQuoteServer: String?
    get() = TODO("Not yet implemented")
  actual val documentation: InfoDocumentation?
    get() = TODO("Not yet implemented")
  actual val principals: List<InfoContact>?
    get() = TODO("Not yet implemented")
  actual val currencies: List<InfoCurrency>?
    get() = TODO("Not yet implemented")
  actual val validators: List<InfoValidator>?
    get() = TODO("Not yet implemented")

  actual fun validate(network: Network) {
  }
}
