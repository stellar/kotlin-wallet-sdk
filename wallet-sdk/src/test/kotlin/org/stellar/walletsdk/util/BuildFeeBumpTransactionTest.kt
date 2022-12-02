package org.stellar.walletsdk.util

import io.mockk.every
import io.mockk.spyk
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.ADDRESS_ACTIVE_TWO
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.SuspendTest
import org.stellar.walletsdk.TXN_XDR_CREATE_ACCOUNT
import org.stellar.walletsdk.extension.buildFeeBumpTransaction
import org.stellar.walletsdk.helpers.objectFromJsonFile

@DisplayName("buildFeeBumpTransaction")
internal class BuildFeeBumpTransactionTest : SuspendTest() {
  private val server = spyk(Server(HORIZON_URL))
  private val network = spyk(Network(Network.TESTNET.toString()))
  private val transaction =
    Transaction.fromEnvelopeXdr(TXN_XDR_CREATE_ACCOUNT, network) as Transaction

  @Test
  fun `throws error if fee account does not exist`() {
    val errorMessage = "was not found"

    every { server.accounts().account(any() as String) } throws ErrorResponse(404, "")

    val exception =
      assertFailsWith<Exception>(
        block = {
          runBlocking { buildFeeBumpTransaction(ADDRESS_ACTIVE_TWO, transaction, 500, server) }
        }
      )

    assertTrue(exception.toString().contains(errorMessage))
  }

  @Test
  fun `successful build`() {
    val account = objectFromJsonFile("account_full.json", AccountResponse::class.java)

    every { server.accounts().account(ADDRESS_ACTIVE_TWO) } returns account

    assertDoesNotThrow {
      runBlocking { buildFeeBumpTransaction(ADDRESS_ACTIVE_TWO, transaction, 500, server) }
    }
  }
}
