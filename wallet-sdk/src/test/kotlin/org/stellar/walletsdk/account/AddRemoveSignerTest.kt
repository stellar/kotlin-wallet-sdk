package org.stellar.walletsdk.account

import io.mockk.spyk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Server
import org.stellar.sdk.SetOptionsOperation
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ADDRESS_ACTIVE_TWO
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.TestWallet

internal class AddRemoveSignerTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val transactions = wallet.stellar().transaction()

  @Test
  fun `add defaults work`() {
    val transaction = runBlocking {
      transactions.addAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO, 10)
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `there is 1 operation in non-sponsored transaction`() {
    val transaction = runBlocking {
      transactions.addAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO, 10)
    }

    assertEquals(transaction.operations.size, 1)
  }

  @Test
  fun `there are 3 operations in sponsored transaction`() {
    val transaction = runBlocking {
      transactions.addAccountSigner(
        ADDRESS_ACTIVE.address,
        ADDRESS_ACTIVE_TWO,
        10,
        sponsorAddress = ADDRESS_ACTIVE.address
      )
    }

    assertEquals(transaction.operations.size, 3)
  }

  @Test
  fun `sets correct account signer weight`() {
    val signerWeight = 13
    val transaction = runBlocking {
      transactions.addAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO, signerWeight)
    }
    val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

    assertEquals(transactionSignerWeight, signerWeight)
  }

  @Test
  fun `remove defaults work`() {
    val transaction = runBlocking {
      transactions.removeAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO)
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `account signer weight is 0`() {
    val transaction = runBlocking {
      transactions.removeAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO)
    }
    val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

    assertEquals(transactionSignerWeight, 0)
  }
}
