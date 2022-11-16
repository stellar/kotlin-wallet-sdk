package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*

/**
 * Format account's balances (assets and liquidity pools).
 *
 * @param account Account response object whose balances to format
 * @param server Horizon [Server] instance
 *
 * @return formatted account balances
 */
suspend fun formatAccountBalances(account: AccountResponse, server: Server): FormattedBalances {
  val cachedAssetTomlInfo = mutableMapOf<String, CachedAsset>()

  // Caching asset info fetched from toml files to avoid multiple calls for the same asset
  fun maybeAddToCachedAssetTomlInfo(assetInfo: List<CachedAsset>) {
    assetInfo.forEach { asset ->
      if (asset.id != XLM_ASSET_DEFAULTS.id && cachedAssetTomlInfo[asset.id] == null) {
        cachedAssetTomlInfo[asset.id] = asset
      }
    }
  }

  val result = FormattedBalances(assets = mutableListOf(), liquidityPools = mutableListOf())

  account.balances.forEach { balance ->
    when {
      // Native (XLM asset)
      balance.assetType == AssetType.NATIVE.type -> {
        val reservedBalance = BigDecimal(accountReservedBalance(account))
        val nativeAsset =
          FormattedAsset(
            id = XLM_ASSET_DEFAULTS.id,
            homeDomain = XLM_ASSET_DEFAULTS.homeDomain,
            name = XLM_ASSET_DEFAULTS.name,
            imageUrl = XLM_ASSET_DEFAULTS.imageUrl,
            assetCode = XLM_ASSET_DEFAULTS.assetCode,
            assetIssuer = XLM_ASSET_DEFAULTS.assetIssuer,
            balance = formatAmount(balance.balance),
            availableBalance =
              formatAmount(BigDecimal(balance.balance).minus(reservedBalance).toString()),
            buyingLiabilities = formatAmount(balance.buyingLiabilities.toString()),
            sellingLiabilities = formatAmount(balance.sellingLiabilities.toString())
          )

        result.assets.add(nativeAsset)
      }
      // Non-native asset
      (balance.assetType == AssetType.ALPHANUM_4.type ||
        balance.assetType == AssetType.ALPHANUM_12.type) -> {
        val asset =
          FormattedAsset(
            id = "${balance.assetCode}:${balance.assetIssuer}",
            // TODO: fetch home domain
            homeDomain = "",
            // TODO: fetch name
            name = "",
            // TODO: fetch imageUrl
            imageUrl = "",
            assetCode = balance.assetCode.toString(),
            assetIssuer = balance.assetIssuer.toString(),
            balance = formatAmount(balance.balance),
            availableBalance = formatAmount(balance.balance),
            buyingLiabilities = formatAmount(balance.buyingLiabilities.toString()),
            sellingLiabilities = formatAmount(balance.sellingLiabilities.toString())
          )

        result.assets.add(asset)

        maybeAddToCachedAssetTomlInfo(
          listOf(
            CachedAsset(
              id = asset.id,
              homeDomain = asset.homeDomain,
              name = asset.name,
              imageUrl = asset.imageUrl,
              amount = formatAmount(asset.balance)
            )
          )
        )
      }
      // Liquidity pool
      balance.assetType == AssetType.LIQUIDITY_POOL.type -> {
        val liquidityPoolInfo =
          fetchLiquidityPoolInfo(
            liquidityPoolId = balance.liquidityPoolID.get(),
            cachedAssetInfo = cachedAssetTomlInfo,
            server = server
          )

        val lpAsset =
          FormattedLiquidityPool(
            id = balance.asset.toString(),
            balance = formatAmount(balance.balance),
            totalTrustlines = liquidityPoolInfo.totalTrustlines,
            totalShares = liquidityPoolInfo.totalShares,
            reserves = liquidityPoolInfo.reserves
          )

        result.liquidityPools.add(lpAsset)

        maybeAddToCachedAssetTomlInfo(
          liquidityPoolInfo.reserves.map {
            CachedAsset(
              id = it.id,
              homeDomain = it.homeDomain,
              name = it.name,
              imageUrl = it.imageUrl,
              amount = it.amount
            )
          }
        )
      }
    }
  }

  return result
}
