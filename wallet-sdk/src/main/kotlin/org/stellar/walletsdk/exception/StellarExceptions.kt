package org.stellar.walletsdk.exception

import java.math.BigDecimal
import org.stellar.sdk.AbstractTransaction
import org.stellar.sdk.exception.BadRequestException

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
  val exception: BadRequestException,
  val transaction: AbstractTransaction
) :
  StellarException(
    "Submit transaction failed with code ${exception.resultCode ?: "<unknown>"}" +
      ".${exception.operationsResultCodes ?. run { " Operation result codes: $this" } ?: ""}" +
      " Transaction XDR: ${transaction.toEnvelopeXdrBase64()}"
  ) {
  val transactionResultCode = exception.resultCode
  val operationsResultCodes = exception.operationsResultCodes
}

private val BadRequestException.resultCode: String?
  get() = this.problem?.extras?.resultCodes?.transactionResultCode

private val BadRequestException.operationsResultCodes: List<String>?
  get() =
    this.problem?.extras?.resultCodes?.operationsResultCodes?.run {
      if (this.isEmpty()) null else this
    }

class OperationsLimitExceededException : StellarException("Maximum limit is 200 operations")
