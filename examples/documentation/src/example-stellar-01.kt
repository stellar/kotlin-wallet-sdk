// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleStellar01

import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.sign

suspend fun main() { 

val wallet = Wallet(StellarConfiguration.Testnet)

val account = wallet
    .stellar()
    .account()

val accountKeyPair = account.createKeyPair()

val accountInfo = account.getInfo(accountKeyPair.address)

val accountHistory = account.getHistory(accountKeyPair.address)

val sourceAccountKeyPair = account.createKeyPair()
val destinationAccountKeyPair = account.createKeyPair()
val txnBuilder = wallet
    .stellar()
    .transaction()

val fundTxn = txnBuilder.fund(sourceAccountKeyPair.address, destinationAccountKeyPair.address)

val lockMasterKeyTxn = txnBuilder.lockAccountMasterKey(destinationAccountKeyPair.address)

val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
val addAssetTxn = txnBuilder.addAssetSupport(sourceAccountKeyPair.address, asset)

val removeAssetTxn = txnBuilder.removeAssetSupport(sourceAccountKeyPair.address, asset)

val newSignerKeyPair = account.createKeyPair()
val addSignerTxn = txnBuilder.addAccountSigner(sourceAccountKeyPair.address, newSignerKeyPair.address, 10)

val removeSignerTxn = txnBuilder.removeAccountSigner(sourceAccountKeyPair.address, newSignerKeyPair.address)

val signedTxn = fundTxn.sign(sourceAccountKeyPair)
val submitTxn = wallet
    .stellar()
    .submitTransaction(signedTxn)
}    
