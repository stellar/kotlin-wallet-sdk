package org.stellar.walletsdk.extension

import java.io.IOException
import org.stellar.sdk.LiquidityPoolID
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.LiquidityPoolResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.AccountNotFoundException
import org.stellar.walletsdk.exception.LiquidityPoolNotFoundException
import org.stellar.walletsdk.util.formatAmount

/**
 * Fetch account information from the Stellar network.
 *
 * @param accountAddress Stellar address of the account
 *
 * @return Account response object
 *
 * @throws [AccountNotFoundException] when account is not found
 */
@Throws(AccountNotFoundException::class)
suspend fun Server.accountByAddress(accountAddress: String): AccountResponse {
  try {
    return accounts().account(accountAddress)
  } catch (e: IOException) {
    // TODO: check that error code is 404
    throw AccountNotFoundException(accountAddress)
  }
}

/**
 * Fetch liquidity pool information from the Stellar network.
 *
 * @param liquidityPoolId Liquidity pool ID
 * @param cachedAssetInfo Previously cached asset information to use for liquidity pool assets
 * @param server Horizon [Server] instance
 *
 * @return liquidity pool data object
 *
 * @throws [LiquidityPoolNotFoundException] when liquidity pool is not found
 */
suspend fun Server.liquidityPoolInfo(
  liquidityPoolId: LiquidityPoolID,
  cachedAssetInfo: MutableMap<String, CachedAsset>
): LiquidityPoolInfo {
  val response: LiquidityPoolResponse

  try {
    response = liquidityPools().liquidityPool(liquidityPoolId)
  } catch (e: Exception) {
    // TODO: throw on 404 only
    throw LiquidityPoolNotFoundException(liquidityPoolId)
  }

  val responseReserves = response.reserves
  val totalTrustlines = response.totalTrustlines
  val totalShares = response.totalShares

  val reserves: MutableList<LiquidityPoolReserve> = mutableListOf()

  responseReserves.forEach { item ->
    if (item.asset.type == AssetType.NATIVE.type) {
      val nativeReserve =
        LiquidityPoolReserve(
          id = XLM_ASSET_DEFAULTS.id,
          homeDomain = XLM_ASSET_DEFAULTS.homeDomain,
          name = XLM_ASSET_DEFAULTS.name,
          imageUrl = XLM_ASSET_DEFAULTS.imageUrl,
          assetCode = XLM_ASSET_DEFAULTS.assetCode,
          assetIssuer = XLM_ASSET_DEFAULTS.assetIssuer,
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
