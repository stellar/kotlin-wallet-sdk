// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleAccount01

import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.walletsdk.*

val wallet = Wallet(StellarConfiguration.Testnet)

val account = wallet.stellar().account()
val accountKeyPair = account.createKeyPair()

suspend fun getAccountInfo(): AccountInfo {
    return account.getInfo(accountKeyPair.address)
}

suspend fun getAccountHistory(): List<WalletOperation<OperationResponse>> {
    return account.getHistory(accountKeyPair.address)
}
suspend fun main() {
  val info = getAccountInfo()
  println(info)

  val history = getAccountHistory()
  println(history)
}
