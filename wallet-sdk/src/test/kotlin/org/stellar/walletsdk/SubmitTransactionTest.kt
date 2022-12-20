package org.stellar.walletsdk

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.walletsdk.exception.TransactionSubmitFailedException

internal class SubmitTransactionTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val transactions = wallet.stellar().transaction()

  private val transaction = runBlocking {
    transactions.removeAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO)
  }

  @Test
  fun `returns true on success`() {
    val mockResponse = mockk<SubmitTransactionResponse>()

    every { mockResponse.isSuccess } returns true
    every { server.submitTransaction(any() as Transaction) } returns mockResponse

    assertTrue(runBlocking { wallet.stellar().submitTransaction(transaction) })
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
}
