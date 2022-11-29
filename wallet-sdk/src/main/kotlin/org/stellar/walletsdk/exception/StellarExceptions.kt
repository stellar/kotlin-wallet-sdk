package org.stellar.walletsdk.exception

import java.math.BigDecimal
import kotlin.reflect.KClass
import org.stellar.sdk.LiquidityPoolID
import org.stellar.sdk.Operation
import org.stellar.sdk.responses.SubmitTransactionResponse

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

class LiquidityPoolNotFoundException(liquidityPoolID: LiquidityPoolID) :
  StellarException("Liquidity pool $liquidityPoolID was not found")

class TransactionSubmitFailedException(
  val response: SubmitTransactionResponse,
) : StellarException("Submit transaction failed") {
  val resultCode = response.extras?.resultCodes?.transactionResultCode
}

class InvalidSponsorOperationTypeException(
  operationType: Collection<Operation>,
  allowedOperations: Collection<KClass<out Operation>>
) :
  StellarException(
    "${operationType.map { it::class.simpleName }} cannot be sponsored. Allowed operations are: ${allowedOperations.map { it.simpleName }}}."
  )

class AccountNotFoundException(accountAddress: String) :
  StellarException("Account $accountAddress was not found")

class OperationsLimitExceededException : StellarException("Maximum limit is 200 operations")
