package org.stellar.example

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign
import org.stellar.walletsdk.horizon.transaction.transferWithdrawalTransaction

// Setup main account that will fund new (user) accounts. You can get new key pair and fill it with
// testnet tokens at
// https://laboratory.stellar.org/#account-creator?network=test
private val myKey =
  System.getenv("STELLAR_KEY") ?: "SAHXRL7XY2RMUETMIRORXSQ7JJ73FOOF4OMLDSCJW22HRPMULKY4M7KP"
private val myAccount = SigningKeyPair.fromSecret(myKey)

// private val SRT = IssuedAssetId("SRT",
// "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")
private val USDC = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
private val asset = USDC
private const val DOMAIN = "testanchor.stellar.org"

suspend fun main() {
  val wallet = Wallet(StellarConfiguration.Testnet)

  // Create instance of stellar, account and transaction services
  val stellar = wallet.stellar()
  val account = wallet.stellar().account()
  // Generate new (user) account and fund it with 10 XLM from main account
  val keypair = account.createKeyPair()
  val tx = stellar.transaction(myAccount).createAccount(keypair, 10u).build()

  // Sign with your main account's key and send transaction to the network
  println("Registering new account")
  tx.sign(myAccount)
  stellar.submitTransaction(tx)

  val anchor = wallet.anchor("https://$DOMAIN")

  // Get info from the anchor server
  val info = anchor.getInfo()

  // Get SEP-24 info
  val servicesInfo = anchor.getServicesInfo()

  println("Info from anchor server: $info")
  println("SEP-24 info from anchor server: $servicesInfo")

  // Create add trustline transaction for an asset. This allows user account to receive trusted
  // asset.
  val addTrustline = stellar.transaction(keypair).addAssetSupport(asset).build()

  // Sign and send transaction
  println("Adding trustline...")
  addTrustline.sign(keypair)
  stellar.submitTransaction(addTrustline)

  // Authorizing
  val token = anchor.auth().authenticate(keypair)

  val sep9 = mapOf("email_address" to "mail@example.com")

  // Start interactive deposit
  val deposit = anchor.interactive().deposit(asset, token, sep9)

  // Request user input
  println("Additional user info is required for the deposit, please visit: ${deposit.url}")

  println("Waiting for tokens...")

  var status: TransactionStatus? = null

  // Optional step: wait for token to appear on user account
  do {
    // Get transaction info
    val transaction = anchor.getTransaction(deposit.id, token)

    if (status != transaction.status) {
      status = transaction.status
      println("Deposit transaction status changed to $status. Message: ${transaction.message}")
    }

    delay(5.seconds)
  } while (transaction.status != TransactionStatus.COMPLETED)

  println("Successful deposit")

  // Start interactive withdrawal
  val withdrawal = anchor.interactive().withdraw(asset, authToken = token)

  // Request user input
  println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

  var transaction: AnchorTransaction

  status = null

  // Wait for user input
  do {
    // Get transaction info
    transaction = anchor.getTransaction(withdrawal.id, token)
    delay(5.seconds)
  } while (transaction.status != TransactionStatus.PENDING_USER_TRANSFER_START)

  // Send transaction with transfer
  val t = (transaction as WithdrawalTransaction)
  val transfer = stellar.transaction(t.from).transferWithdrawalTransaction(t, asset).build()

  transfer.sign(keypair)

  stellar.submitTransaction(transfer)

  do {
    transaction = anchor.getTransaction(withdrawal.id, token) as WithdrawalTransaction

    if (status != transaction.status) {
      status = transaction.status
      println("Withdrawal transaction status changed to $status. Message: ${transaction.message}")
    }

    delay(5.seconds)
  } while (transaction.status != TransactionStatus.COMPLETED)

  println("Successful withdrawal")

  wallet.close()
}
