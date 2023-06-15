package org.stellar.walletsdk.json

import kotlinx.datetime.Instant
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.asset.*
import org.stellar.walletsdk.exception.InvalidJsonException
import org.stellar.walletsdk.util.toAssetId

internal object AnchorTransactionSerializer :
  JsonContentPolymorphicSerializer<AnchorTransaction>(AnchorTransaction::class) {
  @Suppress("NestedBlockDepth")
  override fun selectDeserializer(
    element: JsonElement
  ): DeserializationStrategy<out AnchorTransaction> {
    val kind = element.jsonObject["kind"]?.jsonPrimitive
    val status = element.jsonObject["status"]?.jsonPrimitive
    if (kind?.isString == true) {
      if (status?.isString == true) {
        return when (kind.content) {
          "withdrawal" -> {
            when (status.content) {
              "incomplete" -> IncompleteWithdrawalTransaction.serializer()
              "error" -> ErrorTransaction.serializer()
              else -> WithdrawalTransaction.serializer()
            }
          }
          "deposit" -> {
            when (status.content) {
              "incomplete" -> IncompleteDepositTransaction.serializer()
              "error" -> ErrorTransaction.serializer()
              else -> DepositTransaction.serializer()
            }
          }
          else -> throw InvalidJsonException("invalid kind", element)
        }
      }
      throw InvalidJsonException("status not found", element)
    }
    throw InvalidJsonException("kind not found", element)
  }
}

internal object AssetIdSerializer : KSerializer<AssetId> {
  override val descriptor =
    PrimitiveSerialDescriptor(AssetId::class.qualifiedName!!, PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: AssetId) = encoder.encodeString(value.toString())

  @Suppress("ReturnCount")
  override fun deserialize(decoder: Decoder): AssetId {
    val str = decoder.decodeString()

    return str.toAssetId()
  }
}

internal object InstantEpochSerializer : KSerializer<Instant> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor(Instant::class.qualifiedName!!, PrimitiveKind.LONG)
  override fun serialize(encoder: Encoder, value: Instant) =
    encoder.encodeLong(value.toEpochMilliseconds() / 1000)
  override fun deserialize(decoder: Decoder) = Instant.fromEpochSeconds(decoder.decodeLong())
}
