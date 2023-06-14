@file:UseSerializers(InstantEpochSerializer::class)

package org.stellar.walletsdk.auth

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import org.stellar.walletsdk.STRING_TRIM_LENGTH
import org.stellar.walletsdk.json.InstantEpochSerializer

@Serializable
data class ChallengeResponse(
  val transaction: String,
  @SerialName("network_passphrase") val networkPassphrase: String
)

@Serializable
data class AuthToken(
  @SerialName("iss") val issuer: String,
  @SerialName("sub") val principalAccount: String,
  @SerialName("iat") val issuedAt: Instant,
  @SerialName("exp") val expiresAt: Instant,
  @SerialName("client_domain") val clientDomain: String?
) {
  @Transient // not jvm transient
  lateinit var token: String

  fun prettify(): String {
    return token.take(STRING_TRIM_LENGTH)
  }

  override fun toString(): String {
    return token
  }
}

@Serializable internal data class AuthTokenResponse(val token: String)

@Serializable internal data class AuthTransaction(val transaction: String)
