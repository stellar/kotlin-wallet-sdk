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
import org.stellar.walletsdk.exception.TransactionSubmitFailedException
import org.stellar.walletsdk.helpers.stellarObjectFromJsonFile

internal class AccountTest : SuspendTest() {
  private val server = spyk(Server(HORIZON_URL))
  private val wallet = TestWallet.also { it.cfg.stellar.server = server }
  private val accounts = wallet.stellar().account()
  private val transactions = wallet.stellar().transaction()

  @Nested
  @DisplayName("create")
  inner class Create {
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
  }

  @Nested
  @DisplayName("fund")
  inner class Fund {
    @Test
    fun `defaults work`() {
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
          block = {
            runBlocking { transactions.fund(ADDRESS_ACTIVE.address, ADDRESS_INACTIVE, "0") }
          }
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

  @Nested
  @DisplayName("addAssetSupport")
  inner class AddAssetSupport {
    @Test
    fun `defaults work`() {
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
  }

  @Nested
  @DisplayName("removeAssetSupport")
  inner class RemoveAssetSupport {
    @Test
    fun `defaults work`() {
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

  @Nested
  @DisplayName("addAccountSigner")
  inner class AddAccountSigner {
    @Test
    fun `defaults work`() {
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
  }

  @Nested
  @DisplayName("removeAccountSigner")
  inner class RemoveAccountSigner() {
    @Test
    fun `defaults work`() {
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

  @Nested
  @DisplayName("submitTransaction")
  inner class SubmitTransaction() {
    private val transaction = runBlocking {
      transactions.removeAccountSigner(ADDRESS_ACTIVE.address, ADDRESS_ACTIVE_TWO)
    }

    @Test
    fun `returns true on success`() {
      val mockResponse = mockk<SubmitTransactionResponse>()

      every { mockResponse.isSuccess } returns true
      every { server.submitTransaction(any() as Transaction) } returns mockResponse

      assertTrue(runBlocking { wallet.stellar().submitTransaction(transaction) })
      verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
    }

    @Test
    fun `throws exception with txn result code`() {
      val txnResultCode = "txn_failed_test"
      val mockResponse = mockk<SubmitTransactionResponse>()

      every { mockResponse.isSuccess } returns false
      every { mockResponse.extras.resultCodes.transactionResultCode } returns txnResultCode
      every { mockResponse.extras.resultCodes.operationsResultCodes } returns null
      every { server.submitTransaction(any() as Transaction) } returns mockResponse

      val exception =
        assertFailsWith<TransactionSubmitFailedException>(
          block = { runBlocking { wallet.stellar().submitTransaction(transaction) } }
        )

      assertEquals(txnResultCode, exception.transactionResultCode)
      verify(exactly = 1) { server.submitTransaction(any() as Transaction) }
    }

    @Test
    fun `don't retry when generic exception`() {
      val errorMessage = "Generic error message"

      every { server.submitTransaction(any() as Transaction) } throws Exception(errorMessage)

      val exception =
        assertFailsWith<Exception>(
          block = { runBlocking { wallet.stellar().submitTransaction(transaction) } }
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

      assertEquals(transaction.operations.size, 4)
    }
  }

  @Nested
  @DisplayName("lockAccountMasterKey")
  inner class LockAccountMasterKey {
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

  @Nested
  @DisplayName("getInfo")
  inner class GetInfo {
    @Test
    fun `basic account info`() {
      val accountResponse = stellarObjectFromJsonFile<AccountResponse>("account_basic.json")

      every { server.accounts().account(ADDRESS_BASIC) } returns accountResponse

      val accountInfo = runBlocking { accounts.getInfo(ADDRESS_BASIC, server) }

      assertEquals(ADDRESS_BASIC, accountInfo.publicKey)
      assertEquals("1.0000000", accountInfo.reservedNativeBalance)
      assertEquals(1, accountInfo.assets.size)
      assertEquals(0, accountInfo.liquidityPools.size)
    }

    @Test
    fun `full account info`() {
      val accountResponse = stellarObjectFromJsonFile<AccountResponse>("account_full.json")

      every { server.accounts().account(ADDRESS_FULL) } returns accountResponse

      val accountInfo = runBlocking { accounts.getInfo(ADDRESS_FULL, server) }

      assertEquals(ADDRESS_FULL, accountInfo.publicKey)
      assertEquals("6.5000000", accountInfo.reservedNativeBalance)
      assertEquals(4, accountInfo.assets.size)
      assertEquals(0, accountInfo.liquidityPools.size)
    }
  }
}
