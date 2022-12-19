package org.stellar.walletsdk.auth

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.stellar.walletsdk.STRING_TRIM_LENGTH

@Suppress("ConstructorParameterNaming")
@Serializable
data class ChallengeResponse(val transaction: String, val network_passphrase: String)

@Serializable
@JvmInline
value class AuthToken(private val value: String) {
  fun prettify(): String {
    return value.take(STRING_TRIM_LENGTH)
  }

  override fun toString(): String {
    return value
  }
}

@Serializable internal data class AuthTokenResponse(@Contextual val token: AuthToken)

@Serializable internal data class AuthTransaction(val transaction: String)
