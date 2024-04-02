@file:UseSerializers(NullableAccountAsStringSerializer::class, InstantIso8601Serializer::class)

package org.stellar.walletsdk.anchor

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.*
import org.stellar.walletsdk.horizon.PublicKeyPair
import org.stellar.walletsdk.json.AnchorTransactionSerializer
import org.stellar.walletsdk.json.NullableAccountAsStringSerializer

/**
 * Represents SEP-24 anchor transaction.
 *
 * @constructor Create empty Anchor transaction
 */
@Serializable(AnchorTransactionSerializer::class)
sealed interface AnchorTransaction {
  val id: String
  val status: TransactionStatus
  val moreInfoUrl: String
  val startedAt: Instant
  val message: String?
}

sealed interface ProcessingAnchorTransaction : AnchorTransaction {
  val statusEta: String?
  val kycVerified: Boolean?
  val amountInAsset: String?
  val amountIn: String
  val amountOutAsset: String?
  val amountOut: String
  val feeDetails: FeeDetails?
  val completedAt: Instant?
  val stellarTransactionId: String?
  val externalTransactionId: String?
  val refunds: Refunds?
}

sealed interface IncompleteAnchorTransaction : AnchorTransaction

@Serializable
data class DepositTransaction(
  override val id: String,
  override val status: TransactionStatus,
  @SerialName("status_eta") override val statusEta: String? = null,
  @SerialName("kyc_verified") override val kycVerified: Boolean? = null,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("amount_in_asset") override val amountInAsset: String? = null,
  @SerialName("amount_in") override val amountIn: String,
  @SerialName("amount_out_asset") override val amountOutAsset: String? = null,
  @SerialName("amount_out") override val amountOut: String,
  @SerialName("fee_details") override val feeDetails: FeeDetails,
  @SerialName("started_at") override val startedAt: Instant,
  @SerialName("completed_at") override val completedAt: Instant? = null,
  @SerialName("stellar_transaction_id") override val stellarTransactionId: String? = null,
  @SerialName("external_transaction_id") override val externalTransactionId: String? = null,
  override val message: String? = null,
  override val refunds: Refunds? = null,
  val from: PublicKeyPair? = null,
  val to: PublicKeyPair?,
  @SerialName("deposit_memo") val depositMemo: String? = null,
  @SerialName("deposit_memo_type") val depositMemoType: MemoType? = null,
  @SerialName("claimable_balance_id") val claimableBalanceId: String? = null
) : ProcessingAnchorTransaction

@Serializable
data class WithdrawalTransaction(
  override val id: String,
  override val status: TransactionStatus,
  @SerialName("status_eta") override val statusEta: String? = null,
  @SerialName("kyc_verified") override val kycVerified: Boolean? = null,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("amount_in_asset") override val amountInAsset: String? = null,
  @SerialName("amount_in") override val amountIn: String,
  @SerialName("amount_out_asset") override val amountOutAsset: String? = null,
  @SerialName("amount_out") override val amountOut: String,
  @SerialName("fee_details") override val feeDetails: FeeDetails,
  @SerialName("started_at") override val startedAt: Instant,
  @SerialName("completed_at") override val completedAt: Instant? = null,
  @SerialName("stellar_transaction_id") override val stellarTransactionId: String? = null,
  @SerialName("external_transaction_id") override val externalTransactionId: String? = null,
  override val message: String? = null,
  override val refunds: Refunds? = null,
  val from: PublicKeyPair? = null,
  val to: PublicKeyPair? = null,
  @SerialName("withdraw_memo") val withdrawalMemo: String? = null,
  @SerialName("withdraw_memo_type") val withdrawalMemoType: MemoType,
  @SerialName("withdraw_anchor_account") val withdrawAnchorAccount: String
) : ProcessingAnchorTransaction

@Serializable
data class IncompleteWithdrawalTransaction(
  override val id: String,
  override val status: TransactionStatus,
  @SerialName("more_info_url") override val moreInfoUrl: String = "",
  @SerialName("started_at") override val startedAt: Instant,
  override val message: String? = null,
  @Serializable(with = NullableAccountAsStringSerializer::class) val from: PublicKeyPair?,
) : IncompleteAnchorTransaction

@Serializable
data class IncompleteDepositTransaction(
  override val id: String,
  override val status: TransactionStatus,
  @SerialName("more_info_url") override val moreInfoUrl: String = "",
  @SerialName("started_at") override val startedAt: Instant,
  override val message: String? = null,
  @Serializable(with = NullableAccountAsStringSerializer::class) val to: PublicKeyPair?,
) : IncompleteAnchorTransaction

@Serializable
data class ErrorTransaction(
  override val id: String,
  val kind: TransactionKind,
  override val status: TransactionStatus,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("started_at") override val startedAt: Instant,
  override val message: String? = null,

  // Fields from withdrawal/deposit transactions that may present in error transaction
  @SerialName("status_eta") val statusEta: String? = null,
  @SerialName("kyc_verified") val kycVerified: Boolean? = null,
  @SerialName("amount_in_asset") val amountInAsset: String? = null,
  @SerialName("amount_in") val amountIn: String? = null,
  @SerialName("amount_out_asset") val amountOutAsset: String? = null,
  @SerialName("amount_out") val amountOut: String? = null,
  @SerialName("fee_details") val feeDetails: FeeDetails,
  @SerialName("completed_at") val completedAt: String? = null,
  @SerialName("stellar_transaction_id") val stellarTransactionId: String? = null,
  @SerialName("external_transaction_id") val externalTransactionId: String? = null,
  val refunds: Refunds? = null,
  val from: PublicKeyPair? = null,
  val to: PublicKeyPair? = null,
  @SerialName("deposit_memo") val depositMemo: String? = null,
  @SerialName("deposit_memo_type") val depositMemoType: MemoType? = null,
  @SerialName("claimable_balance_id") val claimableBalanceId: String? = null,
  @SerialName("withdraw_memo") val withdrawalMemo: String? = null,
  @SerialName("withdraw_memo_type") val withdrawalMemoType: MemoType? = null,
  @SerialName("withdraw_anchor_account") val withdrawAnchorAccount: String? = null
) : AnchorTransaction

enum class TransactionKind {
  @SerialName("deposit") DEPOSIT,
  @SerialName("withdrawal") WITHDRAWAL
}
