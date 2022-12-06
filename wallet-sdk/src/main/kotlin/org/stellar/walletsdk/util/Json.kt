package org.stellar.walletsdk.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import okhttp3.Response
import org.stellar.walletsdk.exception.AnchorErrorResponse

private val log = KotlinLogging.logger {}

private val defaultJson = Json { ignoreUnknownKeys = true }

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

internal inline fun <reified T> Response.toJsonOrNull(
  format: Json = defaultJson
): AnchorErrorResponse? {
  return try {
    this.body!!.string().fromJson(format)
  } catch (e: Exception) {
    null
  }
}
