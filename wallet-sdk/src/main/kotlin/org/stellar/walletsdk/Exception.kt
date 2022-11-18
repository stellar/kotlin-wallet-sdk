package org.stellar.walletsdk

import java.io.IOException
import java.math.BigDecimal
import okhttp3.Response
import org.stellar.sdk.LiquidityPoolID
import org.stellar.sdk.responses.SubmitTransactionResponse

class AccountNotEnoughBalanceException(
  accountAddress: String,
  accountBalance: BigDecimal,
  transactionFees: BigDecimal,
) :
  Exception(
    "Source account $accountAddress does not have enough XLM balance to cover " +
      "${transactionFees.toPlainString()} XLM fees. Available balance ${accountBalance.toPlainString()} XLM."
  )

class AccountNotFoundException(accountAddress: String) :
  Exception("Account $accountAddress was not found")

class AssetNotAcceptedForDepositException(assetCode: String) :
  Exception("Asset $assetCode is not accepted for deposits")

class AssetNotAcceptedForWithdrawalException(assetCode: String) :
  Exception("Asset $assetCode is not accepted for withdrawals")

class AssetNotEnabledForDepositException(assetCode: String) :
  Exception("Asset $assetCode is not enabled for deposits")

class AssetNotEnabledForWithdrawalException(assetCode: String) :
  Exception("Asset $assetCode is not enabled for withdrawals")

class ClientDomainWithMemoException : Exception("Client domain cannot be used with memo")

class InvalidAnchorServiceUrl(rawException: Exception) :
  Exception("Anchor service URL is invalid", rawException)

class InvalidMemoIdException : Exception("Memo ID must be a positive integer")

class InvalidSponsorOperationTypeException(operationType: String, allowedOperations: List<String>) :
  Exception(
    "$operationType cannot be sponsored. Allowed operations are: ${allowedOperations
      .joinToString(", ")}."
  )

class InvalidStartingBalanceException :
  Exception("Starting balance must be at least 1 XLM for non-sponsored accounts")

class LiquidityPoolNotFoundException(liquidityPoolID: LiquidityPoolID) :
  Exception("Liquidity pool $liquidityPoolID was not found")

class MissingTokenException : Exception("Token was not returned")

class MissingTransactionException : Exception("The response did not contain a transaction")

class NetworkMismatchException : Exception("Networks don't match")

class NetworkRequestFailedException(
  rawResponse: Response,
  label: String? = "Request failed",
) : IOException("$label: $rawResponse")

class OperationsLimitExceededException : Exception("Maximum limit is 200 operations")

class RecoveryNoAccountSignersOnServerException :
  Exception("There are no signers on this recovery server")

class RecoveryNotAllSignaturesFetchedException :
  Exception("Didn't get all recovery server signatures")

class RecoveryNotRegisteredWithAllServersException :
  Exception("Could not register with all recovery servers")

class StellarTomlAddressMissingHomeDomain(stellarAddress: String) :
  Exception("Stellar address $stellarAddress does not have home domain configured")

class StellarTomlMissingFields(missingFields: List<String>) :
  Exception("TOML configuration is missing: ${missingFields.joinToString(",")}")

class TransactionSubmitFailedException(
  rawResponse: SubmitTransactionResponse,
  label: String? = "Submit transaction failed"
) : Exception("$label: $rawResponse")
