package org.stellar.walletsdk.anchor

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.stellar.sdk.Memo
import org.stellar.walletsdk.util.GlobalConfig
import org.stellar.walletsdk.util.Util.isHex

@Serializable
data class AnchorServiceAsset(
  val enabled: Boolean,
  val min_amount: Double? = null,
  val max_amount: Double? = null,
  val fee_fixed: Double? = null,
  val fee_percent: Double? = null
)

@Serializable
data class AnchorServiceFeatures(val account_creation: Boolean, val claimable_balances: Boolean)

@Serializable data class AnchorServiceFee(val enabled: Boolean)

@Serializable
data class AnchorServiceInfo(
  val deposit: Map<String, AnchorServiceAsset>,
  val withdraw: Map<String, AnchorServiceAsset>,
  val fee: AnchorServiceFee,
  val features: AnchorServiceFeatures,
)

// TODO: polymorphism based on kind
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("kind")
data class AnchorTransaction(
  val id: String,
  val kind: String,
  val status: String,
  @SerialName("status_eta") val statusEta: String? = null,
  @SerialName("kyc_verified") val kycVerified: Boolean? = null,
  @SerialName("more_info_url") val moreInfoUrl: String,
  @SerialName("amount_in_asset") val amountInAsset: String? = null,
  @SerialName("amount_in") val amountIn: String,
  @SerialName("amount_out_asset") val amountOutAsset: String? = null,
  @SerialName("amount_out") val amountOut: String,
  @SerialName("amount_fee_asset") val amountFeeAsset: String? = null,
  @SerialName("amount_fee") val amountFee: String,
  @SerialName("started_at") val startedAt: String,
  @SerialName("completed_at") val completedAt: String? = null,
  @SerialName("stellar_transaction_id") val stellarTransactionId: String,
  @SerialName("external_transaction_id") val externalTransactionId: String? = null,
  val message: String? = null,
  val refunds: Refunds? = null,
  val from: String,
  val to: String,
  val withdraw_memo_type: MemoType,
  val withdraw_memo: String,
  val withdraw_anchor_account: String
)

@Serializable
data class Refunds(
  @SerialName("amount_fee") val amountFee: String,
  @SerialName("amount_refunded") val amountRefunded: String,
  @SerialName("payments") val payments: List<Payment>
)

@Serializable
data class Payment(
  @SerialName("amount") val amount: String,
  @SerialName("fee") val fee: String,
  @SerialName("id") val id: String,
  @SerialName("id_type") val idType: String
)

enum class MemoType(val mapper: (String) -> Memo) {
  @SerialName("text") TEXT(Memo::text),
  /** Hash memo. Supports hex or base64 string encoding */
  @SerialName("hash") HASH(::hash),
  @SerialName("id") ID({ Memo.id(it.toLong()) })
}

private fun hash(s: String): Memo {
  return if (s.isHex()) Memo.hash(s) else Memo.hash(GlobalConfig.base64Decoder(s))
}

@Serializable data class AnchorTransactionStatusResponse(val transaction: AnchorTransaction)

@Serializable data class AnchorAllTransactionsResponse(val transactions: List<AnchorTransaction>)
