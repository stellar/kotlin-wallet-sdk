package org.stellar.walletsdk.util

import kotlin.js.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

internal object UtilJS {
  fun <T> promise(block: suspend CoroutineScope.() -> T): Promise<T> {
    return CoroutineScope(Dispatchers.Default).promise(block = block)
  }
}
