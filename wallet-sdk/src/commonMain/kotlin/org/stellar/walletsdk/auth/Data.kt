@file:UseSerializers(
  AuthTokenAsStringSerializer::class,
)

package org.stellar.walletsdk.auth

import kotlin.js.JsExport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.stellar.walletsdk.STRING_TRIM_LENGTH
import org.stellar.walletsdk.json.AuthTokenAsStringSerializer

@Serializable
data class ChallengeResponse(
  val transaction: String,
  @SerialName("network_passphrase") val networkPassphrase: String
)

@Serializable
@JsExport
data class AuthToken(val value: String) {
  fun prettify(): String {
    return value.take(STRING_TRIM_LENGTH)
  }

  override fun toString(): String {
    return value
  }
}

@Serializable internal data class AuthTokenResponse(val token: AuthToken)

@Serializable internal data class AuthTransaction(val transaction: String)
