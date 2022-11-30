package org.stellar.walletsdk.util

internal object Util {
  fun String.isHex(): Boolean {
    return this.toBigIntegerOrNull(16) != null
  }
}
