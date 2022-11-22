package org.stellar.walletsdk.exception

sealed class AnchorAssetException(message: String) : WalletException(message)

class AssetNotAcceptedForDepositException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not accepted for deposits")

class AssetNotAcceptedForWithdrawalException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not accepted for withdrawals")

class AssetNotEnabledForDepositException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not enabled for deposits")

class AssetNotEnabledForWithdrawalException(assetCode: String) :
  AnchorAssetException("Asset $assetCode is not enabled for withdrawals")
