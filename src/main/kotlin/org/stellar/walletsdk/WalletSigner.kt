package org.stellar.walletsdk

import org.stellar.sdk.Transaction

/** Interface to provide wallet signer methods. */
interface WalletSigner {
  fun signWithClientAccount(txn: Transaction): Transaction

  fun signWithDomainAccount(transactionString: String, networkPassPhrase: String): Transaction
}
