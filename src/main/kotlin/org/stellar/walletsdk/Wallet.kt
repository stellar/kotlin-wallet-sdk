package org.stellar.walletsdk

import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import shadow.com.google.gson.annotations.SerializedName

class Wallet(private val horizonUrl: String, private val horizonPassphrase: String) {
  private val server = Server(this.horizonUrl)
  private val network = Network(this.horizonPassphrase)

  data class AccountKeypair(
    @SerializedName("publicKey") val publicKey: String,
    @SerializedName("secretKey") val secretKey: String
  )

  // Create account keys, generate new keypair
  fun create(): AccountKeypair {
    val keypair: KeyPair = KeyPair.random()
    return AccountKeypair(keypair.accountId, String(keypair.secretSeed))
  }
}
