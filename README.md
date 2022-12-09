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
  <version>0.3.0</version>
</dependency>
```

Gradle:
```kotlin
implementation("org.stellar:wallet-sdk:0.3.0")
```
