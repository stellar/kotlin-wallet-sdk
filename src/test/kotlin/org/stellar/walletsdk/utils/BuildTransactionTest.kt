package org.stellar.walletsdk.utils

import io.mockk.every
import io.mockk.spyk
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.OP_CREATE_ACCOUNT

@DisplayName("buildTransaction")
internal class BuildTransactionTest {
  private val server = spyk(Server(HORIZON_URL))
  private val network = spyk(Network(Network.TESTNET.toString()))

  @Test
  fun `throws error if source account does not exist`() {
    val errorMessage = "Source account was not found"

    every { server.accounts().account(any() as String) } throws IOException("Test message")

    val exception =
      assertFailsWith<Exception>(
        block = { buildTransaction("", server, network, listOfNotNull(OP_CREATE_ACCOUNT)) }
      )

    assertTrue(exception.toString().contains(errorMessage))
  }

  private val sequenceNumber = 1
  private val accountResponse = AccountResponse(ADDRESS_ACTIVE, sequenceNumber.toLong())

  @Test
  fun `successful build`() {
    every { server.accounts().account("") } returns accountResponse

    val transaction = buildTransaction("", server, network, listOfNotNull(OP_CREATE_ACCOUNT))

    assertNotNull(transaction)
    assertEquals(1, transaction.operations.size)
  }

  @Test
  fun `sequence number is increased by 1`() {

    every { server.accounts().account("") } returns accountResponse

    val transaction = buildTransaction("", server, network, listOfNotNull(OP_CREATE_ACCOUNT))

    assertEquals((sequenceNumber + 1).toLong(), transaction.sequenceNumber)
  }
}
