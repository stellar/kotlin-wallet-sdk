package org.stellar.walletsdk.utils

import io.mockk.every
import io.mockk.spyk
import kotlin.test.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.Constants

internal class TransactionTest {
  private val server = spyk(Server(Constants.HORIZON_URL))
  private val network = spyk(Network(Network.TESTNET.toString()))

  @Nested
  @DisplayName("buildTransaction")
  inner class BuildTransaction {
    @Test
    fun `throws error if source account does not exist`() {
      val errorMessage = "Source account was not found"

      every { server.accounts().account("") } throws Error("Test message")

      val error =
        assertFailsWith<Error>(
          block = {
            Transaction.buildTransaction(
              "",
              server,
              network,
              listOfNotNull(Constants.OP_CREATE_ACCOUNT)
            )
          }
        )

      assertTrue(error.toString().contains(errorMessage))
    }

    private val sequenceNumber = 1
    private val accountResponse = AccountResponse(Constants.ADDRESS_ACTIVE, sequenceNumber.toLong())

    @Test
    fun `successful build`() {
      every { server.accounts().account("") } returns accountResponse

      val transaction =
        Transaction.buildTransaction(
          "",
          server,
          network,
          listOfNotNull(Constants.OP_CREATE_ACCOUNT)
        )

      assertNotNull(transaction)
      assertEquals(1, transaction.operations.size)
    }

    @Test
    fun `sequence number is increased by 1`() {

      every { server.accounts().account("") } returns accountResponse

      val transaction =
        Transaction.buildTransaction(
          "",
          server,
          network,
          listOfNotNull(Constants.OP_CREATE_ACCOUNT)
        )

      assertEquals((sequenceNumber + 1).toLong(), transaction.sequenceNumber)
    }
  }
}
