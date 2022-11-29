package org.stellar.walletsdk

import java.math.BigDecimal
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.util.SchemeUtil
import org.stellar.walletsdk.anchor.Anchor

// Setup main account that will fund new (user) accounts. You can get new key pair and fill it with
// testnet tokens at
// https://laboratory.stellar.org/#account-creator?network=test
private val myKey =
  System.getenv("STELLAR_KEY") ?: "SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I"
private val myAddress = KeyPair.fromSecretSeed(myKey).accountId

private val useLocal = true
private val assetCode = if (useLocal) "USDC" else "SRT"
private val assetIssuer =
  if (useLocal) "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP"
  else "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
private val homeDomain = if (useLocal) "localhost:8080" else "testanchor.stellar.org"
private val scheme = if (useLocal) "http" else "https"

suspend fun main() {
  if (useLocal) {
    SchemeUtil.useHttp()
  }

  // Create instance of server that allows to connect to Horizon
  val server = Server("https://horizon-testnet.stellar.org")
  val wallet = Wallet(server, Network.TESTNET)
  // Generate new (user) account and fund it with 10 XLM from main account
  val account = wallet.create()
  val tx = wallet.fund(myAddress, account.publicKeyString, "10")

  // Sign with your main account's key and send transaction to the network
  println("Registering new account")
  tx.sign(KeyPair.fromSecretSeed(myKey))
  assert(wallet.submitTransaction(tx))

  val anchor = Anchor(server, Network.TESTNET, homeDomain)

  // Get info from the anchor server
  val info = anchor.getInfo()

  // Get SEP-24 info
  val servicesInfo = anchor.getServicesInfo("$scheme://$homeDomain/sep24")

  println("Info from anchor server: $info")
  println("SEP-24 info from anchor server: $servicesInfo")

  // Create add trustline transaction for an asset. This allows user account to receive trusted
  // asset.
  val addTrustline = wallet.addAssetSupport(account.publicKeyString, assetCode, assetIssuer)

  // Sign and send transaction
  println("Adding trustline")
  addTrustline.sign(account)
  assert(wallet.submitTransaction(addTrustline))

  // Authorizing
  val token = anchor.auth(info, WalletSignerImpl(account)).authenticate(account.publicKeyString)

  // Start interactive deposit
  val deposit =
    anchor.interactive().deposit(account.publicKeyString, assetCode = assetCode, authToken = token)

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
      .filter { it.assetCode.isPresent && it.assetCode.get() == assetCode }
      .any { it.balance.toBigDecimal() <= BigDecimal.ZERO }
  ) {
    delay(5.seconds)
  }

  // Start interactive withdrawal
  val withdrawal =
    anchor.interactive().withdraw(
      account.publicKeyString,
      assetCode = assetCode,
      authToken = token
    )

  // Request user input
  println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

  // Wait for user input

  // Send transaction with transfer
}

class WalletSignerImpl(private val keyPair: AccountKeypair) : WalletSigner {
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
