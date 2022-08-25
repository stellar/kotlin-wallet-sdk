package org.stellar.walletsdk

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.stellar.sdk.*

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
    fun `defaults work`() {
      val transaction = wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE)

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `throws error when starting balance is less than 1 XLM for non-sponsored accounts`() {
      val errorMessage = "Starting balance must be at least 1 XLM for non-sponsored accounts"

      val error =
        assertFailsWith<Error>(block = { wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE, "0") })

      assertTrue(error.toString().contains(errorMessage))
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE)

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction =
        wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE, sponsorAddress = ADDRESS_ACTIVE)

      assertEquals(transaction.operations.size, 3)
    }
  }

  @Nested
  @DisplayName("addAssetSupport")
  inner class AddAssetSupport {
    @Test
    fun `defaults work`() {
      val transaction = wallet.addAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = wallet.addAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction =
        wallet.addAssetSupport(
          ADDRESS_ACTIVE,
          USDC_ASSET_CODE,
          USDC_ASSET_ISSUER,
          sponsorAddress = ADDRESS_ACTIVE
        )

      assertEquals(transaction.operations.size, 3)
    }
  }

  @Nested
  @DisplayName("removeAssetSupport")
  inner class RemoveAssetSupport {
    @Test
    fun `defaults work`() {
      val transaction =
        wallet.removeAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `trust limit is 0`() {
      val transaction =
        wallet.removeAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)
      val trustLimit = (transaction.operations[0] as ChangeTrustOperation).limit

      assertEquals("0", trustLimit)
    }
  }

  @Nested
  @DisplayName("addAccountSigner")
  inner class AddAccountSigner {
    @Test
    fun `defaults work`() {
      val transaction = wallet.addAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO, 10)

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = wallet.addAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO, 10)

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction =
        wallet.addAccountSigner(
          ADDRESS_ACTIVE,
          ADDRESS_ACTIVE_TWO,
          10,
          sponsorAddress = ADDRESS_ACTIVE
        )

      assertEquals(transaction.operations.size, 3)
    }

    @Test
    fun `sets correct account signer weight`() {
      val signerWeight = 13
      val transaction = wallet.addAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO, signerWeight)
      val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

      assertEquals(transactionSignerWeight, signerWeight)
    }
  }

  @Nested
  @DisplayName("removeAccountSigner")
  inner class RemoveAccountSigner() {
    @Test
    fun `defaults work`() {
      val transaction = wallet.removeAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO)

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `account signer weight is 0`() {
      val transaction = wallet.removeAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO)
      val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

      assertEquals(transactionSignerWeight, 0)
    }
  }
}
