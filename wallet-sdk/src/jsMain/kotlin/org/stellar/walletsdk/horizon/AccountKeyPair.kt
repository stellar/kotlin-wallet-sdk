package org.stellar.walletsdk.horizon

/**
 * Stellar account's key pair. It can be either [PublicKeyPair] obtained from public key, or
 * [SigningKeyPair], obtained from private key. Existing account in string format can be converted
 * to public key pair via calling [toPublicKeyPair] helper function.
 */
actual sealed interface AccountKeyPair {
  actual val address: String
    get() = TODO("Not yet implemented")
  actual val publicKey: ByteArray
    get() = TODO("Not yet implemented")
}

actual class PublicKeyPair : AccountKeyPair

actual class SigningKeyPair : AccountKeyPair {
  actual val secretKey: String
    get() = TODO("Not yet implemented")

  actual fun <T : Transaction> sign(transaction: T): T {
    TODO("Not yet implemented")
  }
}

actual open class Transaction
