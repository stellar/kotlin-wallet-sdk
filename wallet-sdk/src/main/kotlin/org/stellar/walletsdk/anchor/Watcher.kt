package org.stellar.walletsdk.anchor

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.stellar.walletsdk.auth.AuthToken

private val log = KotlinLogging.logger {}
private val ERROR_STATUSES =
  setOf(
    TransactionStatus.ERROR,
    TransactionStatus.NO_MARKET,
    TransactionStatus.TOO_LARGE,
    TransactionStatus.TOO_SMALL
  )

private val TERMINAL_STATUSES =
  setOf(
    TransactionStatus.COMPLETED,
    TransactionStatus.REFUNDED,
    TransactionStatus.EXPIRED,
  ) + ERROR_STATUSES

class Watcher internal constructor(private val anchor: Anchor) {
  suspend fun watchOneTransaction(
    authToken: AuthToken,
    id: String,
    lang: String? = null,
    pullDelay: Duration = 5.seconds,
    channelSize: Int = UNLIMITED,
    exceptionHandler: WalletExceptionHandler = RetryExceptionHandler()
  ): WatcherResult {
    val channel = Channel<StatusUpdateEvent>(channelSize)

    val job = coroutineScope {
      launch(Job()) {
          var oldStatus: TransactionStatus? = null
          val retryContext = RetryContext()

          do {
            var shouldExit: Boolean

            try {
              val transaction = anchor.getTransactionBy(authToken, id = id, lang = lang)
              val statusChange = StatusChange(transaction, transaction.status, oldStatus)

              if (statusChange.status != statusChange.oldStatus) {
                channel.send(statusChange)
              }
              oldStatus = statusChange.status

              if (!statusChange.isTerminal()) {
                delay(pullDelay)
              }

              shouldExit = statusChange.isTerminal()
              retryContext.refresh()
            } catch (e: Exception) {
              try {
                retryContext.onError(e)
                shouldExit = exceptionHandler.invoke(retryContext)
              } catch (e: Exception) {
                shouldExit = true
                log.error(e) { "CRITICAL: Couldn't invoke exception handler" }
              }
              if (shouldExit) {
                channel.send(RetriesExhausted)
              }
            }
          } while (!shouldExit)

          channel.send(ChannelClosed)
        }
        .also { it.invokeOnCompletion { channel.close() } }
    }

    return WatcherResult(channel, job)
  }
}

typealias WalletExceptionHandler = suspend (RetryContext) -> (Boolean)

class RetryExceptionHandler(
  private val maxRetryCount: Int = 3,
  private val backoffPeriod: Duration = 5.seconds
) : WalletExceptionHandler {
  override suspend fun invoke(ctx: RetryContext): Boolean {
    log.error(ctx.exception) {
      "Exception on getting transaction data. Try ${ctx.retries}/${maxRetryCount}"
    }

    if (ctx.retries < maxRetryCount) {
      delay(backoffPeriod)
      return false
    }
    return true
  }
}

class RetryContext {
  var retries: Int = 0
    private set
  var exception: Exception? = null
    private set

  internal fun refresh() {
    retries = 0
    exception = null
  }

  internal fun onError(e: Exception) {
    exception = e
    retries++
  }
}

sealed interface StatusUpdateEvent

data class StatusChange(
  val transaction: AnchorTransaction,
  val status: TransactionStatus,
  val oldStatus: TransactionStatus? = null,
) : StatusUpdateEvent {
  fun isTerminal(): Boolean {
    return TERMINAL_STATUSES.contains(status)
  }

  fun isError(): Boolean {
    return ERROR_STATUSES.contains(status)
  }
}

object ChannelClosed : StatusUpdateEvent

object RetriesExhausted : StatusUpdateEvent

data class WatcherResult(val channel: Channel<StatusUpdateEvent>, val job: Job)
