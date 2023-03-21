// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransactionXdr01

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
val stellar = wallet.stellar()

val sponsorKeyPair = "SponsorAddress".toPublicKeyPair()

suspend fun sponsorAccountCreation(): String {
  val newKeyPair = account.createKeyPair()

  return stellar
    .transaction(sponsorKeyPair)
    .sponsoring(sponsorKeyPair) { createAccount(newKeyPair) }
    .build()
    .sign(newKeyPair)
    .toEnvelopeXdrBase64()
}

// On the server
fun signTransaction(xdrString: String): String {
  val sponsorPrivateKey = SigningKeyPair.fromSecret("MySecred")

  val signedTransaction = stellar.decodeTransaction(xdrString).sign(sponsorPrivateKey)

  return signedTransaction.toEnvelopeXdrBase64()
}

suspend fun recoverSigned(xdrString: String) {
  val signedTransaction = stellar.decodeTransaction(xdrString)

  stellar.submitTransaction(signedTransaction)
}
