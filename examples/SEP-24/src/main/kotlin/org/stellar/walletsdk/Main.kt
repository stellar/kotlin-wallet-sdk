package org.stellar.walletsdk

import kotlinx.coroutines.runBlocking
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.sdk.Transaction

private val myAddress = "GCNVZJLJ5XZZALLZBR7BGUMGMF2KVZTBIGTU2A5KOPS2PPCZS6PV4LAD"
private val myKey = "SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I"

fun main() {
  val wallet = Wallet()
  val account = KeyPair.random()
  val tx = runBlocking { wallet.fund(myAddress, account.publicKeyString, "10") }

  tx.sign(KeyPair.fromSecretSeed(myKey))

  val res = runBlocking { wallet.submitTransaction(tx) }

  val anchor = Anchor(Server("https://horizon-testnet.stellar.org"), Network.TESTNET)

  val info = runBlocking {
    anchor.getInfo("https://testanchor.stellar.org/.well-known/stellar.toml")
  }

  val servicesInfo = runBlocking { anchor.getServicesInfo("https://testanchor.stellar.org/sep24") }

  val resp = runBlocking {
    anchor.getInteractiveWithdrawal(
      account.publicKeyString,
      homeDomain = "testanchor.stellar.org",
      assetCode = "SRT",
      walletSigner = WalletSignerImpl(account)
    )
  }

  print(resp)
}

val KeyPair.publicKeyString: String
  get() = this.accountId

val KeyPair.secretKey: String
  get() = this.secretSeed.concatToString()

class WalletSignerImpl(private val keyPair: KeyPair) : WalletSigner {
  override fun signWithClientAccount(txn: Transaction): Transaction {
    txn.sign(keyPair)
    return txn
  }

  override fun signWithDomainAccount(
    transactionString: String,
    networkPassPhrase: String
  ): Transaction {
    TODO("Not yet implemented")
  }
}
