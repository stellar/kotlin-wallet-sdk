package org.stellar.walletsdk.extension

import org.stellar.sdk.Server
import org.stellar.sdk.exception.NetworkException
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.HorizonRequestFailedException
import org.stellar.walletsdk.exception.OperationsLimitExceededException

@Suppress("TooGenericExceptionCaught", "RethrowCaughtException")
private fun <T> safeHorizonCall(body: () -> T): T {
  try {
    return body()
  } catch (e: NetworkException) {
    throw HorizonRequestFailedException(e)
  } catch (e: Exception) {
    throw e
  }
}

/**
 * Fetch account information from the Stellar network.
 *
 * @param accountAddress Stellar address of the account
 * @return Account response object
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
@Throws(HorizonRequestFailedException::class)
suspend fun Server.accountByAddress(accountAddress: String): AccountResponse {
  return safeHorizonCall { accounts().account(accountAddress) }
}

suspend fun Server.accountOrNull(accountAddress: String): AccountResponse? {
  try {
    return this.accountByAddress(accountAddress)
  } catch (e: HorizonRequestFailedException) {
    if (e.errorCode == 404) {
      return null
    }
    throw e
  }
}

/**
 * Fetch account operations from Stellar network.
 *
 * @param accountAddress Stellar address of the account
 * @param limit optional how many operations to fetch, maximum is 200, default is 10
 * @param order optional data order, ascending or descending, defaults to descending
 * @param cursor optional cursor to specify a starting point
 * @param includeFailed optional flag to include failed operations, defaults to false
 * @return a list of account operations
 * @throws [OperationsLimitExceededException] when maximum limit of 200 is exceeded
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
suspend fun Server.accountOperations(
  accountAddress: String,
  limit: Int? = null,
  order: Order? = Order.DESC,
  cursor: String? = null,
  includeFailed: Boolean? = null
): List<OperationResponse> {
  if (limit != null && limit > HORIZON_LIMIT_MAX) {
    throw OperationsLimitExceededException()
  }

  return safeHorizonCall {
    operations()
      .forAccount(accountAddress)
      .limit(limit ?: HORIZON_LIMIT_DEFAULT)
      .order(order?.builderEnum)
      .cursor(cursor)
      .includeFailed(includeFailed ?: false)
      .includeTransactions(true)
      .execute()
      .records
  }
}
