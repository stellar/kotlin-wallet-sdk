// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleBasic01

import org.stellar.sdk.Network

import org.stellar.walletsdk.*

val wallet = Wallet(StellarConfiguration.Testnet)

val walletMainnet = Wallet(StellarConfiguration(Network.PUBLIC, "https://horizon.stellar.org"))

val walletCustom = Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(useHttp = true))
