package org.stellar.walletsdk.anchor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionStatus {
  /**
   * There is not yet enough information for this transaction to be initiated. Perhaps the user has
   * not yet entered necessary info in an interactive flow
   */
  @SerialName("incomplete") INCOMPLETE,

  /**
   * The user has not yet initiated their transfer to the anchor. This is the next necessary step in
   * any deposit or withdrawal flow after transitioning from `incomplete`
   */
  @SerialName("pending_user_transfer_start") PENDING_USER_TRANSFER_START,

  /**
   * The Stellar payment has been successfully received by the anchor and the off-chain funds are
   * available for the customer to pick up. Only used for withdrawal transactions.
   */
  @SerialName("pending_user_transfer_complete") PENDING_USER_TRANSFER_COMPLETE,

  /**
   * Pending External deposit/withdrawal has been submitted to external network, but is not yet
   * confirmed. This is the status when waiting on Bitcoin or other external crypto network to
   * complete a transaction, or when waiting on a bank transfer.
   */
  @SerialName("pending_external") PENDING_EXTERNAL,

  /**
   * Deposit/withdrawal is being processed internally by anchor. This can also be used when the
   * anchor must verify KYC information prior to deposit/withdrawal.
   */
  @SerialName("pending_anchor") PENDING_ANCHOR,

  /**
   * Deposit/withdrawal operation has been submitted to Stellar network, but is not yet confirmed.
   */
  @SerialName("pending_stellar") PENDING_STELLAR,

  /** The user must add a trustline for the asset for the deposit to complete. */
  @SerialName("pending_trust") PENDING_TRUST,

  /**
   * The user must take additional action before the deposit / withdrawal can complete, for example
   * an email or 2fa confirmation of a withdrawal.
   */
  @SerialName("pending_user") PENDING_USER,

  /** Deposit/withdrawal fully completed */
  @SerialName("completed") COMPLETED,

  /** The deposit/withdrawal is fully refunded */
  @SerialName("refunded") REFUNDED,

  /**
   * Funds were never received by the anchor and the transaction is considered abandoned by the
   * user. Anchors are responsible for determining when transactions are considered expired.
   */
  @SerialName("expired") EXPIRED,

  /**
   * Could not complete deposit because no satisfactory asset/XLM market was available to create the
   * account
   */
  @SerialName("no_market") NO_MARKET,

  /** Deposit/withdrawal size less than min_amount. */
  @SerialName("too_small") TOO_SMALL,

  /** Deposit/withdrawal size exceeded max_amount. */
  @SerialName("too_large") TOO_LARGE,

  /** Catch-all for any error not enumerated above. */
  @SerialName("error") ERROR;

  fun isTerminal(): Boolean {
    return terminalStatuses.contains(this)
  }

  fun isError(): Boolean {
    return errorStatuses.contains(this)
  }
}

private val errorStatuses =
  setOf(
    TransactionStatus.ERROR,
    TransactionStatus.NO_MARKET,
    TransactionStatus.TOO_LARGE,
    TransactionStatus.TOO_SMALL
  )

private val terminalStatuses =
  setOf(
    TransactionStatus.COMPLETED,
    TransactionStatus.REFUNDED,
    TransactionStatus.EXPIRED,
  ) + errorStatuses
