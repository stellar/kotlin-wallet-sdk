package org.stellar.example

import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.sign

object Signer : WalletSigner.DefaultSigner() {
  override suspend fun signWithDomainAccount(
    transactionXDR: String,
    networkPassPhrase: String,
    account: AccountKeyPair
  ): Transaction {
    val tx = Transaction.fromEnvelopeXdr(transactionXDR, Network(networkPassPhrase))
    return tx.sign(accountKp) as Transaction
  }
}
