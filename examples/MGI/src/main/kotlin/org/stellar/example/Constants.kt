package org.stellar.example

import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.SigningKeyPair

// Setup main account that will fund new (user) accounts. You can get new key pair and fill it with
// testnet tokens at
// https://laboratory.stellar.org/#account-creator?network=test
val myKey =
  System.getenv("STELLAR_KEY") ?: "SAHXRL7XY2RMUETMIRORXSQ7JJ73FOOF4OMLDSCJW22HRPMULKY4M7KP"
val myAccount = SigningKeyPair.fromSecret(myKey)

val USDC = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
val asset = USDC
const val DOMAIN = "extstellar.moneygram.com"

val clientDomain: String = System.getenv("CLIENT_DOMAIN")
val clientPrivate: String = System.getenv("CLIENT_PRIVATE")
val accountKp = SigningKeyPair.fromSecret(clientPrivate)

val wallet = Wallet(StellarConfiguration.Testnet)

// Create instance of stellar, account and transaction services
val stellar = wallet.stellar()

val anchor = wallet.anchor("https://$DOMAIN")
