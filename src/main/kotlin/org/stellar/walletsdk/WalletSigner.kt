package org.stellar.walletsdk

import org.stellar.sdk.Transaction

interface WalletSigner {
  /**
   * * Sign client domain transaction on Demo Wallet backend
   *
   * @return
   */
  fun signTransaction(txn: Transaction): Transaction

  /**
   * * Sign client domain transaction
   *
   * @param transactionString
   * @param networkPassphrase
   * @return the signed transaction
   */
  fun signClientDomainTransaction(transactionString: String, networkPassPhrase: String): Transaction
}
