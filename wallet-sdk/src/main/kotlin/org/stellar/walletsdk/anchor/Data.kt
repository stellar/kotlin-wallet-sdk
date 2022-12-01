package org.stellar.walletsdk.anchor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.sdk.Memo
import org.stellar.walletsdk.util.GlobalConfig
import org.stellar.walletsdk.util.Util.isHex

@Serializable
data class AnchorServiceAsset(
  val enabled: Boolean,
  val min_amount: Double,
  val max_amount: Double,
  val fee_fixed: Double,
  val fee_percent: Double
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
@Serializable
data class AnchorTransaction(
  val id: String,
  val kind: String,
  val status: String,
  val more_info_url: String,
  val amount_in: String,
  val amount_out: String,
  val amount_fee: String,
  val started_at: String,
  val stellar_transaction_id: String,
  val from: String,
  val to: String,
  val message: String,
  val withdraw_memo_type: MemoType,
  val withdraw_memo: String,
  val withdraw_anchor_account: String
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
