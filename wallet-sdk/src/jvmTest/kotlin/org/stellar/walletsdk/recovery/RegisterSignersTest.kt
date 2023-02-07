package org.stellar.walletsdk.recovery

import io.mockk.spyk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Server
import org.stellar.walletsdk.*

internal class RegisterSignersTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }

  @Test
  fun `defaults work`() {
    val transaction = runBlocking {
      wallet
        .recovery()
        .registerRecoveryServerSigners(
          ADDRESS_ACTIVE,
          accountSigner = listOf(AccountSigner(address = ADDRESS_ACTIVE_TWO, weight = 10)),
          accountThreshold = AccountThreshold(low = 10, medium = 10, high = 10)
        )
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `there are 2 operations in non-sponsored transaction`() {
    val transaction = runBlocking {
      wallet
        .recovery()
        .registerRecoveryServerSigners(
          ADDRESS_ACTIVE,
          accountSigner = listOf(AccountSigner(address = ADDRESS_ACTIVE_TWO, weight = 10)),
          accountThreshold = AccountThreshold(low = 10, medium = 10, high = 10)
        )
    }

    assertEquals(transaction.operations.size, 2)
  }

  @Test
  fun `there are 4 operations in sponsored transaction`() {
    val transaction = runBlocking {
      wallet
        .recovery()
        .registerRecoveryServerSigners(
          ADDRESS_ACTIVE,
          accountSigner = listOf(AccountSigner(address = ADDRESS_ACTIVE_TWO, weight = 10)),
          accountThreshold = AccountThreshold(low = 10, medium = 10, high = 10),
          sponsorAddress = ADDRESS_ACTIVE_TWO
        )
    }

    // TODO: should be 4, see TODO in org.stellar.walletsdk.util.OperationKt.sponsorOperation
    assertEquals(transaction.operations.size, 6)
  }
}
