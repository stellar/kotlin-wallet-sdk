// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleRecovery02

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

suspend fun createAccount() {

  val account = wallet.stellar().account().createKeyPair()
  val deviceKey = wallet.stellar().account().createKeyPair()

  val recoveryKey = wallet.stellar().account().createKeyPair()

  val identities =
    listOf(
      RecoveryAccountIdentity(
        RecoveryRole.OWNER,
        listOf(RecoveryAccountAuthMethod(RecoveryType.STELLAR_ADDRESS, recoveryKey.address))
      )
    )

  val recoverableWallet =
    recovery.createRecoverableWallet(
      RecoverableWalletConfig(
        account,
        deviceKey,
        AccountThreshold(10, 10, 10),
        mapOf(first to identities, second to identities),
        SignerWeight(10, 5),
        sponsor
      )
    )
  val USDC = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

  val recoverableWalletWithTrustline =
    recovery.createRecoverableWallet(
      RecoverableWalletConfig(
        account,
        deviceKey,
        AccountThreshold(10, 10, 10),
        mapOf(first to identities, second to identities),
        SignerWeight(10, 5),
        sponsor
      ) { it.addAssetSupport(USDC) }
    )

  val tx = recoverableWallet.transaction.sign(account).sign(sponsor)

  wallet.stellar().submitTransaction(tx)

  val recoverySigners = recoverableWallet.signers
}
