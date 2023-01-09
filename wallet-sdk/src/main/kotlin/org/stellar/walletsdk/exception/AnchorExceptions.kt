package org.stellar.walletsdk.exception

import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.asset.IssuedAssetId

sealed class AnchorException(message: String) : WalletException(message)

sealed class AnchorAssetException(message: String) : AnchorException(message)

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
    "Incorrect status of transaction ${transaction.id}. Expected status to be $expectedStatus and" +
      " transaction has status ${transaction.status}"
  )

object AnchorAuthNotSupported :
  AnchorException("Anchor does not have SEP-10 auth configured in TOML file")

object AnchorInteractiveFlowNotSupported :
  AnchorException("Anchor does not have SEP-24 interactive flow configured in TOML file")
