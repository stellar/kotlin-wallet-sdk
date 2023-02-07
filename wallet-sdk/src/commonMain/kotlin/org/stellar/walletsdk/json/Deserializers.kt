@file:Suppress("MatchingDeclarationName")

package org.stellar.walletsdk.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.stellar.walletsdk.horizon.PublicKeyPair
import org.stellar.walletsdk.horizon.toPublicKeyPair

object AccountAsStringSerializer : KSerializer<PublicKeyPair> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("PublicKeyPair", PrimitiveKind.STRING)
  override fun serialize(encoder: Encoder, value: PublicKeyPair) =
    encoder.encodeString(value.address)
  override fun deserialize(decoder: Decoder): PublicKeyPair =
    decoder.decodeString().toPublicKeyPair()
}
