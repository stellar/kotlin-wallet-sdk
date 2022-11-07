package org.stellar.walletsdk

import io.mockk.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.*
import org.junit.jupiter.api.*
import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.walletsdk.helpers.objectFromJsonFile

internal class WalletTest : SuspendTest() {
  private val server = spyk(Server(HORIZON_URL))
  private val network = Network(NETWORK_PASSPHRASE)
  private val wallet = Wallet(server = server, network = network, maxBaseFeeInStroops = 500)

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
      val transaction = runBlocking { wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE) }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `throws error when starting balance is less than 1 XLM for non-sponsored accounts`() {
      val errorMessage = "Starting balance must be at least 1 XLM for non-sponsored accounts"

      val exception =
        assertFailsWith<Exception>(
          block = { runBlocking { wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE, "0") } }
        )

      assertTrue(exception.toString().contains(errorMessage))
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = runBlocking { wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE) }

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction = runBlocking {
        wallet.fund(ADDRESS_ACTIVE, ADDRESS_INACTIVE, sponsorAddress = ADDRESS_ACTIVE)
      }

      assertEquals(transaction.operations.size, 3)
    }
  }

  @Nested
  @DisplayName("addAssetSupport")
  inner class AddAssetSupport {
    @Test
    fun `defaults work`() {
      val transaction = runBlocking {
        wallet.addAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)
      }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = runBlocking {
        wallet.addAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)
      }

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction = runBlocking {
        wallet.addAssetSupport(
          ADDRESS_ACTIVE,
          USDC_ASSET_CODE,
          USDC_ASSET_ISSUER,
          sponsorAddress = ADDRESS_ACTIVE
        )
      }

      assertEquals(transaction.operations.size, 3)
    }
  }

  @Nested
  @DisplayName("removeAssetSupport")
  inner class RemoveAssetSupport {
    @Test
    fun `defaults work`() {
      val transaction = runBlocking {
        wallet.removeAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)
      }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `trust limit is 0`() {
      val transaction = runBlocking {
        wallet.removeAssetSupport(ADDRESS_ACTIVE, USDC_ASSET_CODE, USDC_ASSET_ISSUER)
      }
      val trustLimit = (transaction.operations[0] as ChangeTrustOperation).limit

      assertEquals("0", trustLimit)
    }
  }

  @Nested
  @DisplayName("addAccountSigner")
  inner class AddAccountSigner {
    @Test
    fun `defaults work`() {
      val transaction = runBlocking {
        wallet.addAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO, 10)
      }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = runBlocking {
        wallet.addAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO, 10)
      }

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction = runBlocking {
        wallet.addAccountSigner(
          ADDRESS_ACTIVE,
          ADDRESS_ACTIVE_TWO,
          10,
          sponsorAddress = ADDRESS_ACTIVE
        )
      }

      assertEquals(transaction.operations.size, 3)
    }

    @Test
    fun `sets correct account signer weight`() {
      val signerWeight = 13
      val transaction = runBlocking {
        wallet.addAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO, signerWeight)
      }
      val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

      assertEquals(transactionSignerWeight, signerWeight)
    }
  }

  @Nested
  @DisplayName("removeAccountSigner")
  inner class RemoveAccountSigner() {
    @Test
    fun `defaults work`() {
      val transaction = runBlocking {
        wallet.removeAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO)
      }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `account signer weight is 0`() {
      val transaction = runBlocking {
        wallet.removeAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO)
      }
      val transactionSignerWeight = (transaction.operations[0] as SetOptionsOperation).signerWeight

      assertEquals(transactionSignerWeight, 0)
    }
  }

  @Nested
  @DisplayName("submitTransaction")
  inner class SubmitTransaction() {
    private val transaction = runBlocking {
      wallet.removeAccountSigner(ADDRESS_ACTIVE, ADDRESS_ACTIVE_TWO)
    }

    @Test
    fun `returns true on success`() {
      val mockResponse = mockk<SubmitTransactionResponse>()

      every { mockResponse.isSuccess } returns true
      every { server.submitTransaction(any() as Transaction) } returns mockResponse

      assertTrue(runBlocking { wallet.submitTransaction(transaction) })
      verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
    }

    @Test
    fun `throws exception with txn result code`() {
      val txnResultCode = "txn_failed_test"
      val mockResponse = mockk<SubmitTransactionResponse>()

      every { mockResponse.isSuccess } returns false
      every { mockResponse.extras.resultCodes.transactionResultCode } returns txnResultCode
      every { server.submitTransaction(any() as Transaction) } returns mockResponse

      val exception =
        assertFailsWith<Exception>(
          block = { runBlocking { wallet.submitTransaction(transaction) } }
        )

      assertTrue(exception.toString().contains(txnResultCode))
      verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
    }

    @Test
    fun `don't retry when generic exception`() {
      val errorMessage = "Generic error message"

      every { server.submitTransaction(any() as Transaction) } throws Exception(errorMessage)

      val exception =
        assertFailsWith<Exception>(
          block = { runBlocking { wallet.submitTransaction(transaction) } }
        )

      assertTrue(exception.toString().contains(errorMessage))
      verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
    }
  }

  @Nested
  @DisplayName("registerRecoveryServerSigners")
  inner class RegisterRecoveryServerSigners {
    @Test
    fun `defaults work`() {
      val transaction = runBlocking {
        wallet.registerRecoveryServerSigners(
          accountAddress = ADDRESS_ACTIVE,
          accountSigner = listOf(AccountSigner(address = ADDRESS_ACTIVE_TWO, weight = 10)),
          accountThreshold = AccountThreshold(low = 10, medium = 10, high = 10)
        )
      }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `there are 2 operations in non-sponsored transaction`() {
      val transaction = runBlocking {
        wallet.registerRecoveryServerSigners(
          accountAddress = ADDRESS_ACTIVE,
          accountSigner = listOf(AccountSigner(address = ADDRESS_ACTIVE_TWO, weight = 10)),
          accountThreshold = AccountThreshold(low = 10, medium = 10, high = 10)
        )
      }

      assertEquals(transaction.operations.size, 2)
    }

    @Test
    fun `there are 4 operations in sponsored transaction`() {
      val transaction = runBlocking {
        wallet.registerRecoveryServerSigners(
          accountAddress = ADDRESS_ACTIVE,
          accountSigner = listOf(AccountSigner(address = ADDRESS_ACTIVE_TWO, weight = 10)),
          accountThreshold = AccountThreshold(low = 10, medium = 10, high = 10),
          sponsorAddress = ADDRESS_ACTIVE_TWO
        )
      }

      assertEquals(transaction.operations.size, 4)
    }
  }

  @Nested
  @DisplayName("lockAccountMasterKey")
  inner class LockAccountMasterKey {
    @Test
    fun `defaults work`() {
      val transaction = runBlocking { wallet.lockAccountMasterKey(accountAddress = ADDRESS_ACTIVE) }

      assertDoesNotThrow { transaction.toEnvelopeXdrBase64() }
    }

    @Test
    fun `there is 1 operation in non-sponsored transaction`() {
      val transaction = runBlocking { wallet.lockAccountMasterKey(accountAddress = ADDRESS_ACTIVE) }

      assertEquals(transaction.operations.size, 1)
    }

    @Test
    fun `there are 3 operations in sponsored transaction`() {
      val transaction = runBlocking {
        wallet.lockAccountMasterKey(
          accountAddress = ADDRESS_ACTIVE,
          sponsorAddress = ADDRESS_ACTIVE_TWO
        )
      }

      assertEquals(transaction.operations.size, 3)
    }
  }

  @Nested
  @DisplayName("getInfo")
  inner class GetInfo {
    @Test
    fun `basic account info`() {
      val accountResponse = objectFromJsonFile("account_basic.json", AccountResponse::class.java)

      every { server.accounts().account(ADDRESS_BASIC) } returns accountResponse

      val accountInfo = runBlocking { wallet.getInfo(ADDRESS_BASIC, server) }

      assertEquals(ADDRESS_BASIC, accountInfo.publicKey)
      assertEquals("1.0000000", accountInfo.reservedNativeBalance)
      assertEquals(1, accountInfo.assets.size)
      assertEquals(0, accountInfo.liquidityPools.size)
    }

    @Test
    fun `full account info`() {
      val accountResponse = objectFromJsonFile("account_full.json", AccountResponse::class.java)

      every { server.accounts().account(ADDRESS_FULL) } returns accountResponse

      val accountInfo = runBlocking { wallet.getInfo(ADDRESS_FULL, server) }

      assertEquals(ADDRESS_FULL, accountInfo.publicKey)
      assertEquals("6.5000000", accountInfo.reservedNativeBalance)
      assertEquals(4, accountInfo.assets.size)
      assertEquals(0, accountInfo.liquidityPools.size)
    }
  }
}
