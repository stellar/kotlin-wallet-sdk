package org.stellar.walletsdk

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign

class SponsorTest {
  private val wallet = Wallet(StellarConfiguration.Testnet)
  private val stellar = wallet.stellar()
  private val account = wallet.stellar().account()
  private val sponsoringKey =
    SigningKeyPair.fromSecret("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")

  @Test @Order(0) fun testSponsorNewAccount(): Unit = runBlocking { createAccount(0u) }

  @Test
  @Order(1)
  fun testReplaceMasterKey() = runBlocking {
    val newKeyPair = createAccount(1u)

    val replaceWith = account.createKeyPair()

    val modifyAccountTransaction =
      stellar
        .transaction(newKeyPair)
        .sponsoring(sponsoringKey) {
          addAccountSigner(replaceWith.address, 1)
          lockAccountMasterKey()
        }
        .build()
        .sign(newKeyPair)
        .sign(sponsoringKey)

    wallet.stellar().submitTransaction(modifyAccountTransaction)
  }

  private suspend fun createAccount(startingBalance: UInt): SigningKeyPair {
    val newKeyPair = wallet.stellar().account().createKeyPair()

    val fundTx =
      stellar
        .transaction(sponsoringKey)
        .sponsoring(sponsoringKey) { createAccount(newKeyPair, startingBalance) }
        .build()

    val signedFundTx = fundTx.sign(sponsoringKey).sign(newKeyPair)

    wallet.stellar().submitTransaction(signedFundTx)

    return newKeyPair
  }
}
