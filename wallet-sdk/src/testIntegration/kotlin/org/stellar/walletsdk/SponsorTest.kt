package org.stellar.walletsdk

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.sign

class SponsorTest {
  val wallet = Wallet(StellarConfiguration.Testnet)
  val account = wallet.stellar().account()
  val sponsoringKey =
    SigningKeyPair.fromSecret("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")

  @Test
  fun testSponsorNewAccount() = runBlocking {
    val newKeyPair = wallet.stellar().account().createKeyPair()
    val stellar = wallet.stellar()

    val fundTx =
      stellar
        .transaction(sponsoringKey)
        .startSponsoring(sponsoringKey)
        .createAccount(newKeyPair, "0")
        .stopSponsoring()
        .build()

    val signedFundTx = fundTx.sign(sponsoringKey).sign(newKeyPair)

    wallet.stellar().submitTransaction(signedFundTx)

    //    val deviceKeyPair = account.createKeyPair()
    //
    //    val modifyAccountTransaction =
    //      stellar
    //        .transaction(newKeyPair)
    //        .addAccountSigner(
    //          deviceKeyPair.address,
    //          signerWeight = 1,
    //          sponsorAddress = sponsoringKey.address
    //        )
    //        .lockAccountMasterKey(sponsorAddress = sponsoringKey.address)
    //        .build()
    //        .sign(newKeyPair)
    //        .sign(sponsoringKey)
    //
    //    val feeBump =
    //      FeeBumpTransaction.Builder(modifyAccountTransaction)
    //        .setFeeAccount(sponsoringKey.address)
    //        .setBaseFee(1000)
    //        .build()
    //        .sign(sponsoringKey)
    //
    //    server.submitTransaction(feeBump)
  }
}
