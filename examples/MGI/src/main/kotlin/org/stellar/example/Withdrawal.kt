package org.stellar.example

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.sign
import org.stellar.walletsdk.horizon.transaction.toTransferTransaction

object Withdrawal {
  suspend fun main() {
    // Authorizing
    val token = anchor.auth().authenticate(myAccount, Signer, clientDomain = clientDomain)

    println("Auth token: $token")

    // Start interactive withdrawal
    val withdrawal = anchor.interactive().withdraw(myAccount.address, asset, authToken = token)

    // Request user input
    println("Additional user info is required for the withdrawal, please visit: ${withdrawal.url}")

    var transaction: AnchorTransaction

    var status: TransactionStatus? = null

    // Wait for user input
    do {
      // Get transaction info
      transaction = anchor.getTransaction(withdrawal.id, token)
      delay(5.seconds)
    } while (transaction.status != TransactionStatus.PENDING_USER_TRANSFER_START)

    // Send transaction with transfer
    val transfer = (transaction as WithdrawalTransaction).toTransferTransaction(stellar, asset)

    transfer.sign(myAccount)

    stellar.submitTransaction(transfer)

    println("Stellar transaction has been sent ${transfer.hashHex()}")

    do {
      transaction = anchor.getTransaction(withdrawal.id, token) as WithdrawalTransaction

      if (status != transaction.status) {
        status = transaction.status
        println("Withdrawal transaction status changed to $status. Message: ${transaction.message}")
      }

      delay(5.seconds)
    } while (transaction.status != TransactionStatus.COMPLETED)

    println("Successful withdrawal")
  }
}
