package org.stellar.walletsdk.account

import io.mockk.spyk
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.Server
import org.stellar.sdk.operations.ChangeTrustOperation
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ASSET_USDC
import org.stellar.walletsdk.HORIZON_URL
import org.stellar.walletsdk.TestWallet

internal class AddRemoveAssetTest {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val stellar = wallet.stellar()

  @Test
  fun `add defaults work`() {
    val transaction = runBlocking {
      stellar.transaction(ADDRESS_ACTIVE).addAssetSupport(ASSET_USDC).build()
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `there is 1 operation in non-sponsored transaction`() {
    val transaction = runBlocking {
      stellar.transaction(ADDRESS_ACTIVE).addAssetSupport(ASSET_USDC).build()
    }

    assertEquals(transaction.operations.size, 1)
  }

  @Test
  fun `there are 3 operations in sponsored transaction`() {
    val transaction = runBlocking {
      stellar
        .transaction(ADDRESS_ACTIVE)
        .sponsoring(ADDRESS_ACTIVE) { addAssetSupport(ASSET_USDC) }
        .build()
    }

    assertEquals(transaction.operations.size, 3)
  }

  @Test
  fun `remove defaults work`() {
    val transaction = runBlocking {
      stellar.transaction(ADDRESS_ACTIVE).removeAssetSupport(ASSET_USDC).build()
    }

    assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
  }

  @Test
  fun `trust limit is 0`() {
    val transaction =
      runBlocking { stellar.transaction(ADDRESS_ACTIVE).removeAssetSupport(ASSET_USDC) }.build()
    val trustLimit = (transaction.operations[0] as ChangeTrustOperation).limit
    assertEquals(0, trustLimit.compareTo(BigDecimal.ZERO))
  }
}
