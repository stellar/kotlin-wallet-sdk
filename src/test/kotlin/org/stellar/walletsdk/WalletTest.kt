package org.stellar.walletsdk

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class WalletTest {
  private val wallet = Wallet(HORIZON_URL, NETWORK_PASSPHRASE)

  @Nested
  @DisplayName("create")
  inner class Create {
    @Test
    fun `generates Stellar public and secret keys`() {
      val accountKeys = wallet.create()
      val publicKey = accountKeys.publicKey
      val secretKey = accountKeys.secretKey

      // Public key
      assertEquals(56, publicKey.length)
      assertTrue(publicKey.startsWith("G"))

      // Secret key
      assertEquals(56, secretKey.length)
      assertTrue(secretKey.startsWith("S"))
    }
  }

  @Nested
  @DisplayName("fund")
  inner class Fund {
    @Test
    fun `throws error when starting balance is less than 1 XLM for non-sponsored accounts`() {
      val errorMessage = "Starting balance must be at least 1 XLM for non-sponsored accounts"

      val error =
        assertFailsWith<Error>(block = { wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE, "0") })

      assertTrue(error.toString().contains(errorMessage))
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction =
        wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE, sponsorAddress = ADDRESS_ACTIVE)

      assertEquals(transaction.operations.size, 3)
    }
  }
}
