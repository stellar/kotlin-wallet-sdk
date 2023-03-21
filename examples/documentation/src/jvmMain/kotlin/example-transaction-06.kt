// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction06

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
val stellar = wallet.stellar()
val sponsorKeyPair = SigningKeyPair.fromSecret("MySecred")
val sponsoredKeyPair = SigningKeyPair.fromSecret("Sponsored")

suspend fun sponsorAccountModification() {

  val replaceWith = account.createKeyPair()

  val transaction =
    stellar
      .transaction(sponsoredKeyPair)
      .sponsoring(sponsorKeyPair) {
        lockAccountMasterKey()
        addAccountSigner(replaceWith, signerWeight = 1)
      }
      .build()

  transaction.sign(sponsoredKeyPair).sign(sponsorKeyPair)

  val feeBump = stellar.makeFeeBump(sponsorKeyPair, transaction)
  feeBump.sign(sponsorKeyPair)

  wallet.stellar().submitTransaction(feeBump)
}
