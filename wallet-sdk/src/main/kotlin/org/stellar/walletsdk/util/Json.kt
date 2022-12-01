package org.stellar.walletsdk.util

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Response

private val defaultJson = Json { ignoreUnknownKeys = true }

internal inline fun <reified T> String.fromJson(format: Json = defaultJson): T {
  return format.decodeFromString(this)
}

internal inline fun <reified T> Response.toJson(format: Json = defaultJson): T {
  return this.body!!.string().fromJson(format)
}

internal inline fun <reified T : Any> T.toJson(format: Json = defaultJson): String {
  return format.encodeToString(this)
}
