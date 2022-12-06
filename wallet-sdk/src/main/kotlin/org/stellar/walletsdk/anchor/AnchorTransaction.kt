@file:UseSerializers(AssetIdSerializer::class)

package org.stellar.walletsdk.anchor

import kotlinx.serialization.*
import org.stellar.walletsdk.asset.AssetId
import org.stellar.walletsdk.json.AnchorTransactionSerializer
import org.stellar.walletsdk.json.AssetIdSerializer

@Serializable(AnchorTransactionSerializer::class)
sealed interface AnchorTransaction {
  val id: String
  val status: String
  val moreInfoUrl: String
  val startedAt: String
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
  val completedAt: String?
  val stellarTransactionId: String?
  val externalTransactionId: String?
  val refunds: Refunds?
}

sealed interface IncompleteAnchorTransaction : AnchorTransaction

@Serializable
data class DepositTransaction(
  override val id: String,
  override val status: String,
  @SerialName("status_eta") override val statusEta: String? = null,
  @SerialName("kyc_verified") override val kycVerified: Boolean? = null,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("amount_in_asset") override val amountInAsset: AssetId? = null,
  @SerialName("amount_in") override val amountIn: String,
  @SerialName("amount_out_asset") override val amountOutAsset: AssetId? = null,
  @SerialName("amount_out") override val amountOut: String,
  @SerialName("amount_fee_asset") override val amountFeeAsset: AssetId? = null,
  @SerialName("amount_fee") override val amountFee: String,
  @SerialName("started_at") override val startedAt: String,
  @SerialName("completed_at") override val completedAt: String? = null,
  @SerialName("stellar_transaction_id") override val stellarTransactionId: String?,
  @SerialName("external_transaction_id") override val externalTransactionId: String? = null,
  override val message: String? = null,
  override val refunds: Refunds? = null,
  val from: String?,
  val to: String,
  @SerialName("deposit_memo") val depositMemo: String? = null,
  @SerialName("deposit_memo_type") val depositMemoType: MemoType? = null,
  @SerialName("claimable_balance_id") val claimableBalanceId: String? = null
) : ProcessingAnchorTransaction

@Serializable
data class WithdrawalTransaction(
  override val id: String,
  override val status: String,
  @SerialName("status_eta") override val statusEta: String? = null,
  @SerialName("kyc_verified") override val kycVerified: Boolean? = null,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("amount_in_asset") override val amountInAsset: AssetId? = null,
  @SerialName("amount_in") override val amountIn: String,
  @SerialName("amount_out_asset") override val amountOutAsset: AssetId? = null,
  @SerialName("amount_out") override val amountOut: String,
  @SerialName("amount_fee_asset") override val amountFeeAsset: AssetId? = null,
  @SerialName("amount_fee") override val amountFee: String,
  @SerialName("started_at") override val startedAt: String,
  @SerialName("completed_at") override val completedAt: String? = null,
  @SerialName("stellar_transaction_id") override val stellarTransactionId: String?,
  @SerialName("external_transaction_id") override val externalTransactionId: String? = null,
  override val message: String? = null,
  override val refunds: Refunds? = null,
  val from: String,
  val to: String?,
  @SerialName("withdraw_memo") val withdrawalMemo: String,
  @SerialName("withdraw_memo_type") val withdrawalMemoType: MemoType,
  @SerialName("withdraw_anchor_account") val withdrawAnchorAccount: String
) : ProcessingAnchorTransaction

@Serializable
data class IncompleteWithdrawalTransaction(
  override val id: String,
  val kind: String,
  override val status: String,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("started_at") override val startedAt: String,
  override val message: String? = null,
  val from: String,
) : IncompleteAnchorTransaction

@Serializable
data class IncompleteDepositTransaction(
  override val id: String,
  val kind: String,
  override val status: String,
  @SerialName("more_info_url") override val moreInfoUrl: String,
  @SerialName("started_at") override val startedAt: String,
  override val message: String? = null,
  val to: String,
) : IncompleteAnchorTransaction
