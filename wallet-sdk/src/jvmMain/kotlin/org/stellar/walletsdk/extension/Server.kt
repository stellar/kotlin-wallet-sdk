package org.stellar.walletsdk.extension

import org.stellar.sdk.LiquidityPoolID
import org.stellar.sdk.Server
import org.stellar.sdk.requests.ErrorResponse
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.LiquidityPoolResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.HorizonRequestFailedException
import org.stellar.walletsdk.exception.OperationsLimitExceededException
import org.stellar.walletsdk.util.JvmUtil.builderEnum
import org.stellar.walletsdk.util.formatAmount

@Suppress("TooGenericExceptionCaught", "RethrowCaughtException")
private fun <T> safeHorizonCall(body: () -> T): T {
  try {
    return body()
  } catch (e: ErrorResponse) {
    throw HorizonRequestFailedException(e)
  } catch (e: Exception) {
    throw e
  }
}

/**
 * Fetch account information from the Stellar network.
 *
 * @param accountAddress Stellar address of the account
 *
 * @return Account response object
 *
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
@Throws(HorizonRequestFailedException::class)
suspend fun Server.accountByAddress(accountAddress: String): AccountResponse {
  return safeHorizonCall { accounts().account(accountAddress) }
}

/**
 * Fetch liquidity pool information from the Stellar network.
 *
 * @param liquidityPoolId Liquidity pool ID
 * @param cachedAssetInfo Previously cached asset information to use for liquidity pool assets
 *
 * @return liquidity pool data object
 *
 * @throws [HorizonRequestFailedException] for Horizon exceptions
 */
suspend fun Server.liquidityPoolInfo(
  liquidityPoolId: LiquidityPoolID,
  cachedAssetInfo: MutableMap<String, CachedAsset>
): LiquidityPoolInfo {
  val response: LiquidityPoolResponse = safeHorizonCall {
    liquidityPools().liquidityPool(liquidityPoolId)
  }

  val responseReserves = response.reserves
  val totalTrustlines = response.totalTrustlines
  val totalShares = response.totalShares

  val reserves: MutableList<LiquidityPoolReserve> = mutableListOf()

  responseReserves.forEach { item ->
    if (item.asset.type == AssetType.NATIVE.type) {
      val nativeReserve =
        LiquidityPoolReserve(
          id = XLM_ASSET_DEFAULT.id,
          homeDomain = XLM_ASSET_DEFAULT.homeDomain,
          name = XLM_ASSET_DEFAULT.name,
          imageUrl = XLM_ASSET_DEFAULT.imageUrl,
          assetCode = XLM_ASSET_DEFAULT.assetCode,
          assetIssuer = XLM_ASSET_DEFAULT.assetIssuer,
          amount = formatAmount(item.amount),
        )

      reserves.add(nativeReserve)
    } else {
      val assetArr = item.asset.toString().split(":")
      val assetCode = assetArr[0]
      val assetIssuer = assetArr[1]

      val cachedItem = cachedAssetInfo[item.asset.toString()]
      // TODO: if there is no cached info, fetch toml file to get homeDomain, name, and imageURL
      val lpAsset =
        LiquidityPoolReserve(
          id = item.asset.toString(),
          assetCode = assetCode,
          assetIssuer = assetIssuer,
          homeDomain = cachedItem?.homeDomain,
          name = cachedItem?.name,
          imageUrl = cachedItem?.imageUrl,
          amount = formatAmount(item.amount),
        )

      reserves.add(lpAsset)
    }
  }

  return LiquidityPoolInfo(totalTrustlines, totalShares, reserves)
}

/**
 * Fetch account operations from Stellar network.
 *
 * @param accountAddress Stellar address of the account
 * @param limit optional how many operations to fetch, maximum is 200, default is 10
 * @param order optional data order, ascending or descending, defaults to descending
 * @param cursor optional cursor to specify a starting point
 * @param includeFailed optional flag to include failed operations, defaults to false
 *
 * @return a list of account operations
 *
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
