package org.stellar.example

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.auth
import org.stellar.walletsdk.anchor.interactive

object Deposit {
  @JvmStatic
  suspend fun main() {
    // Authorizing
    val token = anchor.auth().authenticate(myAccount, Signer, clientDomain = clientDomain)

    println("Auth token: $token")

    // Start interactive deposit
    val deposit = anchor.interactive().deposit(asset, authToken = token)

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

    wallet.close()
  }
}
