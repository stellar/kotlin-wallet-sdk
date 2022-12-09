package org.stellar.walletsdk.auth

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.PublicKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign

/** Interface to provide wallet signer methods. */
interface WalletSigner {
  fun signWithClientAccount(txn: Transaction, account: AccountKeyPair): Transaction

  fun signWithDomainAccount(
    transactionString: String,
    networkPassPhrase: String,
    account: AccountKeyPair
  ): Transaction

  companion object Default : WalletSigner {
    override fun signWithClientAccount(txn: Transaction, account: AccountKeyPair): Transaction {
      return when (account) {
        is SigningKeyPair -> txn.sign(account)
        is PublicKeyPair ->
          throw IllegalArgumentException("Can't sign with provided public keypair")
      }
    }

    override fun signWithDomainAccount(
      transactionString: String,
      networkPassPhrase: String,
      account: AccountKeyPair
    ): Transaction {
      throw NotImplementedError("This signer can't sign transaction with domain")
    }
  }
}
