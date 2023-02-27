package org.stellar.walletsdk.account

import io.mockk.spyk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.stellar.sdk.Server
import org.stellar.sdk.SetOptionsOperation
import org.stellar.walletsdk.*

internal class AddRemoveSignerTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val stellar = wallet.stellar()

  @Test
  fun `add defaults work`() {
    val transaction =
      runBlocking { stellar.transaction(ADDRESS_ACTIVE).addAccountSigner(ADDRESS_ACTIVE_TWO, 10) }
        .build()

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `there is 1 operation in non-sponsored transaction`() {
    val transaction =
      runBlocking { stellar.transaction(ADDRESS_ACTIVE).addAccountSigner(ADDRESS_ACTIVE_TWO, 10) }
        .build()

    assertEquals(transaction.operations.size, 1)
  }

  @Test
  fun `there are 3 operations in sponsored transaction`() {
    val transaction =
      runBlocking {
          stellar
            .transaction(ADDRESS_ACTIVE)
            .startSponsoring(ADDRESS_ACTIVE)
            .addAccountSigner(ADDRESS_ACTIVE_TWO, 10)
            .stopSponsoring()
        }
        .build()

    assertEquals(transaction.operations.size, 3)
  }

  @Test
  fun `sets correct account signer weight`() {
    val signerWeight = 13
    val transaction =
      runBlocking {
          stellar.transaction(ADDRESS_ACTIVE).addAccountSigner(ADDRESS_ACTIVE_TWO, signerWeight)
        }
        .build()
    val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

    assertEquals(transactionSignerWeight, signerWeight)
  }

  @Test
  fun `remove defaults work`() {
    val transaction =
      runBlocking { stellar.transaction(ADDRESS_ACTIVE).removeAccountSigner(ADDRESS_ACTIVE_TWO) }
        .build()

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `account signer weight is 0`() {
    val transaction =
      runBlocking { stellar.transaction(ADDRESS_ACTIVE).removeAccountSigner(ADDRESS_ACTIVE_TWO) }
        .build()
    val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

    assertEquals(transactionSignerWeight, 0)
  }

  @Test
  fun `can't remove master key`() {
    assertThrows<IllegalArgumentException> {
      runBlocking {
        stellar.transaction(ADDRESS_ACTIVE).removeAccountSigner(ADDRESS_ACTIVE.address).build()
      }
    }
  }
}
