package org.stellar.walletsdk.auth

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.sdk.Network
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.PublicKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign

/** Interface to provide wallet signer methods. */
interface WalletSigner {
  fun signWithClientAccount(txn: Transaction, account: AccountKeyPair): Transaction

  suspend fun signWithDomainAccount(
    transactionXDR: String,
    networkPassPhrase: String,
    account: AccountKeyPair
  ): Transaction

  open class DefaultSigner : WalletSigner {
    override fun signWithClientAccount(txn: Transaction, account: AccountKeyPair): Transaction {
      return when (account) {
        is SigningKeyPair -> txn.sign(account)
        is PublicKeyPair ->
          throw IllegalArgumentException("Can't sign with provided public keypair")
      }
    }

    override suspend fun signWithDomainAccount(
      transactionXDR: String,
      networkPassPhrase: String,
      account: AccountKeyPair
    ): Transaction {
      throw NotImplementedError("This signer can't sign transaction with domain")
    }
  }

  /**
   * Wallet signer that supports signing with a client domain using standard [SigningData] request
   * and response type.
   *
   * @constructor Create empty Json http signer
   * @property url url to which requests should be made
   * @property requestTransformer optional transformer of the default request. Can be used for
   * authentication purposes, etc.
   */
  class DomainSigner(val url: String, val requestTransformer: HttpRequestBuilder.() -> Unit = {}) :
    DefaultSigner() {
    val client = HttpClient() { install(ContentNegotiation) { json() } }

    @Serializable
    data class SigningData(
      val transaction: String,
      @SerialName("network_passphrase") val networkPassphrase: String
    )

    override suspend fun signWithDomainAccount(
      transactionXDR: String,
      networkPassPhrase: String,
      account: AccountKeyPair
    ): Transaction {
      val response: SigningData =
        client
          .post(url) {
            contentType(ContentType.Application.Json)
            setBody(SigningData(transactionXDR, networkPassPhrase))
            requestTransformer()
          }
          .body()

      return Transaction.fromEnvelopeXdr(response.transaction, Network(networkPassPhrase))
        as Transaction
    }
  }
}
