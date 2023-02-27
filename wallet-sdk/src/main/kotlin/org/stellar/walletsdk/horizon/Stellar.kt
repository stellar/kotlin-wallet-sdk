package org.stellar.walletsdk.horizon

import kotlin.IllegalStateException
import mu.KotlinLogging
import org.stellar.sdk.AbstractTransaction
import org.stellar.sdk.FeeBumpTransaction
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.exception.TransactionSubmitFailedException
import org.stellar.walletsdk.extension.accountByAddress

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
   * @param defaultSponsorAddress Stellar address of account sponsoring operations inside this
   *   transaction
   * @param memo optional memo
   * @return transaction builder
   */
  suspend fun transaction(
    sourceAddress: AccountKeyPair,
    memo: Pair<MemoType, String>? = null,
    defaultSponsorAddress: String? = null
  ): TransactionBuilder {
    val sourceAccount = server.accountByAddress(sourceAddress.address)
    return TransactionBuilder(cfg, sourceAccount, memo, defaultSponsorAddress)
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
}
