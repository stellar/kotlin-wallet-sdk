package org.stellar.walletsdk.anchor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.sdk.Memo
import org.stellar.walletsdk.ApplicationConfiguration
import org.stellar.walletsdk.util.Util.isHex

@Suppress("ConstructorParameterNaming")
@Serializable
data class AnchorServiceAsset(
  val enabled: Boolean,
  val min_amount: Double? = null,
  val max_amount: Double? = null,
  val fee_fixed: Double? = null,
  val fee_percent: Double? = null
)

@Suppress("ConstructorParameterNaming")
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

enum class MemoType(val mapper: (String, cfg: ApplicationConfiguration) -> Memo) {
  @SerialName("text") TEXT({ s, _ -> Memo.text(s) }),
  /** Hash memo. Supports hex or base64 string encoding */
  @SerialName("hash") HASH(::hash),
  @SerialName("id") ID({ s, _ -> Memo.id(s.toLong()) })
}

private fun hash(s: String, cfg: ApplicationConfiguration): Memo {
  return if (s.isHex()) Memo.hash(s) else Memo.hash(cfg.base64Decoder(s))
}

@Serializable data class AnchorTransactionStatusResponse(val transaction: AnchorTransaction)

@Serializable data class AnchorAllTransactionsResponse(val transactions: List<AnchorTransaction>)
