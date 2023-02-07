package org.stellar.walletsdk.horizon

import org.stellar.sdk.AbstractTransaction
import org.stellar.sdk.KeyPair

/**
 * Stellar account's key pair. It can be either [PublicKeyPair] obtained from public key, or
 * [SigningKeyPair], obtained from private key. Existing account in string format can be converted
 * to public key pair via calling [toPublicKeyPair] helper function.
 */
sealed interface AccountKeyPair {
  val keyPair: KeyPair
  val address: String
    get() = keyPair.accountId
  val publicKey: ByteArray
    get() = keyPair.publicKey
}

@JvmInline
value class PublicKeyPair(override val keyPair: KeyPair) : AccountKeyPair {
  override fun toString() = "${PublicKeyPair::class.simpleName}(address=$address)"
}

@JvmInline
value class SigningKeyPair(override val keyPair: KeyPair) : AccountKeyPair {
  init {
    require(keyPair.canSign()) { "This keypair doesn't have private key and can't sign" }
  }

  val secretKey: String
    get() = keyPair.secretSeed.concatToString()

  fun <T : AbstractTransaction> sign(transaction: T): T {
    return transaction.sign(this)
  }

  companion object {
    fun fromSecret(secret: String): SigningKeyPair {
      return SigningKeyPair(KeyPair.fromSecretSeed(secret))
    }
  }

  override fun toString() = "${SigningKeyPair::class.simpleName}(address=$address)"
}

fun <T : AbstractTransaction> T.sign(keyPair: SigningKeyPair): T {
  return this.also { this.sign(keyPair.keyPair) }
}

fun String.toPublicKeyPair(): PublicKeyPair {
  return PublicKeyPair(KeyPair.fromAccountId(this))
}
