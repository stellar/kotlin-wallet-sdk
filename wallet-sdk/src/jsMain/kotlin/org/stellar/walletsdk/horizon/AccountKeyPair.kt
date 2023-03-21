package org.stellar.walletsdk.horizon

import external.Keypair
import external.TransactionI

/**
 * Stellar account's key pair. It can be either [PublicKeyPair] obtained from public key, or
 * [SigningKeyPair], obtained from private key. Existing account in string format can be converted
 * to public key pair via calling [toPublicKeyPair] helper function.
 */
actual sealed interface AccountKeyPair {
  val keypair: Keypair
  actual val address: String
    get() = keypair.publicKey()
  actual val publicKey: ByteArray
    get() = TODO("Not yet implemented")
}

actual class PublicKeyPair(override val keypair: Keypair) : AccountKeyPair {
  override fun toString() = "${PublicKeyPair::class.simpleName}(address=$address)"
}

actual class SigningKeyPair(override val keypair: Keypair) : AccountKeyPair {
  init {
    require(keypair.canSign()) { "This keypair doesn't have private key and can't sign" }
  }

  actual val secretKey = keypair.secret()

  actual fun <T : Transaction> sign(transaction: T): T {

    return transaction.sign(this)
  }

  companion object {
    fun fromSecret(secret: String): SigningKeyPair {
      return SigningKeyPair(Keypair.fromSecret(secret))
    }
  }

  override fun toString() = "${SigningKeyPair::class.simpleName}(address=$address)"
}

fun <T : AbstractTransaction> T.sign(keyPair: SigningKeyPair): T {
  return this.also { this.sign(keyPair.keypair) }
}

fun String.toPublicKeyPair(): PublicKeyPair {
  return PublicKeyPair(Keypair.fromPublicKey(this))
}

actual typealias Transaction = external.Transaction

actual typealias AbstractTransaction = TransactionI

actual  fun Transaction.toEnvelopeXdrBase64(): String {
  return this.toXDR()
}