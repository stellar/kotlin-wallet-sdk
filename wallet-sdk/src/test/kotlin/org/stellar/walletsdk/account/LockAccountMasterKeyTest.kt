package org.stellar.walletsdk.account

import io.mockk.spyk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Server
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ADDRESS_ACTIVE_TWO
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.TestWallet

internal class LockAccountMasterKeyTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }

  @Test
  fun `defaults work`() {
    val transaction = runBlocking {
      wallet.stellar().transaction().lockAccountMasterKey(accountAddress = ADDRESS_ACTIVE.address)
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `there is 1 operation in non-sponsored transaction`() {
    val transaction = runBlocking {
      wallet.stellar().transaction().lockAccountMasterKey(accountAddress = ADDRESS_ACTIVE.address)
    }

    assertEquals(transaction.operations.size, 1)
  }

  @Test
  fun `there are 3 operations in sponsored transaction`() {
    val transaction = runBlocking {
      wallet
        .stellar()
        .transaction()
        .lockAccountMasterKey(ADDRESS_ACTIVE.address, sponsorAddress = ADDRESS_ACTIVE_TWO)
    }

    assertEquals(transaction.operations.size, 3)
  }
}
