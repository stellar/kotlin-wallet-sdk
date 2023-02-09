package org.stellar.walletsdk.horizon

import org.stellar.walletsdk.Config
import org.stellar.walletsdk.anchor.MemoType

actual class Stellar internal actual constructor(cfg: Config) {
  actual fun account(): AccountService {
    TODO("Not yet implemented")
  }

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
  actual suspend fun transaction(
    sourceAddress: AccountKeyPair,
    memo: Pair<MemoType, String>?,
    defaultSponsorAddress: String?
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  /**
   * Submit transaction to the Stellar network.
   *
   * @param signedTransaction Signed transaction that is submitted
   * @return `true` if submitted successfully
   * @throws [TransactionSubmitFailedException] when submission failed
   */
  actual suspend fun submitTransaction(signedTransaction: Transaction) {}
}
