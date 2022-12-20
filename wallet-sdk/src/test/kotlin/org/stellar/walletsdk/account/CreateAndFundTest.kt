package org.stellar.walletsdk.account

import io.mockk.spyk
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Server
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ADDRESS_INACTIVE
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.TestWallet

@DisplayName("createAndFund")
internal class CreateAndFundTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val transactions = wallet.stellar().transaction()

  @Test
  fun `generates Stellar public and secret keys`() {
    val accountKeys = wallet.stellar().account().createKeyPair()
    val publicKey = accountKeys.address
    val secretKey = accountKeys.secretKey

    // Public key
    assertEquals(56, publicKey.length)
    assertTrue(publicKey.startsWith("G"))

    // Secret key
    assertEquals(56, secretKey.length)
    assertTrue(secretKey.startsWith("S"))
  }

  @Test
  fun `fund defaults work`() {
    val transaction = runBlocking {
      wallet.stellar().transaction().fund(ADDRESS_ACTIVE.address, ADDRESS_INACTIVE)
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `throws error when starting balance is less than 1 XLM for non-sponsored accounts`() {
    val errorMessage = "Starting balance must be at least 1 XLM for non-sponsored accounts"

    val exception =
      assertFailsWith<Exception>(
        block = { runBlocking { transactions.fund(ADDRESS_ACTIVE.address, ADDRESS_INACTIVE, "0") } }
      )

    assertTrue(exception.toString().contains(errorMessage))
  }

  @Test
  fun `there is 1 operation in non-sponsored transaction`() {
    val transaction = runBlocking { transactions.fund(ADDRESS_ACTIVE.address, ADDRESS_INACTIVE) }

    assertEquals(transaction.operations.size, 1)
  }

  @Test
  fun `there are 3 operations in sponsored transaction`() {
    val transaction = runBlocking {
      transactions.fund(
        ADDRESS_ACTIVE.address,
        ADDRESS_INACTIVE,
        sponsorAddress = ADDRESS_ACTIVE.address
      )
    }

    assertEquals(transaction.operations.size, 3)
  }
}
