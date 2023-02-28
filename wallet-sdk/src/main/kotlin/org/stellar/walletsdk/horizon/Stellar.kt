package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.sdk.AbstractTransaction
import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.exception.HorizonRequestFailedException
import org.stellar.walletsdk.exception.TransactionSubmitFailedException
import org.stellar.walletsdk.exception.ValidationException
import org.stellar.walletsdk.extension.accountByAddress
import org.stellar.walletsdk.horizon.transaction.TransactionBuilder

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
   * @param sourceAddress Stellar address of account initiating a transaction
   * @param memo optional memo
   * @return transaction builder
   */
  suspend fun transaction(
    sourceAddress: AccountKeyPair,
    memo: Pair<MemoType, String>? = null,
  ): TransactionBuilder {
    val sourceAccount =
      try {
        server.accountByAddress(sourceAddress.address)
      } catch (e: HorizonRequestFailedException) {
        if (e.errorCode == 404) {
          throw ValidationException("Source account $sourceAddress doesn't exist in the network")
        }
        throw e
      }
    return TransactionBuilder(cfg, sourceAccount, memo)
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
    baseFee: UInt? = null
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
  suspend fun submitTransaction(
    signedTransaction: AbstractTransaction,
  ) {
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
          throw TransactionSubmitFailedException(response)
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
          throw TransactionSubmitFailedException(response)
        }

        log.debug { "Transaction submitted with hash ${response.hash}" }
      }
      else -> error("Unknown transaction type")
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
