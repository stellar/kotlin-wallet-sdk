# Kotlin Wallet SDK

Kotlin Wallet SDK is a library that allows developers to build wallet applications on the Stellar network faster. It
utilizes [Java Stellar SDK](https://github.com/stellar/java-stellar-sdk) to communicate with a Stellar Horizon server.

## Wallet

`class Wallet(horizonUrl: String, networkPassphrase: String)`

Example

```kotlin
import org.stellar.sdk.Network

// Using Horizon testnet configuration
val wallet = Wallet("https://horizon-testnet.stellar.org", Network.TESTNET.toString())
```

### `Wallet.create()`

`fun create(): AccountKeypair(val publicKey: String, val secretKey: String)`

Example

```kotlin
val newAccountKeypair = wallet.create()

// Addresses are shortened for example only
// publicKey=GB7S...GETPQ, secretKey=SBTL...GI27
```