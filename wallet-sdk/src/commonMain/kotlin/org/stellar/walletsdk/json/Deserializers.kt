@file:Suppress("MatchingDeclarationName")

package org.stellar.walletsdk.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.horizon.PublicKeyPair

expect object AccountAsStringSerializer : KSerializer<PublicKeyPair>

object AuthTokenAsStringSerializer : KSerializer<AuthToken> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("AuthToken", PrimitiveKind.STRING)
  override fun serialize(encoder: Encoder, value: AuthToken) = encoder.encodeString(value.value)
  override fun deserialize(decoder: Decoder): AuthToken = AuthToken(decoder.decodeString())
}
