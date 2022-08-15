# Kotlin Wallet SDK

Kotlin Wallet SDK is a library that allows developers to build wallet applications on the Stellar network faster. It
utilizes [Java Stellar SDK](https://github.com/stellar/java-stellar-sdk) to communicate with a Stellar Horizon network.

## Wallet

`class Wallet(horizonUrl: String, horizonPassphrase: String)`

Example

```kotlin
// Using Horizon testnet configuration
val wallet = Wallet("https://horizon-testnet.stellar.org", "Test SDF Network ; September 2015")
```

### `Wallet.create()`

`fun create(): AccountKeypair(val publicKey: String, val secretKey: String)`

Example

```kotlin
val newAccountKeypair = wallet.create()

// publicKey=GB7S6OXH4UBA4QLR2OR4IXAOW7KOG67SUBEIXHGMJDNRKADHW7QGETPQ,
// secretKey=SBTLU2GR5FYAQQ63HP65L3HF3M5PNKLX6E2TA2S4T63TZJHZDXE2GI27
```