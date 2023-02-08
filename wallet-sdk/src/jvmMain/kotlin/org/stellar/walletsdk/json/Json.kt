package org.stellar.walletsdk.json

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import okhttp3.Response

private val log = KotlinLogging.logger {}

internal inline fun <reified T> Response.toJson(format: Json = defaultJson): T {
  return this.body!!.string().fromJson(format)
}


@Suppress("SwallowedException", "TooGenericExceptionCaught")
internal inline fun <reified T> Response.toJsonOrNull(format: Json = org.stellar.walletsdk.json.defaultJson): T? {
  return try {
    this.toJson<T>(format)
  } catch (e: Exception) {
    null
  }
}
