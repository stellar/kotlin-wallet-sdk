package org.stellar.walletsdk.auth

import org.stellar.sdk.Transaction

/** Interface to provide wallet signer methods. */
interface WalletSigner {
  fun signWithClientAccount(txn: Transaction, address: String): Transaction

  fun signWithDomainAccount(
    transactionString: String,
    networkPassPhrase: String,
    address: String
  ): Transaction
}
