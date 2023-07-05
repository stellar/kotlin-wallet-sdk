package org.stellar.walletsdk.json

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import okhttp3.Response

private val log = KotlinLogging.logger {}

internal val defaultJson = Json { ignoreUnknownKeys = true }

internal inline fun <reified T> String.fromJson(format: Json = defaultJson): T {
  log.trace { "JSON format: $this" }
  return format.decodeFromString(this)
}

internal inline fun <reified T> Response.toJson(format: Json = defaultJson): T {
  return this.body!!.string().fromJson(format)
}

internal inline fun <reified T : Any> T.toJson(format: Json = defaultJson): String {
  return format.encodeToString(this)
}
