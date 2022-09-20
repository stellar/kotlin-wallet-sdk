package org.stellar.walletsdk

import org.stellar.sdk.Transaction

interface WalletSigner {
  fun signWithClientAccount(txn: Transaction): Transaction

  fun signWithDomainAccount(transactionString: String, networkPassPhrase: String): Transaction
}
