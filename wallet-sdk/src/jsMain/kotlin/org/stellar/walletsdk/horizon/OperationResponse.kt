package org.stellar.walletsdk.horizon

import org.stellar.walletsdk.AccountInfo
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.Order
import org.stellar.walletsdk.WalletOperation

actual abstract class OperationResponse

/**
 * Key store for Stellar accounts
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 */
actual class AccountService internal actual constructor(cfg: Config) {
  /**
   * Generate new account keypair (public and secret key). This key pair can be used to create a
   * Stellar account.
   *
   * @return public key and secret key
   */
  actual fun createKeyPair(): SigningKeyPair {
    TODO("Not yet implemented")
  }

  /**
   * Get account information from the Stellar network.
   *
   * @param accountAddress Stellar address of the account
   * @param serverInstance optional Horizon server instance when default doesn't work
   * @return formatted account information
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual suspend fun getInfo(accountAddress: String): AccountInfo {
    TODO("Not yet implemented")
  }

  /**
   * Get account operations for the specified Stellar address.
   *
   * @param accountAddress Stellar address of the account
   * @param limit optional how many operations to fetch, maximum is 200, default is 10
   * @param order optional data order, ascending or descending, defaults to descending
   * @param cursor optional cursor to specify a starting point
   * @param includeFailed optional flag to include failed operations, defaults to false
   * @return a list of formatted operations
   * @throws [OperationsLimitExceededException] when maximum limit of 200 is exceeded
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual suspend fun getHistory(
    accountAddress: String,
    limit: Int?,
    order: Order?,
    cursor: String?,
    includeFailed: Boolean?
  ): List<WalletOperation<OperationResponse>> {
    TODO("Not yet implemented")
  }
}
