// This file was automatically generated from WalletGuide.md by Knit tool. Do not edit.
package org.stellar.example.exampleTransaction03

import org.stellar.sdk.Transaction
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.*

val wallet = Wallet(StellarConfiguration.Testnet)
val account = wallet.stellar().account()

