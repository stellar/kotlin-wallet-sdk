package org.stellar.walletsdk.response

import arrow.core.Either

typealias Result<E, R> = Either<E, R>

typealias Failure<E> = Either.Left<E>

typealias Success<T> = Either.Right<T>

interface RunningContext

@PublishedApi internal object RunningContextImpl : RunningContext

inline fun <reified E : Error, R> safeRun(f: RunningContext.() -> Result<E, R>): Result<E, R> {
  return try {
    f(RunningContextImpl)
  } catch (e: Exception) {
    return Failure(GenericError(e) as E)
  }
}

inline fun <E : Error> RunningContext.fail(e: E): Result<E, Nothing> {
  return Failure(e)
}

inline fun <R> RunningContext.success(e: R): Result<Nothing, R> {
  return Success(e)
}
