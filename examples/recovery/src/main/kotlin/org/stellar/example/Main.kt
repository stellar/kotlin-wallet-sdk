package org.stellar.example

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.PropertySource
import com.sksamuel.hoplite.addResourceSource
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.asset.XLM
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign
import org.stellar.walletsdk.recovery.*

val wallet = Wallet.Testnet
val recovery = wallet.recovery()
val cfg = readConfig()
val sponsor = SigningKeyPair.fromSecret(cfg.key)

suspend fun main() {
  val recoverData = createAccount()

  val account = recoverData.first
  val recoverySigners = recoverData.second

  recoverAccount(account, recoverySigners)

  wallet.close()
}

fun readConfig(): Config {
  val cfgBuilder = ConfigLoaderBuilder.default()
  cfgBuilder.addPropertySource(PropertySource.environment())
  cfgBuilder.addResourceSource("/secret.yaml")

  return cfgBuilder.build().loadConfigOrThrow<Config>()
}

suspend fun createAccount(): Pair<SigningKeyPair, List<String>> {
  val newAccount = wallet.stellar().account().createKeyPair()
  val deviceKey = wallet.stellar().account().createKeyPair()

  // Register account with recovery servers (can be done for account that does not exist
  // on the network yet)
  val recoverableWallet =
    recovery.createRecoverableWallet(
      RecoverableWalletConfig(
        newAccount,
        deviceKey,
        AccountThreshold(10, 10, 10),
        listOf(
          RecoveryAccountIdentity(
            RecoveryRole.OWNER,
            listOf(RecoveryAccountAuthMethod(RecoveryType.STELLAR_ADDRESS, sponsor.address))
          )
        ),
        listOf(cfg.recovery.server1, cfg.recovery.server2),
        SignerWeight(10, 5),
        sponsor
      )
    )

  val tx = recoverableWallet.transaction.sign(newAccount).sign(sponsor)

  wallet.stellar().submitTransaction(tx)

  println("Recoverable account created: ${newAccount.address} transaction ${tx.hashHex()}")

  return newAccount to recoverableWallet.signers
}

suspend fun recoverAccount(account: AccountKeyPair, recoverySigners: List<String>) {
  val auth1 = wallet.recovery().sep10Auth(cfg.recovery.server1).authenticate(sponsor)
  val auth2 = wallet.recovery().sep10Auth(cfg.recovery.server2).authenticate(sponsor)

  val recoveredSigner = wallet.stellar().account().createKeyPair()

  val transaction =
    wallet.stellar().transaction(account).addAccountSigner(recoveredSigner, 10).build()

  val signed =
    recovery.signWithRecoveryServers(
      transaction,
      account.address,
      listOf(
        RecoveryServerAuth(cfg.recovery.server1.endpoint, recoverySigners[0], auth1),
        RecoveryServerAuth(cfg.recovery.server2.endpoint, recoverySigners[1], auth2)
      ),
    )

  val refill =
    wallet
      .stellar()
      .transaction(sponsor)
      .transfer(account.address, XLM, "0.5")
      .build()
      .sign(sponsor)

  wallet.stellar().submitTransaction(refill)

  val sponsored = wallet.stellar().makeFeeBump(sponsor, signed).sign(sponsor)

  wallet.stellar().submitTransaction(sponsored)
}
