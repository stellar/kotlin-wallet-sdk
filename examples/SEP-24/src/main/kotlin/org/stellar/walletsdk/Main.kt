package org.stellar.walletsdk

import java.math.BigDecimal
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

// Setup main account that will fund new (user) accounts. You can get new key pair and fill it with
// testnet tokens at
// https://laboratory.stellar.org/#account-creator?network=test
private val myAddress = "GCNVZJLJ5XZZALLZBR7BGUMGMF2KVZTBIGTU2A5KOPS2PPCZS6PV4LAD"
private val myKey = "SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I"

suspend fun main() {
  // Create instance of server that allows to connect to Horizon
  val server = Server("https://horizon-testnet.stellar.org")
  val wallet = Wallet()
  // Generate new (user) account and fund it with 10 XLM from main account
  val account = KeyPair.random()
  val tx = wallet.fund(myAddress, account.publicKeyString, "10")

  // Sign with your main account's key and send transaction to the network
  println("Registering new account")
  tx.sign(KeyPair.fromSecretSeed(myKey))
  assert(wallet.submitTransaction(tx))

  val anchor = Anchor(server, Network.TESTNET)

  // Get info from the anchor server
  val info = anchor.getInfo("https://testanchor.stellar.org/.well-known/stellar.toml")

  // Get SEP-24 info
  val servicesInfo = anchor.getServicesInfo("https://testanchor.stellar.org/sep24")

  println("Info from anchor server: $info")
  println("SEP-24 info from anchor server: $servicesInfo")

  // Create add trustline transaction for an asset. This allows user account to receive trusted
  // asset.
  val addTrustline =
    wallet.addAssetSupport(
      account.publicKeyString,
      "SRT",
      "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
    )

  // Sign and send transaction
  println("Adding trustline")
  addTrustline.sign(account)
  assert(wallet.submitTransaction(addTrustline))

  // Start interactive deposit
  val deposit =
    anchor.getInteractiveDeposit(
      account.publicKeyString,
      homeDomain = "testanchor.stellar.org",
      assetCode = "SRT",
      walletSigner = WalletSignerImpl(account)
    )

  // Request user input
  println("Additional user info is required for the deposit, please visit: ${deposit.url}")

  // Get transaction info
  // val transaction = GET TRANSFER_SERVER_SEP0024/transaction

  println("Waiting for tokens...")
  // Optional step: wait for token to appear on user account
  // TODO: replace with waiting for transaction
  while (
    server
      .accounts()
      .account(account.publicKeyString)
      .balances
      .filter { it.assetCode.isPresent && it.assetCode.get() == "SRT" }
      .any { it.balance.toBigDecimal() <= BigDecimal.ZERO }
  ) {
    delay(5.seconds)
  }

  // Start interactive withdrawal
  val withdrawal =
    anchor.getInteractiveWithdrawal(
      account.publicKeyString,
      homeDomain = "testanchor.stellar.org",
      assetCode = "SRT",
      walletSigner = WalletSignerImpl(account)
    )

  // Request user input
  println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

  // Wait for user input

  // Send transaction with transfer
}

val KeyPair.publicKeyString: String
  get() = this.accountId

val KeyPair.secretKey: String
  get() = this.secretSeed.concatToString()

class WalletSignerImpl(private val keyPair: KeyPair) : WalletSigner {
  override fun signWithClientAccount(txn: Transaction): Transaction {
    txn.sign(keyPair)
    return txn
  }

  override fun signWithDomainAccount(
    transactionString: String,
    networkPassPhrase: String
  ): Transaction {
    val txn =
      Transaction.fromEnvelopeXdr(transactionString, Network(networkPassPhrase)) as Transaction
    txn.sign(keyPair)
    return txn
  }
}
