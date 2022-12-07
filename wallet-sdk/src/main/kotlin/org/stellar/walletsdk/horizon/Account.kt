package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.*
import org.stellar.walletsdk.util.*

private val log = KotlinLogging.logger {}

/**
 * Key store for Stellar accounts
 *
 * [Learn about Stellar network fees](https://developers.stellar.org/docs/encyclopedia/fees-surge-pricing-fee-strategies)
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 * @property maxBaseFeeInStroops maximum base fee in stroops
 */
class Account
internal constructor(
  private val cfg: Config,
) {
  private val server: Server = cfg.stellar.server
  private val network: Network = cfg.stellar.network
  private val maxBaseFeeInStroops: Int = cfg.stellar.maxBaseFeeStroops.toInt()

  /**
   * Generate new account keypair (public and secret key).
   *
   * @return public key and secret key
   */
  fun create(): AccountKeypair {
    return AccountKeypair(KeyPair.random())
  }

  /**
   * Get account information from the Stellar network.
   *
   * @param accountAddress Stellar address of the account
   * @param serverInstance optional Horizon server instance when default doesn't work
   *
   * @return formatted account information
   *
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun getInfo(accountAddress: String, serverInstance: Server = server): AccountInfo {
    val account = serverInstance.accountByAddress(accountAddress)
    val balances = formatAccountBalances(account, serverInstance)

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
   *
   * @return a list of formatted operations
   *
   * @throws [OperationsLimitExceededException] when maximum limit of 200 is exceeded
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun getHistory(
    accountAddress: String,
    limit: Int? = null,
    order: Order? = Order.DESC,
    cursor: String? = null,
    includeFailed: Boolean? = null
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
