package org.stellar.walletsdk.horizon

/**
 * Stellar account's key pair. It can be either [PublicKeyPair] obtained from public key, or
 * [SigningKeyPair], obtained from private key. Existing account in string format can be converted
 * to public key pair via calling [toPublicKeyPair] helper function.
 */
expect sealed interface AccountKeyPair {
  open val address: String
  open val publicKey: ByteArray
}

expect class PublicKeyPair : AccountKeyPair

expect class SigningKeyPair : AccountKeyPair {
  val secretKey: String

  fun <T : Transaction> sign(transaction: T): T
}

expect class Transaction

expect abstract class AbstractTransaction

internal expect fun Transaction.toEnvelopeXdrBase64(): String
