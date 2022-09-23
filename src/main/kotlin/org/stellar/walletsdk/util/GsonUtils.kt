package org.stellar.walletsdk.util

import com.google.gson.*

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
