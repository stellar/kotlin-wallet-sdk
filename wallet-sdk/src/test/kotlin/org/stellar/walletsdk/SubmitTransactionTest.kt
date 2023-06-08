package org.stellar.walletsdk

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import java.time.Duration
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.sdk.responses.SubmitTransactionTimeoutResponseException
import org.stellar.walletsdk.exception.TransactionSubmitFailedException
import org.stellar.walletsdk.helpers.stellarObjectFromJsonFile

internal class SubmitTransactionTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val stellar = wallet.stellar()

  private val transaction = runBlocking {
    stellar.transaction(ADDRESS_ACTIVE).removeAccountSigner(ADDRESS_ACTIVE_TWO).build()
  }

  @Test
  fun `returns true on success`() {
    val mockResponse = mockk<SubmitTransactionResponse>()

    every { mockResponse.isSuccess } returns true
    every { server.submitTransaction(any() as Transaction) } returns mockResponse

    assertDoesNotThrow { (runBlocking { wallet.stellar().submitTransaction(transaction) }) }
    verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
  }

  @Test
  fun `throws exception with txn result code`() {
    val txnResultCode = "txn_failed_test"
    val mockResponse = mockk<SubmitTransactionResponse>()

    every { mockResponse.isSuccess } returns false
    every { mockResponse.extras.resultCodes.transactionResultCode } returns txnResultCode
    every { mockResponse.extras.resultCodes.operationsResultCodes } returns null
    every { server.submitTransaction(any() as Transaction) } returns mockResponse

    val exception =
      assertFailsWith<TransactionSubmitFailedException>(
        block = { runBlocking { wallet.stellar().submitTransaction(transaction) } }
      )

    assertEquals(txnResultCode, exception.transactionResultCode)
    verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
  }

  @Test
  fun `don't retry when generic exception`() {
    val errorMessage = "Generic error message"

    every { server.submitTransaction(any() as Transaction) } throws Exception(errorMessage)

    val exception =
      assertFailsWith<Exception>(
        block = { runBlocking { wallet.stellar().submitTransaction(transaction) } }
      )

    assertTrue(exception.toString().contains(errorMessage))
    verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
  }

  @Test
  fun `resubmit works`() {
    val mockResponse = mockk<SubmitTransactionResponse>()
    every { mockResponse.isSuccess } returns true

    every { server.submitTransaction(any() as Transaction) } throws
      SubmitTransactionTimeoutResponseException() andThenThrows
      SubmitTransactionTimeoutResponseException() andThen
      mockResponse

    assertDoesNotThrow { (runBlocking { wallet.stellar().submitTransaction(transaction) }) }
    verify(exactly = 3) { server.submitTransaction(any() as Transaction) }
  }

  // 1. Mock server to return expire transaction response twice
  // 2. Successfully submit transaction
  // 3. Verify transaction has expected fee (base_fee + 2 * fee_increase)
  // 4. Verify account has been created
  @Test
  fun `rebuild works`() {
    val expiredResponse: SubmitTransactionResponse =
      stellarObjectFromJsonFile("expired_transaction.json")

    every { server.submitTransaction(any() as Transaction) } returns
      expiredResponse andThen
      expiredResponse andThenAnswer
      {
        callOriginal()
      }

    val newKeyPair = wallet.stellar().account().createKeyPair()
    val baseFee = 123U
    val feeIncrease = 100U
    val expectedFee = (baseFee + feeIncrease * 2u).toLong()

    val transaction = assertDoesNotThrow {
      runBlocking {
        wallet.stellar().submitWithFeeIncrease(
          ADDRESS_ACTIVE,
          Duration.ofSeconds(30),
          feeIncrease,
          baseFee
        ) { this.createAccount(newKeyPair) }
      }
    }

    assertEquals(expectedFee, transaction.fee)
    assertDoesNotThrow { runBlocking { stellar.account().getInfo(newKeyPair.address) } }
  }
}
