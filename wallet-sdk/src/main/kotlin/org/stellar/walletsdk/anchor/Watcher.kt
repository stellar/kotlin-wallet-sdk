package org.stellar.walletsdk.anchor

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import mu.KotlinLogging
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.auth.AuthToken

private val log = KotlinLogging.logger {}

class Watcher
internal constructor(
  private val anchor: Anchor,
  private val pollDelay: Duration,
  private val channelSize: Int,
  private val exceptionHandler: WalletExceptionHandler
) {
  suspend fun watchOneTransaction(
    authToken: AuthToken,
    id: String,
    lang: String? = null
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
                delay(pollDelay)
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
                channel.send(ExceptionHandlerExit)
              }
            }
          } while (!shouldExit)

          channel.send(ChannelClosed)
        }
        .also { it.invokeOnCompletion { channel.close() } }
    }

    return WatcherResult(channel, job)
  }

  suspend fun watchAsset(
    authToken: AuthToken,
    asset: StellarAssetId,
    since: Instant? = null,
    lang: String? = null,
    kind: TransactionKind? = null
  ): WatcherResult {
    val channel = Channel<StatusUpdateEvent>(channelSize)

    val job = coroutineScope {
      launch(Job()) {
          var transactionStatuses = mapOf<String, AnchorTransaction>()
          val retryContext = RetryContext()

          do {
            var shouldExit: Boolean

            try {
              val transactions =
                anchor
                  .getTransactionsForAsset(asset, authToken, since, kind = kind, lang = lang)
                  .associateBy { it.id }

              transactions.forEach {
                val tx = it.value
                val previousStatus = transactionStatuses[it.key]?.status

                if (tx.status != previousStatus) {
                  channel.send(StatusChange(tx, tx.status, previousStatus))
                }
              }

              val unfinishedTransactions =
                transactions.filter { it.value.status.isTerminal().not() }

              transactionStatuses = transactions

              if (unfinishedTransactions.isNotEmpty()) {
                delay(pollDelay)
              }

              shouldExit = unfinishedTransactions.isEmpty()
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
                channel.send(ExceptionHandlerExit)
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

/**
 * Simple exception handler that retries on the error
 *
 * @constructor Create empty Retry exception handler
 * @property maxRetryCount maximum consequent retry count. If this specified number of errors
 * happens in a row, handler will give up retrying.
 * @property backoffPeriod delay before resuming the job
 */
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
    return status.isTerminal()
  }

  fun isError(): Boolean {
    return status.isError()
  }
}

object ChannelClosed : StatusUpdateEvent

object ExceptionHandlerExit : StatusUpdateEvent

data class WatcherResult(val channel: Channel<StatusUpdateEvent>, val job: Job)
