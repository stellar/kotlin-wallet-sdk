@file:JvmName("StellarJvm")

package org.stellar.walletsdk.horizon

import kotlin.jvm.JvmName
import mu.KotlinLogging
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.anchor.MemoType

private val log = KotlinLogging.logger {}

expect class Stellar
internal constructor(
  cfg: Config,
) {
  fun account(): AccountService

  /**
   * Creates builder that allows to form Stellar transaction, adding Stellar's
   * [operations](https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment)
   *
   * @param sourceAddress Stellar address of account initiating a transaction
   * @param defaultSponsorAddress Stellar address of account sponsoring operations inside this
   * transaction
   * @param memo optional memo
   * @return transaction builder
   */
  suspend fun transaction(
    sourceAddress: AccountKeyPair,
    memo: Pair<MemoType, String>? = null,
    defaultSponsorAddress: String? = null
  ): TransactionBuilder

  /**
   * Submit transaction to the Stellar network.
   *
   * @param signedTransaction Signed transaction that is submitted
   * @return `true` if submitted successfully
   * @throws [TransactionSubmitFailedException] when submission failed
   */
  suspend fun submitTransaction(
    signedTransaction: Transaction,
  )
}
