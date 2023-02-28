// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction02

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()

// Third-party key that will sponsor creating new account
val externalKeyPair = "MySponsorAddress".toPublicKeyPair()
val newKeyPair = account.createKeyPair()
val stellar = wallet.stellar()

suspend fun makeCreateTx(): Transaction {
  return stellar.transaction(externalKeyPair).createAccount(newKeyPair).build()
}

suspend fun submitCreateTx(signedCreateTx: Transaction) {
  wallet.stellar().submitTransaction(signedCreateTx)
}

suspend fun addDeviceKeyPair() {
  val deviceKeyPair = account.createKeyPair()

  val modifyAccountTransaction =
    stellar
      .transaction(newKeyPair)
      .addAccountSigner(
        deviceKeyPair.address,
        signerWeight = 1,
      )
      .lockAccountMasterKey()
      .build()
      .sign(newKeyPair)

  wallet.stellar().submitTransaction(modifyAccountTransaction)
}
