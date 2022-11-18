package org.stellar.walletsdk.examples

import arrow.core.flatMap
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.response.*

suspend fun main() {
  // Create instance of server that allows to connect to Horizon
  val server = Server("https://horizon-testnet.stellar.org")
  val wallet = Wallet(server, Network.TESTNET)
  val account = KeyPair.random()

  // Error passed to handler, tx2 is null
  val tx2 = wallet.fund(myAddress, account.publicKeyString, "0").unwrapOrNull(SampleHandler)

  // Manually handle error
  val tx3: Transaction? =
    when (val fundOrError = wallet.fund(myAddress, account.publicKeyString, "10")) {
      is Success -> fundOrError.value
      is Failure -> {
        println("Error: ${fundOrError.value.message}")
        null
      }
    }

  // Have either fee or error
  val feeOrError = wallet.fund(myAddress, account.publicKeyString, "10").map { tx -> tx.fee }

  // Use result of first transaction to construct second. It will have an error.
  val twoFundsOrError =
    wallet.fund(myAddress, account.publicKeyString, "10").flatMap { tx ->
      wallet.fund(myAddress, tx.sourceAccount, "0")
    }

  SampleHandler.handlingErrors {
    println("Fee: ${feeOrError.unwrap()}")
    twoFundsOrError.unwrap()
  }
}
