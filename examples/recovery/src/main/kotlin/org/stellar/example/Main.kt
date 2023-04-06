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
val cfg = readConfig()
val sponsor = SigningKeyPair.fromSecret(cfg.key)
val first = RecoveryServerKey("first")
val second = RecoveryServerKey("second")
val servers = mapOf(first to cfg.recovery.server1, second to cfg.recovery.server2)
val recovery = wallet.recovery(servers)

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

  val identities =
    listOf(
      RecoveryAccountIdentity(
        RecoveryRole.OWNER,
        listOf(RecoveryAccountAuthMethod(RecoveryType.STELLAR_ADDRESS, sponsor.address))
      )
    )

  // Register account with recovery servers (can be done for account that does not exist
  // on the network yet)
  val recoverableWallet =
    recovery.createRecoverableWallet(
      RecoverableWalletConfig(
        newAccount,
        deviceKey,
        AccountThreshold(10, 10, 10),
        mapOf(first to identities, second to identities),
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
  val auth1 = recovery.sep10Auth(first).authenticate(sponsor)
  val auth2 = recovery.sep10Auth(second).authenticate(sponsor)

  val recoveredSigner = wallet.stellar().account().createKeyPair()

  val transaction =
    wallet.stellar().transaction(account).addAccountSigner(recoveredSigner, 10).build()

  val accountInfo = recovery.getAccountInfo(account, mapOf(first to auth1, second to auth2))

  println(accountInfo)

  val signed =
    recovery.signWithRecoveryServers(
      transaction,
      account,
      mapOf(
        first to RecoveryServerSigning(recoverySigners[0], auth1),
        second to RecoveryServerSigning(recoverySigners[1], auth2)
      )
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
