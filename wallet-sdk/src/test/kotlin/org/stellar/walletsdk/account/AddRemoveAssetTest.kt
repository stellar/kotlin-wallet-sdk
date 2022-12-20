package org.stellar.walletsdk.account

import io.mockk.spyk
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.ChangeTrustOperation
import org.stellar.sdk.Server
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ASSET_USDC
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.TestWallet

internal class AddRemoveAssetTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val transactions = wallet.stellar().transaction()

  @Test
  fun `add defaults work`() {
    val transaction = runBlocking {
      transactions.addAssetSupport(ADDRESS_ACTIVE.address, ASSET_USDC)
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `there is 1 operation in non-sponsored transaction`() {
    val transaction = runBlocking {
      transactions.addAssetSupport(ADDRESS_ACTIVE.address, ASSET_USDC)
    }

    assertEquals(transaction.operations.size, 1)
  }

  @Test
  fun `there are 3 operations in sponsored transaction`() {
    val transaction = runBlocking {
      transactions.addAssetSupport(
        ADDRESS_ACTIVE.address,
        ASSET_USDC,
        sponsorAddress = ADDRESS_ACTIVE.address
      )
    }

    assertEquals(transaction.operations.size, 3)
  }

  @Test
  fun `remove defaults work`() {
    val transaction = runBlocking {
      transactions.removeAssetSupport(ADDRESS_ACTIVE.address, ASSET_USDC)
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `trust limit is 0`() {
    val transaction = runBlocking {
      transactions.removeAssetSupport(ADDRESS_ACTIVE.address, ASSET_USDC)
    }
    val trustLimit = (transaction.operations[0] as ChangeTrustOperation).limit

    assertEquals("0", trustLimit)
  }
}
