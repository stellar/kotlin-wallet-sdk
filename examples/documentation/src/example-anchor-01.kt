// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleAnchor01

import io.ktor.http.Url
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.toml.TomlInfo

val wallet = Wallet(StellarConfiguration.Testnet)
val accountKeyPair = wallet.stellar().account().createKeyPair()

val anchor = wallet.anchor("https://testanchor.stellar.org")

suspend fun anchorToml(): TomlInfo {
  return anchor.getInfo()
}

suspend fun getAuthToken(): AuthToken {
  return anchor.auth().authenticate(accountKeyPair)
}

suspend fun getAnchorServices(): AnchorServiceInfo {
  return anchor.interactive().getServicesInfo()
}

val asset = IssuedAssetId("SRT", "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")

suspend fun interactiveDeposit(): String {
  return anchor.interactive().deposit(asset, getAuthToken()).url
}

suspend fun interactiveWithdrawal(): String {
  return anchor.interactive().withdraw(asset, getAuthToken()).url
}

suspend fun depositSep9(): InteractiveFlowResponse {
  val sep9 = mapOf("email_address" to "mail@example.com")

  return anchor.interactive().deposit(asset, getAuthToken(), sep9)
}

suspend fun depositDifferentAccount(): InteractiveFlowResponse {
  val recipientAccount = "G..."
  val memo = "my memo" to MemoType.TEXT
  return anchor
    .interactive()
    .deposit(asset, getAuthToken(), destinationAccount = recipientAccount, destinationMemo = memo)
}

suspend fun anchorTransaction(): AnchorTransaction {
  return anchor.getTransaction("12345", getAuthToken())
}

suspend fun accountHistory(): List<AnchorTransaction> {
  return anchor.getHistory(asset, getAuthToken())
}

suspend fun watchTransaction() {
  val watcher = anchor.watcher()
  val result = watcher.watchOneTransaction(getAuthToken(), "transaction id")

  do {
    val event = result.channel.receive()
    when (event) {
      is StatusChange ->
        println("Status changed to ${event.status}. Transaction: ${event.transaction}")
      is ChannelClosed -> println("Channel closed. Job is done")
      is ExceptionHandlerExit -> println("Exception handler exited the job")
    }
  } while (event !is ChannelClosed)
}

suspend fun watchAsset() {
  val watcher = anchor.watcher()
  val result = watcher.watchAsset(getAuthToken(), asset)
  
  do {
    val event = result.channel.receive()
    when (event) {
      is StatusChange ->
        println("Status changed to ${event.status}. Transaction: ${event.transaction}")
      is ChannelClosed -> println("Channel closed. Job is done")
      is ExceptionHandlerExit -> println("Exception handler exited the job")
    }
  } while (event !is ChannelClosed)
}
suspend fun main() {
  val anchorInfo = anchorToml()
  println(anchorInfo)

  val authToken = getAuthToken()
  println(authToken)

  val anchorServices = getAnchorServices()
  println(anchorServices)

  val depositUrl = interactiveDeposit()
  println(depositUrl)

  val withdrawalUrl = interactiveWithdrawal()
  println(withdrawalUrl)

  val transactionInfo = anchorTransaction()
  println(transactionInfo)

  val history = accountHistory()
  println(history)
}
