package org.stellar.walletsdk.horizon

import java.time.Duration
import kotlin.math.min
import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.sdk.responses.SubmitTransactionTimeoutResponseException
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.exception.TransactionSubmitFailedException
import org.stellar.walletsdk.exception.ValidationException
import org.stellar.walletsdk.extension.accountOrNull
import org.stellar.walletsdk.horizon.transaction.TransactionBuilder
import org.stellar.walletsdk.util.toTimeBounds

private val log = KotlinLogging.logger {}

class Stellar
internal constructor(
  private val cfg: Config,
) {
  val server: Server = cfg.stellar.server

  fun account(): AccountService {
    return AccountService(cfg)
  }

  /**
   * Creates builder that allows to form Stellar transaction, adding Stellar's
   * [operations](https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment)
   *
   * @param sourceAddress Stellar address of account initiating a transaction.
   * @param baseFee base fee that will be used for this transaction. If not specified [default
   * config fee][StellarConfiguration.baseFee] will be used.
   * @param timeBounds [Time Bounds](https://developers.stellar.org/docs/glossary#time-bounds) for
   * this transaction. If not specified, [default config timeout]
   * [StellarConfiguration.defaultTimeout] will be used.
   * @param memo optional transaction memo
   * @return transaction builder
   */
  suspend fun transaction(
    sourceAddress: AccountKeyPair,
    baseFee: ULong? = null,
    memo: Pair<MemoType, String>? = null,
    timeBounds: TimeBounds? = null
  ): TransactionBuilder {
    val sourceAccount =
      server.accountOrNull(sourceAddress.address)
        ?: throw ValidationException("Source account $sourceAddress doesn't exist in the network")
    return TransactionBuilder(cfg, sourceAccount, baseFee, memo, timeBounds)
  }

  /**
   * Creates builder that allows to form Stellar transaction, adding Stellar's
   * [operations](https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment)
   *
   * @param sourceAddress Stellar address of account initiating a transaction.
   * @param baseFee base fee that will be used for this transaction. If not specified [default
   * config fee][StellarConfiguration.baseFee] will be used.
   * @param timeout Duration after which transaction expires. If not specified, [default config
   * timeout][StellarConfiguration.defaultTimeout] will be used.
   * @param memo optional transaction memo.
   * @return transaction builder
   */
  suspend fun transaction(
    sourceAddress: AccountKeyPair,
    timeout: Duration,
    baseFee: ULong? = null,
    memo: Pair<MemoType, String>? = null,
  ): TransactionBuilder {
    return transaction(sourceAddress, baseFee, memo, timeout.toTimeBounds())
  }

  /**
   * Creates
   * [Fee Bump transaction](https://developers.stellar.org/docs/encyclopedia/fee-bump-transactions)
   *
   * @param feeAddress address that will pay for the transaction's fee
   * @param transaction transaction for which fee should be paid (inner transaction)
   * @param baseFee optional base fee for the transaction. If not specified,
   * [StellarConfiguration.baseFee] will be used
   * @return **unsigned** fee bump transaction
   */
  fun makeFeeBump(
    feeAddress: AccountKeyPair,
    transaction: Transaction,
    baseFee: ULong? = null
  ): FeeBumpTransaction {
    return FeeBumpTransaction.Builder(transaction)
      .setBaseFee((baseFee ?: cfg.stellar.baseFee).toLong())
      .setFeeAccount(feeAddress.address)
      .build()
  }

  /**
   * Submit transaction to the Stellar network.
   *
   * @param signedTransaction Signed transaction that is submitted
   * @return `true` if submitted successfully
   * @throws [TransactionSubmitFailedException] when submission failed
   */
  @Suppress("SwallowedException")
  suspend fun submitTransaction(
    signedTransaction: AbstractTransaction,
  ) {
    try {
      when (signedTransaction) {
        is Transaction -> {
          log.debug {
            "Submit txn to network: sourceAccount = ${signedTransaction.sourceAccount}, memo = " +
              "${signedTransaction.memo}, fee = ${signedTransaction.fee}, operationCount = " +
              "${signedTransaction.operations.size}, signatureCount = ${signedTransaction
                    .signatures.size}"
          }

          val response = server.submitTransaction(signedTransaction)

          if (!response.isSuccess) {
            throw TransactionSubmitFailedException(response, signedTransaction)
          }

          log.debug { "Transaction submitted with hash ${response.hash}" }
        }
        is FeeBumpTransaction -> {
          log.debug {
            "Submit fee bump transaction. Source account :${signedTransaction.feeAccount}. Inner transaction hash: " +
              "${signedTransaction.innerTransaction.hashHex()}."
          }

          val response = server.submitTransaction(signedTransaction)

          if (!response.isSuccess) {
            throw TransactionSubmitFailedException(response, signedTransaction)
          }

          log.debug { "Transaction submitted with hash ${response.hash}" }
        }
        else -> error("Unknown transaction type")
      }
    } catch (e: SubmitTransactionTimeoutResponseException) {
      log.info { "Transaction ${signedTransaction.hashHex()} timed out. Resubmitting..." }
      return submitTransaction(signedTransaction)
    }
  }

  /**
   * Submit transaction with a fee increase. Recommended way of creating transactions. This method
   * repeatedly tries to submit transaction, until it's successful. When [timeout] is reached, base
   * fee will be increased on the [baseFeeIncrease] value.
   *
   * @param sourceAccount source account of transaction. Will be used as a signer.
   * @param timeout transaction timeout.
   * @param baseFeeIncrease amount on which fee will be increased after timeout is reached.
   * @param baseFee base transaction fee. If not specified, [default configuration value]
   * [StellarConfiguration.baseFee] will be used.
   * @param maxFee maximum allowed fee. Increased fee is limited by this value.
   * @param memo optional transaction memo.
   * @param buildingFunction function that will build the transaction.
   * @return transaction that has been submitted to the network.
   */
  @Suppress("LongParameterList")
  suspend fun submitWithFeeIncrease(
    sourceAccount: SigningKeyPair,
    timeout: Duration,
    baseFeeIncrease: ULong,
    baseFee: ULong? = null,
    maxFee: ULong = Integer.MAX_VALUE.toULong(),
    memo: Pair<MemoType, String>? = null,
    buildingFunction: TransactionBuilder.() -> TransactionBuilder
  ): Transaction {
    return submitWithFeeIncrease(
      sourceAccount,
      timeout,
      baseFeeIncrease,
      baseFee,
      maxFee,
      memo,
      { this.sign(sourceAccount) },
      buildingFunction
    )
  }

  /**
   * Submit transaction with a fee increase. Recommended way of creating transactions. This method
   * repeatedly tries to submit transaction, until it's successful. When [timeout] is reached, base
   * fee will be increased on the [baseFeeIncrease] value.
   *
   * @param sourceAddress source address of transaction
   * @param timeout transaction timeout
   * @param baseFeeIncrease amount on which fee will be increased after timeout is reached
   * @param baseFee base transaction fee. If not specified, [default configuration value]
   * [StellarConfiguration.baseFee] will be used
   * @param maxFee maximum allowed fee. Increased fee is limited by this value.
   * @param memo optional transaction memo
   * @param signerFunction function that will be used to sign the transaction
   * @param buildingFunction function that will build the transaction
   * @return transaction that has been submitted to the network.
   */
  @Suppress("LongParameterList")
  suspend fun submitWithFeeIncrease(
    sourceAddress: AccountKeyPair,
    timeout: Duration,
    baseFeeIncrease: ULong,
    baseFee: ULong? = null,
    maxFee: ULong = Integer.MAX_VALUE.toULong(),
    memo: Pair<MemoType, String>? = null,
    signerFunction: Transaction.() -> Transaction,
    buildingFunction: TransactionBuilder.() -> TransactionBuilder
  ): Transaction {
    val builder = transaction(sourceAddress, timeout, baseFee, memo)
    val transaction = builder.buildingFunction().build()
    val signedTransaction = transaction.signerFunction()

    try {
      submitTransaction(signedTransaction)
      return transaction
    } catch (e: TransactionSubmitFailedException) {
      if (e.transactionResultCode == "tx_too_late") {
        val newFee = min(maxFee, transaction.fee.toULong() + baseFeeIncrease)
        log.info {
          "Transaction ${transaction.hashHex()} has expired. Increasing fee to $newFee Stroops."
        }
        return submitWithFeeIncrease(
          sourceAddress,
          timeout,
          baseFeeIncrease,
          newFee,
          maxFee,
          memo,
          signerFunction,
          buildingFunction
        )
      }
      throw e
    }
  }

  /**
   * Decode transaction from XDR
   *
   * @param xdr base64 XDR string
   * @return decoded transaction
   */
  fun decodeTransaction(xdr: String): AbstractTransaction {
    return Transaction.fromEnvelopeXdr(xdr, cfg.stellar.network)
  }
}
