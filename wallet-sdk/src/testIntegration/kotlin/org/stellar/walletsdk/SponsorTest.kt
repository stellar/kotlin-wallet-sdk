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
    SigningKeyPair.fromSecret("SBHTWEF5U7FK53FLGDMBQYGXRUJ24VBM3M6VDXCHRIGCRG3Z64PH45LW")

  @Test @Order(0) fun testSponsorNewAccount(): Unit = runBlocking { createAccount() }

  @Test
  @Order(1)
  fun testReplaceMasterKey() = runBlocking {
    val newKeyPair = createAccount()

    val replaceWith = account.createKeyPair()

    val modifyAccountTransaction =
      stellar
        .transaction(newKeyPair)
        .sponsoring(sponsoringKey) {
          addAccountSigner(replaceWith, 1)
          lockAccountMasterKey()
        }
        .build()
        .sign(newKeyPair)
        .sign(sponsoringKey)

    val feeBump = stellar.makeFeeBump(sponsoringKey, modifyAccountTransaction)
    feeBump.sign(sponsoringKey)

    wallet.stellar().submitTransaction(feeBump)
  }

  @Test
  @Order(2)
  fun testCreateModify() = runBlocking {
    val newKeyPair = wallet.stellar().account().createKeyPair()

    val replaceWith = account.createKeyPair()

    val modifyAccountTransaction =
      stellar
        .transaction(sponsoringKey)
        .sponsoring(sponsoringKey, newKeyPair) {
          createAccount(newKeyPair)
          addAccountSigner(replaceWith, 1)
          lockAccountMasterKey()
        }
        .build()
        .sign(newKeyPair)
        .sign(sponsoringKey)

    wallet.stellar().submitTransaction(modifyAccountTransaction)
  }

  private suspend fun createAccount(): SigningKeyPair {
    val newKeyPair = wallet.stellar().account().createKeyPair()

    val fundTx =
      stellar
        .transaction(sponsoringKey)
        .sponsoring(sponsoringKey, newKeyPair) { createAccount(newKeyPair) }
        .build()

    val signedFundTx = fundTx.sign(sponsoringKey).sign(newKeyPair)

    wallet.stellar().submitTransaction(signedFundTx)

    return newKeyPair
  }
}
