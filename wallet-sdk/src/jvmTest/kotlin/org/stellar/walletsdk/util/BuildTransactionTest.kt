package org.stellar.walletsdk.util

import io.mockk.every
import io.mockk.spyk
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.HorizonRequestFailedException
import org.stellar.walletsdk.extension.buildTransaction

internal class BuildTransactionTest : SuspendTest() {
  private val server = spyk(Server(HORIZON_URL))
  private val network = spyk(Network(Network.TESTNET.toString()))

  @Test
  fun `throws error if source account does not exist`() {
    every { server.accounts().account(any() as String) } throws ErrorResponse(404, "")

    val exception =
      assertFailsWith<HorizonRequestFailedException>(
        block = {
          runBlocking {
            buildTransaction("", MAX_BASE_FEE, server, network, listOfNotNull(OP_CREATE_ACCOUNT))
          }
        }
      )

    assertEquals(exception.errorCode, 404)
  }

  private val sequenceNumber = 1
  private val accountResponse = AccountResponse(ADDRESS_ACTIVE.address, sequenceNumber.toLong())

  @Test
  fun `successful build`() {
    every { server.accounts().account("") } returns accountResponse

    val transaction = runBlocking {
      buildTransaction("", MAX_BASE_FEE, server, network, listOfNotNull(OP_CREATE_ACCOUNT))
    }

    assertNotNull(transaction)
    assertEquals(1, transaction.operations.size)
  }

  @Test
  fun `sequence number is increased by 1`() {

    every { server.accounts().account("") } returns accountResponse

    val transaction = runBlocking {
      buildTransaction("", MAX_BASE_FEE, server, network, listOfNotNull(OP_CREATE_ACCOUNT))
    }

    assertEquals((sequenceNumber + 1).toLong(), transaction.sequenceNumber)
  }
}
