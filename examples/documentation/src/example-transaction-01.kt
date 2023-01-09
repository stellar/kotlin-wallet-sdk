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
val stellar = wallet.stellar()

suspend fun fund(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).fund(destinationAccountKeyPair.address).build()
}

suspend fun lockMasterKey(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).lockAccountMasterKey().build()
}

val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

suspend fun addAsset(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).addAssetSupport(asset).build()
}

suspend fun removeAsset(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).removeAssetSupport(asset).build()
}

val newSignerKeyPair = account.createKeyPair()

suspend fun addSigner(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).addAccountSigner(newSignerKeyPair.address, 10).build()
}

suspend fun removeSigner(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).removeAccountSigner(newSignerKeyPair.address).build()
}

suspend fun setThreshold(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).setThreshold(low = 1, medium = 10, high = 30).build()
}

suspend fun signAndSubmit() {
  val signedTxn = fund().sign(sourceAccountKeyPair)
  wallet.stellar().submitTransaction(signedTxn)
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
