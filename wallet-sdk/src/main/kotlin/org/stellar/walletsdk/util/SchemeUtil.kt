package org.stellar.walletsdk.util

@Deprecated("To be removed: used to force http scheme for local testing")
object SchemeUtil {
  var scheme = "https"
    private set

  fun useHttp() {
    scheme = "http"
  }

  fun useHttps() {
    scheme = "https"
  }
}
