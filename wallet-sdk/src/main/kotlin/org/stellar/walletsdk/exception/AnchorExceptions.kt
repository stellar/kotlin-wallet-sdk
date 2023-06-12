package org.stellar.walletsdk.exception

import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.asset.StellarAssetId

sealed class AnchorException(message: String) : WalletException(message)

sealed class AnchorAssetException(message: String) : AnchorException(message)

class AssetNotAcceptedForDepositException(assetId: StellarAssetId) :
  AnchorAssetException("Asset $assetId is not accepted for deposits")

class AssetNotAcceptedForWithdrawalException(assetId: StellarAssetId) :
  AnchorAssetException("Asset $assetId is not accepted for withdrawals")

class AssetNotEnabledForDepositException(assetId: StellarAssetId) :
  AnchorAssetException("Asset $assetId is not enabled for deposits")

class AssetNotEnabledForWithdrawalException(assetId: StellarAssetId) :
  AnchorAssetException("Asset $assetId is not enabled for withdrawals")

class AssetNotSupportedException(assetCode: AssetId) :
  AnchorAssetException("Asset $assetCode is not supported")

class IncorrectTransactionStatusException(
  val transaction: AnchorTransaction,
  expectedStatus: TransactionStatus
) :
  WalletException(
    "Incorrect status of transaction ${transaction.id}. Expected status to be ${expectedStatus.name.lowercase()} and" +
      " transaction has status ${transaction.status}"
  )

object AnchorAuthNotSupported :
  AnchorException("Anchor does not have SEP-10 auth configured in TOML file")

object AnchorInteractiveFlowNotSupported :
  AnchorException("Anchor does not have SEP-24 interactive flow configured in TOML file")
