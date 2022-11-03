package org.stellar.walletsdk.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.stellar.sdk.LiquidityPoolID
import org.stellar.sdk.Server
import org.stellar.sdk.responses.LiquidityPoolResponse
import org.stellar.walletsdk.*

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
suspend fun fetchLiquidityPoolInfo(
  liquidityPoolId: LiquidityPoolID,
  cachedAssetInfo: MutableMap<String, CachedAsset>,
  server: Server
): LiquidityPoolInfo {
  return CoroutineScope(Dispatchers.IO)
    .async {
      val response: LiquidityPoolResponse

      try {
        response = server.liquidityPools().liquidityPool(liquidityPoolId)
      } catch (e: Exception) {
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

      return@async LiquidityPoolInfo(totalTrustlines, totalShares, reserves)
    }
    .await()
}
