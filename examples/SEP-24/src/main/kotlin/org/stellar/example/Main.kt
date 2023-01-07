package org.stellar.example

import kotlinx.coroutines.delay
import org.stellar.walletsdk.ApplicationConfiguration
import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign
import org.stellar.walletsdk.horizon.toTransferTransaction
import kotlin.time.Duration.Companion.seconds

// Setup main account that will fund new (user) accounts. You can get new key pair and fill it with
// testnet tokens at
// https://laboratory.stellar.org/#account-creator?network=test
private val myKey =
  System.getenv("STELLAR_KEY") ?: "SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I"
private val myAccount = SigningKeyPair.fromSecret(myKey)

private const val IS_LOCAL = false
// private val SRT = IssuedAssetId("SRT",
// "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")
private val USDC = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
private val USDC_ANCHOR_PLATFORM =
  IssuedAssetId("USDC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP")
private val asset = if (IS_LOCAL) USDC_ANCHOR_PLATFORM else USDC
private val homeDomain = if (IS_LOCAL) "localhost:8080" else "testanchor.stellar.org"
private val scheme = if (IS_LOCAL) "http" else "https"

suspend fun main() {
  val wallet = Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(useHttp = IS_LOCAL))

  // Create instance of stellar, account and transaction services
  val stellar = wallet.stellar()
  val account = wallet.stellar().account()
  // Generate new (user) account and fund it with 10 XLM from main account
  val keypair = account.createKeyPair()
  val tx = stellar.transaction(myAccount).fund(keypair.address, "10").build()

  // Sign with your main account's key and send transaction to the network
  println("Registering new account")
  tx.sign(myAccount)
  stellar.submitTransaction(tx)

  val anchor = wallet.anchor(homeDomain)

  // Get info from the anchor server
  val info = anchor.getInfo()

  // Get SEP-24 info
  val servicesInfo = anchor.getServicesInfo("$scheme://$homeDomain/sep24")

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
  val token = anchor.auth(info).authenticate(keypair)

  // Start interactive deposit
  val deposit = anchor.interactive().deposit(keypair.address, asset, authToken = token)

  // Request user input
  println("Additional user info is required for the deposit, please visit: ${deposit.url}")

  println("Waiting for tokens...")

  var status = ""

  // Optional step: wait for token to appear on user account
  do {
    // Get transaction info
    val transaction = anchor.getTransactionStatus(deposit.id, token, info)

    if (status != transaction.status) {
      status = transaction.status
      println("Deposit transaction status changed to $status. Message: ${transaction.message}")
    }

    delay(5.seconds)
  } while (transaction.status != "completed")

  println("Successful deposit")

  // Start interactive withdrawal
  val withdrawal = anchor.interactive().withdraw(keypair.address, asset, authToken = token)

  // Request user input
  println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

  var transaction: AnchorTransaction

  // Wait for user input
  do {
    // Get transaction info
    transaction = anchor.getTransactionStatus(withdrawal.id, token, info)
    delay(5.seconds)
  } while (transaction.status != "pending_user_transfer_start")

  // Send transaction with transfer
  val transfer = (transaction as WithdrawalTransaction).toTransferTransaction(stellar, asset)

  transfer.sign(keypair)

  stellar.submitTransaction(transfer)

  do {
    transaction = anchor.getTransactionStatus(withdrawal.id, token, info) as WithdrawalTransaction

    if (status != transaction.status) {
      status = transaction.status
      println("Withdrawal transaction status changed to $status. Message: ${transaction.message}")
    }

    delay(5.seconds)
  } while (transaction.status != "completed")

  println("Successful withdrawal")
}
