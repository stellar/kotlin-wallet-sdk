// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction04

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
val stellar = wallet.stellar()
val sponsorKeyPair = SigningKeyPair.fromSecret("MySecred")

suspend fun sponsorAccountCreation() {
  val newKeyPair = account.createKeyPair()

  val transaction =
    stellar
      .transaction(sponsorKeyPair)
      .sponsoring(sponsorKeyPair, sponsoredAccount = newKeyPair) { createAccount(newKeyPair) }
      .build()

  transaction.sign(sponsorKeyPair).sign(newKeyPair)
}
