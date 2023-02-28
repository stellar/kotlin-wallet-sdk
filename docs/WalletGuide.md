# Wallet SDK usage guide

<!--- TOC -->

* [Getting started](#getting-started)
* [Build on Stellar](#build-on-stellar)
  * [Account service](#account-service)
  * [Transaction builder](#transaction-builder)
  * [Submit transaction](#submit-transaction)
* [Transaction builder (extra)](#transaction-builder-extra)
* [Transaction builder (sponsoring)](#transaction-builder-sponsoring)
* [Anchor](#anchor)

<!--- END -->

<!--- INCLUDE
import org.stellar.walletsdk.*
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
val walletCustom = Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(useHttp = true))
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
val stellar = wallet.stellar()
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
  return stellar.transaction(sourceAccountKeyPair).addAccountSigner(newSignerKeyPair.address, 10).build()
}
```

Remove a signer from the account.

```kotlin
suspend fun removeSigner(): Transaction {
  return stellar.transaction(sourceAccountKeyPair).removeAccountSigner(newSignerKeyPair.address).build()
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


## Transaction builder (extra)
In some more cases private key may not be known prior to forming a transaction. For example, creating new account requires it to be funded.
Wallet may not have a key of an account with funds and may request such transaction to be sponsored by third-party.  
Let's walk through that flow:
```kotlin
// Third-party key that will sponsor creating new account
val externalKeyPair = "MySponsorAddress".toPublicKeyPair()
val newKeyPair = account.createKeyPair()
val stellar = wallet.stellar()
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
        deviceKeyPair.address,
        signerWeight = 1,
      )
      .lockAccountMasterKey()
      .build()
      .sign(newKeyPair)

  wallet.stellar().submitTransaction(modifyAccountTransaction)
}
```

> You can get the full code [here](../examples/documentation/src/example-transaction-02.kt).

## Transaction builder (sponsoring)
TODO

> You can get the full code [here](../examples/documentation/src/example-transaction-03.kt).

## Anchor

<!--- INCLUDE .*anchor.*
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
val anchor = wallet.anchor("testanchor.stellar.org")
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
