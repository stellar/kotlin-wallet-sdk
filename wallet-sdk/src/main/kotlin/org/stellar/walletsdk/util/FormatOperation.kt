package org.stellar.walletsdk.util

import org.stellar.sdk.Asset
import org.stellar.sdk.responses.operations.CreateAccountOperationResponse
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PathPaymentBaseOperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import org.stellar.walletsdk.*

/**
 * Format Stellar account operations to make them consistent.
 *
 * @param accountAddress Stellar address of the account
 * @param operation Stellar operation to format
 *
 * @return formatted operation
 */
fun formatStellarOperation(accountAddress: String, operation: OperationResponse): WalletOperation {
  when (operation.type) {
    // Create account
    StellarOperationTypes.CREATE_ACCOUNT.type -> {
      val isCreator = (operation as CreateAccountOperationResponse).funder == accountAddress

      return WalletOperation(
        id = operation.id,
        date = operation.createdAt,
        amount = operation.startingBalance,
        account =
          if (isCreator) {
            operation.account
          } else {
            operation.funder
          },
        asset = formatNativeAsset(),
        type =
          if (isCreator) {
            WalletOperationTypes.SEND
          } else {
            WalletOperationTypes.RECEIVE
          },
        rawOperation = operation
      )
    }
    // Payment
    StellarOperationTypes.PAYMENT.type -> {
      // TODO: This version of Java SDK currently doesn't have "to" and "from" muxed account for
      // payment
      val isSender = (operation as PaymentOperationResponse).from == accountAddress

      return WalletOperation(
        id = operation.id,
        date = operation.createdAt,
        amount = operation.amount,
        account =
          if (isSender) {
            operation.to
          } else {
            operation.from
          },
        asset = formatWalletAsset(operation),
        type =
          if (isSender) {
            WalletOperationTypes.SEND
          } else {
            WalletOperationTypes.RECEIVE
          },
        rawOperation = operation
      )
    }
    // Path payment and swap
    StellarOperationTypes.PATH_PAYMENT_STRICT_SEND.type,
    StellarOperationTypes.PATH_PAYMENT_STRICT_RECEIVE.type -> {
      // TODO: check muxed account
      operation as PathPaymentBaseOperationResponse
      val isSender = operation.from == accountAddress
      val isSwap = isSender && operation.from == operation.to

      return WalletOperation(
        id = operation.id,
        date = operation.createdAt,
        amount = operation.amount,
        account =
          if (isSender) {
            if (isSwap) {
              ""
            } else {
              operation.to
            }
          } else {
            operation.from
          },
        asset = formatWalletAsset(operation),
        type =
          if (isSender) {
            if (isSwap) {
              WalletOperationTypes.SWAP
            } else {
              WalletOperationTypes.SEND
            }
          } else {
            WalletOperationTypes.RECEIVE
          },
        rawOperation = operation
      )
    }
    // Other
    else -> {
      return WalletOperation(
        id = operation.id,
        date = operation.createdAt,
        amount = "",
        account = "",
        asset = listOf(),
        type = WalletOperationTypes.OTHER,
        rawOperation = operation
      )
    }
  }
}

fun formatNativeAsset(): List<WalletAsset> {
  val asset = formatAsset()

  return listOf(asset)
}

fun formatWalletAsset(operation: PaymentOperationResponse): List<WalletAsset> {
  val asset = formatAsset(operation.asset)

  return listOf(asset)
}

fun formatWalletAsset(operation: PathPaymentBaseOperationResponse): List<WalletAsset> {
  val asset = formatAsset(operation.asset)
  val sourceAsset = formatAsset(operation.sourceAsset)

  return listOf(sourceAsset, asset)
}

fun formatAsset(asset: Asset? = null): WalletAsset {
  val assetType = asset?.type ?: "native"
  var assetCode = ""
  var assetIssuer = ""

  when (assetType) {
    "native" -> {
      assetCode = "XLM"
      assetIssuer = "Native"
    }
    "credit_alphanum4",
    "credit_alphanum12" -> {
      val assetString = asset.toString().split(":")

      assetCode = assetString[0]
      assetIssuer = assetString[1]
    }
    else -> {
      // TODO: add this when we add support for liquidity pools
    }
  }

  return WalletAsset(id = "$assetCode:$assetIssuer", code = assetCode, issuer = assetIssuer)
}
