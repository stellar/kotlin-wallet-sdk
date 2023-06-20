package org.stellar.walletsdk.customer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddCustomerResponse(
  @SerialName("id") var id: String,
)

enum class Sep12Status(val status: String) {
  @SerialName("NEEDS_INFO") NEEDS_INFO("NEEDS_INFO"),
  @SerialName("ACCEPTED") ACCEPTED("ACCEPTED"),
  @SerialName("PROCESSING") PROCESSING("PROCESSING"),
  @SerialName("REJECTED") REJECTED("REJECTED"),
  @SerialName("VERIFICATION_REQUIRED") VERIFICATION_REQUIRED("VERIFICATION_REQUIRED")
}

@Serializable
data class Field(
  var type: Type? = null,
  var description: String? = null,
  var choices: List<String>? = null,
  var optional: Boolean? = null,
) {
  enum class Type(val status: String) {
    @SerialName("string") STRING("string"),
    @SerialName("binary") BINARY("binary"),
    @SerialName("number") NUMBER("number"),
    @SerialName("date") DATE("date")
  }
}

@Serializable
data class ProvidedField(
  var type: Field.Type? = null,
  var description: String? = null,
  var choices: List<String>? = null,
  var optional: Boolean? = null,
  var status: Sep12Status? = null,
  var error: String? = null,
)

@Serializable
data class GetCustomerResponse(
  var id: String? = null,
  var status: Sep12Status? = null,
  var fields: Map<String?, Field?>? = null,
  @SerialName("provided_fields") var providedFields: Map<String, ProvidedField>? = null,
  var message: String? = null,
)
