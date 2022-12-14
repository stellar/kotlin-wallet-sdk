package org.stellar.walletsdk.exception

import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.asset.IssuedAssetId

sealed class AnchorAssetException(message: String) : WalletException(message)

class AssetNotAcceptedForDepositException(assetCode: IssuedAssetId) :
  AnchorAssetException("Asset $assetCode is not accepted for deposits")

class AssetNotAcceptedForWithdrawalException(assetCode: IssuedAssetId) :
  AnchorAssetException("Asset $assetCode is not accepted for withdrawals")

class AssetNotEnabledForDepositException(assetCode: IssuedAssetId) :
  AnchorAssetException("Asset $assetCode is not enabled for deposits")

class AssetNotEnabledForWithdrawalException(assetCode: IssuedAssetId) :
  AnchorAssetException("Asset $assetCode is not enabled for withdrawals")

class AssetNotSupportedException(assetCode: IssuedAssetId) :
  AnchorAssetException("Asset $assetCode is not supported")

class IncorrectTransactionStatusException(
  val transaction: AnchorTransaction,
  expectedStatus: String
) :
  WalletException(
    "Incorrect status of transaction ${transaction.id}. Expected status to be $expectedStatus and transaction has status ${transaction.status}"
  )

class AnchorAuthNotSupported() :
  AnchorAssetException("Anchor does not have SEP-10 auth configured in TOML file")

class AnchorInteractiveFlowNotSupported() :
  AnchorAssetException("Anchor does not have SEP-24 interactive flow configured in TOML file")
