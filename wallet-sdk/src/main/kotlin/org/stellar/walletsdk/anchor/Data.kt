package org.stellar.walletsdk.anchor

data class AnchorServiceAsset(
  val enabled: Boolean,
  val min_amount: Double,
  val max_amount: Double,
  val fee_fixed: Double,
  val fee_percent: Double
)

data class AnchorServiceFeatures(val account_creation: Boolean, val claimable_balances: Boolean)

data class AnchorServiceFee(val enabled: Boolean)

data class AnchorServiceInfo(
  val deposit: Map<String, AnchorServiceAsset>,
  val withdraw: Map<String, AnchorServiceAsset>,
  val fee: AnchorServiceFee,
  val features: AnchorServiceFeatures,
)

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
)

data class AnchorTransactionStatusResponse(val transaction: AnchorTransaction)

data class AnchorAllTransactionsResponse(val transactions: List<AnchorTransaction>)
