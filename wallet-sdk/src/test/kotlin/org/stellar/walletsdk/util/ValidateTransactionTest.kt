package org.stellar.walletsdk.util

import io.mockk.every
import io.mockk.spyk
import java.io.IOException
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.helpers.objectFromJsonFile

@DisplayName("validateTransaction")
internal class ValidateTransactionTest : SuspendTest() {
  private val server = spyk(Server(HORIZON_URL))
  private val network = spyk(Network(Network.TESTNET.toString()))

  @Test
  fun `throws error if source account does not exist`() {
    val errorMessage = "was not found"

    every { server.accounts().account(ADDRESS_ACTIVE) } throws IOException("Test message")

    val transaction = Transaction.fromEnvelopeXdr(TXN_XDR_CREATE_ACCOUNT, network) as Transaction
    val exception =
      assertFailsWith<Exception>(
        block = { runBlocking { transaction.validateSufficientBalance(server) } }
      )

    assertTrue(exception.toString().contains(errorMessage))
  }

  @Test
  fun `throws error if account balance is less than fees`() {
    val errorMessage = "does not have enough XLM balance to cover"

    val account = objectFromJsonFile("account_basic.json", AccountResponse::class.java)

    every { server.accounts().account(ADDRESS_ACTIVE) } returns account

    val transaction = Transaction.fromEnvelopeXdr(TXN_XDR_CREATE_ACCOUNT, network) as Transaction
    val exception =
      assertFailsWith<Exception>(
        block = { runBlocking { transaction.validateSufficientBalance(server) } }
      )

    assertTrue(exception.toString().contains(errorMessage))
  }

  @Test
  fun `no errors`() {
    val account = objectFromJsonFile("account_full.json", AccountResponse::class.java)

    every { server.accounts().account(ADDRESS_ACTIVE) } returns account

    val transaction = Transaction.fromEnvelopeXdr(TXN_XDR_CREATE_ACCOUNT, network) as Transaction

    assertDoesNotThrow { runBlocking { transaction.validateSufficientBalance(server) } }
  }
}
