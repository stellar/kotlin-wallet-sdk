package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.extension.liquidityPoolInfo

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
        val reservedBalance = BigDecimal(account.reservedBalance())
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
          server.liquidityPoolInfo(
            liquidityPoolId = balance.liquidityPoolID.get(),
            cachedAssetInfo = cachedAssetTomlInfo,
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

/**
 * Format amount to consistent string.
 *
 * @param amount Amount string to format
 *
 * @return formatted amount
 */
@Deprecated("To be removed with wrapper")
fun formatAmount(amount: String): String {
  // TODO: how to always show 7 decimal points (1.0000000)?
  return amount
}

/**
 * Convert amount in [stroops](https://developers.stellar.org/docs/glossary#stroop) to amount in
 * lumens (XLM).
 *
 * @param stroops Amount in stroops to convert to lumens
 *
 * @return amount in lumens (XLM)
 */
@Deprecated("To be removed with wrapper")
fun stroopsToLumens(stroops: String): String {
  return BigDecimal(stroops).divide(BigDecimal(1e7)).toPlainString()
}

/**
 * Convert amount in lumens (XLM) to amount in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop).
 *
 * @param lumens Amount in lumens (XLM) to convert to stroops
 *
 * @return amount in stroops
 */
@Deprecated("To be removed with wrapper")
fun lumensToStroops(lumens: String): String {
  return BigDecimal(lumens).multiply(BigDecimal(1e7)).toPlainString()
}
