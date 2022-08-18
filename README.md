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

```kotlin
fun create(): AccountKeypair(
val publicKey: String,
val secretKey: String)
```

Example

```kotlin
val newAccountKeypair = wallet.create()

// Addresses are shortened for example only
// publicKey=GB7S...GETPQ, secretKey=SBTL...GI27
```

### `Wallet.fund()`

```kotlin
fun fund(
    sourceAddress: String,
    destinationAddress: String,
    startingBalance: String = "1",
    sponsorAddress: String = ""
): Transaction
```

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val destinationAddress = "GDEIYYWIVK24CCQ3Y4QNGEIBEFABTTCFBRTVNQZ43VPOUNQARO7ZEKJY"

val sponsoredFundTransaction =
    wallet.fund(sourceAddress, destinationAddress, sponsorAddress = sourceAddress)
```

## Utils

### `Operation.sponsorOperation()`

```kotlin
fun sponsorOperation(
    sponsorAddress: String,
    accountAddress: String,
    operation: Operation
): List<Operation>
```

### `Transaction.buildTransaction()`

```kotlin
fun buildTransaction(
    sourceAddress: String,
    server: Server,
    network: Network,
    operations: List<Operation>
): Transaction
```