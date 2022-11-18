package org.stellar.walletsdk.response

import arrow.core.Either

interface ErrorHandler {
  fun onError(e: Error)
}

object LoggingHandler : ErrorHandler {
  override fun onError(e: Error) {
    System.err.println(e)
  }
}

internal interface RunContext

inline fun ErrorHandler.handlingErrors(f: () -> (Unit)) {
  try {
    f()
  } catch (e: EitherLeft) {
    this.onError(e.e)
  }
}

@PublishedApi
internal class EitherLeft(val e: Error) :
  Throwable(
    "Unhandled error received: provide an error handler. " +
      "See ErrorHandler interface for more usages info"
  )

inline fun <E : Error, R> Result<E, R>.unwrap(): R {
  when (this) {
    is Either.Right -> return this.value
    is Either.Left -> throw EitherLeft(this.value)
  }
}

inline fun <E : Error, R> Result<E, R>.unwrapOrNull(h: ErrorHandler): R? {
  when (this) {
    is Either.Right -> return this.value
    is Either.Left -> h.onError(this.value)
  }
  return null
}
