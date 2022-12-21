// This file was automatically generated from README.md by Knit tool. Do not edit.
package org.stellar.example.exampleBasic01

import org.stellar.walletsdk.*

fun main() {

  val wallet = Wallet(StellarConfiguration.Testnet)

  val newKeyPair = wallet.stellar().account().createKeyPair()
  println(newKeyPair)
}
