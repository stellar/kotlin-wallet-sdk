@file:UseSerializers(
  AssetIdSerializer::class,
  AccountAsStringSerializer::class,
  InstantIso8601Serializer::class
)

package org.stellar.walletsdk.anchor

import kotlin.js.JsExport
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.*
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.exception.IncorrectTransactionStatusException
import org.stellar.walletsdk.horizon.PublicKeyPair
import org.stellar.walletsdk.json.AccountAsStringSerializer
import org.stellar.walletsdk.json.AnchorTransactionSerializer
import org.stellar.walletsdk.json.AssetIdSerializer

@Serializable(AnchorTransactionSerializer::class)
@JsExport
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
  val amountInAsset: AssetId?
  val amountIn: String
  val amountOutAsset: AssetId?
  val amountOut: String
  val amountFeeAsset: AssetId?
  val amountFee: String
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
  @SerialName("amount_in_asset") override val amountInAsset: AssetId? = null,
  @SerialName("amount_in") override val amountIn: String,
  @SerialName("amount_out_asset") override val amountOutAsset: AssetId? = null,
  @SerialName("amount_out") override val amountOut: String,
  @SerialName("amount_fee_asset") override val amountFeeAsset: AssetId? = null,
  @SerialName("amount_fee") override val amountFee: String,
  @SerialName("started_at") override val startedAt: Instant,
  @SerialName("completed_at") override val completedAt: Instant? = null,
  @SerialName("stellar_transaction_id") override val stellarTransactionId: String? = null,
  @SerialName("external_transaction_id") override val externalTransactionId: String? = null,
  override val message: String? = null,
  override val refunds: Refunds? = null,
  val from: PublicKeyPair? = null,
  val to: PublicKeyPair,
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
  @SerialName("amount_in_asset") override val amountInAsset: AssetId? = null,
  @SerialName("amount_in") override val amountIn: String,
  @SerialName("amount_out_asset") override val amountOutAsset: AssetId? = null,
  @SerialName("amount_out") override val amountOut: String,
  @SerialName("amount_fee_asset") override val amountFeeAsset: AssetId? = null,
  @SerialName("amount_fee") override val amountFee: String,
  @SerialName("started_at") override val startedAt: Instant,
  @SerialName("completed_at") override val completedAt: Instant? = null,
  @SerialName("stellar_transaction_id") override val stellarTransactionId: String? = null,
  @SerialName("external_transaction_id") override val externalTransactionId: String? = null,
  override val message: String? = null,
  override val refunds: Refunds? = null,
  val from: PublicKeyPair,
  val to: PublicKeyPair? = null,
  @SerialName("withdraw_memo") val withdrawalMemo: String,
  @SerialName("withdraw_memo_type") val withdrawalMemoType: MemoType,
  @SerialName("withdraw_anchor_account") val withdrawAnchorAccount: String
) : ProcessingAnchorTransaction

@Serializable
data class IncompleteWithdrawalTransaction(
  override val id: String,
  override val status: TransactionStatus,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("started_at") override val startedAt: Instant,
  override val message: String? = null,
  val from: PublicKeyPair,
) : IncompleteAnchorTransaction

@Serializable
data class IncompleteDepositTransaction(
  override val id: String,
  override val status: TransactionStatus,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("started_at") override val startedAt: Instant,
  override val message: String? = null,
  val to: PublicKeyPair,
) : IncompleteAnchorTransaction

@Serializable
data class ErrorTransaction(
  override val id: String,
  val kind: String,
  override val status: TransactionStatus,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("started_at") override val startedAt: Instant,
  override val message: String? = null,

  // Fields from withdrawal/deposit transactions that may present in error transaction
  @SerialName("status_eta") val statusEta: String? = null,
  @SerialName("kyc_verified") val kycVerified: Boolean? = null,
  @SerialName("amount_in_asset") val amountInAsset: AssetId? = null,
  @SerialName("amount_in") val amountIn: String? = null,
  @SerialName("amount_out_asset") val amountOutAsset: AssetId? = null,
  @SerialName("amount_out") val amountOut: String? = null,
  @SerialName("amount_fee_asset") val amountFeeAsset: AssetId? = null,
  @SerialName("amount_fee") val amountFee: String? = null,
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

fun AnchorTransaction.requireStatus(requiredStatus: TransactionStatus) {
  if (this.status != requiredStatus) {
    throw IncorrectTransactionStatusException(this, requiredStatus)
  }
}
