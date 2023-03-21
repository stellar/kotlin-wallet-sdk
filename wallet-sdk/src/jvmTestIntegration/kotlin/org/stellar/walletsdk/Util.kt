package org.stellar.walletsdk

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal inline fun <reified T> String.fromJson(): T {
  return Json { ignoreUnknownKeys = true }.decodeFromString(this)
}
