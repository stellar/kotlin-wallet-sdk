// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleRecovery04

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
val sponsor = SigningKeyPair.fromSecret("<example key>")

val recoveryKey = SigningKeyPair.fromSecret("<recovery key>")
val account = "<account address>".toPublicKeyPair()
val recoverySigners = listOf("<first signer address>", "<second signer address>")

suspend fun recover() {

  val refill =
    wallet
        .stellar()
        .transaction(sponsor)
        .transfer(account.address, XLM, "0.5")
        .build()
        .sign(sponsor)

  wallet.stellar().submitTransaction(refill)

  val auth1 = recovery.sep10Auth(first).authenticate(recoveryKey)
  val auth2 = recovery.sep10Auth(second).authenticate(recoveryKey)

  val newKey = wallet.stellar().account().createKeyPair()

  val serverAuth = mapOf(
    first to RecoveryServerSigning(recoverySigners[0], auth1),
    second to RecoveryServerSigning(recoverySigners[1], auth2)
  )

  val signedReplaceKeyTransaction =
    recovery.replaceDeviceKey(
      account,
      newKey,
      serverAuth
    )

  val feeBumped = wallet.stellar().makeFeeBump(sponsor, signedReplaceKeyTransaction).sign(sponsor)

  wallet.stellar().submitTransaction(feeBumped)
}
