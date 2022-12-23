# Wallet SDK usage guide

<!--- TOC -->

* [Getting started](#getting-started)
* [Build on Stellar](#build-on-stellar)
  * [Account service](#account-service)
  * [Transaction builder](#transaction-builder)
  * [Submit transaction](#submit-transaction)

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
import org.stellar.walletsdk.horizon.sign

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()
-->
<!--- SUFFIX .*transaction.*
suspend fun main() {
  val fundTxn = fund()
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
val txnBuilder = wallet
    .stellar()
    .transaction()
```

Fund account transaction activates/creates an account with a starting balance (by default, it's 1 XLM). This transaction
can be sponsored.

```kotlin
suspend fun fund(): Transaction {
    return txnBuilder.fund(sourceAccountKeyPair.address, destinationAccountKeyPair.address)
}
```

Lock the master key of the account (set its weight to 0). Use caution when locking the account's master key. Make sure
you have set the correct signers and weights. Otherwise, you will lock the account irreversibly. This transaction can be
sponsored.

```kotlin
suspend fun lockMasterKey(): Transaction {
    return txnBuilder.lockAccountMasterKey(destinationAccountKeyPair.address)
}
```

Add an asset (trustline) to the account. This transaction can be sponsored.

```kotlin
val asset = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

suspend fun addAsset(): Transaction {
    return txnBuilder.addAssetSupport(sourceAccountKeyPair.address, asset)
}
```

Remove an asset from the account (the balance must be 0)

```kotlin
suspend fun removeAsset(): Transaction {
    return txnBuilder.removeAssetSupport(sourceAccountKeyPair.address, asset)
}
```

Add a new signer to the account. Use caution when adding new signers. Make sure you set the correct signer weight.
Otherwise, you will lock the account irreversibly. This transaction can be sponsored.

```kotlin
val newSignerKeyPair = account.createKeyPair()

suspend fun addSigner(): Transaction {
    return txnBuilder.addAccountSigner(sourceAccountKeyPair.address, newSignerKeyPair.address, 10)
}
```

Remove a signer from the account.

```kotlin
suspend fun removeSigner(): Transaction {
    return txnBuilder.removeAccountSigner(sourceAccountKeyPair.address, newSignerKeyPair.address)
}
```

### Submit transaction

Submit a signed transaction to the Stellar network. A sponsored transaction must be signed by both the account and the
sponsor.

```kotlin
suspend fun signAndSubmit(): Boolean {
    val signedTxn = fund().sign(sourceAccountKeyPair)
    return wallet.stellar().submitTransaction(signedTxn)
}
```

> You can get the full code [here](../examples/documentation/src/example-transaction-01.kt).
