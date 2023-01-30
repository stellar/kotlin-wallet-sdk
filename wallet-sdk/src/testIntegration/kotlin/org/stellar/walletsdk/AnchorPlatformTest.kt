@file:OptIn(ExperimentalCoroutinesApi::class)

package org.stellar.walletsdk

import io.ktor.client.*
import io.ktor.client.request.*
import kotlin.test.assertEquals
import kotlin.test.fail
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign
import org.stellar.walletsdk.horizon.toTransferTransaction

class AnchorPlatformTest {
  private val USDC =
    IssuedAssetId("USDC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP")
  private val JPYC =
    IssuedAssetId("JPYC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP")
  private val asset = USDC
  private val homeDomain = "localhost:8080"
  private val wallet =
    Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(useHttp = true))
  private val anchor = wallet.anchor(homeDomain)
  private val client = HttpClient()
  private val maxTries = 40

  // TODO: take into account network reset
  val keypair =
    SigningKeyPair.fromSecret("SDGEPZ5QOQ24XGCVA274ZL43OKI6NND5CSR4XD4UH6QT3AA33P66FAJW")

  @Test
  @Disabled
  fun `info works`() = runTest {
    anchor.getInfo()

    anchor.getServicesInfo()
  }

  @Test
  @Disabled
  fun `deposit url works`() = runTest {
    val token = anchor.auth().authenticate(keypair)

    // Start interactive deposit
    val deposit = anchor.interactive().deposit(keypair.address, asset, token)

    val transaction = anchor.getTransaction(deposit.id, token)

    assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

    println(deposit.url)
  }

  @Test
  @Disabled
  fun `withdrawal url works`() = runTest {
    val token = anchor.auth().authenticate(keypair)

    // Start interactive deposit
    val deposit = anchor.interactive().deposit(keypair.address, asset, token)

    val transaction = anchor.getTransaction(deposit.id, token)

    assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

    println(deposit.url)
  }

  @Test
  @Disabled // TODO: enable using docker
  fun `test SEP-24 deposit`() {
    runBlocking {
      val token = anchor.auth().authenticate(keypair)

      // Start interactive deposit
      val deposit =
        anchor.interactive().deposit(keypair.address, asset, token, mapOf("amount" to "10"))

      val transaction = anchor.getTransaction(deposit.id, token)

      assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

      val resp = client.get(deposit.url)

      assertEquals(200, resp.status.value)

      waitStatus(deposit.id, TransactionStatus.COMPLETED, token)
    }
  }

  @Test
  @Disabled // TODO: enable using docker
  fun `test SEP-24 withdrawal`() {
    runBlocking {
      val token = anchor.auth().authenticate(keypair)

      // Start interactive deposit
      val withdrawal =
        anchor.interactive().withdraw(keypair.address, asset, token, mapOf("amount" to "10"))

      val transaction = anchor.getTransaction(withdrawal.id, token)

      assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

      val resp = client.get(withdrawal.url)

      assertEquals(200, resp.status.value)

      waitStatus(withdrawal.id, TransactionStatus.PENDING_USER_TRANSFER_START, token)

      val transfer =
        (anchor.getTransaction(withdrawal.id, token) as WithdrawalTransaction)
          .toTransferTransaction(wallet.stellar(), asset)

      transfer.sign(keypair)

      wallet.stellar().submitTransaction(transfer)

      waitStatus(withdrawal.id, TransactionStatus.COMPLETED, token)
    }
  }

  suspend fun waitStatus(id: String, expectedStatus: TransactionStatus, token: AuthToken) {
    var status: TransactionStatus? = null

    for (i in 0..maxTries) {
      // Get transaction info
      val transaction = anchor.getTransaction(id, token)

      if (status != transaction.status) {
        status = transaction.status
        println("Deposit transaction status changed to $status. Message: ${transaction.message}")
      }

      delay(1.seconds)

      if (transaction.status == expectedStatus) {
        return
      }
    }

    fail("Transaction wasn't $expectedStatus in 100 seconds")
  }
}
