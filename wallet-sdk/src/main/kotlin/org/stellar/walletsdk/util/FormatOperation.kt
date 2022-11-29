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
fun formatStellarOperation(
  accountAddress: String,
  operation: OperationResponse
): WalletOperation<OperationResponse> {
  when (operation.type) {
    // Create account
    "create_account" -> {
      val isCreator = (operation as CreateAccountOperationResponse).funder == accountAddress

      return WalletOperation(
        id = operation.id.toString(),
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
            WalletOperationType.SEND
          } else {
            WalletOperationType.RECEIVE
          },
        rawOperation = operation
      )
    }
    // Payment
    "payment" -> {
      // TODO: This version of Java SDK currently doesn't have "to" and "from" muxed account for
      // payment
      val isSender = (operation as PaymentOperationResponse).from == accountAddress

      return WalletOperation(
        id = operation.id.toString(),
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
            WalletOperationType.SEND
          } else {
            WalletOperationType.RECEIVE
          },
        rawOperation = operation
      )
    }
    // Path payment and swap
    "path_payment_strict_receive",
    "path_payment_strict_send" -> {
      // TODO: check muxed account
      operation as PathPaymentBaseOperationResponse
      val isSender = operation.from == accountAddress
      val isSwap = isSender && operation.from == operation.to

      return WalletOperation(
        id = operation.id.toString(),
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
              WalletOperationType.SWAP
            } else {
              WalletOperationType.SEND
            }
          } else {
            WalletOperationType.RECEIVE
          },
        rawOperation = operation
      )
    }
    // Other
    else -> {
      return WalletOperation(
        id = operation.id.toString(),
        date = operation.createdAt,
        amount = "",
        account = "",
        asset = listOf(),
        type = WalletOperationType.OTHER,
        rawOperation = operation
      )
    }
  }
}

/**
 * Format anchor transactions to make them consistent.
 *
 * @param transaction anchor transaction to format
 * @param asset transaction asset
 *
 * @return formatter transaction
 */
fun formatAnchorTransaction(
  transaction: AnchorTransaction,
  asset: Asset
): WalletOperation<AnchorTransaction> {
  val formattedAsset = formatAsset(asset)

  when (transaction.kind) {
    "deposit",
    "withdrawal" -> {
      return WalletOperation(
        id = transaction.id,
        date = transaction.started_at,
        amount = transaction.amount_out,
        account = "",
        asset = listOf(formattedAsset),
        type =
          if (transaction.kind == "deposit") {
            WalletOperationType.DEPOSIT
          } else {
            WalletOperationType.WITHDRAW
          },
        rawOperation = transaction
      )
    }
    else -> {
      return WalletOperation(
        id = transaction.id,
        date = transaction.started_at,
        amount = "",
        account = "",
        asset = listOf(formattedAsset),
        type = WalletOperationType.OTHER,
        rawOperation = transaction
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
