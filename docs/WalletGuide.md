# Wallet SDK usage guide

<!--- TOC -->

* [Getting started](#getting-started)
  * [Configuring client](#configuring-client)
  * [Closing resources](#closing-resources)
* [Build on Stellar](#build-on-stellar)
  * [Account service](#account-service)
  * [Transaction builder](#transaction-builder)
  * [Submit transaction](#submit-transaction)
  * [Transaction builder (extra)](#transaction-builder-extra)
  * [Transaction builder (sponsoring)](#transaction-builder-sponsoring)
  * [Sponsoring account creation](#sponsoring-account-creation)
  * [Sponsoring account creation and modification](#sponsoring-account-creation-and-modification)
  * [Fee bump transaction](#fee-bump-transaction)
  * [Using XDR to exchange transaction data between server and client](#using-xdr-to-exchange-transaction-data-between-server-and-client)
* [Anchor](#anchor)
  * [Add client domain signing](#add-client-domain-signing)
    * [Example](#example)
* [Recovery](#recovery)
  * [Create recovery class](#create-recovery-class)
  * [Create recoverable account](#create-recoverable-account)
  * [Get account info](#get-account-info)
  * [Recover account](#recover-account)
    * [Prepare for recovery](#prepare-for-recovery)
    * [Recover](#recover)
  * [Sample project](#sample-project)

<!--- END -->

<!--- INCLUDE
import org.stellar.walletsdk.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import java.time.Duration
-->

## Getting started

First, root Wallet object should be created. This is a core class, that provides all functionality
available in the current SDK. Later, it will be shown, how to use wallet object to access
methods.  
It's advised to have a singleton wallet object shared across the app.  
Creating wallet with default configuration connected to testnet is simple:

<!--- INCLUDE .*basic.*
import org.stellar.sdk.Network
-->

```kotlin
val wallet = Wallet(StellarConfiguration.Testnet)
```

The wallet instance can be further configured. For example, to connect to public main network:

```kotlin
val walletMainnet = Wallet(StellarConfiguration(Network.PUBLIC, "https://horizon.stellar.org"))
```

There is one more available configuration for wallet that allows to configure internal logic of the SDK.  
For example, to test with local servers on http protocol, http can be manually enabled.

```kotlin
val walletCustom = Wallet(
  StellarConfiguration.Testnet,
  ApplicationConfiguration { defaultRequest { url { protocol = URLProtocol.HTTP } } }
)
```

### Configuring client
The Wallet SDK uses the [ktor client](https://ktor.io/docs/getting-started-ktor-client.html) for all network requests 
(excluding Horizon, where the Stellar SDK's http client is used). Currently, the okhttp engine is configured to be used with the client.  
You can read more about how to configure the ktor client [here](https://ktor.io/docs/create-client.html#configure-client).
For example, the client can be globally configured:
```kotlin
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
```
This code will set the connect timeout to 10 seconds via the
[okhttp configuration](https://ktor.io/docs/http-client-engines.html#okhttp) 
and also installs the [retry plugin](https://ktor.io/docs/client-retry.html).
You can also specify client configuration for specific Wallet SDK classes. For example, to change connect timeout when connecting to some anchor 
server:
```kotlin
val anchorCustomClient =
  walletCustomClient.anchor("example.com") {
    engine { this.config { this.connectTimeout(Duration.ofSeconds(30)) } }
  }
```

### Closing resources
After the wallet class is no longer used, it's necessary to close all clients used by it. While in some applications it may 
not be required (e.g. the wallet lives for the whole lifetime of the app), in other cases it can be required.  
If your wallet class is short-lived, it's recommended to close client resources using close function:
```kotlin
fun closeWallet() {
  wallet.close()
}
```

> You can get the full code [here](../examples/documentation/src/example-basic-01.kt).

<!--- INCLUDE
val wallet = Wallet(StellarConfiguration.Testnet)
-->

## Build on Stellar

Once the Wallet is configured, you can use the following:

- transaction builder to create transactions (for example, fund the account or add asset support),
- account service to generate account keypair or get account information, and
- submit a signed transaction to Stellar network.

### Account service

<!--- INCLUDE .*account.*
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.walletsdk.*
-->
<!--- SUFFIX .*account.*
suspend fun main() {
  val info = getAccountInfo()
  println(info)

  val history = getAccountHistory()
  println(history)
}
-->

Generate new account keypair (public and secret keys).

```kotlin
val account = wallet.stellar().account()
val accountKeyPair = account.createKeyPair()
```

Get account information from the Stellar network (assets, liquidity pools, and reserved native balance).

```kotlin
suspend fun getAccountInfo(): AccountInfo {
  return account.getInfo(accountKeyPair.address)
}
```

Get account history (all operations) from the Stellar network.

```kotlin
suspend fun getAccountHistory(): List<WalletOperation<OperationResponse>> {
  return account.getHistory(accountKeyPair.address)
}
```

> You can get the full code [here](../examples/documentation/src/example-account-01.kt).

### Transaction builder

Transaction builder allows you to create various transactions that can be signed and submitted to the Stellar network.
Some transactions can be sponsored.

<!--- INCLUDE .*transaction.*
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
val stellar = wallet.stellar()
-->
<!--- SUFFIX .*transaction-01.*
suspend fun main() {
  val fundTxn = createAccount()
  println(fundTxn)

  val lockMasterKeyTxn = lockMasterKey()
  println(lockMasterKeyTxn)

  val addAssetTxn = addAsset()
  println(addAssetTxn)

  val removeAssetTxn = removeAsset()
  println(removeAssetTxn)

  val addSignerTxn = addSigner()
  println(addSignerTxn)

  val removeSignerTxn = removeSigner()
  println(removeSignerTxn)

  val signAndSubmitTxn = signAndSubmit()
  println(signAndSubmitTxn)
}
-->

```kotlin
val sourceAccountKeyPair = account.createKeyPair()
val destinationAccountKeyPair = account.createKeyPair()
```

Create account transaction activates/creates an account with a starting balance (by default, it's 1 XLM). This transaction
can be sponsored.

```kotlin
suspend fun createAccount(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).createAccount(destinationAccountKeyPair).build()
}
```

Lock the master key of the account (set its weight to 0). Use caution when locking the account's master key. Make sure
you have set the correct signers and weights. Otherwise, you will lock the account irreversibly. This transaction can be
sponsored.

```kotlin
suspend fun lockMasterKey(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).lockAccountMasterKey().build()
}
```

Add an asset (trustline) to the account. This allows account to receive the asset. This transaction can be sponsored.

```kotlin
val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

suspend fun addAsset(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).addAssetSupport(asset).build()
}
```

Remove an asset from the account (the balance must be 0)

```kotlin
suspend fun removeAsset(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).removeAssetSupport(asset).build()
}
```

Add a new signer to the account. Use caution when adding new signers. Make sure you set the correct signer weight.
Otherwise, you will lock the account irreversibly. This transaction can be sponsored.

```kotlin
val newSignerKeyPair = account.createKeyPair()

suspend fun addSigner(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).addAccountSigner(newSignerKeyPair, 10).build()
}
```

Remove a signer from the account.

```kotlin
suspend fun removeSigner(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).removeAccountSigner(newSignerKeyPair).build()
}
```

Modify account thresholds (usefully with multiple signers assigned to the account). Allows to restrict access to certain 
operations when limit is not reached.
```kotlin
suspend fun setThreshold(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).setThreshold(low = 1, medium = 10, high = 30).build()
}
```

### Submit transaction

Submit a signed transaction to the Stellar network. A sponsored transaction must be signed by both the account and the
sponsor.

```kotlin
suspend fun signAndSubmit() {
  val signedTxn = createAccount().sign(sourceAccountKeyPair)
  wallet.stellar().submitTransaction(signedTxn)
}
```

> You can get the full code [here](../examples/documentation/src/example-transaction-01.kt).


### Transaction builder (extra)
In some more cases private key may not be known prior to forming a transaction. For example, creating new account requires it to be funded.
Wallet may not have a key of an account with funds and may request such transaction to be sponsored by third-party.  
Let's walk through that flow:
```kotlin
// Third-party key that will sponsor creating new account
val externalKeyPair = "MySponsorAddress".toPublicKeyPair()
val newKeyPair = account.createKeyPair()
```
First, account must be created
```kotlin
suspend fun makeCreateTx(): Transaction {
  return stellar.transaction(externalKeyPair).createAccount(newKeyPair).build()
}
```
This transaction must be sent to external signer (holder of `externalKeyPair`) to be signed. Signed transaction can 
be submitted by wallet 
```kotlin
suspend fun submitCreateTx(signedCreateTx: Transaction) {
  wallet.stellar().submitTransaction(signedCreateTx)
}
```
Now, after account is created, it can perform operations. For example, we can disable master key pair 
and replace it with a new one (let's call it device key pair).
```kotlin
suspend fun addDeviceKeyPair() {
  val deviceKeyPair = account.createKeyPair()

  val modifyAccountTransaction =
    stellar
      .transaction(newKeyPair)
      .addAccountSigner(
        deviceKeyPair,
        signerWeight = 1,
      )
      .lockAccountMasterKey()
      .build()
      .sign(newKeyPair)

  wallet.stellar().submitTransaction(modifyAccountTransaction)
}
```

> You can get the full code [here](../examples/documentation/src/example-transaction-02.kt).

### Transaction builder (sponsoring)
<!--- INCLUDE .*transaction-\d.*
val sponsorKeyPair = SigningKeyPair.fromSecret("MySecred")
-->

<!--- INCLUDE .*transaction-03.*
val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")
val sponsoredKeyPair = SigningKeyPair.fromSecret("Sponsored")
-->

Some operations, that modify account reserves can be 
[sponsored](https://developers.stellar.org/docs/encyclopedia/sponsored-reserves#sponsored-reserves-operations). For 
sponsored operations, sponsoring account will be paying for reserves, instead of the account that being sponsored.  
This allows to do some operations, even if account doesn't have enough funds to perform such operations.  
To sponsor a transaction, simply start `sponsoring` block:

```kotlin
suspend fun sponsorOperation() {
  val transaction =
    stellar
      .transaction(sponsoredKeyPair)
      .sponsoring(sponsorKeyPair) { addAssetSupport(asset) }
      .build()

  transaction.sign(sponsorKeyPair).sign(sponsoredKeyPair)
}
```
Only some operations can be sponsored, and sponsoring block has a slightly different set of functions available, 
compared to regular `TransactionBuilder`.  
Note, that transaction must be signed by both sponsor account (`sponsoringKeyPair`) and account being 
sponsored (`sponsoredKeyPair`)

> You can get the full code [here](../examples/documentation/src/example-transaction-03.kt).

### Sponsoring account creation
One of things that can be done via sponsoring is to create account having 0 starting balance. This account creation can
be created simply writing:
```kotlin
suspend fun sponsorAccountCreation() {
  val newKeyPair = account.createKeyPair()

  val transaction =
    stellar
      .transaction(sponsorKeyPair)
      .sponsoring(sponsorKeyPair, sponsoredAccount = newKeyPair) { createAccount(newKeyPair) }
      .build()

  transaction.sign(sponsorKeyPair).sign(newKeyPair)
}
```
Note how transaction source account should be set to `sponsorKeyPair`. This time, we need to pass sponsored account.
In the example above, it was omitted and was default to the transaction source account (`sponsoredKey`). However, this 
time sponsored account (freshly created) is different from the transaction source account. Therefore, it's necessary to
specify it. Otherwise, transaction will contain malformed operation. As before, transaction must be signed by 
both keys.  

> You can get the full code [here](../examples/documentation/src/example-transaction-04.kt).

### Sponsoring account creation and modification
If you want to create account and modify it in one transaction, it's possible to do so with passing `sponsoredAccount` 
optional argument to the sponsored block. If this argument is present, all operations inside sponsored block will be 
sourced by `sponsoredAccount`. (Except account creation, which is always sourced by sponsor).

```kotlin
suspend fun sponsorAccountCreationAndModification() {
  val newKeyPair = account.createKeyPair()
  val replaceWith = account.createKeyPair()

  val transaction =
    stellar
        .transaction(sponsorKeyPair)
        .sponsoring(sponsorKeyPair, newKeyPair) {
          createAccount(newKeyPair)
          addAccountSigner(replaceWith, 1)
          lockAccountMasterKey()
        }
        .build()

  transaction.sign(sponsorKeyPair).sign(newKeyPair)
}
```

> You can get the full code [here](../examples/documentation/src/example-transaction-05.kt).
> 
### Fee bump transaction
<!--- INCLUDE .*transaction-06.*
val sponsoredKeyPair = SigningKeyPair.fromSecret("Sponsored")

suspend fun sponsorAccountModification() {
-->

<!--- SUFFIX .*transaction-06.*
}
-->

If you wish to modify newly created account with 0 balance, it's also possible to do so via `FeeBump`. It can be combined 
with sponsoring block to achieve the same result as in the example above. However, with `FeeBump` it's also possible to add 
more operations (not sponsored), such as transfer.

First, let's create a transaction that will replace master key of an account with a new key pair.
```kotlin
  val replaceWith = account.createKeyPair()

  val transaction =
    stellar
      .transaction(sponsoredKeyPair)
      .sponsoring(sponsorKeyPair) {
        lockAccountMasterKey()
        addAccountSigner(replaceWith, signerWeight = 1)
      }
      .build()
```
Second, sign transaction with both keys
```kotlin
  transaction.sign(sponsoredKeyPair).sign(sponsorKeyPair)
```
Next, create a fee bump, targeting the transaction
```kotlin
  val feeBump = stellar.makeFeeBump(sponsorKeyPair, transaction)
  feeBump.sign(sponsorKeyPair)
```
Finally, submit a fee bump transaction. Executing this transaction will be fully covered by `sponsorKeyPair` and 
`sponsoredKeyPair` may not even have any XLM funds on its account.
```kotlin
  wallet.stellar().submitTransaction(feeBump)
```

> You can get the full code [here](../examples/documentation/src/example-transaction-06.kt).

### Using XDR to exchange transaction data between server and client
Note, that wallet may not have a signing key for `sponsorKeyPair`. In that case, it's necessary to convert transaction
to XDR, send it to the server, containing `sponsorKey` and return signed transaction back to the wallet.  
Let's use previous example of sponsoring account creation, but this time with sponsor key not being known to the wallet.  
First step would be defining public key of sponsor key pair:
```kotlin
val sponsorKeyPair = "SponsorAddress".toPublicKeyPair()
```
Next, create an account in the same manner as before. Sign it with `newKeyPair`. This time, convert transaction to XDR:
```kotlin
suspend fun sponsorAccountCreation(): String {
  val newKeyPair = account.createKeyPair()

  return stellar
    .transaction(sponsorKeyPair)
    .sponsoring(sponsorKeyPair) { createAccount(newKeyPair) }
    .build()
    .sign(newKeyPair)
    .toEnvelopeXdrBase64()
}

```
It can now be sent to the server. On the server, sign with a private key for sponsor address:
```kotlin
// On the server
fun signTransaction(xdrString: String): String {
  val sponsorPrivateKey = SigningKeyPair.fromSecret("MySecred")

  val signedTransaction = stellar.decodeTransaction(xdrString).sign(sponsorPrivateKey)

  return signedTransaction.toEnvelopeXdrBase64()
}
```
When client receives fully signed transaction, it can be decoded and sent to the Stellar network:
```kotlin
suspend fun recoverSigned(xdrString: String) {
  val signedTransaction = stellar.decodeTransaction(xdrString)

  stellar.submitTransaction(signedTransaction)
}
```

> You can get the full code [here](../examples/documentation/src/example-transaction-xdr-01.kt).

## Anchor

<!--- INCLUDE .*anchor.*
import io.ktor.http.Url
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.AnchorServiceInfo
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.toml.TomlInfo

val wallet = Wallet(StellarConfiguration.Testnet)
val accountKeyPair = wallet.stellar().account().createKeyPair()
-->
<!--- SUFFIX .*anchor.*
suspend fun main() {
  val anchorInfo = anchorToml()
  println(anchorInfo)

  val authToken = getAuthToken()
  println(authToken)

  val anchorServices = getAnchorServices()
  println(anchorServices)

  val depositUrl = interactiveDeposit()
  println(depositUrl)

  val withdrawalUrl = interactiveWithdrawal()
  println(withdrawalUrl)

  val transactionInfo = anchorTransaction()
  println(transactionInfo)

  val history = accountHistory()
  println(history)
}
-->

Build on and off ramps with anchors for deposits and withdrawals.

```kotlin
val anchor = wallet.anchor("https://testanchor.stellar.org")
```

Get anchor information from a TOML file.

```kotlin
suspend fun anchorToml(): TomlInfo {
  return anchor.getInfo()
}
```

Authenticate an account with the anchor using SEP-10.

```kotlin
suspend fun getAuthToken(): AuthToken {
  return anchor.auth().authenticate(accountKeyPair)
}
```

Available anchor services and information about them. For example, interactive deposit/withdrawal limits, currency,
fees, payment methods.

```kotlin
suspend fun getAnchorServices(): AnchorServiceInfo {
  return anchor.getServicesInfo()
}
```

Interactive deposit and withdrawal.

```kotlin
val asset = IssuedAssetId("SRT", "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")

suspend fun interactiveDeposit(): String {
  return anchor.interactive().deposit(accountKeyPair.address, asset, getAuthToken()).url
}

suspend fun interactiveWithdrawal(): String {
  return anchor.interactive().withdraw(accountKeyPair.address, asset, getAuthToken()).url
}
```

Get single transaction's current status and details.

```kotlin
suspend fun anchorTransaction(): AnchorTransaction {
  return anchor.getTransaction("12345", getAuthToken())
}
```

Get account transactions for specified asset.

```kotlin
suspend fun accountHistory(): List<AnchorTransaction> {
  return anchor.getHistory(asset, getAuthToken())
}
```

> You can get the full code [here](../examples/documentation/src/example-anchor-01.kt).

### Add client domain signing

One of the features being supported
by [SEP-10 Authentication](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md)
is verifying client domain. This enables anchor to recognize that user request was made using a specific client.

Functionality of signing with client domain is enabled
by [WalletSigner](../wallet-sdk/src/main/kotlin/org/stellar/walletsdk/auth/WalletSigner.kt). You can use a `DefaultSigner`
class as a baseline for your implementation. The main method that needs to be implemented is `signWithDomainAccount`.
Implementation should make a request to backend server containing client domain private key. Server returns signed
transaction, finishing the flow.

For your wallet signer to be used, it's required to pass it to SDK. It's recommended to set it globally via `ApplicationConfiguration` `defaultSigner`
[option](https://javadoc.io/doc/org.stellar/wallet-sdk/latest/wallet-sdk/org.stellar.walletsdk/-application-configuration/index.html).
Alternatively, it can be passed per `authenticate` call of `Auth` class.

#### Example
<!--- INCLUDE
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
import org.stellar.walletsdk.auth.*
import org.stellar.walletsdk.horizon.AccountKeyPair

val client = HttpClient() { install(ContentNegotiation) { json() } }
-->
In the example below, [Stellar demo wallet](https://demo-wallet.stellar.org) will be used as a client domain. Server-side
implementation, responsible for signing transaction can be found [here](https://github.com/stellar/stellar-demo-wallet/tree/master/packages/demo-wallet-server).  
First, add definition of request/response object for server requests:
```kotlin
@Serializable
data class SigningData(
  val transaction: String,
  @SerialName("network_passphrase") val networkPassphrase: String
)
```
Next, create a class extending `DefaultSigner`

```kotlin
class DemoWalletSigner: WalletSigner.DefaultSigner()
```
<!--- INCLUDE
{
-->
After that add implementation for `signWithDomainAccount` function
```kotlin
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
```
<!--- INCLUDE
}
-->
Wallet can now use this class:
```kotlin
val wallet = Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(DemoWalletSigner()))
```
<!--- INCLUDE
val anchor = wallet.anchor("https://testanchor.stellar.org")
-->
It can now be used for autentication with client domain:
```kotlin
suspend fun authenticate(): AuthToken {
  return anchor
    .auth()
    .authenticate(
      wallet.stellar().account().createKeyPair(),
      clientDomain = "demo-wallet-server.stellar.org"
    )
}
```

<!--- INCLUDE
suspend fun main() {
  println("Token for auth with client domain: ${authenticate()}")
  client.close()
}
-->
> You can get the full code [here](../examples/documentation/src/example-client-domain-01.kt).

## Recovery

<!--- INCLUDE .*recovery.*
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.asset.XLM
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.horizon.*
import org.stellar.walletsdk.recovery.*

val wallet = Wallet.Testnet
-->

Use [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md) to create recoverable accounts and restore device keys using recovery servers.  

### Create recovery class
First, we need to define recovery server that will be used. For simplicity, there is a key assigned to each server, so they can be referenced later. 

```kotlin
val first = RecoveryServerKey("first")
val second = RecoveryServerKey("second")
val firstServer = RecoveryServer("recovery.example.com", "auth.example.com", "example.com")
val secondServer = RecoveryServer("recovery2.example.com", "auth2.example.com", "example.com")
val servers = mapOf(first to firstServer, second to secondServer)
val recovery = wallet.recovery(servers)
```

> You can get the full code [here](../examples/documentation/src/example-recovery-01.kt).

<!--- INCLUDE .*recovery.*
val first = RecoveryServerKey("first")
val second = RecoveryServerKey("second")
val firstServer = RecoveryServer("recovery.example.com", "auth.example.com", "example.com")
val secondServer = RecoveryServer("recovery2.example.com", "auth2.example.com", "example.com")
val servers = mapOf(first to firstServer, second to secondServer)
val recovery = wallet.recovery(servers)
val sponsor = SigningKeyPair.fromSecret("<example key>")
-->

### Create recoverable account

<!--- INCLUDE 
suspend fun createAccount() {
-->

<!--- SUFFIX 
}
-->

First, let's create an account key and a device key that will be attached to this account
```kotlin
  val account = wallet.stellar().account().createKeyPair()
  val deviceKey = wallet.stellar().account().createKeyPair()
```

Next, we need to define SEP-30 identities. In this example we are going to use the same list of identities on both recovery servers. 
```kotlin
  val recoveryKey = wallet.stellar().account().createKeyPair()

  val identities =
    listOf(
      RecoveryAccountIdentity(
        RecoveryRole.OWNER,
        listOf(RecoveryAccountAuthMethod(RecoveryType.STELLAR_ADDRESS, recoveryKey.address))
      )
    )
```
Here, stellar key will be used as a recovery method. Other recovery servers may support email or phone as a recovery methods

You can read more about SEP-30 identities [here](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md#common-request-fields)

Next, let's create a recoverable account:
```kotlin
  val recoverableWallet =
    recovery.createRecoverableWallet(
      RecoverableWalletConfig(
        account,
        deviceKey,
        AccountThreshold(10, 10, 10),
        mapOf(first to identities, second to identities),
        SignerWeight(10, 5),
        sponsor
      )
    )
```
With given parameters, this function will:
1. Create `account` on the Stellar network (if it's not created yet).
2. `sponsor` will be used to sponsor all operations. You may not sponsor creating recoverable wallet, but in that case `account` must already be created.
3. `deviceKey` will be used as a primary account key. Please note that master key belonging to `account` will be locked. `deviceKey` should be used as a primary signer instead.
4. Set all account thresholds to 10. You can read more about threshold in the [documentation](https://developers.stellar.org/docs/encyclopedia/signatures-multisig#thresholds)
5. Use identities that were defined earlier on both servers. (That means, both server will accept SEP-10 authentication via `recoveryKey` as an auth method)
6. Set device key weight to 10, and recovery server weight to 5. Given account thresholds, both servers must be used to recover account, as transaction signed by one will only have weight of 5, which is not sufficient to change account key.

Finally, sign and submit transaction to the network
```kotlin
  val tx = recoverableWallet.transaction.sign(account).sign(sponsor)

  wallet.stellar().submitTransaction(tx)

  val recoverySigners = recoverableWallet.signers
```
Note, that sponsor must sign key, as well as master key of the account. (After transaction is executed, you won't be able to use master key anymore)

After transaction is executed, new account will be created.
1. Address of account will be `account.address`
2. `account` key will not be usable. Transaction signed with this key won't have enough authority.
3. Primary signer with weight 10 will be `deviceKey` key.
4. Recovery signers with weight 5 will be stored in `recoverySigners` list.
5. All reserves for this account are sponsored by `sponsor`.
6. Account will have 0 XLM on its balance.

> You can get the full code [here](../examples/documentation/src/example-recovery-02.kt).

### Get account info

<!--- INCLUDE
val recoveryKey = SigningKeyPair.fromSecret("<recovery key>")
val account = "<account address>".toPublicKeyPair()

suspend fun accountInfo() {
-->

<!--- SUFFIX 
}
-->

You can fetch account info from one or more servers. To do so, first we need to authenticate,
```kotlin
  val auth1 = recovery.sep10Auth(first).authenticate(recoveryKey)
  val auth2 = recovery.sep10Auth(second).authenticate(recoveryKey)
```
This implementation uses SEP-10 as authentication method.

Next, get account info using auth tokens:
```kotlin
  val accountInfo = recovery.getAccountInfo(account, mapOf(first to auth1, second to auth2))

  println("Recoverable info: $accountInfo")
```

The result info will be associated with a respective recovery server key.

> You can get the full code [here](../examples/documentation/src/example-recovery-03.kt).

### Recover account

<!--- INCLUDE
val recoveryKey = SigningKeyPair.fromSecret("<recovery key>")
val account = "<account address>".toPublicKeyPair()
val recoverySigners = listOf("<first signer address>", "<second signer address>")

suspend fun recover() {
-->

<!--- SUFFIX 
}
-->

#### Prepare for recovery
**Note**: This section is when recovery server doesn't support signing sponsored transaction. If your SEP-30 provider supports it, you can skip to [the next part](#prepare-for-recovery)

With no sponsor it's required to refill account to pay for new key reserves:

```kotlin
  val refill =
    wallet
        .stellar()
        .transaction(sponsor)
        .transfer(account.address, XLM, "0.5")
        .build()
        .sign(sponsor)

  wallet.stellar().submitTransaction(refill)
```

#### Recover

First, we need to authenticate with recovery servers,
```kotlin
  val auth1 = recovery.sep10Auth(first).authenticate(recoveryKey)
  val auth2 = recovery.sep10Auth(second).authenticate(recoveryKey)
```
We need to know signers that will be used to sign the transaction. You can get them either on wallet creation step (`recoverySigners` variable), or via fetching account info from recovery serves. 

Next, create a new device key and retrieve a signed key replacement transaction from recovery servers:
```kotlin
  val newKey = wallet.stellar().account().createKeyPair()

  val serverAuth = mapOf(
    first to RecoveryServerSigning(recoverySigners[0], auth1),
    second to RecoveryServerSigning(recoverySigners[1], auth2)
  )

  val signedReplaceKeyTransaction =
    recovery.replaceDeviceKey(
      account,
      newKey,
      serverAuth
    )
```
Calling with function will create a transaction that locks previous device key and replaces it with a new key (having the same weight as an old one). 
Lost device key is deduced automatically. Signer will be considered a device key, if one of these conditions matches:
1. It's the only signer that's not in `serverAuth`
2. All signers in `serverAuth` has the same weight, and potential signer is the only one with a different weight.

Note that account created above will match the first criteria. If 2-3 schema were used, then second criteria would match. (In 2-3 schema, 3 serves are used and 2 of them is enough to recover key. This is a recommended approach.)

Note: you can also use more low-level `signWithRecoveryServers` functions to sign arbitrary transaction.

Finally, it's time to submit transaction. In this example we will use fee bump, as account only has funds to pay for reserves. If your account has more funds available, or replace device key transaction is being sponsored, you can submit `signedReplaceKeyTransaction` transaction directly.

```kotlin
  val feeBumped = wallet.stellar().makeFeeBump(sponsor, signedReplaceKeyTransaction).sign(sponsor)

  wallet.stellar().submitTransaction(feeBumped)
```

> You can get the full code [here](../examples/documentation/src/example-recovery-04.kt).

### Sample project
You can find the sample project in the [examples](../examples/recovery)
