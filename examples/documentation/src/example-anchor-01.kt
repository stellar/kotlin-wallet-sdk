// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleAnchor01

import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.AnchorServiceInfo
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.toml.TomlInfo

val wallet = Wallet(StellarConfiguration.Testnet)
val accountKeyPair = wallet.stellar().account().createKeyPair()

val anchor = wallet.anchor("testanchor.stellar.org")

suspend fun anchorToml(): TomlInfo {
  return anchor.getInfo()
}

suspend fun getAuthToken(): AuthToken {
  return anchor.auth(anchorToml()).authenticate(accountKeyPair)
}

suspend fun getAnchorServices(): AnchorServiceInfo? {
  return anchorToml().services.sep24?.let { anchor.getServicesInfo(it.transferServerSep24) }
}

val asset = IssuedAssetId("SRT", "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")

suspend fun interactiveDeposit(): String {
  return anchor.interactive().deposit(accountKeyPair.address, asset, getAuthToken()).url
}

suspend fun interactiveWithdrawal(): String {
  return anchor.interactive().withdraw(accountKeyPair.address, asset, getAuthToken()).url
}

suspend fun anchorTransaction(): AnchorTransaction {
  return anchor.getTransactionStatus("12345", getAuthToken(), anchorToml())
}

suspend fun accountHistory(): List<WalletOperation<AnchorTransaction>> {
  return anchor.getHistory(asset, getAuthToken(), anchorToml())
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
