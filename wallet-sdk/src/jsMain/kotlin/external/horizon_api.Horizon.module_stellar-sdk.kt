@file:JsQualifier("Horizon")
@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external

import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

// import tsstdlib.`T$3`

external interface ResponseLink {
  var href: String
  var templated: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface BaseResponse<T : String> {
  var _links: Any
}

external interface SubmitTransactionResponse {
  var hash: String
  var ledger: Number
  var successful: Boolean
  var envelope_xdr: String
  var result_xdr: String
  var result_meta_xdr: String
}

external interface FeeBumpTransactionResponse {
  var hash: String
  var signatures: Array<String>
}

external interface InnerTransactionResponse {
  var hash: String
  var signatures: Array<String>
  var max_fee: String
}

external interface `T$21` {
  var min_time: String
  var max_time: String
}

external interface `T$22` {
  var min_ledger: Number
  var max_ledger: Number
}

external interface TransactionPreconditions {
  var timebounds: `T$21`?
    get() = definedExternally
    set(value) = definedExternally
  var ledgerbounds: `T$22`?
    get() = definedExternally
    set(value) = definedExternally
  var min_account_sequence: String?
    get() = definedExternally
    set(value) = definedExternally
  var min_account_sequence_age: String?
    get() = definedExternally
    set(value) = definedExternally
  var min_account_sequence_ledger_gap: Number?
    get() = definedExternally
    set(value) = definedExternally
  var extra_signers: Array<String>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface TransactionResponse :
  SubmitTransactionResponse,
  BaseResponse<
    String /* "account" | "ledger" | "operations" | "effects" | "succeeds" | "precedes" */> {
  var created_at: String
  var fee_meta_xdr: String
  var fee_charged: dynamic /* Number | String */
    get() = definedExternally
    set(value) = definedExternally
  var max_fee: dynamic /* Number | String */
    get() = definedExternally
    set(value) = definedExternally
  var id: String
  var memo_type: Any
  var memo: String?
    get() = definedExternally
    set(value) = definedExternally
  var memo_bytes: String?
    get() = definedExternally
    set(value) = definedExternally
  var operation_count: Number
  var paging_token: String
  var signatures: Array<String>
  var source_account: String
  var source_account_sequence: String
  var fee_account: String
  var inner_transaction: InnerTransactionResponse?
    get() = definedExternally
    set(value) = definedExternally
  var fee_bump_transaction: FeeBumpTransactionResponse?
    get() = definedExternally
    set(value) = definedExternally
  var preconditions: TransactionPreconditions?
    get() = definedExternally
    set(value) = definedExternally
}

external interface BalanceLineNative {
  var balance: String
  var asset_type: String /* "native" */
  var buying_liabilities: String
  var selling_liabilities: String
}

external interface BalanceLineLiquidityPool {
  var liquidity_pool_id: String
  var asset_type: String /* "liquidity_pool_shares" */
  var balance: String
  var limit: String
  var last_modified_ledger: Number
  var is_authorized: Boolean
  var is_authorized_to_maintain_liabilities: Boolean
  var is_clawback_enabled: Boolean
  var sponsor: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface BalanceLineAsset<T : String> {
  var balance: String
  var limit: String
  var asset_type: T
  var asset_code: String
  var asset_issuer: String
  var buying_liabilities: String
  var selling_liabilities: String
  var last_modified_ledger: Number
  var is_authorized: Boolean
  var is_authorized_to_maintain_liabilities: Boolean
  var is_clawback_enabled: Boolean
  var sponsor: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface AssetAccounts {
  var authorized: Number
  var authorized_to_maintain_liabilities: Number
  var unauthorized: Number
}

external interface AssetBalances {
  var authorized: String
  var authorized_to_maintain_liabilities: String
  var unauthorized: String
}

external interface PriceR {
  var numerator: Number
  var denominator: Number
}

external interface PriceRShorthand {
  var n: Number
  var d: Number
}

external interface AccountThresholds {
  var low_threshold: Number
  var med_threshold: Number
  var high_threshold: Number
}

external interface Flags {
  var auth_immutable: Boolean
  var auth_required: Boolean
  var auth_revocable: Boolean
  var auth_clawback_enabled: Boolean
}

external interface AccountSigner {
  var key: String
  var weight: Number
  var type: String
  var sponsor: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface AccountResponse :
  BaseResponse<
    String /* "transactions" | "operations" | "payments" | "effects" | "offers" | "trades" | "data" */
  > {
  var id: String
  var paging_token: String
  var account_id: String
  var sequence: String
  var sequence_ledger: Number?
    get() = definedExternally
    set(value) = definedExternally
  var sequence_time: String?
    get() = definedExternally
    set(value) = definedExternally
  var subentry_count: Number
  var thresholds: AccountThresholds
  var last_modified_ledger: Number
  var last_modified_time: String
  var flags: Flags
  //    var balances: Array<BalanceLine>
  var signers: Array<AccountSigner>
  //    var data: `T$3`
  var sponsor: String?
    get() = definedExternally
    set(value) = definedExternally
  var num_sponsoring: Number
  var num_sponsored: Number
}

external enum class LiquidityPoolType {
  constantProduct /* = "constant_product" */
}

external enum class OperationResponseType {
  createAccount /* = "create_account" */,
  payment /* = "payment" */,
  pathPayment /* = "path_payment_strict_receive" */,
  createPassiveOffer /* = "create_passive_sell_offer" */,
  manageOffer /* = "manage_sell_offer" */,
  setOptions /* = "set_options" */,
  changeTrust /* = "change_trust" */,
  allowTrust /* = "allow_trust" */,
  accountMerge /* = "account_merge" */,
  inflation /* = "inflation" */,
  manageData /* = "manage_data" */,
  bumpSequence /* = "bump_sequence" */,
  manageBuyOffer /* = "manage_buy_offer" */,
  pathPaymentStrictSend /* = "path_payment_strict_send" */,
  createClaimableBalance /* = "create_claimable_balance" */,
  claimClaimableBalance /* = "claim_claimable_balance" */,
  beginSponsoringFutureReserves /* = "begin_sponsoring_future_reserves" */,
  endSponsoringFutureReserves /* = "end_sponsoring_future_reserves" */,
  revokeSponsorship /* = "revoke_sponsorship" */,
  clawback /* = "clawback" */,
  clawbackClaimableBalance /* = "clawback_claimable_balance" */,
  setTrustLineFlags /* = "set_trust_line_flags" */,
  liquidityPoolDeposit /* = "liquidity_pool_deposit" */,
  liquidityPoolWithdraw /* = "liquidity_pool_withdraw" */
}

external enum class OperationResponseTypeI {
  createAccount /* = 0 */,
  payment /* = 1 */,
  pathPayment /* = 2 */,
  createPassiveOffer /* = 3 */,
  manageOffer /* = 4 */,
  setOptions /* = 5 */,
  changeTrust /* = 6 */,
  allowTrust /* = 7 */,
  accountMerge /* = 8 */,
  inflation /* = 9 */,
  manageData /* = 10 */,
  bumpSequence /* = 11 */,
  manageBuyOffer /* = 12 */,
  pathPaymentStrictSend /* = 13 */,
  createClaimableBalance /* = 14 */,
  claimClaimableBalance /* = 15 */,
  beginSponsoringFutureReserves /* = 16 */,
  endSponsoringFutureReserves /* = 17 */,
  revokeSponsorship /* = 18 */,
  clawback /* = 19 */,
  clawbackClaimableBalance /* = 20 */,
  setTrustLineFlags /* = 21 */,
  liquidityPoolDeposit /* = 22 */,
  liquidityPoolWithdraw /* = 23 */
}

external interface BaseOperationResponse<T : OperationResponseType, TI : OperationResponseTypeI> :
  BaseResponse<String /* "succeeds" | "precedes" | "effects" | "transaction" */> {
  var id: String
  var paging_token: String
  var source_account: String
  var type: T
  var type_i: TI
  var created_at: String
  var transaction_hash: String
}

external interface CreateAccountOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var account: String
  var funder: String
  var starting_balance: String
}

external interface PaymentOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var from: String
  var to: String
  var asset_type: Any
  var asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var amount: String
}

external interface `T$23` {
  var asset_code: String
  var asset_issuer: String
  var asset_type: Any
}

external interface PathPaymentOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var amount: String
  var asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var asset_type: Any
  var from: String
  var path: Array<`T$23`>
  var source_amount: String
  var source_asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var source_asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var source_asset_type: Any
  var source_max: String
  var to: String
}

external interface PathPaymentStrictSendOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var amount: String
  var asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var asset_type: Any
  var destination_min: String
  var from: String
  var path: Array<`T$23`>
  var source_amount: String
  var source_asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var source_asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var source_asset_type: Any
  var to: String
}

external interface ManageOfferOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var offer_id: dynamic /* Number | String */
    get() = definedExternally
    set(value) = definedExternally
  var amount: String
  var buying_asset_type: Any
  var buying_asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var buying_asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var price: String
  var price_r: PriceR
  var selling_asset_type: Any
  var selling_asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var selling_asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface PassiveOfferOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var offer_id: dynamic /* Number | String */
    get() = definedExternally
    set(value) = definedExternally
  var amount: String
  var buying_asset_type: Any
  var buying_asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var buying_asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var price: String
  var price_r: PriceR
  var selling_asset_type: Any
  var selling_asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var selling_asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SetOptionsOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var signer_key: String?
    get() = definedExternally
    set(value) = definedExternally
  var signer_weight: Number?
    get() = definedExternally
    set(value) = definedExternally
  var master_key_weight: Number?
    get() = definedExternally
    set(value) = definedExternally
  var low_threshold: Number?
    get() = definedExternally
    set(value) = definedExternally
  var med_threshold: Number?
    get() = definedExternally
    set(value) = definedExternally
  var high_threshold: Number?
    get() = definedExternally
    set(value) = definedExternally
  var home_domain: String?
    get() = definedExternally
    set(value) = definedExternally
  var set_flags: Array<Number /* 1 | 2 | 4 */>
  var set_flags_s:
    Array<String /* "auth_required_flag" | "auth_revocable_flag" | "auth_clawback_enabled_flag" */>
  var clear_flags: Array<Number /* 1 | 2 | 4 */>
  var clear_flags_s:
    Array<String /* "auth_required_flag" | "auth_revocable_flag" | "auth_clawback_enabled_flag" */>
}

external interface ChangeTrustOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var asset_type: String /* "credit_alphanum4" | "credit_alphanum12" | "liquidity_pool_shares" */
  var asset_code: String?
    get() = definedExternally
    set(value) = definedExternally
  var asset_issuer: String?
    get() = definedExternally
    set(value) = definedExternally
  var liquidity_pool_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var trustee: String?
    get() = definedExternally
    set(value) = definedExternally
  var trustor: String
  var limit: String
}

external interface AllowTrustOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var asset_type: Any
  var asset_code: String
  var asset_issuer: String
  var authorize: Boolean
  var authorize_to_maintain_liabilities: Boolean
  var trustee: String
  var trustor: String
}

external interface AccountMergeOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var into: String
}

external interface InflationOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI>

external interface ManageDataOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var name: String
  var value: Buffer
}

external interface BumpSequenceOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var bump_to: String
}

external interface Predicate {
  var and: Array<Predicate>?
    get() = definedExternally
    set(value) = definedExternally
  var or: Array<Predicate>?
    get() = definedExternally
    set(value) = definedExternally
  var not: Predicate?
    get() = definedExternally
    set(value) = definedExternally
  var abs_before: String?
    get() = definedExternally
    set(value) = definedExternally
  var rel_before: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface Claimant {
  var destination: String
  var predicate: Predicate
}

external interface CreateClaimableBalanceOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var asset: String
  var amount: String
  var sponsor: String
  var claimants: Array<Claimant>
}

external interface ClaimClaimableBalanceOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var balance_id: String
  var claimant: String
}

external interface BeginSponsoringFutureReservesOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var sponsored_id: String
}

external interface EndSponsoringFutureReservesOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var begin_sponsor: String
}

external interface RevokeSponsorshipOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var account_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var claimable_balance_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var data_account_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var data_name: String?
    get() = definedExternally
    set(value) = definedExternally
  var offer_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var trustline_account_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var trustline_asset: String?
    get() = definedExternally
    set(value) = definedExternally
  var trustline_liquidity_pool_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var signer_account_id: String?
    get() = definedExternally
    set(value) = definedExternally
  var signer_key: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface ClawbackOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var asset_type: Any
  var asset_code: String
  var asset_issuer: String
  var from: String
  var amount: String
}

external interface ClawbackClaimableBalanceOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var balance_id: String
}

external interface SetTrustLineFlagsOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var asset_type: Any
  var asset_code: String
  var asset_issuer: String
  var trustor: String
  var set_flags: Array<Number /* 1 | 2 | 4 */>
  var clear_flags: Array<Number /* 1 | 2 | 4 */>
}

external interface Reserve {
  var asset: String
  var amount: String
}

external interface DepositLiquidityOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var liquidity_pool_id: String
  var reserves_max: Array<Reserve>
  var min_price: String
  var min_price_r: PriceRShorthand
  var max_price: String
  var max_price_r: PriceRShorthand
  var reserves_deposited: Array<Reserve>
  var shares_received: String
}

external interface WithdrawLiquidityOperationResponse :
  BaseOperationResponse<OperationResponseType, OperationResponseTypeI> {
  var liquidity_pool_id: String
  var reserves_min: Array<Reserve>
  var shares: String
  var reserves_received: Array<Reserve>
}

external interface `T$24` {
  var self: ResponseLink
  var next: ResponseLink
  var prev: ResponseLink
}

external interface `T$25`<T> {
  var records: Array<T>
}

external interface ResponseCollection<T : BaseResponse<*>> {
  var _links: `T$24`
  var _embedded: `T$25`<Any?>
}

external interface TransactionResponseCollection : ResponseCollection<TransactionResponse>

external interface FeeDistribution {
  var max: String
  var min: String
  var mode: String
  var p10: String
  var p20: String
  var p30: String
  var p40: String
  var p50: String
  var p60: String
  var p70: String
  var p80: String
  var p90: String
  var p95: String
  var p99: String
}

external interface FeeStatsResponse {
  var last_ledger: String
  var last_ledger_base_fee: String
  var ledger_capacity_usage: String
  var fee_charged: FeeDistribution
  var max_fee: FeeDistribution
}

external enum class TransactionFailedResultCodes {
  TX_FAILED /* = "tx_failed" */,
  TX_BAD_SEQ /* = "tx_bad_seq" */,
  TX_BAD_AUTH /* = "tx_bad_auth" */,
  TX_BAD_AUTH_EXTRA /* = "tx_bad_auth_extra" */,
  TX_FEE_BUMP_INNER_SUCCESS /* = "tx_fee_bump_inner_success" */,
  TX_FEE_BUMP_INNER_FAILED /* = "tx_fee_bump_inner_failed" */,
  TX_NOT_SUPPORTED /* = "tx_not_supported" */,
  TX_SUCCESS /* = "tx_success" */,
  TX_TOO_EARLY /* = "tx_too_early" */,
  TX_TOO_LATE /* = "tx_too_late" */,
  TX_MISSING_OPERATION /* = "tx_missing_operation" */,
  TX_INSUFFICIENT_BALANCE /* = "tx_insufficient_balance" */,
  TX_NO_SOURCE_ACCOUNT /* = "tx_no_source_account" */,
  TX_INSUFFICIENT_FEE /* = "tx_insufficient_fee" */,
  TX_INTERNAL_ERROR /* = "tx_internal_error" */
}

external interface `T$26` {
  var transaction: TransactionFailedResultCodes
  var operations: Array<String>
}

external interface TransactionFailedExtras {
  var envelope_xdr: String
  var result_codes: `T$26`
  var result_xdr: String
}
