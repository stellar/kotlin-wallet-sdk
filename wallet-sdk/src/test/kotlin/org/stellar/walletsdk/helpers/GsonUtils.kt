package org.stellar.walletsdk.helpers

import com.google.gson.*

/** Helpers for [Gson] */
object GsonUtils {
  var instance: Gson? = null
    get() {
      if (field == null) field = builder().create()
      return field
    }
    private set

  private fun builder(): GsonBuilder {
    return GsonBuilder()
  }
}
