# Kotlin Wallet SDK [![javadoc](https://javadoc.io/badge2/org.stellar/wallet-sdk/dokka.svg?logo=kotlin)](https://javadoc.io/doc/org.stellar/wallet-sdk) [![Maven Central](https://img.shields.io/maven-central/v/org.stellar/wallet-sdk?color=success&logo=apache-maven)](https://search.maven.org/artifact/org.stellar/wallet-sdk)

Kotlin Wallet SDK is a library that allows developers to build wallet applications on the Stellar network faster. It
utilizes [Java Stellar SDK](https://github.com/stellar/java-stellar-sdk) to communicate with a Stellar Horizon server.

## Dependency
The library is hosted on the [Maven Central](https://search.maven.org/artifact/org.stellar/wallet-sdk).
To import `wallet-sdk` library you need to add following dependencies to your code:  

Maven:
```pom
<dependency>
  <groupId>org.stellar</groupId>
  <artifactId>wallet-sdk</artifactId>
  <version>0.1.0</version>
</dependency>
```

Gradle:
```kotlin
implementation("org.stellar:wallet-sdk:0.1.0")
```
### Using snapshots
<details>

This library is currently in the active development. Snapshot from the latest `main` is uploaded periodically to the 
snapshot repository. To use it, please update your version to the latest snapshot (`0.2.0-SNAPSHOT`) and configure snapshot repository: 

Maven:
```pom
<repositories>
    <repository>
        <id>snapshots-repo</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

Gradle:
```kotlin
repositories {
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}
```
</details>

## Wallet

```kotlin
class Wallet(
    private val server: Server,
    private val network: Network,
    private val maxBaseFeeInStroops: Int = 100
)
```

Example

```kotlin
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.Wallet

// Using testnet configuration
val server = Server("https://horizon-testnet.stellar.org")
val network = Network.TESTNET
val wallet = Wallet(server, network)
```

### `Wallet.create()`

```kotlin
fun create(): AccountKeypair

data class AccountKeypair(
    val publicKey: String,
    val secretKey: String
)
```

Example

```kotlin
val newAccountKeypair = wallet.create()

// Addresses are shortened for example only
// publicKey=GB7S...GETPQ, secretKey=SBTL...GI27
```

### `Wallet.fund()`

```kotlin
suspend fun fund(
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
suspend fun addAssetSupport(
    sourceAddress: String,
    assetCode: String,
    assetIssuer: String,
    trustLimit: String = Long.MAX_VALUE
        .toBigDecimal()
        .movePointLeft(7)
        .toPlainString(),
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
suspend fun removeAssetSupport(
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
suspend fun addAccountSigner(
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
suspend fun removeAccountSigner(
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
suspend fun submitTransaction(
    signedTransaction: Transaction,
)
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

### `Wallet.signWithRecoveryServers()`

```kotlin
suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: String,
    recoveryServers: List<RecoveryServerAuth>,
    base64Decoder: ((String) -> ByteArray)? = null
): Transaction

data class RecoveryServerAuth(
    val endpoint: String,
    val signerAddress: String,
    val authToken: String
)
```

Note: `base64Decoder` by default uses `java.util.Base64`. If you need to support Android API level 22 and below, you can
provide your own `base64Decorator` (for example, `android.util.Base64`).

Example

```kotlin
val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val transaction = wallet.addAssetSupport(sourceAddress, "USDC", assetIssuer)

wallet.signWithRecoveryServers(
    transaction = transaction,
    accountAddress = sourceAddress,
    recoveryServers =
    listOf(
        RecoveryServerAuth(
            endpoint = recoveryServer1.endpoint,
            signerAddress = recoveryServer1.stellarAddress,
            authToken = authToken1
        ),
        RecoveryServerAuth(
            endpoint = recoveryServer2.endpoint,
            signerAddress = recoveryServer2.stellarAddress,
            authToken = authToken2
        )
    ),
)
```

### `Wallet.enrollWithRecoveryServer()`

```kotlin
suspend fun enrollWithRecoveryServer(
    recoveryServers: List<RecoveryServer>,
    accountAddress: String,
    accountIdentity: List<RecoveryAccountIdentity>,
    walletSigner: WalletSigner
): List<String>
```

Example

```kotlin
val accountAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val recoveryServerOne = RecoveryServer(
    endpoint = "",
    authEndpoint = "",
    stellarAddress = "",
    homeDomain = ""
)
val recoveryServerTwo = RecoveryServer(
    endpoint = "",
    authEndpoint = "",
    stellarAddress = "",
    homeDomain = ""
)

wallet.enrollWithRecoveryServer(
    recoveryServers = listOf(recoveryServerOne, recoveryServerTwo),
    accountAddress = accountAddress,
    accountIdentity =
    listOf(
        RecoveryAccountIdentity(
            // Role can be "owner", "sender", or "receiver"
            role = "owner",
            auth_methods =
            // Account auth methods (phone number, email, etc)
            listOf(
                RecoveryAccountAuthMethod(
                    type = "phone_number",
                    value = "1231231234"
                )
            )
        )
    ),
    walletSigner = accountWalletSigner
)
```

### `Wallet.registerRecoveryServerSigners()`

```kotlin
suspend fun registerRecoveryServerSigners(
    accountAddress: String,
    accountSigner: List<AccountSigner>,
    accountThreshold: AccountThreshold,
    sponsorAddress: String = ""
): Transaction
```

Example

```kotlin
val accountAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val deviceAddress = "GAN7KZMNUEWWN7E35VGGST72QSA2YWA34XWJ55H7CL6MFLR6WPJVEA2U"
val recoveryServerOneAddress = "GBQRAFYEZOW36SUIPGZQEJUBVMULVGFJLSR7HRQAUO2ZIJDB6JFC3Q7D"
val recoveryServerTwoAddress = "GDHSJGZTRRCQQQGHKULJJHPZDF4MLM2U3HTOLIOJTRWFDXSFTQY2RP23"
val signerMasterWeight = 20
val signerRecoveryWeight = 10

wallet.registerRecoveryServerSigners(
    accountAddress = accountAddress,
    accountSigner = listOf(
        AccountSigner(address = recoveryServerOneAddress, weight = signerRecoveryWeight),
        AccountSigner(address = recoveryServerTwoAddress, weight = signerRecoveryWeight),
        AccountSigner(address = deviceAddress, weight = signerMasterWeight)
    ),
    accountThreshold =
    AccountThreshold(
        low = signerMasterWeight,
        medium = signerMasterWeight,
        high = signerMasterWeight
    ),
)
```

### `Wallet.createRecoverableWallet()`

```kotlin
suspend fun createRecoverableWallet(
    accountAddress: String,
    deviceAddress: String,
    accountThreshold: AccountThreshold,
    accountIdentity: List<RecoveryAccountIdentity>,
    recoveryServers: List<RecoveryServer>,
    accountWalletSigner: WalletSigner,
    signerWeight: SignerWeight,
    sponsorAddress: String = ""
): Transaction
```

Example

```kotlin
val accountAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
val deviceAddress = "GAN7KZMNUEWWN7E35VGGST72QSA2YWA34XWJ55H7CL6MFLR6WPJVEA2U"
val signerMasterWeight = 20
val signerRecoveryWeight = 10
val recoveryServerOne = RecoveryServer(
    endpoint = "",
    authEndpoint = "",
    stellarAddress = "",
    homeDomain = ""
)
val recoveryServerTwo = RecoveryServer(
    endpoint = "",
    authEndpoint = "",
    stellarAddress = "",
    homeDomain = ""
)

wallet.createRecoverableWallet(
    accountAddress = accountAddress,
    deviceAddress = deviceAddress,
    // Account thresholds
    accountThreshold =
    AccountThreshold(
        low = signerMasterWeight,
        medium = signerMasterWeight,
        high = signerMasterWeight
    ),
    accountIdentity =
    // Account identity (can be multiple) to be registered with recovery server
    listOf(
        RecoveryAccountIdentity(
            // Role can be "owner", "sender", or "receiver"
            role = "owner",
            auth_methods =
            // Account auth methods (phone number, email, etc)
            listOf(
                RecoveryAccountAuthMethod(
                    type = "phone_number",
                    value = "1231231234"
                )
            )
        )
    ),
    // Recovery server information
    recoveryServers = listOf(recoveryServerOne, recoveryServerTwo),
    accountWalletSigner = accountWalletSigner,
    // Account signer weights
    signerWeight =
    SignerWeight(master = signerMasterWeight, recoveryServer = signerRecoveryWeight)
)
```

### `Wallet.lockAccountMasterKey()`

```kotlin
suspend fun lockAccountMasterKey(
    accountAddress: String,
    sponsorAddress: String = ""
): Transaction 
```

Example

```kotlin
val accountAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"

wallet.lockAccountMasterKey(
    accountAddress = accountAddress,
)
```

## Auth

```kotlin
class Auth(
    private val accountAddress: String,
    private val webAuthEndpoint: String,
    private val homeDomain: String,
    private val memoId: String? = null,
    private val clientDomain: String? = null,
    private val networkPassPhrase: String = Network.TESTNET.toString(),
    private val walletSigner: WalletSigner,
    private val httpClient: OkHttpClient = OkHttpClient()
) 
```

### `Auth.authenticate()`

```kotlin
fun authenticate(): String
```

Example

```kotlin
val authToken = Auth(
    accountAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22",
    webAuthEndpoint = "https://testanchor.stellar.org/auth",
    homeDomain = "testanchor.stellar.org",
    walletSigner = WalletSigner()
).authenticate()
```