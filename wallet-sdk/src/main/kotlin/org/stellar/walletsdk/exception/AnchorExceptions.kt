package org.stellar.walletsdk.exception

import org.stellar.walletsdk.anchor.AnchorTransaction

sealed class AnchorAssetException(message: String) : WalletException(message)

class AssetNotAcceptedForDepositException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not accepted for deposits")

class AssetNotAcceptedForWithdrawalException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not accepted for withdrawals")

class AssetNotEnabledForDepositException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not enabled for deposits")

class AssetNotEnabledForWithdrawalException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not enabled for withdrawals")

class AssetNotSupportedException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not supported")

class IncorrectTransactionStatusException(
  val transaction: AnchorTransaction,
  expectedStatus: String
) :
  WalletException(
    "Incorrect status of transaction ${transaction.id}. Expected status to be $expectedStatus and transaction has status ${transaction.status}"
  )
