// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleRecovery01

import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.asset.*
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.horizon.*
import org.stellar.walletsdk.recovery.*

val wallet = Wallet.Testnet

val first = RecoveryServerKey("first")
val second = RecoveryServerKey("second")
val firstServer = RecoveryServer("recovery.example.com", "auth.example.com", "example.com")
val secondServer = RecoveryServer("recovery2.example.com", "auth2.example.com", "example.com")
val servers = mapOf(first to firstServer, second to secondServer)
val recovery = wallet.recovery(servers)
