// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction01

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.sign

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()

val sourceAccountKeyPair = account.createKeyPair()
val destinationAccountKeyPair = account.createKeyPair()
val txnBuilder = wallet
  .stellar()
  .transaction()

suspend fun fund(): Transaction {
  return txnBuilder.fund(sourceAccountKeyPair.address, destinationAccountKeyPair.address)
}

suspend fun lockMasterKey(): Transaction {
  return txnBuilder.lockAccountMasterKey(destinationAccountKeyPair.address)
}

val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

suspend fun addAsset(): Transaction {
  return txnBuilder.addAssetSupport(sourceAccountKeyPair.address, asset)
}

suspend fun removeAsset(): Transaction {
  return txnBuilder.removeAssetSupport(sourceAccountKeyPair.address, asset)
}

val newSignerKeyPair = account.createKeyPair()

suspend fun addSigner(): Transaction {
  return txnBuilder.addAccountSigner(sourceAccountKeyPair.address, newSignerKeyPair.address, 10)
}

suspend fun removeSigner(): Transaction {
  return txnBuilder.removeAccountSigner(sourceAccountKeyPair.address, newSignerKeyPair.address)
}

suspend fun signAndSubmit(): Boolean {
  val signedTxn = fund().sign(sourceAccountKeyPair)
  return wallet.stellar().submitTransaction(signedTxn)
}
suspend fun main() {
  val fundTxn = fund()
  println(fundTxn)

  val lockMasterKeyTxn = lockMasterKey()
  println(lockMasterKeyTxn)

  val addAssetTxn = addAsset()
  println(addAssetTxn)

  val removeAssetTxn = removeAsset()
  println(removeAssetTxn)

  val addSignerTxn = addSigner()
  println(addSignerTxn)

  val removeSignerTxn = removeSigner()
  println(removeSignerTxn)

  val signAndSubmitTxn = signAndSubmit()
  println(signAndSubmitTxn)
}
