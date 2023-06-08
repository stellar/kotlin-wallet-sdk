// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction05

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*
import java.time.Duration

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
val stellar = wallet.stellar()
val sponsorKeyPair = SigningKeyPair.fromSecret("MySecred")

suspend fun sponsorAccountCreationAndModification() {
  val newKeyPair = account.createKeyPair()
  val replaceWith = account.createKeyPair()

  val transaction =
    stellar
        .transaction(sponsorKeyPair)
        .sponsoring(sponsorKeyPair, newKeyPair) {
          createAccount(newKeyPair)
          addAccountSigner(replaceWith, 1)
          lockAccountMasterKey()
        }
        .build()

  transaction.sign(sponsorKeyPair).sign(newKeyPair)
}
