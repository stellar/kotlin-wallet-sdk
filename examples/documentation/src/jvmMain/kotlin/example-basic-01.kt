// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleBasic01

import org.stellar.sdk.Network

import org.stellar.walletsdk.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import java.time.Duration

val wallet = Wallet(StellarConfiguration.Testnet)

val walletMainnet = Wallet(StellarConfiguration(Network.PUBLIC, "https://horizon.stellar.org"))

val walletCustom = Wallet(
  StellarConfiguration.Testnet,
  ApplicationConfiguration { defaultRequest { url { protocol = URLProtocol.HTTP } } }
)

val walletCustomClient =
  Wallet(
    StellarConfiguration.Testnet,
    ApplicationConfiguration(
      defaultClientConfig = {
        engine { this.config { this.connectTimeout(Duration.ofSeconds(10)) } }
        install(HttpRequestRetry) {
          retryOnServerErrors(maxRetries = 5)
          exponentialDelay()
        }
      }
    )
  )

val recoveryCustomClient =
  walletCustomClient.recovery {
    engine { this.config { this.connectTimeout(Duration.ofSeconds(30)) } }
  }

fun closeWallet() {
  wallet.close()
}
