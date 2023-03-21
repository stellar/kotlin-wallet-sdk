@file:JsQualifier("ServerApi")
@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external

import external.horizon.AccountThresholds
import external.horizon.Flags
import external.horizon.LiquidityPoolType
import external.horizon.Reserve
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

external interface CollectionPage<T : BaseResponse__0> {
  var records: Array<T>
  var next: () -> Promise<CollectionPage<T>>
  var prev: () -> Promise<CollectionPage<T>>
}

external interface CallFunctionTemplateOptions {
  var cursor: dynamic /* String? | Number? */
    get() = definedExternally
    set(value) = definedExternally
  var limit: Number?
    get() = definedExternally
    set(value) = definedExternally
  var order: String? /* "asc" | "desc" */
    get() = definedExternally
    set(value) = definedExternally
}

external interface ClaimableBalanceRecord : BaseResponse__0 {
  var id: String
  var paging_token: String
  var asset: String
  var amount: String
  var sponsor: String?
    get() = definedExternally
    set(value) = definedExternally
  var last_modified_ledger: Number
  var claimants: Array<Claimant>
}

external interface `T$225` {
  var value: String
}

external interface AccountRecord : BaseResponse__0 {
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
  var home_domain: String?
    get() = definedExternally
    set(value) = definedExternally
  var inflation_destination: String?
    get() = definedExternally
    set(value) = definedExternally
  var last_modified_ledger: Number
  var last_modified_time: String
  var thresholds: AccountThresholds
  var flags: Flags
  //    var balances: Array<BalanceLine>
  //    var signers: Array<AccountRecordSigners>
  var data: (options: `T$225`) -> Promise<`T$225`>
  //    var data_attr: `T$3`
  var sponsor: String?
    get() = definedExternally
    set(value) = definedExternally
  var num_sponsoring: Number
  var num_sponsored: Number
  //    var effects: CallCollectionFunction<dynamic /* Effects.AccountCreated |
  // Effects.AccountCredited | Effects.AccountDebited | Effects.AccountThresholdsUpdated |
  // Effects.AccountHomeDomainUpdated | Effects.AccountFlagsUpdated | Effects.DataCreated |
  // Effects.DataRemoved | Effects.DataUpdated | Effects.SequenceBumped | Effects.SignerCreated |
  // Effects.SignerRemoved | Effects.SignerUpdated | Effects.TrustlineCreated |
  // Effects.TrustlineRemoved | Effects.TrustlineUpdated | Effects.TrustlineAuthorized |
  // Effects.TrustlineDeauthorized | Effects.TrustlineAuthorizedToMaintainLiabilities |
  // Effects.ClaimableBalanceCreated | Effects.ClaimableBalanceClaimed |
  // Effects.ClaimableBalanceClaimantCreated | Omit<AccountSponsorshipEvents, String /*
  // "new_sponsor" | "former_sponsor" */> & `T$230` | Omit<AccountSponsorshipEvents, String /*
  // "new_sponsor" | "sponsor" */> & `T$232` | Omit<AccountSponsorshipEvents, String /* "sponsor"
  // */> & `T$231` | Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */>
  // & `T$233` | Omit<TrustlineSponsorshipEvents, String /* "sponsor" */> & `T$234` |
  // Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$235` |
  // Omit<DataSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$236` |
  // Omit<DataSponsorshipEvents, String /* "sponsor" */> & `T$237` | Omit<DataSponsorshipEvents,
  // String /* "new_sponsor" | "sponsor" */> & `T$238` | Omit<ClaimableBalanceSponsorshipEvents,
  // String /* "new_sponsor" | "former_sponsor" */> & `T$239` |
  // Omit<ClaimableBalanceSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$241` |
  // Omit<ClaimableBalanceSponsorshipEvents, String /* "sponsor" */> & `T$240` |
  // Omit<SignerSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$242` |
  // Omit<SignerSponsorshipEvents, String /* "sponsor" */> & `T$243` | Omit<SignerSponsorshipEvents,
  // String /* "new_sponsor" | "sponsor" */> & `T$244` | Trade */>
  //    var offers: CallCollectionFunction<OfferRecord>
  //    var operations: CallCollectionFunction<dynamic /* CreateAccountOperationRecord |
  // PaymentOperationRecord | PathPaymentOperationRecord | ManageOfferOperationRecord |
  // PassiveOfferOperationRecord | SetOptionsOperationRecord | ChangeTrustOperationRecord |
  // AllowTrustOperationRecord | AccountMergeOperationRecord | InflationOperationRecord |
  // ManageDataOperationRecord | BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord
  // | CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
  // BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
  // RevokeSponsorshipOperationRecord */>
  //    var payments: CallCollectionFunction<PaymentOperationRecord>
  //    var trades: CallCollectionFunction<dynamic /* TradeRecord.Orderbook |
  // TradeRecord.LiquidityPool */>
}

external interface LiquidityPoolRecord : BaseResponse__0 {
  var id: String
  var paging_token: String
  var fee_bp: Number
  var type: LiquidityPoolType
  var total_trustlines: String
  var total_shares: String
  var reserves: Array<Reserve>
}

external enum class TradeType {
  all /* = "all" */,
  liquidityPools /* = "liquidity_pool" */,
  orderbook /* = "orderbook" */
}

// external interface EffectRecordMethods {
//    var operation: CallFunction<dynamic /* CreateAccountOperationRecord | PaymentOperationRecord |
// PathPaymentOperationRecord | ManageOfferOperationRecord | PassiveOfferOperationRecord |
// SetOptionsOperationRecord | ChangeTrustOperationRecord | AllowTrustOperationRecord |
// AccountMergeOperationRecord | InflationOperationRecord | ManageDataOperationRecord |
// BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord |
// CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
// BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
// RevokeSponsorshipOperationRecord */>?
//        get() = definedExternally
//        set(value) = definedExternally
//    var precedes: CallFunction<dynamic /* Effects.AccountCreated | Effects.AccountCredited |
// Effects.AccountDebited | Effects.AccountThresholdsUpdated | Effects.AccountHomeDomainUpdated |
// Effects.AccountFlagsUpdated | Effects.DataCreated | Effects.DataRemoved | Effects.DataUpdated |
// Effects.SequenceBumped | Effects.SignerCreated | Effects.SignerRemoved | Effects.SignerUpdated |
// Effects.TrustlineCreated | Effects.TrustlineRemoved | Effects.TrustlineUpdated |
// Effects.TrustlineAuthorized | Effects.TrustlineDeauthorized |
// Effects.TrustlineAuthorizedToMaintainLiabilities | Effects.ClaimableBalanceCreated |
// Effects.ClaimableBalanceClaimed | Effects.ClaimableBalanceClaimantCreated |
// Omit<AccountSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$230` |
// Omit<AccountSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$232` |
// Omit<AccountSponsorshipEvents, String /* "sponsor" */> & `T$231` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$233` |
// Omit<TrustlineSponsorshipEvents, String /* "sponsor" */> & `T$234` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$235` |
// Omit<DataSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$236` |
// Omit<DataSponsorshipEvents, String /* "sponsor" */> & `T$237` | Omit<DataSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$238` | Omit<ClaimableBalanceSponsorshipEvents,
// String /* "new_sponsor" | "former_sponsor" */> & `T$239` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$241` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "sponsor" */> & `T$240` |
// Omit<SignerSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$242` |
// Omit<SignerSponsorshipEvents, String /* "sponsor" */> & `T$243` | Omit<SignerSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$244` | Trade */>?
//        get() = definedExternally
//        set(value) = definedExternally
//    var succeeds: CallFunction<dynamic /* Effects.AccountCreated | Effects.AccountCredited |
// Effects.AccountDebited | Effects.AccountThresholdsUpdated | Effects.AccountHomeDomainUpdated |
// Effects.AccountFlagsUpdated | Effects.DataCreated | Effects.DataRemoved | Effects.DataUpdated |
// Effects.SequenceBumped | Effects.SignerCreated | Effects.SignerRemoved | Effects.SignerUpdated |
// Effects.TrustlineCreated | Effects.TrustlineRemoved | Effects.TrustlineUpdated |
// Effects.TrustlineAuthorized | Effects.TrustlineDeauthorized |
// Effects.TrustlineAuthorizedToMaintainLiabilities | Effects.ClaimableBalanceCreated |
// Effects.ClaimableBalanceClaimed | Effects.ClaimableBalanceClaimantCreated |
// Omit<AccountSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$230` |
// Omit<AccountSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$232` |
// Omit<AccountSponsorshipEvents, String /* "sponsor" */> & `T$231` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$233` |
// Omit<TrustlineSponsorshipEvents, String /* "sponsor" */> & `T$234` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$235` |
// Omit<DataSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$236` |
// Omit<DataSponsorshipEvents, String /* "sponsor" */> & `T$237` | Omit<DataSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$238` | Omit<ClaimableBalanceSponsorshipEvents,
// String /* "new_sponsor" | "former_sponsor" */> & `T$239` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$241` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "sponsor" */> & `T$240` |
// Omit<SignerSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$242` |
// Omit<SignerSponsorshipEvents, String /* "sponsor" */> & `T$243` | Omit<SignerSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$244` | Trade */>?
//        get() = definedExternally
//        set(value) = definedExternally
// }

external interface LedgerRecord : BaseResponse__0 {
  var id: String
  var paging_token: String
  var hash: String
  var prev_hash: String
  var sequence: Number
  var successful_transaction_count: Number
  var failed_transaction_count: Number
  var operation_count: Number
  var tx_set_operation_count: Number?
  var closed_at: String
  var total_coins: String
  var fee_pool: String
  var max_tx_set_size: Number
  var protocol_version: Number
  var header_xdr: String
  var base_fee_in_stroops: Number
  var base_reserve_in_stroops: Number
  var transaction_count: Number
  var base_fee: Number
  var base_reserve: String
  //    var effects: CallCollectionFunction<dynamic /* Effects.AccountCreated |
  // Effects.AccountCredited | Effects.AccountDebited | Effects.AccountThresholdsUpdated |
  // Effects.AccountHomeDomainUpdated | Effects.AccountFlagsUpdated | Effects.DataCreated |
  // Effects.DataRemoved | Effects.DataUpdated | Effects.SequenceBumped | Effects.SignerCreated |
  // Effects.SignerRemoved | Effects.SignerUpdated | Effects.TrustlineCreated |
  // Effects.TrustlineRemoved | Effects.TrustlineUpdated | Effects.TrustlineAuthorized |
  // Effects.TrustlineDeauthorized | Effects.TrustlineAuthorizedToMaintainLiabilities |
  // Effects.ClaimableBalanceCreated | Effects.ClaimableBalanceClaimed |
  // Effects.ClaimableBalanceClaimantCreated | Omit<AccountSponsorshipEvents, String /*
  // "new_sponsor" | "former_sponsor" */> & `T$230` | Omit<AccountSponsorshipEvents, String /*
  // "new_sponsor" | "sponsor" */> & `T$232` | Omit<AccountSponsorshipEvents, String /* "sponsor"
  // */> & `T$231` | Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */>
  // & `T$233` | Omit<TrustlineSponsorshipEvents, String /* "sponsor" */> & `T$234` |
  // Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$235` |
  // Omit<DataSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$236` |
  // Omit<DataSponsorshipEvents, String /* "sponsor" */> & `T$237` | Omit<DataSponsorshipEvents,
  // String /* "new_sponsor" | "sponsor" */> & `T$238` | Omit<ClaimableBalanceSponsorshipEvents,
  // String /* "new_sponsor" | "former_sponsor" */> & `T$239` |
  // Omit<ClaimableBalanceSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$241` |
  // Omit<ClaimableBalanceSponsorshipEvents, String /* "sponsor" */> & `T$240` |
  // Omit<SignerSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$242` |
  // Omit<SignerSponsorshipEvents, String /* "sponsor" */> & `T$243` | Omit<SignerSponsorshipEvents,
  // String /* "new_sponsor" | "sponsor" */> & `T$244` | Trade */>
  //    var operations: CallCollectionFunction<dynamic /* CreateAccountOperationRecord |
  // PaymentOperationRecord | PathPaymentOperationRecord | ManageOfferOperationRecord |
  // PassiveOfferOperationRecord | SetOptionsOperationRecord | ChangeTrustOperationRecord |
  // AllowTrustOperationRecord | AccountMergeOperationRecord | InflationOperationRecord |
  // ManageDataOperationRecord | BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord
  // | CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
  // BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
  // RevokeSponsorshipOperationRecord */>
  //    var self: CallFunction<LedgerRecord>
  //    var transactions: CallCollectionFunction<TransactionRecord>
}

// external interface BaseOperationRecord<T : OperationResponseType, TI : OperationResponseTypeI> :
// BaseOperationResponse<T, TI> {
//    var self: CallFunction<dynamic /* CreateAccountOperationRecord | PaymentOperationRecord |
// PathPaymentOperationRecord | ManageOfferOperationRecord | PassiveOfferOperationRecord |
// SetOptionsOperationRecord | ChangeTrustOperationRecord | AllowTrustOperationRecord |
// AccountMergeOperationRecord | InflationOperationRecord | ManageDataOperationRecord |
// BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord |
// CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
// BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
// RevokeSponsorshipOperationRecord */>
//    var succeeds: CallFunction<dynamic /* CreateAccountOperationRecord | PaymentOperationRecord |
// PathPaymentOperationRecord | ManageOfferOperationRecord | PassiveOfferOperationRecord |
// SetOptionsOperationRecord | ChangeTrustOperationRecord | AllowTrustOperationRecord |
// AccountMergeOperationRecord | InflationOperationRecord | ManageDataOperationRecord |
// BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord |
// CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
// BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
// RevokeSponsorshipOperationRecord */>
//    var precedes: CallFunction<dynamic /* CreateAccountOperationRecord | PaymentOperationRecord |
// PathPaymentOperationRecord | ManageOfferOperationRecord | PassiveOfferOperationRecord |
// SetOptionsOperationRecord | ChangeTrustOperationRecord | AllowTrustOperationRecord |
// AccountMergeOperationRecord | InflationOperationRecord | ManageDataOperationRecord |
// BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord |
// CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
// BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
// RevokeSponsorshipOperationRecord */>
//    var effects: CallCollectionFunction<dynamic /* Effects.AccountCreated |
// Effects.AccountCredited | Effects.AccountDebited | Effects.AccountThresholdsUpdated |
// Effects.AccountHomeDomainUpdated | Effects.AccountFlagsUpdated | Effects.DataCreated |
// Effects.DataRemoved | Effects.DataUpdated | Effects.SequenceBumped | Effects.SignerCreated |
// Effects.SignerRemoved | Effects.SignerUpdated | Effects.TrustlineCreated |
// Effects.TrustlineRemoved | Effects.TrustlineUpdated | Effects.TrustlineAuthorized |
// Effects.TrustlineDeauthorized | Effects.TrustlineAuthorizedToMaintainLiabilities |
// Effects.ClaimableBalanceCreated | Effects.ClaimableBalanceClaimed |
// Effects.ClaimableBalanceClaimantCreated | Omit<AccountSponsorshipEvents, String /* "new_sponsor"
// | "former_sponsor" */> & `T$230` | Omit<AccountSponsorshipEvents, String /* "new_sponsor" |
// "sponsor" */> & `T$232` | Omit<AccountSponsorshipEvents, String /* "sponsor" */> & `T$231` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$233` |
// Omit<TrustlineSponsorshipEvents, String /* "sponsor" */> & `T$234` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$235` |
// Omit<DataSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$236` |
// Omit<DataSponsorshipEvents, String /* "sponsor" */> & `T$237` | Omit<DataSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$238` | Omit<ClaimableBalanceSponsorshipEvents,
// String /* "new_sponsor" | "former_sponsor" */> & `T$239` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$241` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "sponsor" */> & `T$240` |
// Omit<SignerSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$242` |
// Omit<SignerSponsorshipEvents, String /* "sponsor" */> & `T$243` | Omit<SignerSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$244` | Trade */>
//    var transaction: CallFunction<TransactionRecord>
// }
//
// external interface CreateAccountOperationRecord :
// BaseOperationRecord<OperationResponseType.createAccount, OperationResponseTypeI.createAccount>,
// CreateAccountOperationResponse
//
// external interface PaymentOperationRecord : BaseOperationRecord<OperationResponseType.payment,
// OperationResponseTypeI.payment>, PaymentOperationResponse {
//    var sender: CallFunction<AccountRecord>
//    var receiver: CallFunction<AccountRecord>
// }
//
// external interface PathPaymentOperationRecord :
// BaseOperationRecord<OperationResponseType.pathPayment, OperationResponseTypeI.pathPayment>,
// PathPaymentOperationResponse
//
// external interface PathPaymentStrictSendOperationRecord :
// BaseOperationRecord<OperationResponseType.pathPaymentStrictSend,
// OperationResponseTypeI.pathPaymentStrictSend>, PathPaymentStrictSendOperationResponse
//
// external interface ManageOfferOperationRecord :
// BaseOperationRecord<OperationResponseType.manageOffer, OperationResponseTypeI.manageOffer>,
// ManageOfferOperationResponse
//
// external interface PassiveOfferOperationRecord :
// BaseOperationRecord<OperationResponseType.createPassiveOffer,
// OperationResponseTypeI.createPassiveOffer>, PassiveOfferOperationResponse
//
// external interface SetOptionsOperationRecord :
// BaseOperationRecord<OperationResponseType.setOptions, OperationResponseTypeI.setOptions>,
// SetOptionsOperationResponse
//
// external interface ChangeTrustOperationRecord :
// BaseOperationRecord<OperationResponseType.changeTrust, OperationResponseTypeI.changeTrust>,
// ChangeTrustOperationResponse
//
// external interface AllowTrustOperationRecord :
// BaseOperationRecord<OperationResponseType.allowTrust, OperationResponseTypeI.allowTrust>,
// AllowTrustOperationResponse
//
// external interface AccountMergeOperationRecord :
// BaseOperationRecord<OperationResponseType.accountMerge, OperationResponseTypeI.accountMerge>,
// AccountMergeOperationResponse
//
// external interface InflationOperationRecord :
// BaseOperationRecord<OperationResponseType.inflation, OperationResponseTypeI.inflation>,
// InflationOperationResponse
//
// external interface ManageDataOperationRecord :
// BaseOperationRecord<OperationResponseType.manageData, OperationResponseTypeI.manageData>,
// ManageDataOperationResponse
//
// external interface BumpSequenceOperationRecord :
// BaseOperationRecord<OperationResponseType.bumpSequence, OperationResponseTypeI.bumpSequence>,
// BumpSequenceOperationResponse
//
// external interface CreateClaimableBalanceOperationRecord :
// BaseOperationRecord<OperationResponseType.createClaimableBalance,
// OperationResponseTypeI.createClaimableBalance>, CreateClaimableBalanceOperationResponse
//
// external interface ClaimClaimableBalanceOperationRecord :
// BaseOperationRecord<OperationResponseType.claimClaimableBalance,
// OperationResponseTypeI.claimClaimableBalance>, ClaimClaimableBalanceOperationResponse
//
// external interface BeginSponsoringFutureReservesOperationRecord :
// BaseOperationRecord<OperationResponseType.beginSponsoringFutureReserves,
// OperationResponseTypeI.beginSponsoringFutureReserves>,
// BeginSponsoringFutureReservesOperationResponse
//
// external interface EndSponsoringFutureReservesOperationRecord :
// BaseOperationRecord<OperationResponseType.endSponsoringFutureReserves,
// OperationResponseTypeI.endSponsoringFutureReserves>, EndSponsoringFutureReservesOperationResponse
//
// external interface RevokeSponsorshipOperationRecord :
// BaseOperationRecord<OperationResponseType.revokeSponsorship,
// OperationResponseTypeI.revokeSponsorship>, RevokeSponsorshipOperationResponse
//
// external interface TransactionRecord : Omit<TransactionResponse, String /* "ledger" */> {
//    var ledger_attr: Any
//    var account: CallFunction<AccountRecord>
//    var effects: CallCollectionFunction<dynamic /* Effects.AccountCreated |
// Effects.AccountCredited | Effects.AccountDebited | Effects.AccountThresholdsUpdated |
// Effects.AccountHomeDomainUpdated | Effects.AccountFlagsUpdated | Effects.DataCreated |
// Effects.DataRemoved | Effects.DataUpdated | Effects.SequenceBumped | Effects.SignerCreated |
// Effects.SignerRemoved | Effects.SignerUpdated | Effects.TrustlineCreated |
// Effects.TrustlineRemoved | Effects.TrustlineUpdated | Effects.TrustlineAuthorized |
// Effects.TrustlineDeauthorized | Effects.TrustlineAuthorizedToMaintainLiabilities |
// Effects.ClaimableBalanceCreated | Effects.ClaimableBalanceClaimed |
// Effects.ClaimableBalanceClaimantCreated | Omit<AccountSponsorshipEvents, String /* "new_sponsor"
// | "former_sponsor" */> & `T$230` | Omit<AccountSponsorshipEvents, String /* "new_sponsor" |
// "sponsor" */> & `T$232` | Omit<AccountSponsorshipEvents, String /* "sponsor" */> & `T$231` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$233` |
// Omit<TrustlineSponsorshipEvents, String /* "sponsor" */> & `T$234` |
// Omit<TrustlineSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$235` |
// Omit<DataSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$236` |
// Omit<DataSponsorshipEvents, String /* "sponsor" */> & `T$237` | Omit<DataSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$238` | Omit<ClaimableBalanceSponsorshipEvents,
// String /* "new_sponsor" | "former_sponsor" */> & `T$239` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "new_sponsor" | "sponsor" */> & `T$241` |
// Omit<ClaimableBalanceSponsorshipEvents, String /* "sponsor" */> & `T$240` |
// Omit<SignerSponsorshipEvents, String /* "new_sponsor" | "former_sponsor" */> & `T$242` |
// Omit<SignerSponsorshipEvents, String /* "sponsor" */> & `T$243` | Omit<SignerSponsorshipEvents,
// String /* "new_sponsor" | "sponsor" */> & `T$244` | Trade */>
//    var ledger: CallFunction<LedgerRecord>
//    var operations: CallCollectionFunction<dynamic /* CreateAccountOperationRecord |
// PaymentOperationRecord | PathPaymentOperationRecord | ManageOfferOperationRecord |
// PassiveOfferOperationRecord | SetOptionsOperationRecord | ChangeTrustOperationRecord |
// AllowTrustOperationRecord | AccountMergeOperationRecord | InflationOperationRecord |
// ManageDataOperationRecord | BumpSequenceOperationRecord | PathPaymentStrictSendOperationRecord |
// CreateClaimableBalanceOperationRecord | ClaimClaimableBalanceOperationRecord |
// BeginSponsoringFutureReservesOperationRecord | EndSponsoringFutureReservesOperationRecord |
// RevokeSponsorshipOperationRecord */>
//    var precedes: CallFunction<TransactionRecord>
//    var self: CallFunction<TransactionRecord>
//    var succeeds: CallFunction<TransactionRecord>
// }

external interface `T$227` {
  var d: Number
  var n: Number
}

external interface `T$228` {
  var price_r: `T$227`
  var price: String
  var amount: String
}

// external interface OrderbookRecord : BaseResponse__0 {
//    var bids: Array<`T$228`>
//    var asks: Array<`T$228`>
//    var base: Asset
//    var counter: Asset
// }

external interface `T$229` {
  var asset_code: String
  var asset_issuer: String
  var asset_type: String
}

external interface PaymentPathRecord : BaseResponse__0 {
  var path: Array<`T$229`>
  var source_amount: String
  var source_asset_type: String
  var source_asset_code: String
  var source_asset_issuer: String
  var destination_amount: String
  var destination_asset_type: String
  var destination_asset_code: String
  var destination_asset_issuer: String
}
