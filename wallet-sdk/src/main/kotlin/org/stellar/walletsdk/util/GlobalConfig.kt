package org.stellar.walletsdk.util

import java.util.*

/**
 * Global config to set up some commonly used variables across the SDK,
 *
 * @constructor Create empty Global config
 */
object GlobalConfig {
  /**
   * Base64 decoder. Default [java.util.Base64] decoder works with Android API 23+. To support
   * Android API older than API 23, custom base64Decoder needs to be provided. For example,
   * `android.util.Base64`.
   */
  var base64Decoder = defaultBase64Decoder
}

typealias Base64Decoder = ((String) -> ByteArray)

private val defaultBase64Decoder: Base64Decoder = { it: String -> Base64.getDecoder().decode(it) }
