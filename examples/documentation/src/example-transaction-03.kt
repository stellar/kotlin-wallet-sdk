// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction03

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
val stellar = wallet.stellar()
val sponsorKeyPair = SigningKeyPair.fromSecret("MySecred")
val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
val sponsoredKeyPair = SigningKeyPair.fromSecret("Sponsored")

suspend fun sponsorOperation() {
  val transaction =
    stellar
      .transaction(sponsoredKeyPair)
      .sponsoring(sponsorKeyPair) { addAssetSupport(asset) }
      .build()

  transaction.sign(sponsorKeyPair).sign(sponsoredKeyPair)
}
