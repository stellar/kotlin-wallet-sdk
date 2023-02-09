package org.stellar.walletsdk.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.stellar.walletsdk.horizon.PublicKeyPair

actual object AccountAsStringSerializer : KSerializer<PublicKeyPair> {
  override fun deserialize(decoder: Decoder): PublicKeyPair {
    TODO("Not yet implemented")
  }

  override val descriptor: SerialDescriptor
    get() = TODO("Not yet implemented")

  override fun serialize(encoder: Encoder, value: PublicKeyPair) {
    TODO("Not yet implemented")
  }
}
