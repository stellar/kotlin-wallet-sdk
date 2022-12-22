# Wallet SDK usage guide

<!--- TOC -->

* [Getting started](#getting-started)

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
