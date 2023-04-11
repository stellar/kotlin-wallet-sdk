// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleRecovery03

import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.asset.XLM
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
val sponsor = SigningKeyPair.fromSecret("<example key>")

val recoveryKey = SigningKeyPair.fromSecret("<recovery key>")
val account = "<account address>".toPublicKeyPair()

suspend fun accountInfo() {

  val auth1 = recovery.sep10Auth(first).authenticate(recoveryKey)
  val auth2 = recovery.sep10Auth(second).authenticate(recoveryKey)

  val accountInfo = recovery.getAccountInfo(account, mapOf(first to auth1, second to auth2))

  println("Recoverable info: $accountInfo")
}
