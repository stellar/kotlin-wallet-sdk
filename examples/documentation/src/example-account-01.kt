// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleAccount01

import org.stellar.walletsdk.*

suspend fun main() { 

val wallet = Wallet(StellarConfiguration.Testnet)

val account = wallet
    .stellar()
    .account()

val accountKeyPair = account.createKeyPair()

val accountInfo = account.getInfo(accountKeyPair.address)

val accountHistory = account.getHistory(accountKeyPair.address)
}    
