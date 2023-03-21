package org.stellar.walletsdk.auth

import kotlin.jvm.JvmInline
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.walletsdk.STRING_TRIM_LENGTH

@Serializable
data class ChallengeResponse(
  val transaction: String,
  @SerialName("network_passphrase") val networkPassphrase: String
)

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
