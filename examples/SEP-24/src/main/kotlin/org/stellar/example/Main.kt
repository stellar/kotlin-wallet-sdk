package org.stellar.example

import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.anchor.*
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

  val depositWatcher = anchor.watcher().watchOneTransaction(token, deposit.id)

  do {
    val statusChange = depositWatcher.channel.receive()

    when (statusChange) {
      is StatusChange ->
        println(
          "Transaction status changed from ${statusChange.oldStatus ?: "none"} to ${statusChange.status}. Message: ${statusChange.transaction.message}"
        )
      is ChannelClosed -> println("Transaction tracking finished")
      is RetriesExhausted -> println("Retries exhausted trying obtain transaction data, giving up.")
    }
  } while (statusChange !is ChannelClosed)

  println("Successful deposit")

  // Start interactive withdrawal
  val withdrawal = anchor.interactive().withdraw(asset, authToken = token)

  // Request user input
  println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

  val withdrawalWatcher = anchor.watcher().watchOneTransaction(token, withdrawal.id)
  var statusChange: StatusUpdateEvent

  // Wait for user input
  do {
    statusChange = withdrawalWatcher.channel.receive()
  } while (
    ((statusChange as? StatusChange) ?: throw Exception("Channel unexpectedly closed")).status !=
      TransactionStatus.PENDING_USER_TRANSFER_START
  )

  // Send transaction with transfer
  val t = (statusChange.transaction as WithdrawalTransaction)
  val transfer = stellar.transaction(t.from).transferWithdrawalTransaction(t, asset).build()

  transfer.sign(keypair)

  stellar.submitTransaction(transfer)

  do {
    statusChange = withdrawalWatcher.channel.receive()

    when (statusChange) {
      is StatusChange ->
        println(
          "Withdrawal transaction status changed to ${statusChange.status}. Message: ${statusChange.transaction.message}"
        )
      is ChannelClosed -> println("Transaction tracking finished")
      is RetriesExhausted -> println("Retries exhausted trying obtain transaction data, giving up.")
    }
  } while (statusChange !is ChannelClosed)

  println("Successful withdrawal")

  wallet.close()
}
