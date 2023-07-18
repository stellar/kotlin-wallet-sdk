# Kotlin Wallet SDK [![javadoc](https://javadoc.io/badge2/org.stellar/wallet-sdk/dokka.svg?logo=kotlin)](https://javadoc.io/doc/org.stellar/wallet-sdk) [![Maven Central](https://img.shields.io/maven-central/v/org.stellar/wallet-sdk?color=success&logo=apache-maven)](https://central.sonatype.com/search?q=pkg%253Amaven%252Forg.stellar%252Fwallet-sdk&namespace=org.stellar)

Kotlin Wallet SDK is a library that allows developers to build wallet applications on the Stellar network faster. It
utilizes [Java Stellar SDK](https://github.com/stellar/java-stellar-sdk) to communicate with a Stellar Horizon server.

## Dependency

The library is hosted on the [Maven Central](https://central.sonatype.com/search?q=pkg%253Amaven%252Forg.stellar%252Fwallet-sdk&namespace=org.stellar).
To import `wallet-sdk` library you need to add following dependencies to your code:

Maven:

```pom
<dependency>
  <groupId>org.stellar</groupId>
  <artifactId>wallet-sdk</artifactId>
  <version>0.9.2</version>
</dependency>
```

Gradle:

```gradle
implementation("org.stellar:wallet-sdk:0.9.2")
```

## Introduction

<!--- INCLUDE .*readme.*
import org.stellar.walletsdk.*

fun main() { 
-->
<!--- SUFFIX .*readme.*
  println(newKeyPair)
}    
-->

Here's a small example creating main wallet class with default configuration connected to testnet network:

```kotlin
  val wallet = Wallet(StellarConfiguration.Testnet)
```

It should later be re-used across the code, as it has access to various useful children classes. For example, new key pair can be
created using it.

```kotlin
  val newKeyPair = wallet.stellar().account().createKeyPair()
```

> You can get the full code [here](examples/documentation/src/example-readme-01.kt).

Read [full wallet guide](docs/WalletGuide.md) for more info

You can find auto-generated documentation website on [javadoc.io](https://javadoc.io/doc/org.stellar/wallet-sdk) 
