package org.stellar.example

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.horizon.sign

object Withdrawal {
  suspend fun main() {
    // Authorizing
    val token = anchor.auth().authenticate(myAccount, Signer, clientDomain = clientDomain)

    println("Auth token: $token")

    // Start interactive withdrawal
    val withdrawal =
      anchor.interactive().withdraw(asset, withdrawalAccount = myAccount.address, authToken = token)

    // Request user input
    println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

    var transaction: AnchorTransaction

    var status: TransactionStatus? = null

    // Wait for user input
    do {
      // Get transaction info
      transaction = anchor.sep24().getTransaction(withdrawal.id, token)
      delay(5.seconds)
    } while (transaction.status != TransactionStatus.PENDING_USER_TRANSFER_START)

    // Send transaction with transfer
    val t = (transaction as WithdrawalTransaction)
    val transfer = stellar.transaction(t.from!!).transferWithdrawalTransaction(t, asset).build()

    transfer.sign(myAccount)

    stellar.submitTransaction(transfer)

    println("Stellar transaction has been sent ${transfer.hashHex()}")

    do {
      transaction = anchor.sep24().getTransaction(withdrawal.id, token) as WithdrawalTransaction

      if (status != transaction.status) {
        status = transaction.status
        println("Withdrawal transaction status changed to $status. Transaction: $transaction")
      }

      delay(5.seconds)
    } while (transaction.status != TransactionStatus.COMPLETED)

    println("Successful withdrawal")
  }
}
