package org.stellar.walletsdk.anchor

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.stellar.walletsdk.auth.AuthToken

private val TERMINAL_STATUSES =
  setOf(
    TransactionStatus.COMPLETED,
    TransactionStatus.REFUNDED,
    TransactionStatus.EXPIRED,
    // Errors
    TransactionStatus.ERROR,
    TransactionStatus.NO_MARKET,
    TransactionStatus.TOO_LARGE,
    TransactionStatus.TOO_SMALL
  )

class Watcher internal constructor(private val anchor: Anchor) {
  suspend fun watchOneTransaction(
    authToken: AuthToken,
    id: String,
    lang: String? = null,
    pullDelay: Duration = 5.seconds,
    channelSize: Int = UNLIMITED
  ): WatcherResult {
    val channel = Channel<TransactionStatusChange>(channelSize)

    val job = coroutineScope {
      launch(Job()) {
          var transaction = anchor.getTransactionBy(authToken, id = id, lang = lang)
          var statusChange: TransactionStatusChange =
            FirstStatusChange(transaction, transaction.status)
          channel.send(statusChange)

          while (!statusChange.isTerminal()) {
            delay(pullDelay)

            transaction = anchor.getTransactionBy(authToken, id = id, lang = lang)
            statusChange =
              IntermediateStatusChange(transaction, statusChange.status, transaction.status)

            if (statusChange.status != statusChange.oldStatus) {
              channel.send(statusChange)
            }
          }
        }
        .also { it.invokeOnCompletion { channel.close() } }
    }

    return WatcherResult(channel, job)
  }
}

sealed interface TransactionStatusChange {
  val transaction: AnchorTransaction
  val status: TransactionStatus

  fun isTerminal(): Boolean {
    return TERMINAL_STATUSES.contains(status)
  }
}

data class FirstStatusChange(
  override val transaction: AnchorTransaction,
  override val status: TransactionStatus
) : TransactionStatusChange

data class IntermediateStatusChange(
  override val transaction: AnchorTransaction,
  val oldStatus: TransactionStatus,
  override val status: TransactionStatus
) : TransactionStatusChange

data class WatcherResult(val channel: Channel<TransactionStatusChange>, val job: Job)
