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

val fundTransaction = wallet.fund(sourceAddress, destinationAddress)

// Note: In this example `sponsorAddress` is the same as `sourceAddress`, but it doesn't have to be.
val sponsoredFundTransaction =
    wallet.fund(sourceAddress, destinationAddress, sponsorAddress = sourceAddress)
```

### `Wallet.addAssetSupport()`

```kotlin
fun addAssetSupport(
    sourceAddress: String,
    assetCode: String,
    assetIssuer: String,
    trustLimit: String = "",
    sponsorAddress: String = ""
): Transaction
```

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val assetIssuer = "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5"

val transaction = wallet.addAssetSupport(sourceAddress, "USDC", assetIssuer)
```

### `Wallet.removeAssetSupport()`

```kotlin
fun removeAssetSupport(
    sourceAddress: String,
    assetCode: String,
    assetIssuer: String
): Transaction
```

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val assetIssuer = "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5"

val transaction = wallet.removeAssetSupport(sourceAddress, "USDC", assetIssuer)
```

### `Wallet.addAccountSigner()`

```kotlin
fun addAccountSigner(
    sourceAddress: String,
    signerAddress: String,
    signerWeight: Int,
    sponsorAddress: String = ""
): Transaction
```

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val signerAddress = "GD2YC3HSNQEHSOTRCGGPOUN4J3DETJQR4ENPKY5WLF67XBOSVG5OIEQT"

val transaction = wallet.addAccountSigner(sourceAddress, signerAddress, 10)
```

### `Wallet.removeAccountSigner()`

```kotlin
fun removeAccountSigner(
    sourceAddress: String,
    signerAddress: String
): Transaction
```

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val signerAddress = "GD2YC3HSNQEHSOTRCGGPOUN4J3DETJQR4ENPKY5WLF67XBOSVG5OIEQT"

val transaction = wallet.removeAccountSigner(sourceAddress, signerAddress)
```

### `Wallet.submitTransaction()`

```kotlin
fun submitTransaction(
    signedTransaction: Transaction
): SubmitTransactionResponse
```

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val assetIssuer = "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5"

val transaction = wallet.addAssetSupport(sourceAddress, "USDC", assetIssuer)

// Sign transaction using KeyStore
// Validate transaction
// Submit signed transaction to the network

wallet.submitTransaction(transaction)
```

## Utils

### `accountAvailableNativeBalance()`

```kotlin
fun accountAvailableNativeBalance(
    account: AccountResponse
): String
```

### `accountReservedBalance()`

```kotlin
fun accountReservedBalance(
    account: AccountResponse
): String
```

### `buildFeeBumpTransaction()`

```kotlin
fun buildFeeBumpTransaction(
    feeAccount: String,
    innerTransaction: Transaction,
    maxFeeInStroops: Long,
    server: Server
): FeeBumpTransaction
```

### `buildTransaction()`

```kotlin
fun buildTransaction(
    sourceAddress: String,
    server: Server,
    network: Network,
    operations: List<Operation>
): Transaction
```

### `convertLumensStroops()`

```kotlin
fun stroopsToLumens(
    stroops: String
): String

fun lumensToStroops(
    lumens: String
): String
```

### `sponsorOperation()`

```kotlin
fun sponsorOperation(
    sponsorAddress: String,
    accountAddress: String,
    operation: Operation
): List<Operation>
```

### `validateTransaction()`

```kotlin
fun validateTransaction(
    transaction: Transaction,
    server: Server
)
```