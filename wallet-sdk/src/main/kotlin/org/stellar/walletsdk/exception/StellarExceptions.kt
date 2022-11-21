package org.stellar.walletsdk.exception

import org.stellar.sdk.LiquidityPoolID
import org.stellar.sdk.responses.SubmitTransactionResponse
import java.math.BigDecimal

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

class InvalidSponsorOperationTypeException(operationType: String, allowedOperations: List<String>) :
    StellarException(
        "$operationType cannot be sponsored. Allowed operations are: ${allowedOperations
            .joinToString(", ")}."
    )

class AccountNotFoundException(accountAddress: String) :
    StellarException("Account $accountAddress was not found")
