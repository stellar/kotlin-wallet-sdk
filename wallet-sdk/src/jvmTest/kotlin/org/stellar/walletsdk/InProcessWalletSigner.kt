package org.stellar.walletsdk

import io.ktor.client.call.*
import io.ktor.client.request.*
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.auth.ChallengeResponse
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.Util.postJson

class InProcessWalletSigner : WalletSigner {
  override fun signWithClientAccount(txn: Transaction, account: AccountKeyPair): Transaction {
    return (account as SigningKeyPair).sign(txn)
  }

  override suspend fun signWithDomainAccount(
    transactionXDR: String,
    networkPassPhrase: String,
    account: AccountKeyPair
  ): Transaction {
    val clientDomainRequestParams = ChallengeResponse(transactionXDR, networkPassPhrase)

    val jsonResponse: ChallengeResponse =
      TestWallet.cfg.app.defaultClient.postJson(AUTH_CLIENT_DOMAIN_URL, clientDomainRequestParams)

    return Transaction.fromEnvelopeXdr(
      jsonResponse.transaction,
      Network(jsonResponse.networkPassphrase)
    ) as Transaction
  }
}
