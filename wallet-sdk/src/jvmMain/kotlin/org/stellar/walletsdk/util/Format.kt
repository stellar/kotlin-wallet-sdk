@file:Suppress("TooManyFunctions")

package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.sdk.Server
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PathPaymentBaseOperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.asset.NativeAssetId
import org.stellar.walletsdk.asset.toAssetId
import org.stellar.walletsdk.extension.liquidityPoolInfo
import org.stellar.walletsdk.extension.reservedBalance

/**
 * Format account's balances (assets and liquidity pools).
 *
 * @param account Account response object whose balances to format
 * @param server Horizon [Server] instance
 * @return formatted account balances
 */
@Suppress("LongMethod")
suspend fun formatAccountBalances(account: AccountResponse, server: Server): FormattedBalances {
  val cachedAssetTomlInfo = mutableMapOf<String, CachedAsset>()

  // Caching asset info fetched from toml files to avoid multiple calls for the same asset
  fun maybeAddToCachedAssetTomlInfo(assetInfo: List<CachedAsset>) {
    assetInfo.forEach { asset ->
      if (asset.id != XLM_ASSET_DEFAULT.id && cachedAssetTomlInfo[asset.id] == null) {
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
            id = XLM_ASSET_DEFAULT.id,
            homeDomain = XLM_ASSET_DEFAULT.homeDomain,
            name = XLM_ASSET_DEFAULT.name,
            imageUrl = XLM_ASSET_DEFAULT.imageUrl,
            assetCode = XLM_ASSET_DEFAULT.assetCode,
            assetIssuer = XLM_ASSET_DEFAULT.assetIssuer,
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
 * Format Stellar account operations to make them consistent.
 *
 * @param accountAddress Stellar address of the account
 * @param operation Stellar operation to format
 * @return formatted operation
 */
@Suppress("ReturnCount")
fun formatStellarOperation(
  accountAddress: String,
  operation: OperationResponse
): WalletOperation<OperationResponse> {
  val opBuilder = WalletOperationBuilder<OperationResponse>().fromOperation(operation)

  when (operation) {
    // Create account
    is CreateAccountOperationResponse -> {
      val isCreator = operation.funder == accountAddress

      return opBuilder
        .amount(operation.startingBalance)
        .account(if (isCreator) operation.account else operation.funder)
        .asset(listOf(NativeAssetId))
        .type(if (isCreator) WalletOperationType.SEND else WalletOperationType.RECEIVE)
        .build()
    }
    // Payment
    is PaymentOperationResponse -> {
      // TODO: This version of Java SDK currently doesn't have "to" and "from" muxed account for
      // payment
      val isSender = operation.from == accountAddress

      return opBuilder
        .amount(operation.amount)
        .account(if (isSender) operation.to else operation.from)
        .asset(listOf(operation.asset.toAssetId()))
        .type(if (isSender) WalletOperationType.SEND else WalletOperationType.RECEIVE)
        .build()
    }
    // Path payment and swap
    is PathPaymentBaseOperationResponse -> {
      // TODO: check muxed account
      val isSender = operation.from == accountAddress
      val isSwap = isSender && operation.from == operation.to

      return opBuilder
        .amount(operation.amount)
        .account(
          if (isSender) {
            if (isSwap) {
              ""
            } else {
              operation.to
            }
          } else {
            operation.from
          }
        )
        .asset(listOf(operation.sourceAsset.toAssetId(), operation.asset.toAssetId()))
        .type(
          if (isSender) {
            if (isSwap) {
              WalletOperationType.SWAP
            } else {
              WalletOperationType.SEND
            }
          } else {
            WalletOperationType.RECEIVE
          }
        )
        .build()
    }
    // Other
    else -> {
      return opBuilder.build()
    }
  }
}

/**
 * Helper class to format wallet operation from Stellar operation or anchor transaction.
 *
 * @param T type of operation or transaction
 */
internal class WalletOperationBuilder<T : Any> {
  lateinit var id: String
  lateinit var date: String
  lateinit var amount: String
  lateinit var account: String
  lateinit var asset: List<AssetId>
  lateinit var type: WalletOperationType
  lateinit var rawOperation: T

  internal fun defaults() = apply {
    this.amount = ""
    this.account = ""
    this.type = WalletOperationType.OTHER
  }

  fun id(id: String) = apply { this.id = id }
  fun date(date: String) = apply { this.date = date }
  fun amount(amount: String) = apply { this.amount = amount }
  fun account(account: String) = apply { this.account = account }
  fun asset(asset: List<AssetId>) = apply { this.asset = asset }
  fun type(type: WalletOperationType) = apply { this.type = type }
  fun rawOperation(rawOperation: T) = apply { this.rawOperation = rawOperation }

  fun build(): WalletOperation<T> {
    return WalletOperation(
      id = this.id,
      date = this.date,
      amount = this.amount,
      account = this.account,
      asset = this.asset,
      type = this.type,
      rawOperation = this.rawOperation as T
    )
  }
}

internal fun <T : OperationResponse> WalletOperationBuilder<T>.fromOperation(operation: T) = apply {
  this.defaults()
  this.id = operation.id.toString()
  this.date = operation.createdAt
  this.asset = listOf()
  this.rawOperation = operation
}

/**
 * Format amount to consistent string.
 *
 * @param amount Amount string to format
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
 * @return amount in lumens (XLM)
 */
@Deprecated("To be removed with wrapper")
fun stroopsToLumens(stroops: String): String {
  return BigDecimal(stroops).divide(BigDecimal(XLM_PRECISION)).toPlainString()
}

/**
 * Convert amount in lumens (XLM) to amount in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop).
 *
 * @param lumens Amount in lumens (XLM) to convert to stroops
 * @return amount in stroops
 */
@Deprecated("To be removed with wrapper")
fun lumensToStroops(lumens: String): String {
  return BigDecimal(lumens).multiply(BigDecimal(XLM_PRECISION)).toPlainString()
}
