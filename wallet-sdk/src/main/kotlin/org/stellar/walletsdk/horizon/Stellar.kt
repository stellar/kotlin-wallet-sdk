package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.*

private val log = KotlinLogging.logger {}

class Stellar
internal constructor(
  private val cfg: Config,
) {
  val server: Server = cfg.stellar.server

  fun account(): AccountService {
    return AccountService(cfg)
  }

  fun transaction(): TransactionBuilder {
    return TransactionBuilder(cfg)
  }

  /**
   * Submit transaction to the Stellar network.
   *
   * @param signedTransaction Signed transaction that is submitted
   *
   * @return `true` if submitted successfully
   *
   * @throws [TransactionSubmitFailedException] when submission failed
   */
  suspend fun submitTransaction(
    signedTransaction: Transaction,
  ): Boolean {
    log.debug {
      "Submit txn to network: sourceAccount = ${signedTransaction.sourceAccount}, memo = " +
        "${signedTransaction.memo}, fee = ${signedTransaction.fee}, operationCount = " +
        "${signedTransaction.operations.size}, signatureCount = ${signedTransaction
                        .signatures.size}"
    }

    val response = server.submitTransaction(signedTransaction)

    if (response.isSuccess) {
      return true
    }

    throw TransactionSubmitFailedException(response)
  }
}
