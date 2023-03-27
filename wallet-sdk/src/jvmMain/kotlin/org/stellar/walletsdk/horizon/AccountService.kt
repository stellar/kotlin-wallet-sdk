package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.walletsdk.*
import org.stellar.walletsdk.Network
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.*
import org.stellar.walletsdk.util.*

private val log = KotlinLogging.logger {}

/**
 * Key store for Stellar accounts
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 */
actual class AccountService
internal actual constructor(
  cfg: Config,
) {
  private val server: Server = cfg.stellar.server
  private val network: Network = cfg.stellar.network

  /**
   * Generate new account keypair (public and secret key). This key pair can be used to create a
   * Stellar account.
   *
   * @return public key and secret key
   */
  actual fun createKeyPair(): SigningKeyPair {
    return SigningKeyPair(KeyPair.random())
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
    val account = server.accountByAddress(accountAddress)
    val balances = formatAccountBalances(account, server)

    log.debug { "Account info: accountAddress = $accountAddress" }

    // TODO: add accountDetails

    return AccountInfo(
      publicKey = account.accountId,
      assets = balances.assets,
      liquidityPools = balances.liquidityPools,
      reservedNativeBalance = account.reservedBalance()
    )
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
    log.debug {
      "Account history: accountAddress = $accountAddress, limit = $limit, order" +
        " = $order, cursor = $cursor, includeFailed = $includeFailed"
    }

    return server.accountOperations(accountAddress, limit, order, cursor, includeFailed).map {
      formatStellarOperation(accountAddress, it)
    }
  }
}

actual typealias OperationResponse = org.stellar.sdk.responses.operations.OperationResponse
