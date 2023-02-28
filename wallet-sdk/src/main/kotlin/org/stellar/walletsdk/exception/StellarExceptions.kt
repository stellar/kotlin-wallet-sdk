package org.stellar.walletsdk.exception

import java.math.BigDecimal
import org.stellar.sdk.Operation
import org.stellar.sdk.responses.SubmitTransactionResponse
import org.stellar.walletsdk.horizon.transaction.SponsoringBuilder

sealed class StellarException : WalletException {
  constructor(message: String) : super(message)
  constructor(message: String, cause: Exception) : super(message, cause)
}

class AccountNotEnoughBalanceException(
  accountAddress: String,
  accountBalance: BigDecimal,
  transactionFees: BigDecimal,
) :
  StellarException(
    "Source account $accountAddress does not have enough XLM balance to cover " +
      "${transactionFees.toPlainString()} XLM fees. Available balance ${accountBalance.toPlainString()} XLM."
  )

class TransactionSubmitFailedException(
  val response: SubmitTransactionResponse,
) :
  StellarException(
    "Submit transaction failed with code ${response.resultCode ?: "<unknown>"}" +
      ".${response.operationsResultCodes ?. run { " Operation result codes: $this" } ?: ""}"
  ) {
  val transactionResultCode = response.resultCode
  val operationsResultCodes = response.operationsResultCodes
}

private val SubmitTransactionResponse.resultCode: String?
  get() = this.extras?.resultCodes?.transactionResultCode

private val SubmitTransactionResponse.operationsResultCodes: List<String>?
  get() =
    this.extras?.resultCodes?.operationsResultCodes?.run { if (this.isEmpty()) null else this }

class OperationsLimitExceededException : StellarException("Maximum limit is 200 operations")
