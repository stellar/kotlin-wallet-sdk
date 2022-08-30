package org.stellar.walletsdk.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAccessor

object GsonUtils {
  var instance: Gson? = null
    get() {
      if (field == null) field = builder().create()
      return field
    }
    private set

  private fun builder(): GsonBuilder {
    return GsonBuilder()
      .registerTypeAdapter(Instant::class.java, InstantConverter())
      .registerTypeAdapter(Duration::class.java, DurationConverter())
  }
}

class DurationConverter : JsonSerializer<Duration?>, JsonDeserializer<Duration?> {
  override fun serialize(
    src: Duration?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(src?.toMillis())
  }

  @Throws(JsonParseException::class)
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): Duration {
    return Duration.of(json.asString.toLong(), ChronoUnit.MILLIS)
  }
}

class InstantConverter : JsonSerializer<Instant?>, JsonDeserializer<Instant?> {
  override fun serialize(
    src: Instant?,
    typeOfSrc: Type?,
    context: JsonSerializationContext?
  ): JsonElement {
    return JsonPrimitive(dateTimeFormatter.format(src))
  }

  @Throws(JsonParseException::class)
  override fun deserialize(
    json: JsonElement,
    typeOfT: Type?,
    context: JsonDeserializationContext?
  ): Instant {
    return dateTimeFormatter.parse(json.asString) { temporal: TemporalAccessor? ->
      Instant.from(temporal)
    }
  }

  companion object {
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT
  }
}
