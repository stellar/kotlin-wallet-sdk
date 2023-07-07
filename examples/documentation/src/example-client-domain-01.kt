// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleClientDomain01

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.*
import org.stellar.example.exampleAnchor01.*
import org.stellar.sdk.*
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.auth
import org.stellar.walletsdk.auth.*
import org.stellar.walletsdk.horizon.AccountKeyPair

val client = HttpClient() { install(ContentNegotiation) { json() } }

@Serializable
data class SigningData(
  val transaction: String,
  @SerialName("network_passphrase") val networkPassphrase: String
)

class DemoWalletSigner: WalletSigner.DefaultSigner()
{

  override suspend fun signWithDomainAccount(
    transactionXDR: String,
    networkPassPhrase: String,
    account: AccountKeyPair
  ): Transaction {
    val response: SigningData =
      client
        .post("https://demo-wallet-server.stellar.org/sign") {
          contentType(ContentType.Application.Json)
          setBody(SigningData(transactionXDR, networkPassPhrase))
        }
        .body()

    return Transaction.fromEnvelopeXdr(response.transaction, Network(networkPassPhrase)) as Transaction
  }
}

val wallet = Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(DemoWalletSigner()))
val anchor = wallet.anchor("https://testanchor.stellar.org")

suspend fun authenticate(): AuthToken {
  return anchor
    .auth()
    .authenticate(
      wallet.stellar().account().createKeyPair(),
      clientDomain = "demo-wallet-server.stellar.org"
    )
}
suspend fun main() {
  println("Token for auth with client domain: ${authenticate()}")
  client.close()
}
