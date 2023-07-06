@file:OptIn(ExperimentalCoroutinesApi::class)

package org.stellar.walletsdk

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.test.*
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.walletsdk.anchor.DepositTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.customer.Sep12Status
import org.stellar.walletsdk.exception.CustomerExceptions
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign
import org.stellar.walletsdk.horizon.transaction.toStellarTransfer

class AnchorPlatformTest {
  private val USDC =
    IssuedAssetId("USDC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP")
  private val JPYC =
    IssuedAssetId("JPYC", "GDQOE23CFSUMSVQK4Y5JHPPYK73VYCNHZHA7ENKCV37P6SUEO6XQBKPP")
  private val asset = USDC
  private val homeDomain = "localhost:8080"
  private val wallet =
    Wallet(
      StellarConfiguration.Testnet,
      ApplicationConfiguration { defaultRequest { url { protocol = URLProtocol.HTTP } } }
    )
  private val anchor = wallet.anchor("http://$homeDomain")
  private val client = HttpClient()
  private val maxTries = 40

  // TODO: take into account network reset
  val keypair =
    SigningKeyPair.fromSecret("SDGEPZ5QOQ24XGCVA274ZL43OKI6NND5CSR4XD4UH6QT3AA33P66FAJW")

  @Test
  @Disabled
  fun `info works`() = runTest {
    anchor.getInfo()

    anchor.interactive().getServicesInfo()
  }

  @Test
  @Disabled
  fun `deposit url works`() = runBlocking {
    val token = anchor.auth().authenticate(keypair)

    // Start interactive deposit
    val deposit = anchor.interactive().deposit(asset, token)

    val transaction = anchor.getTransaction(deposit.id, token)

    assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

    println(deposit.url)
  }

  @Test
  @Disabled
  fun `withdrawal url works`() = runBlocking {
    val token = anchor.auth().authenticate(keypair)

    // Start interactive withdrawal
    val withdrawal = anchor.interactive().withdraw(asset, token)

    val transaction = anchor.getTransaction(withdrawal.id, token)

    assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

    println(withdrawal.url)

    waitStatus(withdrawal.id, TransactionStatus.PENDING_USER_TRANSFER_START, token)

    val transfer =
      (anchor.getTransaction(withdrawal.id, token) as WithdrawalTransaction).toStellarTransfer(
        wallet.stellar(),
        asset
      )

    transfer.sign(keypair)

    wallet.stellar().submitTransaction(transfer)

    waitStatus(withdrawal.id, TransactionStatus.COMPLETED, token)
  }

  @Test
  @Disabled // TODO: enable using docker
  fun `test SEP-24 deposit`() {
    runBlocking {
      val token = anchor.auth().authenticate(keypair)

      val txId = makeDeposit(token)

      waitStatus(txId, TransactionStatus.COMPLETED, token)
    }
  }

  @Test
  @Disabled // TODO: enable using docker
  fun `test SEP-24 withdrawal`() {
    runBlocking {
      val token = anchor.auth().authenticate(keypair)

      // Start interactive withdrawal
      val withdrawal = anchor.interactive().withdraw(asset, token, mapOf("amount" to "10"))

      val transaction = anchor.getTransaction(withdrawal.id, token)

      assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

      val resp = client.get(withdrawal.url)

      assertEquals(200, resp.status.value)

      waitStatus(withdrawal.id, TransactionStatus.PENDING_USER_TRANSFER_START, token)

      val transfer =
        (anchor.getTransaction(withdrawal.id, token) as WithdrawalTransaction).toStellarTransfer(
          wallet.stellar(),
          asset
        )

      transfer.sign(keypair)

      wallet.stellar().submitTransaction(transfer)

      waitStatus(withdrawal.id, TransactionStatus.COMPLETED, token)
    }
  }

  @Test
  @Disabled // TODO: enable using docker
  fun `listAllTransaction works`() = runBlocking {
    val newAcc = wallet.stellar().account().createKeyPair()

    val tx =
      wallet
        .stellar()
        .transaction(keypair)
        .sponsoring(keypair, newAcc) {
          createAccount(newAcc)
          addAssetSupport(USDC)
        }
        .build()
        .sign(keypair)
        .sign(newAcc)

    wallet.stellar().submitTransaction(tx)

    val token = anchor.auth().authenticate(newAcc)
    val deposits = (0..5).map { makeDeposit(token, newAcc).also { delay(7.seconds) } }
    deposits.forEach { waitStatus(it, TransactionStatus.COMPLETED, token) }
    val history = anchor.getHistory(USDC, token)

    history.forEach { assertContains(deposits, it.id) }
  }

  @Test
  @Disabled // TODO: enable using docker
  fun `list by stellar transaction id works`() = runBlocking {
    val token = anchor.auth().authenticate(keypair)

    val txId = makeDeposit(token)

    waitStatus(txId, TransactionStatus.COMPLETED, token)

    val transaction = anchor.getTransaction(txId, token) as DepositTransaction

    val transactionByStellarId =
      anchor.getTransactionBy(token, stellarTransactionId = transaction.stellarTransactionId)

    assertEquals(transaction, transactionByStellarId)
  }

  @Test
  @Disabled
  fun `manage customer`() {
    runBlocking {
      val token = anchor.auth().authenticate(keypair)
      val customer = anchor.customer(token)
      val testCustomerType = "sep31-receiver"
      val testCustomerAccount = token.principalAccount
      val testCreateSep9Payload =
        mapOf(
          "first_name" to "John",
          "last_name" to "Doe",
          "email_address" to "jonhdoe@email.com",
        )
      val testUpdateSep9Payload =
        mapOf(
          "address" to "123 Washington Street",
          "city" to "San Francisco",
          "state_or_province" to "CA",
          "address_country_code" to "US",
          "clabe_number" to "1234",
          "bank_number" to "abcd",
          "bank_account_number" to "1234",
          "bank_account_type" to "checking"
        )

      val addCustomerResponse =
        customer.add(
          sep9Info = testCreateSep9Payload,
          type = testCustomerType,
        )
      assertNotNull(addCustomerResponse.id)

      var customerData = customer.getByIdAndType(addCustomerResponse.id, testCustomerType)
      assertNotNull(customerData)
      assertEquals(customerData.providedFields?.get("first_name")?.status, Sep12Status.ACCEPTED)
      assertEquals(customerData.providedFields?.get("last_name")?.status, Sep12Status.ACCEPTED)
      assertEquals(customerData.providedFields?.get("email_address")?.status, Sep12Status.ACCEPTED)
      assertNull(customerData.providedFields?.get("bank_number"))

      val updateCustomerResponse =
        customer.update(
          sep9Info = testUpdateSep9Payload,
          id = addCustomerResponse.id,
          type = testCustomerType,
        )
      assertNotNull(updateCustomerResponse.id)

      customerData = customer.getByIdAndType(addCustomerResponse.id, testCustomerType)
      assertNotNull(customerData)
      assertEquals(customerData.providedFields?.get("bank_number")?.status, Sep12Status.ACCEPTED)
      assertEquals(
        customerData.providedFields?.get("bank_account_number")?.status,
        Sep12Status.ACCEPTED
      )

      assertDoesNotThrow { runBlocking { customer.delete(testCustomerAccount) } }
      assertFailsWith<CustomerExceptions> {
        runBlocking { customer.getByIdAndType(addCustomerResponse.id, testCustomerType) }
      }
    }
  }

  private suspend fun makeDeposit(token: AuthToken, keyPair: SigningKeyPair = keypair): String {
    // Start interactive deposit
    val deposit = anchor.interactive().deposit(asset, token, mapOf("amount" to "10"))

    val transaction = anchor.getTransaction(deposit.id, token)

    assertEquals(TransactionStatus.INCOMPLETE, transaction.status)

    val resp = client.get(deposit.url)

    assertEquals(200, resp.status.value)

    return transaction.id
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
