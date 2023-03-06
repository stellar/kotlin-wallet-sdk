@file:JsQualifier("xdr")
@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external.xdr

import external.Buffer
import external.`T$102`
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

external interface SignedInt {
  var MAX_VALUE: Number /* 2147483647 */
  var MIN_VALUE: String /* "-2147483648" */
  fun read(io: Buffer): Number
  fun write(value: Number, io: Buffer)
  fun isValid(value: Number): Boolean
  fun toXDR(value: Number): Buffer
  fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Number
  fun fromXDR(input: Buffer): Number
  fun fromXDR(input: String, format: String /* "hex" | "base64" */): Number
  fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  fun validateXDR(input: Buffer): Boolean
  fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external interface UnsignedInt {
  var MAX_VALUE: Number /* 4294967295 */
  var MIN_VALUE: Number /* 0 */
  fun read(io: Buffer): Number
  fun write(value: Number, io: Buffer)
  fun isValid(value: Number): Boolean
  fun toXDR(value: Number): Buffer
  fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Number
  fun fromXDR(input: Buffer): Number
  fun fromXDR(input: String, format: String /* "hex" | "base64" */): Number
  fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  fun validateXDR(input: Buffer): Boolean
  fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external interface Bool {
  fun read(io: Buffer): Boolean
  fun write(value: Boolean, io: Buffer)
  fun isValid(value: Boolean): Boolean
  fun toXDR(value: Boolean): Buffer
  fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  fun fromXDR(input: Buffer): Boolean
  fun fromXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  fun validateXDR(input: Buffer): Boolean
  fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external open class Hyper(low: Number, high: Number) {
  open var low: Number
  open var high: Number
  open var unsigned: Boolean
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun toXDR(value: Hyper): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Hyper
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Hyper
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
    var MAX_VALUE: Hyper
    var MIN_VALUE: Hyper
    fun read(io: Buffer): Hyper
    fun write(value: Hyper, io: Buffer)
    fun fromString(input: String): Hyper
    fun fromBytes(low: Number, high: Number): Hyper
    fun isValid(value: Hyper): Boolean
  }
}

external open class UnsignedHyper(low: Number, high: Number) {
  open var low: Number
  open var high: Number
  open var unsigned: Boolean
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun toXDR(value: UnsignedHyper): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): UnsignedHyper
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): UnsignedHyper
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
    var MAX_VALUE: UnsignedHyper
    var MIN_VALUE: UnsignedHyper
    fun read(io: Buffer): UnsignedHyper
    fun write(value: UnsignedHyper, io: Buffer)
    fun fromString(input: String): UnsignedHyper
    fun fromBytes(low: Number, high: Number): UnsignedHyper
    fun isValid(value: UnsignedHyper): Boolean
  }
}

external open class XDRString(maxLength: Number /* 4294967295 */) {
  open fun read(io: Buffer): Buffer
  open fun readString(io: Buffer): String
  open fun write(value: String, io: Buffer)
  open fun write(value: Buffer, io: Buffer)
  open fun isValid(value: String): Boolean
  open fun isValid(value: Array<Number>): Boolean
  open fun isValid(value: Buffer): Boolean
  open fun toXDR(value: String): Buffer
  open fun toXDR(value: Buffer): Buffer
  open fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Buffer
  open fun fromXDR(input: Buffer): Buffer
  open fun fromXDR(input: String, format: String /* "hex" | "base64" */): Buffer
  open fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  open fun validateXDR(input: Buffer): Boolean
  open fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external open class XDRArray<T> {
  open fun read(io: Buffer): Buffer
  open fun write(value: Array<T>, io: Buffer)
  open fun isValid(value: Array<T>): Boolean
  open fun toXDR(value: Array<T>): Buffer
  open fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Array<T>
  open fun fromXDR(input: Buffer): Array<T>
  open fun fromXDR(input: String, format: String /* "hex" | "base64" */): Array<T>
  open fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  open fun validateXDR(input: Buffer): Boolean
  open fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external open class Opaque(length: Number) {
  open fun read(io: Buffer): Buffer
  open fun write(value: Buffer, io: Buffer)
  open fun isValid(value: Buffer): Boolean
  open fun toXDR(value: Buffer): Buffer
  open fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Buffer
  open fun fromXDR(input: Buffer): Buffer
  open fun fromXDR(input: String, format: String /* "hex" | "base64" */): Buffer
  open fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  open fun validateXDR(input: Buffer): Boolean
  open fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external open class VarOpaque(length: Number) : Opaque

external interface `T$106` {
  fun read(io: Any): Any
  fun write(value: Any, io: Buffer)
  fun isValid(value: Any): Boolean
}

external open class Option(childType: `T$106`) {
  open fun read(io: Buffer): Any
  open fun write(value: Any, io: Buffer)
  open fun isValid(value: Any): Boolean
  open fun toXDR(value: Any): Buffer
  open fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Any
  open fun fromXDR(input: Buffer): Any
  open fun fromXDR(input: String, format: String /* "hex" | "base64" */): Any
  open fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
  open fun validateXDR(input: Buffer): Boolean
  open fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
}

external open class ScpStatementType {
  open var name: String /* "scpStPrepare" | "scpStConfirm" | "scpStExternalize" | "scpStNominate" */
  open var value: Number /* 0 | 1 | 2 | 3 */

  companion object {
    fun scpStPrepare(): ScpStatementType
    fun scpStConfirm(): ScpStatementType
    fun scpStExternalize(): ScpStatementType
    fun scpStNominate(): ScpStatementType
  }
}

external open class AssetType {
  open var name:
    String /* "assetTypeNative" | "assetTypeCreditAlphanum4" | "assetTypeCreditAlphanum12" | "assetTypePoolShare" */
  open var value: Number /* 0 | 1 | 2 | 3 */

  companion object {
    fun assetTypeNative(): AssetType
    fun assetTypeCreditAlphanum4(): AssetType
    fun assetTypeCreditAlphanum12(): AssetType
    fun assetTypePoolShare(): AssetType
  }
}

external open class ThresholdIndices {
  open var name:
    String /* "thresholdMasterWeight" | "thresholdLow" | "thresholdMed" | "thresholdHigh" */
  open var value: Number /* 0 | 1 | 2 | 3 */

  companion object {
    fun thresholdMasterWeight(): ThresholdIndices
    fun thresholdLow(): ThresholdIndices
    fun thresholdMed(): ThresholdIndices
    fun thresholdHigh(): ThresholdIndices
  }
}

external open class LedgerEntryType {
  open var name:
    String /* "account" | "trustline" | "offer" | "data" | "claimableBalance" | "liquidityPool" */
  open var value: Number /* 0 | 1 | 2 | 3 | 4 | 5 */

  companion object {
    fun account(): LedgerEntryType
    fun trustline(): LedgerEntryType
    fun offer(): LedgerEntryType
    fun data(): LedgerEntryType
    fun claimableBalance(): LedgerEntryType
    fun liquidityPool(): LedgerEntryType
  }
}

external open class AccountFlags {
  open var name:
    String /* "authRequiredFlag" | "authRevocableFlag" | "authImmutableFlag" | "authClawbackEnabledFlag" */
  open var value: Number /* 1 | 2 | 4 | 8 */

  companion object {
    fun authRequiredFlag(): AccountFlags
    fun authRevocableFlag(): AccountFlags
    fun authImmutableFlag(): AccountFlags
    fun authClawbackEnabledFlag(): AccountFlags
  }
}

external open class TrustLineFlags {
  open var name:
    String /* "authorizedFlag" | "authorizedToMaintainLiabilitiesFlag" | "trustlineClawbackEnabledFlag" */
  open var value: Number /* 1 | 2 | 4 */

  companion object {
    fun authorizedFlag(): TrustLineFlags
    fun authorizedToMaintainLiabilitiesFlag(): TrustLineFlags
    fun trustlineClawbackEnabledFlag(): TrustLineFlags
  }
}

external open class LiquidityPoolType {
  open var name: String /* "liquidityPoolConstantProduct" */
  open var value: Number /* 0 */

  companion object {
    fun liquidityPoolConstantProduct(): LiquidityPoolType
  }
}

external open class OfferEntryFlags {
  open var name: String /* "passiveFlag" */
  open var value: Number /* 1 */

  companion object {
    fun passiveFlag(): OfferEntryFlags
  }
}

external open class ClaimPredicateType {
  open var name:
    String /* "claimPredicateUnconditional" | "claimPredicateAnd" | "claimPredicateOr" | "claimPredicateNot" | "claimPredicateBeforeAbsoluteTime" | "claimPredicateBeforeRelativeTime" */
  open var value: Number /* 0 | 1 | 2 | 3 | 4 | 5 */

  companion object {
    fun claimPredicateUnconditional(): ClaimPredicateType
    fun claimPredicateAnd(): ClaimPredicateType
    fun claimPredicateOr(): ClaimPredicateType
    fun claimPredicateNot(): ClaimPredicateType
    fun claimPredicateBeforeAbsoluteTime(): ClaimPredicateType
    fun claimPredicateBeforeRelativeTime(): ClaimPredicateType
  }
}

external open class ClaimantType {
  open var name: String /* "claimantTypeV0" */
  open var value: Number /* 0 */

  companion object {
    fun claimantTypeV0(): ClaimantType
  }
}

external open class ClaimableBalanceIdType {
  open var name: String /* "claimableBalanceIdTypeV0" */
  open var value: Number /* 0 */

  companion object {
    fun claimableBalanceIdTypeV0(): ClaimableBalanceIdType
  }
}

external open class ClaimableBalanceFlags {
  open var name: String /* "claimableBalanceClawbackEnabledFlag" */
  open var value: Number /* 1 */

  companion object {
    fun claimableBalanceClawbackEnabledFlag(): ClaimableBalanceFlags
  }
}

external open class EnvelopeType {
  open var name:
    String /* "envelopeTypeTxV0" | "envelopeTypeScp" | "envelopeTypeTx" | "envelopeTypeAuth" | "envelopeTypeScpvalue" | "envelopeTypeTxFeeBump" | "envelopeTypeOpId" | "envelopeTypePoolRevokeOpId" */
  open var value: Number /* 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 */

  companion object {
    fun envelopeTypeTxV0(): EnvelopeType
    fun envelopeTypeScp(): EnvelopeType
    fun envelopeTypeTx(): EnvelopeType
    fun envelopeTypeAuth(): EnvelopeType
    fun envelopeTypeScpvalue(): EnvelopeType
    fun envelopeTypeTxFeeBump(): EnvelopeType
    fun envelopeTypeOpId(): EnvelopeType
    fun envelopeTypePoolRevokeOpId(): EnvelopeType
  }
}

external open class StellarValueType {
  open var name: String /* "stellarValueBasic" | "stellarValueSigned" */
  open var value: Number /* 0 | 1 */

  companion object {
    fun stellarValueBasic(): StellarValueType
    fun stellarValueSigned(): StellarValueType
  }
}

external open class LedgerHeaderFlags {
  open var name:
    String /* "disableLiquidityPoolTradingFlag" | "disableLiquidityPoolDepositFlag" | "disableLiquidityPoolWithdrawalFlag" */
  open var value: Number /* 1 | 2 | 4 */

  companion object {
    fun disableLiquidityPoolTradingFlag(): LedgerHeaderFlags
    fun disableLiquidityPoolDepositFlag(): LedgerHeaderFlags
    fun disableLiquidityPoolWithdrawalFlag(): LedgerHeaderFlags
  }
}

external open class LedgerUpgradeType {
  open var name:
    String /* "ledgerUpgradeVersion" | "ledgerUpgradeBaseFee" | "ledgerUpgradeMaxTxSetSize" | "ledgerUpgradeBaseReserve" | "ledgerUpgradeFlags" */
  open var value: Number /* 1 | 2 | 3 | 4 | 5 */

  companion object {
    fun ledgerUpgradeVersion(): LedgerUpgradeType
    fun ledgerUpgradeBaseFee(): LedgerUpgradeType
    fun ledgerUpgradeMaxTxSetSize(): LedgerUpgradeType
    fun ledgerUpgradeBaseReserve(): LedgerUpgradeType
    fun ledgerUpgradeFlags(): LedgerUpgradeType
  }
}

external open class BucketEntryType {
  open var name: String /* "metaentry" | "liveentry" | "deadentry" | "initentry" */
  open var value: dynamic /* "-1" | 0 | 1 | 2 */

  companion object {
    fun metaentry(): BucketEntryType
    fun liveentry(): BucketEntryType
    fun deadentry(): BucketEntryType
    fun initentry(): BucketEntryType
  }
}

external open class TxSetComponentType {
  open var name: String /* "txsetCompTxsMaybeDiscountedFee" */
  open var value: Number /* 0 */

  companion object {
    fun txsetCompTxsMaybeDiscountedFee(): TxSetComponentType
  }
}

external open class LedgerEntryChangeType {
  open var name:
    String /* "ledgerEntryCreated" | "ledgerEntryUpdated" | "ledgerEntryRemoved" | "ledgerEntryState" */
  open var value: Number /* 0 | 1 | 2 | 3 */

  companion object {
    fun ledgerEntryCreated(): LedgerEntryChangeType
    fun ledgerEntryUpdated(): LedgerEntryChangeType
    fun ledgerEntryRemoved(): LedgerEntryChangeType
    fun ledgerEntryState(): LedgerEntryChangeType
  }
}

external open class ErrorCode {
  open var name: String /* "errMisc" | "errData" | "errConf" | "errAuth" | "errLoad" */
  open var value: Number /* 0 | 1 | 2 | 3 | 4 */

  companion object {
    fun errMisc(): ErrorCode
    fun errData(): ErrorCode
    fun errConf(): ErrorCode
    fun errAuth(): ErrorCode
    fun errLoad(): ErrorCode
  }
}

external open class IpAddrType {
  open var name: String /* "iPv4" | "iPv6" */
  open var value: Number /* 0 | 1 */

  companion object {
    fun iPv4(): IpAddrType
    fun iPv6(): IpAddrType
  }
}

external open class MessageType {
  open var name:
    String /* "errorMsg" | "auth" | "dontHave" | "getPeers" | "peers" | "getTxSet" | "txSet" | "generalizedTxSet" | "transaction" | "getScpQuorumset" | "scpQuorumset" | "scpMessage" | "getScpState" | "hello" | "surveyRequest" | "surveyResponse" | "sendMore" */
  open var value:
    Number /* 0 | 2 | 3 | 4 | 5 | 6 | 7 | 17 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 */

  companion object {
    fun errorMsg(): MessageType
    fun auth(): MessageType
    fun dontHave(): MessageType
    fun getPeers(): MessageType
    fun peers(): MessageType
    fun getTxSet(): MessageType
    fun txSet(): MessageType
    fun generalizedTxSet(): MessageType
    fun transaction(): MessageType
    fun getScpQuorumset(): MessageType
    fun scpQuorumset(): MessageType
    fun scpMessage(): MessageType
    fun getScpState(): MessageType
    fun hello(): MessageType
    fun surveyRequest(): MessageType
    fun surveyResponse(): MessageType
    fun sendMore(): MessageType
  }
}

external open class SurveyMessageCommandType {
  open var name: String /* "surveyTopology" */
  open var value: Number /* 0 */

  companion object {
    fun surveyTopology(): SurveyMessageCommandType
  }
}

external open class OperationType {
  open var name:
    String /* "createAccount" | "payment" | "pathPaymentStrictReceive" | "manageSellOffer" | "createPassiveSellOffer" | "setOptions" | "changeTrust" | "allowTrust" | "accountMerge" | "inflation" | "manageData" | "bumpSequence" | "manageBuyOffer" | "pathPaymentStrictSend" | "createClaimableBalance" | "claimClaimableBalance" | "beginSponsoringFutureReserves" | "endSponsoringFutureReserves" | "revokeSponsorship" | "clawback" | "clawbackClaimableBalance" | "setTrustLineFlags" | "liquidityPoolDeposit" | "liquidityPoolWithdraw" */
  open var value:
    Number /* 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 */

  companion object {
    fun createAccount(): OperationType
    fun payment(): OperationType
    fun pathPaymentStrictReceive(): OperationType
    fun manageSellOffer(): OperationType
    fun createPassiveSellOffer(): OperationType
    fun setOptions(): OperationType
    fun changeTrust(): OperationType
    fun allowTrust(): OperationType
    fun accountMerge(): OperationType
    fun inflation(): OperationType
    fun manageData(): OperationType
    fun bumpSequence(): OperationType
    fun manageBuyOffer(): OperationType
    fun pathPaymentStrictSend(): OperationType
    fun createClaimableBalance(): OperationType
    fun claimClaimableBalance(): OperationType
    fun beginSponsoringFutureReserves(): OperationType
    fun endSponsoringFutureReserves(): OperationType
    fun revokeSponsorship(): OperationType
    fun clawback(): OperationType
    fun clawbackClaimableBalance(): OperationType
    fun setTrustLineFlags(): OperationType
    fun liquidityPoolDeposit(): OperationType
    fun liquidityPoolWithdraw(): OperationType
  }
}

external open class RevokeSponsorshipType {
  open var name: String /* "revokeSponsorshipLedgerEntry" | "revokeSponsorshipSigner" */
  open var value: Number /* 0 | 1 */

  companion object {
    fun revokeSponsorshipLedgerEntry(): RevokeSponsorshipType
    fun revokeSponsorshipSigner(): RevokeSponsorshipType
  }
}

external open class MemoType {
  open var name: String /* "memoNone" | "memoText" | "memoId" | "memoHash" | "memoReturn" */
  open var value: Number /* 0 | 1 | 2 | 3 | 4 */

  companion object {
    fun memoNone(): MemoType
    fun memoText(): MemoType
    fun memoId(): MemoType
    fun memoHash(): MemoType
    fun memoReturn(): MemoType
  }
}

external open class PreconditionType {
  open var name: String /* "precondNone" | "precondTime" | "precondV2" */
  open var value: Number /* 0 | 1 | 2 */

  companion object {
    fun precondNone(): PreconditionType
    fun precondTime(): PreconditionType
    fun precondV2(): PreconditionType
  }
}

external open class ClaimAtomType {
  open var name:
    String /* "claimAtomTypeV0" | "claimAtomTypeOrderBook" | "claimAtomTypeLiquidityPool" */
  open var value: Number /* 0 | 1 | 2 */

  companion object {
    fun claimAtomTypeV0(): ClaimAtomType
    fun claimAtomTypeOrderBook(): ClaimAtomType
    fun claimAtomTypeLiquidityPool(): ClaimAtomType
  }
}

external open class CreateAccountResultCode {
  open var name:
    String /* "createAccountSuccess" | "createAccountMalformed" | "createAccountUnderfunded" | "createAccountLowReserve" | "createAccountAlreadyExist" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" */

  companion object {
    fun createAccountSuccess(): CreateAccountResultCode
    fun createAccountMalformed(): CreateAccountResultCode
    fun createAccountUnderfunded(): CreateAccountResultCode
    fun createAccountLowReserve(): CreateAccountResultCode
    fun createAccountAlreadyExist(): CreateAccountResultCode
  }
}

external open class PaymentResultCode {
  open var name:
    String /* "paymentSuccess" | "paymentMalformed" | "paymentUnderfunded" | "paymentSrcNoTrust" | "paymentSrcNotAuthorized" | "paymentNoDestination" | "paymentNoTrust" | "paymentNotAuthorized" | "paymentLineFull" | "paymentNoIssuer" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" */

  companion object {
    fun paymentSuccess(): PaymentResultCode
    fun paymentMalformed(): PaymentResultCode
    fun paymentUnderfunded(): PaymentResultCode
    fun paymentSrcNoTrust(): PaymentResultCode
    fun paymentSrcNotAuthorized(): PaymentResultCode
    fun paymentNoDestination(): PaymentResultCode
    fun paymentNoTrust(): PaymentResultCode
    fun paymentNotAuthorized(): PaymentResultCode
    fun paymentLineFull(): PaymentResultCode
    fun paymentNoIssuer(): PaymentResultCode
  }
}

external open class PathPaymentStrictReceiveResultCode {
  open var name:
    String /* "pathPaymentStrictReceiveSuccess" | "pathPaymentStrictReceiveMalformed" | "pathPaymentStrictReceiveUnderfunded" | "pathPaymentStrictReceiveSrcNoTrust" | "pathPaymentStrictReceiveSrcNotAuthorized" | "pathPaymentStrictReceiveNoDestination" | "pathPaymentStrictReceiveNoTrust" | "pathPaymentStrictReceiveNotAuthorized" | "pathPaymentStrictReceiveLineFull" | "pathPaymentStrictReceiveNoIssuer" | "pathPaymentStrictReceiveTooFewOffers" | "pathPaymentStrictReceiveOfferCrossSelf" | "pathPaymentStrictReceiveOverSendmax" */
  open var value:
    dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" | "-10" | "-11" | "-12" */

  companion object {
    fun pathPaymentStrictReceiveSuccess(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveMalformed(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveUnderfunded(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveSrcNoTrust(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveSrcNotAuthorized(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveNoDestination(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveNoTrust(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveNotAuthorized(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveLineFull(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveNoIssuer(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveTooFewOffers(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveOfferCrossSelf(): PathPaymentStrictReceiveResultCode
    fun pathPaymentStrictReceiveOverSendmax(): PathPaymentStrictReceiveResultCode
  }
}

external open class PathPaymentStrictSendResultCode {
  open var name:
    String /* "pathPaymentStrictSendSuccess" | "pathPaymentStrictSendMalformed" | "pathPaymentStrictSendUnderfunded" | "pathPaymentStrictSendSrcNoTrust" | "pathPaymentStrictSendSrcNotAuthorized" | "pathPaymentStrictSendNoDestination" | "pathPaymentStrictSendNoTrust" | "pathPaymentStrictSendNotAuthorized" | "pathPaymentStrictSendLineFull" | "pathPaymentStrictSendNoIssuer" | "pathPaymentStrictSendTooFewOffers" | "pathPaymentStrictSendOfferCrossSelf" | "pathPaymentStrictSendUnderDestmin" */
  open var value:
    dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" | "-10" | "-11" | "-12" */

  companion object {
    fun pathPaymentStrictSendSuccess(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendMalformed(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendUnderfunded(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendSrcNoTrust(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendSrcNotAuthorized(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendNoDestination(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendNoTrust(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendNotAuthorized(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendLineFull(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendNoIssuer(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendTooFewOffers(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendOfferCrossSelf(): PathPaymentStrictSendResultCode
    fun pathPaymentStrictSendUnderDestmin(): PathPaymentStrictSendResultCode
  }
}

external open class ManageSellOfferResultCode {
  open var name:
    String /* "manageSellOfferSuccess" | "manageSellOfferMalformed" | "manageSellOfferSellNoTrust" | "manageSellOfferBuyNoTrust" | "manageSellOfferSellNotAuthorized" | "manageSellOfferBuyNotAuthorized" | "manageSellOfferLineFull" | "manageSellOfferUnderfunded" | "manageSellOfferCrossSelf" | "manageSellOfferSellNoIssuer" | "manageSellOfferBuyNoIssuer" | "manageSellOfferNotFound" | "manageSellOfferLowReserve" */
  open var value:
    dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" | "-10" | "-11" | "-12" */

  companion object {
    fun manageSellOfferSuccess(): ManageSellOfferResultCode
    fun manageSellOfferMalformed(): ManageSellOfferResultCode
    fun manageSellOfferSellNoTrust(): ManageSellOfferResultCode
    fun manageSellOfferBuyNoTrust(): ManageSellOfferResultCode
    fun manageSellOfferSellNotAuthorized(): ManageSellOfferResultCode
    fun manageSellOfferBuyNotAuthorized(): ManageSellOfferResultCode
    fun manageSellOfferLineFull(): ManageSellOfferResultCode
    fun manageSellOfferUnderfunded(): ManageSellOfferResultCode
    fun manageSellOfferCrossSelf(): ManageSellOfferResultCode
    fun manageSellOfferSellNoIssuer(): ManageSellOfferResultCode
    fun manageSellOfferBuyNoIssuer(): ManageSellOfferResultCode
    fun manageSellOfferNotFound(): ManageSellOfferResultCode
    fun manageSellOfferLowReserve(): ManageSellOfferResultCode
  }
}

external open class ManageOfferEffect {
  open var name: String /* "manageOfferCreated" | "manageOfferUpdated" | "manageOfferDeleted" */
  open var value: Number /* 0 | 1 | 2 */

  companion object {
    fun manageOfferCreated(): ManageOfferEffect
    fun manageOfferUpdated(): ManageOfferEffect
    fun manageOfferDeleted(): ManageOfferEffect
  }
}

external open class ManageBuyOfferResultCode {
  open var name:
    String /* "manageBuyOfferSuccess" | "manageBuyOfferMalformed" | "manageBuyOfferSellNoTrust" | "manageBuyOfferBuyNoTrust" | "manageBuyOfferSellNotAuthorized" | "manageBuyOfferBuyNotAuthorized" | "manageBuyOfferLineFull" | "manageBuyOfferUnderfunded" | "manageBuyOfferCrossSelf" | "manageBuyOfferSellNoIssuer" | "manageBuyOfferBuyNoIssuer" | "manageBuyOfferNotFound" | "manageBuyOfferLowReserve" */
  open var value:
    dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" | "-10" | "-11" | "-12" */

  companion object {
    fun manageBuyOfferSuccess(): ManageBuyOfferResultCode
    fun manageBuyOfferMalformed(): ManageBuyOfferResultCode
    fun manageBuyOfferSellNoTrust(): ManageBuyOfferResultCode
    fun manageBuyOfferBuyNoTrust(): ManageBuyOfferResultCode
    fun manageBuyOfferSellNotAuthorized(): ManageBuyOfferResultCode
    fun manageBuyOfferBuyNotAuthorized(): ManageBuyOfferResultCode
    fun manageBuyOfferLineFull(): ManageBuyOfferResultCode
    fun manageBuyOfferUnderfunded(): ManageBuyOfferResultCode
    fun manageBuyOfferCrossSelf(): ManageBuyOfferResultCode
    fun manageBuyOfferSellNoIssuer(): ManageBuyOfferResultCode
    fun manageBuyOfferBuyNoIssuer(): ManageBuyOfferResultCode
    fun manageBuyOfferNotFound(): ManageBuyOfferResultCode
    fun manageBuyOfferLowReserve(): ManageBuyOfferResultCode
  }
}

external open class SetOptionsResultCode {
  open var name:
    String /* "setOptionsSuccess" | "setOptionsLowReserve" | "setOptionsTooManySigners" | "setOptionsBadFlags" | "setOptionsInvalidInflation" | "setOptionsCantChange" | "setOptionsUnknownFlag" | "setOptionsThresholdOutOfRange" | "setOptionsBadSigner" | "setOptionsInvalidHomeDomain" | "setOptionsAuthRevocableRequired" */
  open var value:
    dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" | "-10" */

  companion object {
    fun setOptionsSuccess(): SetOptionsResultCode
    fun setOptionsLowReserve(): SetOptionsResultCode
    fun setOptionsTooManySigners(): SetOptionsResultCode
    fun setOptionsBadFlags(): SetOptionsResultCode
    fun setOptionsInvalidInflation(): SetOptionsResultCode
    fun setOptionsCantChange(): SetOptionsResultCode
    fun setOptionsUnknownFlag(): SetOptionsResultCode
    fun setOptionsThresholdOutOfRange(): SetOptionsResultCode
    fun setOptionsBadSigner(): SetOptionsResultCode
    fun setOptionsInvalidHomeDomain(): SetOptionsResultCode
    fun setOptionsAuthRevocableRequired(): SetOptionsResultCode
  }
}

external open class ChangeTrustResultCode {
  open var name:
    String /* "changeTrustSuccess" | "changeTrustMalformed" | "changeTrustNoIssuer" | "changeTrustInvalidLimit" | "changeTrustLowReserve" | "changeTrustSelfNotAllowed" | "changeTrustTrustLineMissing" | "changeTrustCannotDelete" | "changeTrustNotAuthMaintainLiabilities" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" */

  companion object {
    fun changeTrustSuccess(): ChangeTrustResultCode
    fun changeTrustMalformed(): ChangeTrustResultCode
    fun changeTrustNoIssuer(): ChangeTrustResultCode
    fun changeTrustInvalidLimit(): ChangeTrustResultCode
    fun changeTrustLowReserve(): ChangeTrustResultCode
    fun changeTrustSelfNotAllowed(): ChangeTrustResultCode
    fun changeTrustTrustLineMissing(): ChangeTrustResultCode
    fun changeTrustCannotDelete(): ChangeTrustResultCode
    fun changeTrustNotAuthMaintainLiabilities(): ChangeTrustResultCode
  }
}

external open class AllowTrustResultCode {
  open var name:
    String /* "allowTrustSuccess" | "allowTrustMalformed" | "allowTrustNoTrustLine" | "allowTrustTrustNotRequired" | "allowTrustCantRevoke" | "allowTrustSelfNotAllowed" | "allowTrustLowReserve" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" */

  companion object {
    fun allowTrustSuccess(): AllowTrustResultCode
    fun allowTrustMalformed(): AllowTrustResultCode
    fun allowTrustNoTrustLine(): AllowTrustResultCode
    fun allowTrustTrustNotRequired(): AllowTrustResultCode
    fun allowTrustCantRevoke(): AllowTrustResultCode
    fun allowTrustSelfNotAllowed(): AllowTrustResultCode
    fun allowTrustLowReserve(): AllowTrustResultCode
  }
}

external open class AccountMergeResultCode {
  open var name:
    String /* "accountMergeSuccess" | "accountMergeMalformed" | "accountMergeNoAccount" | "accountMergeImmutableSet" | "accountMergeHasSubEntries" | "accountMergeSeqnumTooFar" | "accountMergeDestFull" | "accountMergeIsSponsor" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" */

  companion object {
    fun accountMergeSuccess(): AccountMergeResultCode
    fun accountMergeMalformed(): AccountMergeResultCode
    fun accountMergeNoAccount(): AccountMergeResultCode
    fun accountMergeImmutableSet(): AccountMergeResultCode
    fun accountMergeHasSubEntries(): AccountMergeResultCode
    fun accountMergeSeqnumTooFar(): AccountMergeResultCode
    fun accountMergeDestFull(): AccountMergeResultCode
    fun accountMergeIsSponsor(): AccountMergeResultCode
  }
}

external open class InflationResultCode {
  open var name: String /* "inflationSuccess" | "inflationNotTime" */
  open var value: dynamic /* 0 | "-1" */

  companion object {
    fun inflationSuccess(): InflationResultCode
    fun inflationNotTime(): InflationResultCode
  }
}

external open class ManageDataResultCode {
  open var name:
    String /* "manageDataSuccess" | "manageDataNotSupportedYet" | "manageDataNameNotFound" | "manageDataLowReserve" | "manageDataInvalidName" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" */

  companion object {
    fun manageDataSuccess(): ManageDataResultCode
    fun manageDataNotSupportedYet(): ManageDataResultCode
    fun manageDataNameNotFound(): ManageDataResultCode
    fun manageDataLowReserve(): ManageDataResultCode
    fun manageDataInvalidName(): ManageDataResultCode
  }
}

external open class BumpSequenceResultCode {
  open var name: String /* "bumpSequenceSuccess" | "bumpSequenceBadSeq" */
  open var value: dynamic /* 0 | "-1" */

  companion object {
    fun bumpSequenceSuccess(): BumpSequenceResultCode
    fun bumpSequenceBadSeq(): BumpSequenceResultCode
  }
}

external open class CreateClaimableBalanceResultCode {
  open var name:
    String /* "createClaimableBalanceSuccess" | "createClaimableBalanceMalformed" | "createClaimableBalanceLowReserve" | "createClaimableBalanceNoTrust" | "createClaimableBalanceNotAuthorized" | "createClaimableBalanceUnderfunded" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" */

  companion object {
    fun createClaimableBalanceSuccess(): CreateClaimableBalanceResultCode
    fun createClaimableBalanceMalformed(): CreateClaimableBalanceResultCode
    fun createClaimableBalanceLowReserve(): CreateClaimableBalanceResultCode
    fun createClaimableBalanceNoTrust(): CreateClaimableBalanceResultCode
    fun createClaimableBalanceNotAuthorized(): CreateClaimableBalanceResultCode
    fun createClaimableBalanceUnderfunded(): CreateClaimableBalanceResultCode
  }
}

external open class ClaimClaimableBalanceResultCode {
  open var name:
    String /* "claimClaimableBalanceSuccess" | "claimClaimableBalanceDoesNotExist" | "claimClaimableBalanceCannotClaim" | "claimClaimableBalanceLineFull" | "claimClaimableBalanceNoTrust" | "claimClaimableBalanceNotAuthorized" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" */

  companion object {
    fun claimClaimableBalanceSuccess(): ClaimClaimableBalanceResultCode
    fun claimClaimableBalanceDoesNotExist(): ClaimClaimableBalanceResultCode
    fun claimClaimableBalanceCannotClaim(): ClaimClaimableBalanceResultCode
    fun claimClaimableBalanceLineFull(): ClaimClaimableBalanceResultCode
    fun claimClaimableBalanceNoTrust(): ClaimClaimableBalanceResultCode
    fun claimClaimableBalanceNotAuthorized(): ClaimClaimableBalanceResultCode
  }
}

external open class BeginSponsoringFutureReservesResultCode {
  open var name:
    String /* "beginSponsoringFutureReservesSuccess" | "beginSponsoringFutureReservesMalformed" | "beginSponsoringFutureReservesAlreadySponsored" | "beginSponsoringFutureReservesRecursive" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" */

  companion object {
    fun beginSponsoringFutureReservesSuccess(): BeginSponsoringFutureReservesResultCode
    fun beginSponsoringFutureReservesMalformed(): BeginSponsoringFutureReservesResultCode
    fun beginSponsoringFutureReservesAlreadySponsored(): BeginSponsoringFutureReservesResultCode
    fun beginSponsoringFutureReservesRecursive(): BeginSponsoringFutureReservesResultCode
  }
}

external open class EndSponsoringFutureReservesResultCode {
  open var name:
    String /* "endSponsoringFutureReservesSuccess" | "endSponsoringFutureReservesNotSponsored" */
  open var value: dynamic /* 0 | "-1" */

  companion object {
    fun endSponsoringFutureReservesSuccess(): EndSponsoringFutureReservesResultCode
    fun endSponsoringFutureReservesNotSponsored(): EndSponsoringFutureReservesResultCode
  }
}

external open class RevokeSponsorshipResultCode {
  open var name:
    String /* "revokeSponsorshipSuccess" | "revokeSponsorshipDoesNotExist" | "revokeSponsorshipNotSponsor" | "revokeSponsorshipLowReserve" | "revokeSponsorshipOnlyTransferable" | "revokeSponsorshipMalformed" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" */

  companion object {
    fun revokeSponsorshipSuccess(): RevokeSponsorshipResultCode
    fun revokeSponsorshipDoesNotExist(): RevokeSponsorshipResultCode
    fun revokeSponsorshipNotSponsor(): RevokeSponsorshipResultCode
    fun revokeSponsorshipLowReserve(): RevokeSponsorshipResultCode
    fun revokeSponsorshipOnlyTransferable(): RevokeSponsorshipResultCode
    fun revokeSponsorshipMalformed(): RevokeSponsorshipResultCode
  }
}

external open class ClawbackResultCode {
  open var name:
    String /* "clawbackSuccess" | "clawbackMalformed" | "clawbackNotClawbackEnabled" | "clawbackNoTrust" | "clawbackUnderfunded" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" */

  companion object {
    fun clawbackSuccess(): ClawbackResultCode
    fun clawbackMalformed(): ClawbackResultCode
    fun clawbackNotClawbackEnabled(): ClawbackResultCode
    fun clawbackNoTrust(): ClawbackResultCode
    fun clawbackUnderfunded(): ClawbackResultCode
  }
}

external open class ClawbackClaimableBalanceResultCode {
  open var name:
    String /* "clawbackClaimableBalanceSuccess" | "clawbackClaimableBalanceDoesNotExist" | "clawbackClaimableBalanceNotIssuer" | "clawbackClaimableBalanceNotClawbackEnabled" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" */

  companion object {
    fun clawbackClaimableBalanceSuccess(): ClawbackClaimableBalanceResultCode
    fun clawbackClaimableBalanceDoesNotExist(): ClawbackClaimableBalanceResultCode
    fun clawbackClaimableBalanceNotIssuer(): ClawbackClaimableBalanceResultCode
    fun clawbackClaimableBalanceNotClawbackEnabled(): ClawbackClaimableBalanceResultCode
  }
}

external open class SetTrustLineFlagsResultCode {
  open var name:
    String /* "setTrustLineFlagsSuccess" | "setTrustLineFlagsMalformed" | "setTrustLineFlagsNoTrustLine" | "setTrustLineFlagsCantRevoke" | "setTrustLineFlagsInvalidState" | "setTrustLineFlagsLowReserve" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" */

  companion object {
    fun setTrustLineFlagsSuccess(): SetTrustLineFlagsResultCode
    fun setTrustLineFlagsMalformed(): SetTrustLineFlagsResultCode
    fun setTrustLineFlagsNoTrustLine(): SetTrustLineFlagsResultCode
    fun setTrustLineFlagsCantRevoke(): SetTrustLineFlagsResultCode
    fun setTrustLineFlagsInvalidState(): SetTrustLineFlagsResultCode
    fun setTrustLineFlagsLowReserve(): SetTrustLineFlagsResultCode
  }
}

external open class LiquidityPoolDepositResultCode {
  open var name:
    String /* "liquidityPoolDepositSuccess" | "liquidityPoolDepositMalformed" | "liquidityPoolDepositNoTrust" | "liquidityPoolDepositNotAuthorized" | "liquidityPoolDepositUnderfunded" | "liquidityPoolDepositLineFull" | "liquidityPoolDepositBadPrice" | "liquidityPoolDepositPoolFull" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" */

  companion object {
    fun liquidityPoolDepositSuccess(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositMalformed(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositNoTrust(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositNotAuthorized(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositUnderfunded(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositLineFull(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositBadPrice(): LiquidityPoolDepositResultCode
    fun liquidityPoolDepositPoolFull(): LiquidityPoolDepositResultCode
  }
}

external open class LiquidityPoolWithdrawResultCode {
  open var name:
    String /* "liquidityPoolWithdrawSuccess" | "liquidityPoolWithdrawMalformed" | "liquidityPoolWithdrawNoTrust" | "liquidityPoolWithdrawUnderfunded" | "liquidityPoolWithdrawLineFull" | "liquidityPoolWithdrawUnderMinimum" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" */

  companion object {
    fun liquidityPoolWithdrawSuccess(): LiquidityPoolWithdrawResultCode
    fun liquidityPoolWithdrawMalformed(): LiquidityPoolWithdrawResultCode
    fun liquidityPoolWithdrawNoTrust(): LiquidityPoolWithdrawResultCode
    fun liquidityPoolWithdrawUnderfunded(): LiquidityPoolWithdrawResultCode
    fun liquidityPoolWithdrawLineFull(): LiquidityPoolWithdrawResultCode
    fun liquidityPoolWithdrawUnderMinimum(): LiquidityPoolWithdrawResultCode
  }
}

external open class OperationResultCode {
  open var name:
    String /* "opInner" | "opBadAuth" | "opNoAccount" | "opNotSupported" | "opTooManySubentries" | "opExceededWorkLimit" | "opTooManySponsoring" */
  open var value: dynamic /* 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" */

  companion object {
    fun opInner(): OperationResultCode
    fun opBadAuth(): OperationResultCode
    fun opNoAccount(): OperationResultCode
    fun opNotSupported(): OperationResultCode
    fun opTooManySubentries(): OperationResultCode
    fun opExceededWorkLimit(): OperationResultCode
    fun opTooManySponsoring(): OperationResultCode
  }
}

external open class TransactionResultCode {
  open var name:
    String /* "txFeeBumpInnerSuccess" | "txSuccess" | "txFailed" | "txTooEarly" | "txTooLate" | "txMissingOperation" | "txBadSeq" | "txBadAuth" | "txInsufficientBalance" | "txNoAccount" | "txInsufficientFee" | "txBadAuthExtra" | "txInternalError" | "txNotSupported" | "txFeeBumpInnerFailed" | "txBadSponsorship" | "txBadMinSeqAgeOrGap" | "txMalformed" */
  open var value:
    dynamic /* 1 | 0 | "-1" | "-2" | "-3" | "-4" | "-5" | "-6" | "-7" | "-8" | "-9" | "-10" | "-11" | "-12" | "-13" | "-14" | "-15" | "-16" */

  companion object {
    fun txFeeBumpInnerSuccess(): TransactionResultCode
    fun txSuccess(): TransactionResultCode
    fun txFailed(): TransactionResultCode
    fun txTooEarly(): TransactionResultCode
    fun txTooLate(): TransactionResultCode
    fun txMissingOperation(): TransactionResultCode
    fun txBadSeq(): TransactionResultCode
    fun txBadAuth(): TransactionResultCode
    fun txInsufficientBalance(): TransactionResultCode
    fun txNoAccount(): TransactionResultCode
    fun txInsufficientFee(): TransactionResultCode
    fun txBadAuthExtra(): TransactionResultCode
    fun txInternalError(): TransactionResultCode
    fun txNotSupported(): TransactionResultCode
    fun txFeeBumpInnerFailed(): TransactionResultCode
    fun txBadSponsorship(): TransactionResultCode
    fun txBadMinSeqAgeOrGap(): TransactionResultCode
    fun txMalformed(): TransactionResultCode
  }
}

external open class CryptoKeyType {
  open var name:
    String /* "keyTypeEd25519" | "keyTypePreAuthTx" | "keyTypeHashX" | "keyTypeEd25519SignedPayload" | "keyTypeMuxedEd25519" */
  open var value: Number /* 0 | 1 | 2 | 3 | 256 */

  companion object {
    fun keyTypeEd25519(): CryptoKeyType
    fun keyTypePreAuthTx(): CryptoKeyType
    fun keyTypeHashX(): CryptoKeyType
    fun keyTypeEd25519SignedPayload(): CryptoKeyType
    fun keyTypeMuxedEd25519(): CryptoKeyType
  }
}

external open class PublicKeyType {
  open var name: String /* "publicKeyTypeEd25519" */
  open var value: Number /* 0 */

  companion object {
    fun publicKeyTypeEd25519(): PublicKeyType
  }
}

external open class SignerKeyType {
  open var name:
    String /* "signerKeyTypeEd25519" | "signerKeyTypePreAuthTx" | "signerKeyTypeHashX" | "signerKeyTypeEd25519SignedPayload" */
  open var value: Number /* 0 | 1 | 2 | 3 */

  companion object {
    fun signerKeyTypeEd25519(): SignerKeyType
    fun signerKeyTypePreAuthTx(): SignerKeyType
    fun signerKeyTypeHashX(): SignerKeyType
    fun signerKeyTypeEd25519SignedPayload(): SignerKeyType
  }
}

external var Value: VarOpaque

external var Thresholds: Opaque

external var String32: XDRString

external var String64: XDRString

external var DataValue: VarOpaque

external var AssetCode4: Opaque

external var AssetCode12: Opaque

external var UpgradeType: VarOpaque

external var LedgerEntryChanges: XDRArray<LedgerEntryChange>

external var EncryptedBody: VarOpaque

external var PeerStatList: XDRArray<PeerStats>

external var Hash: Opaque

external var Uint256: Opaque

external var Uint32: UnsignedInt

external var Int32: SignedInt

external open class Uint64(low: Number, high: Number) : UnsignedHyper

external open class Int64(low: Number, high: Number) : Hyper

external var Signature: VarOpaque

external var SignatureHint: Opaque

external interface `T$107` {
  var counter: Number
  var value: Buffer
}

external open class ScpBallot(attributes: `T$107`) {
  open fun counter(value: Number = definedExternally): Number
  open fun value(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpBallot
    fun write(value: ScpBallot, io: Buffer)
    fun isValid(value: ScpBallot): Boolean
    fun toXDR(value: ScpBallot): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpBallot
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpBallot
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$108` {
  var quorumSetHash: Buffer
  var votes: Array<Buffer>
  var accepted: Array<Buffer>
}

external open class ScpNomination(attributes: `T$108`) {
  open fun quorumSetHash(value: Buffer = definedExternally): Buffer
  open fun votes(value: Array<Buffer> = definedExternally): Array<Buffer>
  open fun accepted(value: Array<Buffer> = definedExternally): Array<Buffer>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpNomination
    fun write(value: ScpNomination, io: Buffer)
    fun isValid(value: ScpNomination): Boolean
    fun toXDR(value: ScpNomination): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpNomination
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpNomination
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$109` {
  var quorumSetHash: Buffer
  var ballot: ScpBallot
  var prepared: ScpBallot?
  var preparedPrime: ScpBallot?
  var nC: Number
  var nH: Number
}

external open class ScpStatementPrepare(attributes: `T$109`) {
  open fun quorumSetHash(value: Buffer = definedExternally): Buffer
  open fun ballot(value: ScpBallot = definedExternally): ScpBallot
  open fun prepared(value: ScpBallot? = definedExternally): ScpBallot?
  open fun preparedPrime(value: ScpBallot? = definedExternally): ScpBallot?
  open fun nC(value: Number = definedExternally): Number
  open fun nH(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpStatementPrepare
    fun write(value: ScpStatementPrepare, io: Buffer)
    fun isValid(value: ScpStatementPrepare): Boolean
    fun toXDR(value: ScpStatementPrepare): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpStatementPrepare
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpStatementPrepare
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$110` {
  var ballot: ScpBallot
  var nPrepared: Number
  var nCommit: Number
  var nH: Number
  var quorumSetHash: Buffer
}

external open class ScpStatementConfirm(attributes: `T$110`) {
  open fun ballot(value: ScpBallot = definedExternally): ScpBallot
  open fun nPrepared(value: Number = definedExternally): Number
  open fun nCommit(value: Number = definedExternally): Number
  open fun nH(value: Number = definedExternally): Number
  open fun quorumSetHash(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpStatementConfirm
    fun write(value: ScpStatementConfirm, io: Buffer)
    fun isValid(value: ScpStatementConfirm): Boolean
    fun toXDR(value: ScpStatementConfirm): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpStatementConfirm
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpStatementConfirm
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$111` {
  var commit: ScpBallot
  var nH: Number
  var commitQuorumSetHash: Buffer
}

external open class ScpStatementExternalize(attributes: `T$111`) {
  open fun commit(value: ScpBallot = definedExternally): ScpBallot
  open fun nH(value: Number = definedExternally): Number
  open fun commitQuorumSetHash(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpStatementExternalize
    fun write(value: ScpStatementExternalize, io: Buffer)
    fun isValid(value: ScpStatementExternalize): Boolean
    fun toXDR(value: ScpStatementExternalize): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ScpStatementExternalize
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpStatementExternalize
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$112` {
  var nodeId: NodeId
  var slotIndex: Uint64
  var pledges: ScpStatementPledges
}

external open class ScpStatement(attributes: `T$112`) {
  open fun nodeId(value: NodeId = definedExternally): NodeId
  open fun slotIndex(value: Uint64 = definedExternally): Uint64
  open fun pledges(value: ScpStatementPledges = definedExternally): ScpStatementPledges
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpStatement
    fun write(value: ScpStatement, io: Buffer)
    fun isValid(value: ScpStatement): Boolean
    fun toXDR(value: ScpStatement): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpStatement
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpStatement
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$113` {
  var statement: ScpStatement
  var signature: Buffer
}

external open class ScpEnvelope(attributes: `T$113`) {
  open fun statement(value: ScpStatement = definedExternally): ScpStatement
  open fun signature(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpEnvelope
    fun write(value: ScpEnvelope, io: Buffer)
    fun isValid(value: ScpEnvelope): Boolean
    fun toXDR(value: ScpEnvelope): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpEnvelope
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpEnvelope
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$114` {
  var threshold: Number
  var validators: Array<NodeId>
  var innerSets: Array<ScpQuorumSet>
}

external open class ScpQuorumSet(attributes: `T$114`) {
  open fun threshold(value: Number = definedExternally): Number
  open fun validators(value: Array<NodeId> = definedExternally): Array<NodeId>
  open fun innerSets(value: Array<ScpQuorumSet> = definedExternally): Array<ScpQuorumSet>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpQuorumSet
    fun write(value: ScpQuorumSet, io: Buffer)
    fun isValid(value: ScpQuorumSet): Boolean
    fun toXDR(value: ScpQuorumSet): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpQuorumSet
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpQuorumSet
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$115` {
  var assetCode: Buffer
  var issuer: AccountId
}

external open class AlphaNum4(attributes: `T$115`) {
  open fun assetCode(value: Buffer = definedExternally): Buffer
  open fun issuer(value: AccountId = definedExternally): AccountId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AlphaNum4
    fun write(value: AlphaNum4, io: Buffer)
    fun isValid(value: AlphaNum4): Boolean
    fun toXDR(value: AlphaNum4): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AlphaNum4
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AlphaNum4
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AlphaNum12(attributes: `T$115`) {
  open fun assetCode(value: Buffer = definedExternally): Buffer
  open fun issuer(value: AccountId = definedExternally): AccountId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AlphaNum12
    fun write(value: AlphaNum12, io: Buffer)
    fun isValid(value: AlphaNum12): Boolean
    fun toXDR(value: AlphaNum12): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AlphaNum12
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AlphaNum12
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$116` {
  var n: Number
  var d: Number
}

external open class Price(attributes: `T$116`) {
  open fun n(value: Number = definedExternally): Number
  open fun d(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Price
    fun write(value: Price, io: Buffer)
    fun isValid(value: Price): Boolean
    fun toXDR(value: Price): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Price
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Price
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$117` {
  var buying: Int64
  var selling: Int64
}

external open class Liabilities(attributes: `T$117`) {
  open fun buying(value: Int64 = definedExternally): Int64
  open fun selling(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Liabilities
    fun write(value: Liabilities, io: Buffer)
    fun isValid(value: Liabilities): Boolean
    fun toXDR(value: Liabilities): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Liabilities
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Liabilities
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$118` {
  var key: SignerKey
  var weight: Number
}

external open class Signer(attributes: `T$118`) {
  open fun key(value: SignerKey = definedExternally): SignerKey
  open fun weight(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Signer
    fun write(value: Signer, io: Buffer)
    fun isValid(value: Signer): Boolean
    fun toXDR(value: Signer): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Signer
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Signer
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$119` {
  var ext: ExtensionPoint
  var seqLedger: Number
  var seqTime: TimePoint
}

external open class AccountEntryExtensionV3(attributes: `T$119`) {
  open fun ext(value: ExtensionPoint = definedExternally): ExtensionPoint
  open fun seqLedger(value: Number = definedExternally): Number
  open fun seqTime(value: TimePoint = definedExternally): TimePoint
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntryExtensionV3
    fun write(value: AccountEntryExtensionV3, io: Buffer)
    fun isValid(value: AccountEntryExtensionV3): Boolean
    fun toXDR(value: AccountEntryExtensionV3): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): AccountEntryExtensionV3
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntryExtensionV3
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$120` {
  var numSponsored: Number
  var numSponsoring: Number
  var signerSponsoringIDs: Array<AccountId?>
  var ext: AccountEntryExtensionV2Ext
}

external open class AccountEntryExtensionV2(attributes: `T$120`) {
  open fun numSponsored(value: Number = definedExternally): Number
  open fun numSponsoring(value: Number = definedExternally): Number
  open fun signerSponsoringIDs(value: Array<AccountId?> = definedExternally): Array<AccountId?>
  open fun ext(value: AccountEntryExtensionV2Ext = definedExternally): AccountEntryExtensionV2Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntryExtensionV2
    fun write(value: AccountEntryExtensionV2, io: Buffer)
    fun isValid(value: AccountEntryExtensionV2): Boolean
    fun toXDR(value: AccountEntryExtensionV2): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): AccountEntryExtensionV2
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntryExtensionV2
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$121` {
  var liabilities: Liabilities
  var ext: AccountEntryExtensionV1Ext
}

external open class AccountEntryExtensionV1(attributes: `T$121`) {
  open fun liabilities(value: Liabilities = definedExternally): Liabilities
  open fun ext(value: AccountEntryExtensionV1Ext = definedExternally): AccountEntryExtensionV1Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntryExtensionV1
    fun write(value: AccountEntryExtensionV1, io: Buffer)
    fun isValid(value: AccountEntryExtensionV1): Boolean
    fun toXDR(value: AccountEntryExtensionV1): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): AccountEntryExtensionV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntryExtensionV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$122` {
  var accountId: AccountId
  var balance: Int64
  var seqNum: SequenceNumber
  var numSubEntries: Number
  var inflationDest: AccountId?
  var flags: Number
  var homeDomain: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
  var thresholds: Buffer
  var signers: Array<Signer>
  var ext: AccountEntryExt
}

external open class AccountEntry(attributes: `T$122`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun balance(value: Int64 = definedExternally): Int64
  open fun seqNum(value: SequenceNumber = definedExternally): SequenceNumber
  open fun numSubEntries(value: Number = definedExternally): Number
  open fun inflationDest(value: AccountId? = definedExternally): AccountId?
  open fun flags(value: Number = definedExternally): Number
  open fun homeDomain(value: String = definedExternally): dynamic /* String | Buffer */
  open fun homeDomain(): dynamic /* String | Buffer */
  open fun homeDomain(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun thresholds(value: Buffer = definedExternally): Buffer
  open fun signers(value: Array<Signer> = definedExternally): Array<Signer>
  open fun ext(value: AccountEntryExt = definedExternally): AccountEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntry
    fun write(value: AccountEntry, io: Buffer)
    fun isValid(value: AccountEntry): Boolean
    fun toXDR(value: AccountEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AccountEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$123` {
  var liquidityPoolUseCount: Number
  var ext: TrustLineEntryExtensionV2Ext
}

external open class TrustLineEntryExtensionV2(attributes: `T$123`) {
  open fun liquidityPoolUseCount(value: Number = definedExternally): Number
  open fun ext(
    value: TrustLineEntryExtensionV2Ext = definedExternally
  ): TrustLineEntryExtensionV2Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TrustLineEntryExtensionV2
    fun write(value: TrustLineEntryExtensionV2, io: Buffer)
    fun isValid(value: TrustLineEntryExtensionV2): Boolean
    fun toXDR(value: TrustLineEntryExtensionV2): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TrustLineEntryExtensionV2
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineEntryExtensionV2
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$124` {
  var liabilities: Liabilities
  var ext: TrustLineEntryV1Ext
}

external open class TrustLineEntryV1(attributes: `T$124`) {
  open fun liabilities(value: Liabilities = definedExternally): Liabilities
  open fun ext(value: TrustLineEntryV1Ext = definedExternally): TrustLineEntryV1Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TrustLineEntryV1
    fun write(value: TrustLineEntryV1, io: Buffer)
    fun isValid(value: TrustLineEntryV1): Boolean
    fun toXDR(value: TrustLineEntryV1): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TrustLineEntryV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineEntryV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$125` {
  var accountId: AccountId
  var asset: TrustLineAsset
  var balance: Int64
  var limit: Int64
  var flags: Number
  var ext: TrustLineEntryExt
}

external open class TrustLineEntry(attributes: `T$125`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun asset(value: TrustLineAsset = definedExternally): TrustLineAsset
  open fun balance(value: Int64 = definedExternally): Int64
  open fun limit(value: Int64 = definedExternally): Int64
  open fun flags(value: Number = definedExternally): Number
  open fun ext(value: TrustLineEntryExt = definedExternally): TrustLineEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TrustLineEntry
    fun write(value: TrustLineEntry, io: Buffer)
    fun isValid(value: TrustLineEntry): Boolean
    fun toXDR(value: TrustLineEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TrustLineEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$126` {
  var sellerId: AccountId
  var offerId: Int64
  var selling: Asset
  var buying: Asset
  var amount: Int64
  var price: Price
  var flags: Number
  var ext: OfferEntryExt
}

external open class OfferEntry(attributes: `T$126`) {
  open fun sellerId(value: AccountId = definedExternally): AccountId
  open fun offerId(value: Int64 = definedExternally): Int64
  open fun selling(value: Asset = definedExternally): Asset
  open fun buying(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun price(value: Price = definedExternally): Price
  open fun flags(value: Number = definedExternally): Number
  open fun ext(value: OfferEntryExt = definedExternally): OfferEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): OfferEntry
    fun write(value: OfferEntry, io: Buffer)
    fun isValid(value: OfferEntry): Boolean
    fun toXDR(value: OfferEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): OfferEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): OfferEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$127` {
  var accountId: AccountId
  var dataName: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
  var dataValue: Buffer
  var ext: DataEntryExt
}

external open class DataEntry(attributes: `T$127`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun dataName(value: String = definedExternally): dynamic /* String | Buffer */
  open fun dataName(): dynamic /* String | Buffer */
  open fun dataName(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun dataValue(value: Buffer = definedExternally): Buffer
  open fun ext(value: DataEntryExt = definedExternally): DataEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): DataEntry
    fun write(value: DataEntry, io: Buffer)
    fun isValid(value: DataEntry): Boolean
    fun toXDR(value: DataEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): DataEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): DataEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$128` {
  var destination: AccountId
  var predicate: ClaimPredicate
}

external open class ClaimantV0(attributes: `T$128`) {
  open fun destination(value: AccountId = definedExternally): AccountId
  open fun predicate(value: ClaimPredicate = definedExternally): ClaimPredicate
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimantV0
    fun write(value: ClaimantV0, io: Buffer)
    fun isValid(value: ClaimantV0): Boolean
    fun toXDR(value: ClaimantV0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimantV0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimantV0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$129` {
  var ext: ClaimableBalanceEntryExtensionV1Ext
  var flags: Number
}

external open class ClaimableBalanceEntryExtensionV1(attributes: `T$129`) {
  open fun ext(
    value: ClaimableBalanceEntryExtensionV1Ext = definedExternally
  ): ClaimableBalanceEntryExtensionV1Ext
  open fun flags(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimableBalanceEntryExtensionV1
    fun write(value: ClaimableBalanceEntryExtensionV1, io: Buffer)
    fun isValid(value: ClaimableBalanceEntryExtensionV1): Boolean
    fun toXDR(value: ClaimableBalanceEntryExtensionV1): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClaimableBalanceEntryExtensionV1
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): ClaimableBalanceEntryExtensionV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$130` {
  var balanceId: ClaimableBalanceId
  var claimants: Array<Claimant>
  var asset: Asset
  var amount: Int64
  var ext: ClaimableBalanceEntryExt
}

external open class ClaimableBalanceEntry(attributes: `T$130`) {
  open fun balanceId(value: ClaimableBalanceId = definedExternally): ClaimableBalanceId
  open fun claimants(value: Array<Claimant> = definedExternally): Array<Claimant>
  open fun asset(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun ext(value: ClaimableBalanceEntryExt = definedExternally): ClaimableBalanceEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimableBalanceEntry
    fun write(value: ClaimableBalanceEntry, io: Buffer)
    fun isValid(value: ClaimableBalanceEntry): Boolean
    fun toXDR(value: ClaimableBalanceEntry): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClaimableBalanceEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimableBalanceEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$131` {
  var assetA: Asset
  var assetB: Asset
  var fee: Number
}

external open class LiquidityPoolConstantProductParameters(attributes: `T$131`) {
  open fun assetA(value: Asset = definedExternally): Asset
  open fun assetB(value: Asset = definedExternally): Asset
  open fun fee(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LiquidityPoolConstantProductParameters
    fun write(value: LiquidityPoolConstantProductParameters, io: Buffer)
    fun isValid(value: LiquidityPoolConstantProductParameters): Boolean
    fun toXDR(value: LiquidityPoolConstantProductParameters): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolConstantProductParameters
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): LiquidityPoolConstantProductParameters
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$132` {
  var params: LiquidityPoolConstantProductParameters
  var reserveA: Int64
  var reserveB: Int64
  var totalPoolShares: Int64
  var poolSharesTrustLineCount: Int64
}

external open class LiquidityPoolEntryConstantProduct(attributes: `T$132`) {
  open fun params(
    value: LiquidityPoolConstantProductParameters = definedExternally
  ): LiquidityPoolConstantProductParameters
  open fun reserveA(value: Int64 = definedExternally): Int64
  open fun reserveB(value: Int64 = definedExternally): Int64
  open fun totalPoolShares(value: Int64 = definedExternally): Int64
  open fun poolSharesTrustLineCount(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LiquidityPoolEntryConstantProduct
    fun write(value: LiquidityPoolEntryConstantProduct, io: Buffer)
    fun isValid(value: LiquidityPoolEntryConstantProduct): Boolean
    fun toXDR(value: LiquidityPoolEntryConstantProduct): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolEntryConstantProduct
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): LiquidityPoolEntryConstantProduct
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$133` {
  var liquidityPoolId: PoolId
  var body: LiquidityPoolEntryBody
}

external open class LiquidityPoolEntry(attributes: `T$133`) {
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun body(value: LiquidityPoolEntryBody = definedExternally): LiquidityPoolEntryBody
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LiquidityPoolEntry
    fun write(value: LiquidityPoolEntry, io: Buffer)
    fun isValid(value: LiquidityPoolEntry): Boolean
    fun toXDR(value: LiquidityPoolEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LiquidityPoolEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$134` {
  var sponsoringId: AccountId?
  var ext: LedgerEntryExtensionV1Ext
}

external open class LedgerEntryExtensionV1(attributes: `T$134`) {
  open fun sponsoringId(value: AccountId? = definedExternally): AccountId?
  open fun ext(value: LedgerEntryExtensionV1Ext = definedExternally): LedgerEntryExtensionV1Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerEntryExtensionV1
    fun write(value: LedgerEntryExtensionV1, io: Buffer)
    fun isValid(value: LedgerEntryExtensionV1): Boolean
    fun toXDR(value: LedgerEntryExtensionV1): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerEntryExtensionV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerEntryExtensionV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$135` {
  var lastModifiedLedgerSeq: Number
  var data: LedgerEntryData
  var ext: LedgerEntryExt
}

external open class LedgerEntry(attributes: `T$135`) {
  open fun lastModifiedLedgerSeq(value: Number = definedExternally): Number
  open fun data(value: LedgerEntryData = definedExternally): LedgerEntryData
  open fun ext(value: LedgerEntryExt = definedExternally): LedgerEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerEntry
    fun write(value: LedgerEntry, io: Buffer)
    fun isValid(value: LedgerEntry): Boolean
    fun toXDR(value: LedgerEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$136` {
  var accountId: AccountId
}

external open class LedgerKeyAccount(attributes: `T$136`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerKeyAccount
    fun write(value: LedgerKeyAccount, io: Buffer)
    fun isValid(value: LedgerKeyAccount): Boolean
    fun toXDR(value: LedgerKeyAccount): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerKeyAccount
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKeyAccount
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$137` {
  var accountId: AccountId
  var asset: TrustLineAsset
}

external open class LedgerKeyTrustLine(attributes: `T$137`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun asset(value: TrustLineAsset = definedExternally): TrustLineAsset
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerKeyTrustLine
    fun write(value: LedgerKeyTrustLine, io: Buffer)
    fun isValid(value: LedgerKeyTrustLine): Boolean
    fun toXDR(value: LedgerKeyTrustLine): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerKeyTrustLine
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKeyTrustLine
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$138` {
  var sellerId: AccountId
  var offerId: Int64
}

external open class LedgerKeyOffer(attributes: `T$138`) {
  open fun sellerId(value: AccountId = definedExternally): AccountId
  open fun offerId(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerKeyOffer
    fun write(value: LedgerKeyOffer, io: Buffer)
    fun isValid(value: LedgerKeyOffer): Boolean
    fun toXDR(value: LedgerKeyOffer): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerKeyOffer
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKeyOffer
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$139` {
  var accountId: AccountId
  var dataName: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
}

external open class LedgerKeyData(attributes: `T$139`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun dataName(value: String = definedExternally): dynamic /* String | Buffer */
  open fun dataName(): dynamic /* String | Buffer */
  open fun dataName(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerKeyData
    fun write(value: LedgerKeyData, io: Buffer)
    fun isValid(value: LedgerKeyData): Boolean
    fun toXDR(value: LedgerKeyData): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerKeyData
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKeyData
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$140` {
  var balanceId: ClaimableBalanceId
}

external open class LedgerKeyClaimableBalance(attributes: `T$140`) {
  open fun balanceId(value: ClaimableBalanceId = definedExternally): ClaimableBalanceId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerKeyClaimableBalance
    fun write(value: LedgerKeyClaimableBalance, io: Buffer)
    fun isValid(value: LedgerKeyClaimableBalance): Boolean
    fun toXDR(value: LedgerKeyClaimableBalance): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerKeyClaimableBalance
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKeyClaimableBalance
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$141` {
  var liquidityPoolId: PoolId
}

external open class LedgerKeyLiquidityPool(attributes: `T$141`) {
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerKeyLiquidityPool
    fun write(value: LedgerKeyLiquidityPool, io: Buffer)
    fun isValid(value: LedgerKeyLiquidityPool): Boolean
    fun toXDR(value: LedgerKeyLiquidityPool): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerKeyLiquidityPool
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKeyLiquidityPool
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$142` {
  var nodeId: NodeId
  var signature: Buffer
}

external open class LedgerCloseValueSignature(attributes: `T$142`) {
  open fun nodeId(value: NodeId = definedExternally): NodeId
  open fun signature(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerCloseValueSignature
    fun write(value: LedgerCloseValueSignature, io: Buffer)
    fun isValid(value: LedgerCloseValueSignature): Boolean
    fun toXDR(value: LedgerCloseValueSignature): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerCloseValueSignature
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerCloseValueSignature
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$143` {
  var txSetHash: Buffer
  var closeTime: TimePoint
  var upgrades: Array<Buffer>
  var ext: StellarValueExt
}

external open class StellarValue(attributes: `T$143`) {
  open fun txSetHash(value: Buffer = definedExternally): Buffer
  open fun closeTime(value: TimePoint = definedExternally): TimePoint
  open fun upgrades(value: Array<Buffer> = definedExternally): Array<Buffer>
  open fun ext(value: StellarValueExt = definedExternally): StellarValueExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): StellarValue
    fun write(value: StellarValue, io: Buffer)
    fun isValid(value: StellarValue): Boolean
    fun toXDR(value: StellarValue): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): StellarValue
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): StellarValue
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$144` {
  var flags: Number
  var ext: LedgerHeaderExtensionV1Ext
}

external open class LedgerHeaderExtensionV1(attributes: `T$144`) {
  open fun flags(value: Number = definedExternally): Number
  open fun ext(value: LedgerHeaderExtensionV1Ext = definedExternally): LedgerHeaderExtensionV1Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerHeaderExtensionV1
    fun write(value: LedgerHeaderExtensionV1, io: Buffer)
    fun isValid(value: LedgerHeaderExtensionV1): Boolean
    fun toXDR(value: LedgerHeaderExtensionV1): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerHeaderExtensionV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerHeaderExtensionV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$145` {
  var ledgerVersion: Number
  var previousLedgerHash: Buffer
  var scpValue: StellarValue
  var txSetResultHash: Buffer
  var bucketListHash: Buffer
  var ledgerSeq: Number
  var totalCoins: Int64
  var feePool: Int64
  var inflationSeq: Number
  var idPool: Uint64
  var baseFee: Number
  var baseReserve: Number
  var maxTxSetSize: Number
  var skipList: Array<Buffer>
  var ext: LedgerHeaderExt
}

external open class LedgerHeader(attributes: `T$145`) {
  open fun ledgerVersion(value: Number = definedExternally): Number
  open fun previousLedgerHash(value: Buffer = definedExternally): Buffer
  open fun scpValue(value: StellarValue = definedExternally): StellarValue
  open fun txSetResultHash(value: Buffer = definedExternally): Buffer
  open fun bucketListHash(value: Buffer = definedExternally): Buffer
  open fun ledgerSeq(value: Number = definedExternally): Number
  open fun totalCoins(value: Int64 = definedExternally): Int64
  open fun feePool(value: Int64 = definedExternally): Int64
  open fun inflationSeq(value: Number = definedExternally): Number
  open fun idPool(value: Uint64 = definedExternally): Uint64
  open fun baseFee(value: Number = definedExternally): Number
  open fun baseReserve(value: Number = definedExternally): Number
  open fun maxTxSetSize(value: Number = definedExternally): Number
  open fun skipList(value: Array<Buffer> = definedExternally): Array<Buffer>
  open fun ext(value: LedgerHeaderExt = definedExternally): LedgerHeaderExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerHeader
    fun write(value: LedgerHeader, io: Buffer)
    fun isValid(value: LedgerHeader): Boolean
    fun toXDR(value: LedgerHeader): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerHeader
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerHeader
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$146` {
  var ledgerVersion: Number
  var ext: BucketMetadataExt
}

external open class BucketMetadata(attributes: `T$146`) {
  open fun ledgerVersion(value: Number = definedExternally): Number
  open fun ext(value: BucketMetadataExt = definedExternally): BucketMetadataExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): BucketMetadata
    fun write(value: BucketMetadata, io: Buffer)
    fun isValid(value: BucketMetadata): Boolean
    fun toXDR(value: BucketMetadata): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): BucketMetadata
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): BucketMetadata
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$147` {
  var baseFee: Int64?
  var txes: Array<TransactionEnvelope>
}

external open class TxSetComponentTxsMaybeDiscountedFee(attributes: `T$147`) {
  open fun baseFee(value: Int64? = definedExternally): Int64?
  open fun txes(value: Array<TransactionEnvelope> = definedExternally): Array<TransactionEnvelope>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TxSetComponentTxsMaybeDiscountedFee
    fun write(value: TxSetComponentTxsMaybeDiscountedFee, io: Buffer)
    fun isValid(value: TxSetComponentTxsMaybeDiscountedFee): Boolean
    fun toXDR(value: TxSetComponentTxsMaybeDiscountedFee): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TxSetComponentTxsMaybeDiscountedFee
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): TxSetComponentTxsMaybeDiscountedFee
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$148` {
  var previousLedgerHash: Buffer
  var txes: Array<TransactionEnvelope>
}

external open class TransactionSet(attributes: `T$148`) {
  open fun previousLedgerHash(value: Buffer = definedExternally): Buffer
  open fun txes(value: Array<TransactionEnvelope> = definedExternally): Array<TransactionEnvelope>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionSet
    fun write(value: TransactionSet, io: Buffer)
    fun isValid(value: TransactionSet): Boolean
    fun toXDR(value: TransactionSet): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionSet
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionSet
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$149` {
  var previousLedgerHash: Buffer
  var phases: Array<TransactionPhase>
}

external open class TransactionSetV1(attributes: `T$149`) {
  open fun previousLedgerHash(value: Buffer = definedExternally): Buffer
  open fun phases(value: Array<TransactionPhase> = definedExternally): Array<TransactionPhase>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionSetV1
    fun write(value: TransactionSetV1, io: Buffer)
    fun isValid(value: TransactionSetV1): Boolean
    fun toXDR(value: TransactionSetV1): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionSetV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionSetV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$150` {
  var transactionHash: Buffer
  var result: TransactionResult
}

external open class TransactionResultPair(attributes: `T$150`) {
  open fun transactionHash(value: Buffer = definedExternally): Buffer
  open fun result(value: TransactionResult = definedExternally): TransactionResult
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionResultPair
    fun write(value: TransactionResultPair, io: Buffer)
    fun isValid(value: TransactionResultPair): Boolean
    fun toXDR(value: TransactionResultPair): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionResultPair
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionResultPair
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$151` {
  var results: Array<TransactionResultPair>
}

external open class TransactionResultSet(attributes: `T$151`) {
  open fun results(
    value: Array<TransactionResultPair> = definedExternally
  ): Array<TransactionResultPair>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionResultSet
    fun write(value: TransactionResultSet, io: Buffer)
    fun isValid(value: TransactionResultSet): Boolean
    fun toXDR(value: TransactionResultSet): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionResultSet
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionResultSet
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$152` {
  var ledgerSeq: Number
  var txSet: TransactionSet
  var ext: TransactionHistoryEntryExt
}

external open class TransactionHistoryEntry(attributes: `T$152`) {
  open fun ledgerSeq(value: Number = definedExternally): Number
  open fun txSet(value: TransactionSet = definedExternally): TransactionSet
  open fun ext(value: TransactionHistoryEntryExt = definedExternally): TransactionHistoryEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionHistoryEntry
    fun write(value: TransactionHistoryEntry, io: Buffer)
    fun isValid(value: TransactionHistoryEntry): Boolean
    fun toXDR(value: TransactionHistoryEntry): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionHistoryEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionHistoryEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$153` {
  var ledgerSeq: Number
  var txResultSet: TransactionResultSet
  var ext: TransactionHistoryResultEntryExt
}

external open class TransactionHistoryResultEntry(attributes: `T$153`) {
  open fun ledgerSeq(value: Number = definedExternally): Number
  open fun txResultSet(value: TransactionResultSet = definedExternally): TransactionResultSet
  open fun ext(
    value: TransactionHistoryResultEntryExt = definedExternally
  ): TransactionHistoryResultEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionHistoryResultEntry
    fun write(value: TransactionHistoryResultEntry, io: Buffer)
    fun isValid(value: TransactionHistoryResultEntry): Boolean
    fun toXDR(value: TransactionHistoryResultEntry): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionHistoryResultEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionHistoryResultEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$154` {
  var hash: Buffer
  var header: LedgerHeader
  var ext: LedgerHeaderHistoryEntryExt
}

external open class LedgerHeaderHistoryEntry(attributes: `T$154`) {
  open fun hash(value: Buffer = definedExternally): Buffer
  open fun header(value: LedgerHeader = definedExternally): LedgerHeader
  open fun ext(value: LedgerHeaderHistoryEntryExt = definedExternally): LedgerHeaderHistoryEntryExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerHeaderHistoryEntry
    fun write(value: LedgerHeaderHistoryEntry, io: Buffer)
    fun isValid(value: LedgerHeaderHistoryEntry): Boolean
    fun toXDR(value: LedgerHeaderHistoryEntry): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerHeaderHistoryEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerHeaderHistoryEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$155` {
  var ledgerSeq: Number
  var messages: Array<ScpEnvelope>
}

external open class LedgerScpMessages(attributes: `T$155`) {
  open fun ledgerSeq(value: Number = definedExternally): Number
  open fun messages(value: Array<ScpEnvelope> = definedExternally): Array<ScpEnvelope>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerScpMessages
    fun write(value: LedgerScpMessages, io: Buffer)
    fun isValid(value: LedgerScpMessages): Boolean
    fun toXDR(value: LedgerScpMessages): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerScpMessages
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerScpMessages
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$156` {
  var quorumSets: Array<ScpQuorumSet>
  var ledgerMessages: LedgerScpMessages
}

external open class ScpHistoryEntryV0(attributes: `T$156`) {
  open fun quorumSets(value: Array<ScpQuorumSet> = definedExternally): Array<ScpQuorumSet>
  open fun ledgerMessages(value: LedgerScpMessages = definedExternally): LedgerScpMessages
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpHistoryEntryV0
    fun write(value: ScpHistoryEntryV0, io: Buffer)
    fun isValid(value: ScpHistoryEntryV0): Boolean
    fun toXDR(value: ScpHistoryEntryV0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpHistoryEntryV0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpHistoryEntryV0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$157` {
  var changes: Array<LedgerEntryChange>
}

external open class OperationMeta(attributes: `T$157`) {
  open fun changes(value: Array<LedgerEntryChange> = definedExternally): Array<LedgerEntryChange>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): OperationMeta
    fun write(value: OperationMeta, io: Buffer)
    fun isValid(value: OperationMeta): Boolean
    fun toXDR(value: OperationMeta): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): OperationMeta
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): OperationMeta
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$158` {
  var txChanges: Array<LedgerEntryChange>
  var operations: Array<OperationMeta>
}

external open class TransactionMetaV1(attributes: `T$158`) {
  open fun txChanges(value: Array<LedgerEntryChange> = definedExternally): Array<LedgerEntryChange>
  open fun operations(value: Array<OperationMeta> = definedExternally): Array<OperationMeta>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionMetaV1
    fun write(value: TransactionMetaV1, io: Buffer)
    fun isValid(value: TransactionMetaV1): Boolean
    fun toXDR(value: TransactionMetaV1): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionMetaV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionMetaV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$159` {
  var txChangesBefore: Array<LedgerEntryChange>
  var operations: Array<OperationMeta>
  var txChangesAfter: Array<LedgerEntryChange>
}

external open class TransactionMetaV2(attributes: `T$159`) {
  open fun txChangesBefore(
    value: Array<LedgerEntryChange> = definedExternally
  ): Array<LedgerEntryChange>
  open fun operations(value: Array<OperationMeta> = definedExternally): Array<OperationMeta>
  open fun txChangesAfter(
    value: Array<LedgerEntryChange> = definedExternally
  ): Array<LedgerEntryChange>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionMetaV2
    fun write(value: TransactionMetaV2, io: Buffer)
    fun isValid(value: TransactionMetaV2): Boolean
    fun toXDR(value: TransactionMetaV2): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionMetaV2
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionMetaV2
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$160` {
  var result: TransactionResultPair
  var feeProcessing: Array<LedgerEntryChange>
  var txApplyProcessing: TransactionMeta
}

external open class TransactionResultMeta(attributes: `T$160`) {
  open fun result(value: TransactionResultPair = definedExternally): TransactionResultPair
  open fun feeProcessing(
    value: Array<LedgerEntryChange> = definedExternally
  ): Array<LedgerEntryChange>
  open fun txApplyProcessing(value: TransactionMeta = definedExternally): TransactionMeta
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionResultMeta
    fun write(value: TransactionResultMeta, io: Buffer)
    fun isValid(value: TransactionResultMeta): Boolean
    fun toXDR(value: TransactionResultMeta): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionResultMeta
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionResultMeta
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$161` {
  var upgrade: LedgerUpgrade
  var changes: Array<LedgerEntryChange>
}

external open class UpgradeEntryMeta(attributes: `T$161`) {
  open fun upgrade(value: LedgerUpgrade = definedExternally): LedgerUpgrade
  open fun changes(value: Array<LedgerEntryChange> = definedExternally): Array<LedgerEntryChange>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): UpgradeEntryMeta
    fun write(value: UpgradeEntryMeta, io: Buffer)
    fun isValid(value: UpgradeEntryMeta): Boolean
    fun toXDR(value: UpgradeEntryMeta): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): UpgradeEntryMeta
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): UpgradeEntryMeta
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$162` {
  var ledgerHeader: LedgerHeaderHistoryEntry
  var txSet: TransactionSet
  var txProcessing: Array<TransactionResultMeta>
  var upgradesProcessing: Array<UpgradeEntryMeta>
  var scpInfo: Array<ScpHistoryEntry>
}

external open class LedgerCloseMetaV0(attributes: `T$162`) {
  open fun ledgerHeader(
    value: LedgerHeaderHistoryEntry = definedExternally
  ): LedgerHeaderHistoryEntry
  open fun txSet(value: TransactionSet = definedExternally): TransactionSet
  open fun txProcessing(
    value: Array<TransactionResultMeta> = definedExternally
  ): Array<TransactionResultMeta>
  open fun upgradesProcessing(
    value: Array<UpgradeEntryMeta> = definedExternally
  ): Array<UpgradeEntryMeta>
  open fun scpInfo(value: Array<ScpHistoryEntry> = definedExternally): Array<ScpHistoryEntry>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerCloseMetaV0
    fun write(value: LedgerCloseMetaV0, io: Buffer)
    fun isValid(value: LedgerCloseMetaV0): Boolean
    fun toXDR(value: LedgerCloseMetaV0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerCloseMetaV0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerCloseMetaV0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$163` {
  var ledgerHeader: LedgerHeaderHistoryEntry
  var txSet: GeneralizedTransactionSet
  var txProcessing: Array<TransactionResultMeta>
  var upgradesProcessing: Array<UpgradeEntryMeta>
  var scpInfo: Array<ScpHistoryEntry>
}

external open class LedgerCloseMetaV1(attributes: `T$163`) {
  open fun ledgerHeader(
    value: LedgerHeaderHistoryEntry = definedExternally
  ): LedgerHeaderHistoryEntry
  open fun txSet(value: GeneralizedTransactionSet = definedExternally): GeneralizedTransactionSet
  open fun txProcessing(
    value: Array<TransactionResultMeta> = definedExternally
  ): Array<TransactionResultMeta>
  open fun upgradesProcessing(
    value: Array<UpgradeEntryMeta> = definedExternally
  ): Array<UpgradeEntryMeta>
  open fun scpInfo(value: Array<ScpHistoryEntry> = definedExternally): Array<ScpHistoryEntry>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerCloseMetaV1
    fun write(value: LedgerCloseMetaV1, io: Buffer)
    fun isValid(value: LedgerCloseMetaV1): Boolean
    fun toXDR(value: LedgerCloseMetaV1): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerCloseMetaV1
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerCloseMetaV1
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$164` {
  var code: ErrorCode
  var msg: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
}

external open class Error(attributes: `T$164`) {
  open fun code(value: ErrorCode = definedExternally): ErrorCode
  open fun msg(value: String = definedExternally): dynamic /* String | Buffer */
  open fun msg(): dynamic /* String | Buffer */
  open fun msg(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Error
    fun write(value: Error, io: Buffer)
    fun isValid(value: Error): Boolean
    fun toXDR(value: Error): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Error
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Error
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$165` {
  var numMessages: Number
}

external open class SendMore(attributes: `T$165`) {
  open fun numMessages(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SendMore
    fun write(value: SendMore, io: Buffer)
    fun isValid(value: SendMore): Boolean
    fun toXDR(value: SendMore): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SendMore
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SendMore
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$166` {
  var pubkey: Curve25519Public
  var expiration: Uint64
  var sig: Buffer
}

external open class AuthCert(attributes: `T$166`) {
  open fun pubkey(value: Curve25519Public = definedExternally): Curve25519Public
  open fun expiration(value: Uint64 = definedExternally): Uint64
  open fun sig(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AuthCert
    fun write(value: AuthCert, io: Buffer)
    fun isValid(value: AuthCert): Boolean
    fun toXDR(value: AuthCert): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AuthCert
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AuthCert
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$167` {
  var ledgerVersion: Number
  var overlayVersion: Number
  var overlayMinVersion: Number
  var networkId: Buffer
  var versionStr: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
  var listeningPort: Number
  var peerId: NodeId
  var cert: AuthCert
  var nonce: Buffer
}

external open class Hello(attributes: `T$167`) {
  open fun ledgerVersion(value: Number = definedExternally): Number
  open fun overlayVersion(value: Number = definedExternally): Number
  open fun overlayMinVersion(value: Number = definedExternally): Number
  open fun networkId(value: Buffer = definedExternally): Buffer
  open fun versionStr(value: String = definedExternally): dynamic /* String | Buffer */
  open fun versionStr(): dynamic /* String | Buffer */
  open fun versionStr(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun listeningPort(value: Number = definedExternally): Number
  open fun peerId(value: NodeId = definedExternally): NodeId
  open fun cert(value: AuthCert = definedExternally): AuthCert
  open fun nonce(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Hello
    fun write(value: Hello, io: Buffer)
    fun isValid(value: Hello): Boolean
    fun toXDR(value: Hello): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Hello
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Hello
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$168` {
  var unused: Number
}

external open class Auth(attributes: `T$168`) {
  open fun unused(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Auth
    fun write(value: Auth, io: Buffer)
    fun isValid(value: Auth): Boolean
    fun toXDR(value: Auth): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Auth
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Auth
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$169` {
  var ip: PeerAddressIp
  var port: Number
  var numFailures: Number
}

external open class PeerAddress(attributes: `T$169`) {
  open fun ip(value: PeerAddressIp = definedExternally): PeerAddressIp
  open fun port(value: Number = definedExternally): Number
  open fun numFailures(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PeerAddress
    fun write(value: PeerAddress, io: Buffer)
    fun isValid(value: PeerAddress): Boolean
    fun toXDR(value: PeerAddress): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PeerAddress
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PeerAddress
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$170` {
  var type: MessageType
  var reqHash: Buffer
}

external open class DontHave(attributes: `T$170`) {
  open fun type(value: MessageType = definedExternally): MessageType
  open fun reqHash(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): DontHave
    fun write(value: DontHave, io: Buffer)
    fun isValid(value: DontHave): Boolean
    fun toXDR(value: DontHave): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): DontHave
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): DontHave
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$171` {
  var surveyorPeerId: NodeId
  var surveyedPeerId: NodeId
  var ledgerNum: Number
  var encryptionKey: Curve25519Public
  var commandType: SurveyMessageCommandType
}

external open class SurveyRequestMessage(attributes: `T$171`) {
  open fun surveyorPeerId(value: NodeId = definedExternally): NodeId
  open fun surveyedPeerId(value: NodeId = definedExternally): NodeId
  open fun ledgerNum(value: Number = definedExternally): Number
  open fun encryptionKey(value: Curve25519Public = definedExternally): Curve25519Public
  open fun commandType(
    value: SurveyMessageCommandType = definedExternally
  ): SurveyMessageCommandType
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SurveyRequestMessage
    fun write(value: SurveyRequestMessage, io: Buffer)
    fun isValid(value: SurveyRequestMessage): Boolean
    fun toXDR(value: SurveyRequestMessage): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SurveyRequestMessage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SurveyRequestMessage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$172` {
  var requestSignature: Buffer
  var request: SurveyRequestMessage
}

external open class SignedSurveyRequestMessage(attributes: `T$172`) {
  open fun requestSignature(value: Buffer = definedExternally): Buffer
  open fun request(value: SurveyRequestMessage = definedExternally): SurveyRequestMessage
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SignedSurveyRequestMessage
    fun write(value: SignedSurveyRequestMessage, io: Buffer)
    fun isValid(value: SignedSurveyRequestMessage): Boolean
    fun toXDR(value: SignedSurveyRequestMessage): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): SignedSurveyRequestMessage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SignedSurveyRequestMessage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$173` {
  var surveyorPeerId: NodeId
  var surveyedPeerId: NodeId
  var ledgerNum: Number
  var commandType: SurveyMessageCommandType
  var encryptedBody: Buffer
}

external open class SurveyResponseMessage(attributes: `T$173`) {
  open fun surveyorPeerId(value: NodeId = definedExternally): NodeId
  open fun surveyedPeerId(value: NodeId = definedExternally): NodeId
  open fun ledgerNum(value: Number = definedExternally): Number
  open fun commandType(
    value: SurveyMessageCommandType = definedExternally
  ): SurveyMessageCommandType
  open fun encryptedBody(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SurveyResponseMessage
    fun write(value: SurveyResponseMessage, io: Buffer)
    fun isValid(value: SurveyResponseMessage): Boolean
    fun toXDR(value: SurveyResponseMessage): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): SurveyResponseMessage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SurveyResponseMessage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$174` {
  var responseSignature: Buffer
  var response: SurveyResponseMessage
}

external open class SignedSurveyResponseMessage(attributes: `T$174`) {
  open fun responseSignature(value: Buffer = definedExternally): Buffer
  open fun response(value: SurveyResponseMessage = definedExternally): SurveyResponseMessage
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SignedSurveyResponseMessage
    fun write(value: SignedSurveyResponseMessage, io: Buffer)
    fun isValid(value: SignedSurveyResponseMessage): Boolean
    fun toXDR(value: SignedSurveyResponseMessage): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): SignedSurveyResponseMessage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SignedSurveyResponseMessage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$175` {
  var id: NodeId
  var versionStr: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
  var messagesRead: Uint64
  var messagesWritten: Uint64
  var bytesRead: Uint64
  var bytesWritten: Uint64
  var secondsConnected: Uint64
  var uniqueFloodBytesRecv: Uint64
  var duplicateFloodBytesRecv: Uint64
  var uniqueFetchBytesRecv: Uint64
  var duplicateFetchBytesRecv: Uint64
  var uniqueFloodMessageRecv: Uint64
  var duplicateFloodMessageRecv: Uint64
  var uniqueFetchMessageRecv: Uint64
  var duplicateFetchMessageRecv: Uint64
}

external open class PeerStats(attributes: `T$175`) {
  open fun id(value: NodeId = definedExternally): NodeId
  open fun versionStr(value: String = definedExternally): dynamic /* String | Buffer */
  open fun versionStr(): dynamic /* String | Buffer */
  open fun versionStr(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun messagesRead(value: Uint64 = definedExternally): Uint64
  open fun messagesWritten(value: Uint64 = definedExternally): Uint64
  open fun bytesRead(value: Uint64 = definedExternally): Uint64
  open fun bytesWritten(value: Uint64 = definedExternally): Uint64
  open fun secondsConnected(value: Uint64 = definedExternally): Uint64
  open fun uniqueFloodBytesRecv(value: Uint64 = definedExternally): Uint64
  open fun duplicateFloodBytesRecv(value: Uint64 = definedExternally): Uint64
  open fun uniqueFetchBytesRecv(value: Uint64 = definedExternally): Uint64
  open fun duplicateFetchBytesRecv(value: Uint64 = definedExternally): Uint64
  open fun uniqueFloodMessageRecv(value: Uint64 = definedExternally): Uint64
  open fun duplicateFloodMessageRecv(value: Uint64 = definedExternally): Uint64
  open fun uniqueFetchMessageRecv(value: Uint64 = definedExternally): Uint64
  open fun duplicateFetchMessageRecv(value: Uint64 = definedExternally): Uint64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PeerStats
    fun write(value: PeerStats, io: Buffer)
    fun isValid(value: PeerStats): Boolean
    fun toXDR(value: PeerStats): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PeerStats
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PeerStats
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$176` {
  var inboundPeers: Array<PeerStats>
  var outboundPeers: Array<PeerStats>
  var totalInboundPeerCount: Number
  var totalOutboundPeerCount: Number
}

external open class TopologyResponseBody(attributes: `T$176`) {
  open fun inboundPeers(value: Array<PeerStats> = definedExternally): Array<PeerStats>
  open fun outboundPeers(value: Array<PeerStats> = definedExternally): Array<PeerStats>
  open fun totalInboundPeerCount(value: Number = definedExternally): Number
  open fun totalOutboundPeerCount(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TopologyResponseBody
    fun write(value: TopologyResponseBody, io: Buffer)
    fun isValid(value: TopologyResponseBody): Boolean
    fun toXDR(value: TopologyResponseBody): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TopologyResponseBody
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TopologyResponseBody
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$177` {
  var sequence: Uint64
  var message: StellarMessage
  var mac: HmacSha256Mac
}

external open class AuthenticatedMessageV0(attributes: `T$177`) {
  open fun sequence(value: Uint64 = definedExternally): Uint64
  open fun message(value: StellarMessage = definedExternally): StellarMessage
  open fun mac(value: HmacSha256Mac = definedExternally): HmacSha256Mac
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AuthenticatedMessageV0
    fun write(value: AuthenticatedMessageV0, io: Buffer)
    fun isValid(value: AuthenticatedMessageV0): Boolean
    fun toXDR(value: AuthenticatedMessageV0): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): AuthenticatedMessageV0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AuthenticatedMessageV0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$178` {
  var id: Uint64
  var ed25519: Buffer
}

external open class MuxedAccountMed25519(attributes: `T$178`) {
  open fun id(value: Uint64 = definedExternally): Uint64
  open fun ed25519(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): MuxedAccountMed25519
    fun write(value: MuxedAccountMed25519, io: Buffer)
    fun isValid(value: MuxedAccountMed25519): Boolean
    fun toXDR(value: MuxedAccountMed25519): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): MuxedAccountMed25519
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): MuxedAccountMed25519
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$179` {
  var hint: Buffer
  var signature: Buffer
}

external open class DecoratedSignature(attributes: `T$179`) {
  open fun hint(value: Buffer = definedExternally): Buffer
  open fun signature(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): DecoratedSignature
    fun write(value: DecoratedSignature, io: Buffer)
    fun isValid(value: DecoratedSignature): Boolean
    fun toXDR(value: DecoratedSignature): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): DecoratedSignature
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): DecoratedSignature
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$180` {
  var destination: AccountId
  var startingBalance: Int64
}

external open class CreateAccountOp(attributes: `T$180`) {
  open fun destination(value: AccountId = definedExternally): AccountId
  open fun startingBalance(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): CreateAccountOp
    fun write(value: CreateAccountOp, io: Buffer)
    fun isValid(value: CreateAccountOp): Boolean
    fun toXDR(value: CreateAccountOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): CreateAccountOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): CreateAccountOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$181` {
  var destination: MuxedAccount
  var asset: Asset
  var amount: Int64
}

external open class PaymentOp(attributes: `T$181`) {
  open fun destination(value: MuxedAccount = definedExternally): MuxedAccount
  open fun asset(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PaymentOp
    fun write(value: PaymentOp, io: Buffer)
    fun isValid(value: PaymentOp): Boolean
    fun toXDR(value: PaymentOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PaymentOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PaymentOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$182` {
  var sendAsset: Asset
  var sendMax: Int64
  var destination: MuxedAccount
  var destAsset: Asset
  var destAmount: Int64
  var path: Array<Asset>
}

external open class PathPaymentStrictReceiveOp(attributes: `T$182`) {
  open fun sendAsset(value: Asset = definedExternally): Asset
  open fun sendMax(value: Int64 = definedExternally): Int64
  open fun destination(value: MuxedAccount = definedExternally): MuxedAccount
  open fun destAsset(value: Asset = definedExternally): Asset
  open fun destAmount(value: Int64 = definedExternally): Int64
  open fun path(value: Array<Asset> = definedExternally): Array<Asset>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PathPaymentStrictReceiveOp
    fun write(value: PathPaymentStrictReceiveOp, io: Buffer)
    fun isValid(value: PathPaymentStrictReceiveOp): Boolean
    fun toXDR(value: PathPaymentStrictReceiveOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): PathPaymentStrictReceiveOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PathPaymentStrictReceiveOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$183` {
  var sendAsset: Asset
  var sendAmount: Int64
  var destination: MuxedAccount
  var destAsset: Asset
  var destMin: Int64
  var path: Array<Asset>
}

external open class PathPaymentStrictSendOp(attributes: `T$183`) {
  open fun sendAsset(value: Asset = definedExternally): Asset
  open fun sendAmount(value: Int64 = definedExternally): Int64
  open fun destination(value: MuxedAccount = definedExternally): MuxedAccount
  open fun destAsset(value: Asset = definedExternally): Asset
  open fun destMin(value: Int64 = definedExternally): Int64
  open fun path(value: Array<Asset> = definedExternally): Array<Asset>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PathPaymentStrictSendOp
    fun write(value: PathPaymentStrictSendOp, io: Buffer)
    fun isValid(value: PathPaymentStrictSendOp): Boolean
    fun toXDR(value: PathPaymentStrictSendOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): PathPaymentStrictSendOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PathPaymentStrictSendOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$184` {
  var selling: Asset
  var buying: Asset
  var amount: Int64
  var price: Price
  var offerId: Int64
}

external open class ManageSellOfferOp(attributes: `T$184`) {
  open fun selling(value: Asset = definedExternally): Asset
  open fun buying(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun price(value: Price = definedExternally): Price
  open fun offerId(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ManageSellOfferOp
    fun write(value: ManageSellOfferOp, io: Buffer)
    fun isValid(value: ManageSellOfferOp): Boolean
    fun toXDR(value: ManageSellOfferOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ManageSellOfferOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageSellOfferOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$185` {
  var selling: Asset
  var buying: Asset
  var buyAmount: Int64
  var price: Price
  var offerId: Int64
}

external open class ManageBuyOfferOp(attributes: `T$185`) {
  open fun selling(value: Asset = definedExternally): Asset
  open fun buying(value: Asset = definedExternally): Asset
  open fun buyAmount(value: Int64 = definedExternally): Int64
  open fun price(value: Price = definedExternally): Price
  open fun offerId(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ManageBuyOfferOp
    fun write(value: ManageBuyOfferOp, io: Buffer)
    fun isValid(value: ManageBuyOfferOp): Boolean
    fun toXDR(value: ManageBuyOfferOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ManageBuyOfferOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageBuyOfferOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$186` {
  var selling: Asset
  var buying: Asset
  var amount: Int64
  var price: Price
}

external open class CreatePassiveSellOfferOp(attributes: `T$186`) {
  open fun selling(value: Asset = definedExternally): Asset
  open fun buying(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun price(value: Price = definedExternally): Price
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): CreatePassiveSellOfferOp
    fun write(value: CreatePassiveSellOfferOp, io: Buffer)
    fun isValid(value: CreatePassiveSellOfferOp): Boolean
    fun toXDR(value: CreatePassiveSellOfferOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): CreatePassiveSellOfferOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): CreatePassiveSellOfferOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$187` {
  var inflationDest: AccountId?
  var clearFlags: Number?
  var setFlags: Number?
  var masterWeight: Number?
  var lowThreshold: Number?
  var medThreshold: Number?
  var highThreshold: Number?
  var homeDomain: dynamic /* String? | Buffer? */
    get() = definedExternally
    set(value) = definedExternally
  var signer: Signer?
}

external open class SetOptionsOp(attributes: `T$187`) {
  open fun inflationDest(value: AccountId? = definedExternally): AccountId?
  open fun clearFlags(value: Number? = definedExternally): Number?
  open fun setFlags(value: Number? = definedExternally): Number?
  open fun masterWeight(value: Number? = definedExternally): Number?
  open fun lowThreshold(value: Number? = definedExternally): Number?
  open fun medThreshold(value: Number? = definedExternally): Number?
  open fun highThreshold(value: Number? = definedExternally): Number?
  open fun homeDomain(value: String? = definedExternally): dynamic /* String? | Buffer? */
  open fun homeDomain(): dynamic /* String? | Buffer? */
  open fun homeDomain(value: Buffer? = definedExternally): dynamic /* String? | Buffer? */
  open fun signer(value: Signer? = definedExternally): Signer?
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SetOptionsOp
    fun write(value: SetOptionsOp, io: Buffer)
    fun isValid(value: SetOptionsOp): Boolean
    fun toXDR(value: SetOptionsOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SetOptionsOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SetOptionsOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$188` {
  var line: ChangeTrustAsset
  var limit: Int64
}

external open class ChangeTrustOp(attributes: `T$188`) {
  open fun line(value: ChangeTrustAsset = definedExternally): ChangeTrustAsset
  open fun limit(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ChangeTrustOp
    fun write(value: ChangeTrustOp, io: Buffer)
    fun isValid(value: ChangeTrustOp): Boolean
    fun toXDR(value: ChangeTrustOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ChangeTrustOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ChangeTrustOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$189` {
  var trustor: AccountId
  var asset: AssetCode
  var authorize: Number
}

external open class AllowTrustOp(attributes: `T$189`) {
  open fun trustor(value: AccountId = definedExternally): AccountId
  open fun asset(value: AssetCode = definedExternally): AssetCode
  open fun authorize(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AllowTrustOp
    fun write(value: AllowTrustOp, io: Buffer)
    fun isValid(value: AllowTrustOp): Boolean
    fun toXDR(value: AllowTrustOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AllowTrustOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AllowTrustOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$190` {
  var dataName: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
  var dataValue: Buffer?
}

external open class ManageDataOp(attributes: `T$190`) {
  open fun dataName(value: String = definedExternally): dynamic /* String | Buffer */
  open fun dataName(): dynamic /* String | Buffer */
  open fun dataName(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun dataValue(value: Buffer? = definedExternally): Buffer?
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ManageDataOp
    fun write(value: ManageDataOp, io: Buffer)
    fun isValid(value: ManageDataOp): Boolean
    fun toXDR(value: ManageDataOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ManageDataOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageDataOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$191` {
  var bumpTo: SequenceNumber
}

external open class BumpSequenceOp(attributes: `T$191`) {
  open fun bumpTo(value: SequenceNumber = definedExternally): SequenceNumber
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): BumpSequenceOp
    fun write(value: BumpSequenceOp, io: Buffer)
    fun isValid(value: BumpSequenceOp): Boolean
    fun toXDR(value: BumpSequenceOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): BumpSequenceOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): BumpSequenceOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$192` {
  var asset: Asset
  var amount: Int64
  var claimants: Array<Claimant>
}

external open class CreateClaimableBalanceOp(attributes: `T$192`) {
  open fun asset(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun claimants(value: Array<Claimant> = definedExternally): Array<Claimant>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): CreateClaimableBalanceOp
    fun write(value: CreateClaimableBalanceOp, io: Buffer)
    fun isValid(value: CreateClaimableBalanceOp): Boolean
    fun toXDR(value: CreateClaimableBalanceOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): CreateClaimableBalanceOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): CreateClaimableBalanceOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimClaimableBalanceOp(attributes: `T$140`) {
  open fun balanceId(value: ClaimableBalanceId = definedExternally): ClaimableBalanceId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimClaimableBalanceOp
    fun write(value: ClaimClaimableBalanceOp, io: Buffer)
    fun isValid(value: ClaimClaimableBalanceOp): Boolean
    fun toXDR(value: ClaimClaimableBalanceOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClaimClaimableBalanceOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimClaimableBalanceOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$193` {
  var sponsoredId: AccountId
}

external open class BeginSponsoringFutureReservesOp(attributes: `T$193`) {
  open fun sponsoredId(value: AccountId = definedExternally): AccountId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): BeginSponsoringFutureReservesOp
    fun write(value: BeginSponsoringFutureReservesOp, io: Buffer)
    fun isValid(value: BeginSponsoringFutureReservesOp): Boolean
    fun toXDR(value: BeginSponsoringFutureReservesOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): BeginSponsoringFutureReservesOp
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): BeginSponsoringFutureReservesOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$194` {
  var accountId: AccountId
  var signerKey: SignerKey
}

external open class RevokeSponsorshipOpSigner(attributes: `T$194`) {
  open fun accountId(value: AccountId = definedExternally): AccountId
  open fun signerKey(value: SignerKey = definedExternally): SignerKey
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): RevokeSponsorshipOpSigner
    fun write(value: RevokeSponsorshipOpSigner, io: Buffer)
    fun isValid(value: RevokeSponsorshipOpSigner): Boolean
    fun toXDR(value: RevokeSponsorshipOpSigner): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): RevokeSponsorshipOpSigner
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): RevokeSponsorshipOpSigner
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$195` {
  var asset: Asset
  var from: MuxedAccount
  var amount: Int64
}

external open class ClawbackOp(attributes: `T$195`) {
  open fun asset(value: Asset = definedExternally): Asset
  open fun from(value: MuxedAccount = definedExternally): MuxedAccount
  open fun amount(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClawbackOp
    fun write(value: ClawbackOp, io: Buffer)
    fun isValid(value: ClawbackOp): Boolean
    fun toXDR(value: ClawbackOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClawbackOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClawbackOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClawbackClaimableBalanceOp(attributes: `T$140`) {
  open fun balanceId(value: ClaimableBalanceId = definedExternally): ClaimableBalanceId
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClawbackClaimableBalanceOp
    fun write(value: ClawbackClaimableBalanceOp, io: Buffer)
    fun isValid(value: ClawbackClaimableBalanceOp): Boolean
    fun toXDR(value: ClawbackClaimableBalanceOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClawbackClaimableBalanceOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClawbackClaimableBalanceOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$196` {
  var trustor: AccountId
  var asset: Asset
  var clearFlags: Number
  var setFlags: Number
}

external open class SetTrustLineFlagsOp(attributes: `T$196`) {
  open fun trustor(value: AccountId = definedExternally): AccountId
  open fun asset(value: Asset = definedExternally): Asset
  open fun clearFlags(value: Number = definedExternally): Number
  open fun setFlags(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SetTrustLineFlagsOp
    fun write(value: SetTrustLineFlagsOp, io: Buffer)
    fun isValid(value: SetTrustLineFlagsOp): Boolean
    fun toXDR(value: SetTrustLineFlagsOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SetTrustLineFlagsOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SetTrustLineFlagsOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$197` {
  var liquidityPoolId: PoolId
  var maxAmountA: Int64
  var maxAmountB: Int64
  var minPrice: Price
  var maxPrice: Price
}

external open class LiquidityPoolDepositOp(attributes: `T$197`) {
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun maxAmountA(value: Int64 = definedExternally): Int64
  open fun maxAmountB(value: Int64 = definedExternally): Int64
  open fun minPrice(value: Price = definedExternally): Price
  open fun maxPrice(value: Price = definedExternally): Price
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LiquidityPoolDepositOp
    fun write(value: LiquidityPoolDepositOp, io: Buffer)
    fun isValid(value: LiquidityPoolDepositOp): Boolean
    fun toXDR(value: LiquidityPoolDepositOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolDepositOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolDepositOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$198` {
  var liquidityPoolId: PoolId
  var amount: Int64
  var minAmountA: Int64
  var minAmountB: Int64
}

external open class LiquidityPoolWithdrawOp(attributes: `T$198`) {
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun amount(value: Int64 = definedExternally): Int64
  open fun minAmountA(value: Int64 = definedExternally): Int64
  open fun minAmountB(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LiquidityPoolWithdrawOp
    fun write(value: LiquidityPoolWithdrawOp, io: Buffer)
    fun isValid(value: LiquidityPoolWithdrawOp): Boolean
    fun toXDR(value: LiquidityPoolWithdrawOp): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolWithdrawOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolWithdrawOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$199` {
  var sourceAccount: MuxedAccount?
  var body: OperationBody
}

external open class Operation2(attributes: `T$199`) {
  open fun sourceAccount(value: MuxedAccount? = definedExternally): MuxedAccount?
  open fun body(value: OperationBody = definedExternally): OperationBody
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Operation2__0
    fun write(value: Operation2__0, io: Buffer)
    fun isValid(value: Operation2__0): Boolean
    fun toXDR(value: Operation2__0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Operation2__0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Operation2__0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$200` {
  var sourceAccount: AccountId
  var seqNum: SequenceNumber
  var opNum: Number
}

external open class HashIdPreimageOperationId(attributes: `T$200`) {
  open fun sourceAccount(value: AccountId = definedExternally): AccountId
  open fun seqNum(value: SequenceNumber = definedExternally): SequenceNumber
  open fun opNum(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): HashIdPreimageOperationId
    fun write(value: HashIdPreimageOperationId, io: Buffer)
    fun isValid(value: HashIdPreimageOperationId): Boolean
    fun toXDR(value: HashIdPreimageOperationId): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): HashIdPreimageOperationId
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): HashIdPreimageOperationId
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$201` {
  var sourceAccount: AccountId
  var seqNum: SequenceNumber
  var opNum: Number
  var liquidityPoolId: PoolId
  var asset: Asset
}

external open class HashIdPreimageRevokeId(attributes: `T$201`) {
  open fun sourceAccount(value: AccountId = definedExternally): AccountId
  open fun seqNum(value: SequenceNumber = definedExternally): SequenceNumber
  open fun opNum(value: Number = definedExternally): Number
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun asset(value: Asset = definedExternally): Asset
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): HashIdPreimageRevokeId
    fun write(value: HashIdPreimageRevokeId, io: Buffer)
    fun isValid(value: HashIdPreimageRevokeId): Boolean
    fun toXDR(value: HashIdPreimageRevokeId): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): HashIdPreimageRevokeId
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): HashIdPreimageRevokeId
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$202` {
  var minTime: TimePoint
  var maxTime: TimePoint
}

external open class TimeBounds(attributes: `T$202`) {
  open fun minTime(value: TimePoint = definedExternally): TimePoint
  open fun maxTime(value: TimePoint = definedExternally): TimePoint
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TimeBounds
    fun write(value: TimeBounds, io: Buffer)
    fun isValid(value: TimeBounds): Boolean
    fun toXDR(value: TimeBounds): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TimeBounds
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TimeBounds
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerBounds(attributes: `T$102`) {
  open fun minLedger(value: Number = definedExternally): Number
  open fun maxLedger(value: Number = definedExternally): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerBounds
    fun write(value: LedgerBounds, io: Buffer)
    fun isValid(value: LedgerBounds): Boolean
    fun toXDR(value: LedgerBounds): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerBounds
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerBounds
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$203` {
  var timeBounds: TimeBounds?
  var ledgerBounds: LedgerBounds?
  var minSeqNum: SequenceNumber?
  var minSeqAge: Duration
  var minSeqLedgerGap: Number
  var extraSigners: Array<SignerKey>
}

external open class PreconditionsV2(attributes: `T$203`) {
  open fun timeBounds(value: TimeBounds? = definedExternally): TimeBounds?
  open fun ledgerBounds(value: LedgerBounds? = definedExternally): LedgerBounds?
  open fun minSeqNum(value: SequenceNumber? = definedExternally): SequenceNumber?
  open fun minSeqAge(value: Duration = definedExternally): Duration
  open fun minSeqLedgerGap(value: Number = definedExternally): Number
  open fun extraSigners(value: Array<SignerKey> = definedExternally): Array<SignerKey>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PreconditionsV2
    fun write(value: PreconditionsV2, io: Buffer)
    fun isValid(value: PreconditionsV2): Boolean
    fun toXDR(value: PreconditionsV2): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PreconditionsV2
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PreconditionsV2
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$204` {
  var sourceAccountEd25519: Buffer
  var fee: Number
  var seqNum: SequenceNumber
  var timeBounds: TimeBounds?
  var memo: Memo
  var operations: Array<Operation2__0>
  var ext: TransactionV0Ext
}

external open class TransactionV0(attributes: `T$204`) {
  open fun sourceAccountEd25519(value: Buffer = definedExternally): Buffer
  open fun fee(value: Number = definedExternally): Number
  open fun seqNum(value: SequenceNumber = definedExternally): SequenceNumber
  open fun timeBounds(value: TimeBounds? = definedExternally): TimeBounds?
  open fun memo(value: Memo = definedExternally): Memo
  open fun operations(value: Array<Operation2__0> = definedExternally): Array<Operation2__0>
  open fun ext(value: TransactionV0Ext = definedExternally): TransactionV0Ext
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionV0
    fun write(value: TransactionV0, io: Buffer)
    fun isValid(value: TransactionV0): Boolean
    fun toXDR(value: TransactionV0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionV0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionV0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$205` {
  var tx: TransactionV0
  var signatures: Array<DecoratedSignature>
}

external open class TransactionV0Envelope(attributes: `T$205`) {
  open fun tx(value: TransactionV0 = definedExternally): TransactionV0
  open fun signatures(
    value: Array<DecoratedSignature> = definedExternally
  ): Array<DecoratedSignature>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionV0Envelope
    fun write(value: TransactionV0Envelope, io: Buffer)
    fun isValid(value: TransactionV0Envelope): Boolean
    fun toXDR(value: TransactionV0Envelope): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionV0Envelope
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionV0Envelope
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$206` {
  var sourceAccount: MuxedAccount
  var fee: Number
  var seqNum: SequenceNumber
  var cond: Preconditions
  var memo: Memo
  var operations: Array<Operation2__0>
  var ext: TransactionExt
}

external open class Transaction(attributes: `T$206`) {
  open fun sourceAccount(value: MuxedAccount = definedExternally): MuxedAccount
  open fun fee(value: Number = definedExternally): Number
  open fun seqNum(value: SequenceNumber = definedExternally): SequenceNumber
  open fun cond(value: Preconditions = definedExternally): Preconditions
  open fun memo(value: Memo = definedExternally): Memo
  open fun operations(value: Array<Operation2__0> = definedExternally): Array<Operation2__0>
  open fun ext(value: TransactionExt = definedExternally): TransactionExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Transaction
    fun write(value: Transaction, io: Buffer)
    fun isValid(value: Transaction): Boolean
    fun toXDR(value: Transaction): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Transaction
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Transaction
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$207` {
  var tx: Transaction
  var signatures: Array<DecoratedSignature>
}

external open class TransactionV1Envelope(attributes: `T$207`) {
  open fun tx(value: Transaction = definedExternally): Transaction
  open fun signatures(
    value: Array<DecoratedSignature> = definedExternally
  ): Array<DecoratedSignature>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionV1Envelope
    fun write(value: TransactionV1Envelope, io: Buffer)
    fun isValid(value: TransactionV1Envelope): Boolean
    fun toXDR(value: TransactionV1Envelope): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionV1Envelope
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionV1Envelope
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$208` {
  var feeSource: MuxedAccount
  var fee: Int64
  var innerTx: FeeBumpTransactionInnerTx
  var ext: FeeBumpTransactionExt
}

external open class FeeBumpTransaction(attributes: `T$208`) {
  open fun feeSource(value: MuxedAccount = definedExternally): MuxedAccount
  open fun fee(value: Int64 = definedExternally): Int64
  open fun innerTx(value: FeeBumpTransactionInnerTx = definedExternally): FeeBumpTransactionInnerTx
  open fun ext(value: FeeBumpTransactionExt = definedExternally): FeeBumpTransactionExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): FeeBumpTransaction
    fun write(value: FeeBumpTransaction, io: Buffer)
    fun isValid(value: FeeBumpTransaction): Boolean
    fun toXDR(value: FeeBumpTransaction): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): FeeBumpTransaction
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): FeeBumpTransaction
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$209` {
  var tx: FeeBumpTransaction
  var signatures: Array<DecoratedSignature>
}

external open class FeeBumpTransactionEnvelope(attributes: `T$209`) {
  open fun tx(value: FeeBumpTransaction = definedExternally): FeeBumpTransaction
  open fun signatures(
    value: Array<DecoratedSignature> = definedExternally
  ): Array<DecoratedSignature>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): FeeBumpTransactionEnvelope
    fun write(value: FeeBumpTransactionEnvelope, io: Buffer)
    fun isValid(value: FeeBumpTransactionEnvelope): Boolean
    fun toXDR(value: FeeBumpTransactionEnvelope): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): FeeBumpTransactionEnvelope
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): FeeBumpTransactionEnvelope
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$210` {
  var networkId: Buffer
  var taggedTransaction: TransactionSignaturePayloadTaggedTransaction
}

external open class TransactionSignaturePayload(attributes: `T$210`) {
  open fun networkId(value: Buffer = definedExternally): Buffer
  open fun taggedTransaction(
    value: TransactionSignaturePayloadTaggedTransaction = definedExternally
  ): TransactionSignaturePayloadTaggedTransaction
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionSignaturePayload
    fun write(value: TransactionSignaturePayload, io: Buffer)
    fun isValid(value: TransactionSignaturePayload): Boolean
    fun toXDR(value: TransactionSignaturePayload): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionSignaturePayload
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionSignaturePayload
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$211` {
  var sellerEd25519: Buffer
  var offerId: Int64
  var assetSold: Asset
  var amountSold: Int64
  var assetBought: Asset
  var amountBought: Int64
}

external open class ClaimOfferAtomV0(attributes: `T$211`) {
  open fun sellerEd25519(value: Buffer = definedExternally): Buffer
  open fun offerId(value: Int64 = definedExternally): Int64
  open fun assetSold(value: Asset = definedExternally): Asset
  open fun amountSold(value: Int64 = definedExternally): Int64
  open fun assetBought(value: Asset = definedExternally): Asset
  open fun amountBought(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimOfferAtomV0
    fun write(value: ClaimOfferAtomV0, io: Buffer)
    fun isValid(value: ClaimOfferAtomV0): Boolean
    fun toXDR(value: ClaimOfferAtomV0): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimOfferAtomV0
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimOfferAtomV0
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$212` {
  var sellerId: AccountId
  var offerId: Int64
  var assetSold: Asset
  var amountSold: Int64
  var assetBought: Asset
  var amountBought: Int64
}

external open class ClaimOfferAtom(attributes: `T$212`) {
  open fun sellerId(value: AccountId = definedExternally): AccountId
  open fun offerId(value: Int64 = definedExternally): Int64
  open fun assetSold(value: Asset = definedExternally): Asset
  open fun amountSold(value: Int64 = definedExternally): Int64
  open fun assetBought(value: Asset = definedExternally): Asset
  open fun amountBought(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimOfferAtom
    fun write(value: ClaimOfferAtom, io: Buffer)
    fun isValid(value: ClaimOfferAtom): Boolean
    fun toXDR(value: ClaimOfferAtom): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimOfferAtom
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimOfferAtom
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$213` {
  var liquidityPoolId: PoolId
  var assetSold: Asset
  var amountSold: Int64
  var assetBought: Asset
  var amountBought: Int64
}

external open class ClaimLiquidityAtom(attributes: `T$213`) {
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun assetSold(value: Asset = definedExternally): Asset
  open fun amountSold(value: Int64 = definedExternally): Int64
  open fun assetBought(value: Asset = definedExternally): Asset
  open fun amountBought(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimLiquidityAtom
    fun write(value: ClaimLiquidityAtom, io: Buffer)
    fun isValid(value: ClaimLiquidityAtom): Boolean
    fun toXDR(value: ClaimLiquidityAtom): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimLiquidityAtom
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimLiquidityAtom
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$214` {
  var destination: AccountId
  var asset: Asset
  var amount: Int64
}

external open class SimplePaymentResult(attributes: `T$214`) {
  open fun destination(value: AccountId = definedExternally): AccountId
  open fun asset(value: Asset = definedExternally): Asset
  open fun amount(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SimplePaymentResult
    fun write(value: SimplePaymentResult, io: Buffer)
    fun isValid(value: SimplePaymentResult): Boolean
    fun toXDR(value: SimplePaymentResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SimplePaymentResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SimplePaymentResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$215` {
  var offers: Array<ClaimAtom>
  var last: SimplePaymentResult
}

external open class PathPaymentStrictReceiveResultSuccess(attributes: `T$215`) {
  open fun offers(value: Array<ClaimAtom> = definedExternally): Array<ClaimAtom>
  open fun last(value: SimplePaymentResult = definedExternally): SimplePaymentResult
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PathPaymentStrictReceiveResultSuccess
    fun write(value: PathPaymentStrictReceiveResultSuccess, io: Buffer)
    fun isValid(value: PathPaymentStrictReceiveResultSuccess): Boolean
    fun toXDR(value: PathPaymentStrictReceiveResultSuccess): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): PathPaymentStrictReceiveResultSuccess
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): PathPaymentStrictReceiveResultSuccess
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$216` {
  var offers: Array<ClaimAtom>
  var last: SimplePaymentResult
}

external open class PathPaymentStrictSendResultSuccess(attributes: `T$216`) {
  open fun offers(value: Array<ClaimAtom> = definedExternally): Array<ClaimAtom>
  open fun last(value: SimplePaymentResult = definedExternally): SimplePaymentResult
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): PathPaymentStrictSendResultSuccess
    fun write(value: PathPaymentStrictSendResultSuccess, io: Buffer)
    fun isValid(value: PathPaymentStrictSendResultSuccess): Boolean
    fun toXDR(value: PathPaymentStrictSendResultSuccess): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): PathPaymentStrictSendResultSuccess
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): PathPaymentStrictSendResultSuccess
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$217` {
  var offersClaimed: Array<ClaimAtom>
  var offer: ManageOfferSuccessResultOffer
}

external open class ManageOfferSuccessResult(attributes: `T$217`) {
  open fun offersClaimed(value: Array<ClaimAtom> = definedExternally): Array<ClaimAtom>
  open fun offer(
    value: ManageOfferSuccessResultOffer = definedExternally
  ): ManageOfferSuccessResultOffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ManageOfferSuccessResult
    fun write(value: ManageOfferSuccessResult, io: Buffer)
    fun isValid(value: ManageOfferSuccessResult): Boolean
    fun toXDR(value: ManageOfferSuccessResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ManageOfferSuccessResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageOfferSuccessResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$218` {
  var destination: AccountId
  var amount: Int64
}

external open class InflationPayout(attributes: `T$218`) {
  open fun destination(value: AccountId = definedExternally): AccountId
  open fun amount(value: Int64 = definedExternally): Int64
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): InflationPayout
    fun write(value: InflationPayout, io: Buffer)
    fun isValid(value: InflationPayout): Boolean
    fun toXDR(value: InflationPayout): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): InflationPayout
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): InflationPayout
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$219` {
  var feeCharged: Int64
  var result: InnerTransactionResultResult
  var ext: InnerTransactionResultExt
}

external open class InnerTransactionResult(attributes: `T$219`) {
  open fun feeCharged(value: Int64 = definedExternally): Int64
  open fun result(
    value: InnerTransactionResultResult = definedExternally
  ): InnerTransactionResultResult
  open fun ext(value: InnerTransactionResultExt = definedExternally): InnerTransactionResultExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): InnerTransactionResult
    fun write(value: InnerTransactionResult, io: Buffer)
    fun isValid(value: InnerTransactionResult): Boolean
    fun toXDR(value: InnerTransactionResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): InnerTransactionResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): InnerTransactionResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$220` {
  var transactionHash: Buffer
  var result: InnerTransactionResult
}

external open class InnerTransactionResultPair(attributes: `T$220`) {
  open fun transactionHash(value: Buffer = definedExternally): Buffer
  open fun result(value: InnerTransactionResult = definedExternally): InnerTransactionResult
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): InnerTransactionResultPair
    fun write(value: InnerTransactionResultPair, io: Buffer)
    fun isValid(value: InnerTransactionResultPair): Boolean
    fun toXDR(value: InnerTransactionResultPair): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): InnerTransactionResultPair
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): InnerTransactionResultPair
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$221` {
  var feeCharged: Int64
  var result: TransactionResultResult
  var ext: TransactionResultExt
}

external open class TransactionResult(attributes: `T$221`) {
  open fun feeCharged(value: Int64 = definedExternally): Int64
  open fun result(value: TransactionResultResult = definedExternally): TransactionResultResult
  open fun ext(value: TransactionResultExt = definedExternally): TransactionResultExt
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionResult
    fun write(value: TransactionResult, io: Buffer)
    fun isValid(value: TransactionResult): Boolean
    fun toXDR(value: TransactionResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$222` {
  var ed25519: Buffer
  var payload: Buffer
}

external open class SignerKeyEd25519SignedPayload(attributes: `T$222`) {
  open fun ed25519(value: Buffer = definedExternally): Buffer
  open fun payload(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): SignerKeyEd25519SignedPayload
    fun write(value: SignerKeyEd25519SignedPayload, io: Buffer)
    fun isValid(value: SignerKeyEd25519SignedPayload): Boolean
    fun toXDR(value: SignerKeyEd25519SignedPayload): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): SignerKeyEd25519SignedPayload
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SignerKeyEd25519SignedPayload
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$223` {
  var key: Buffer
}

external open class Curve25519Secret(attributes: `T$223`) {
  open fun key(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Curve25519Secret
    fun write(value: Curve25519Secret, io: Buffer)
    fun isValid(value: Curve25519Secret): Boolean
    fun toXDR(value: Curve25519Secret): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Curve25519Secret
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Curve25519Secret
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class Curve25519Public(attributes: `T$223`) {
  open fun key(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): Curve25519Public
    fun write(value: Curve25519Public, io: Buffer)
    fun isValid(value: Curve25519Public): Boolean
    fun toXDR(value: Curve25519Public): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Curve25519Public
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Curve25519Public
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class HmacSha256Key(attributes: `T$223`) {
  open fun key(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): HmacSha256Key
    fun write(value: HmacSha256Key, io: Buffer)
    fun isValid(value: HmacSha256Key): Boolean
    fun toXDR(value: HmacSha256Key): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): HmacSha256Key
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): HmacSha256Key
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external interface `T$224` {
  var mac: Buffer
}

external open class HmacSha256Mac(attributes: `T$224`) {
  open fun mac(value: Buffer = definedExternally): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): HmacSha256Mac
    fun write(value: HmacSha256Mac, io: Buffer)
    fun isValid(value: HmacSha256Mac): Boolean
    fun toXDR(value: HmacSha256Mac): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): HmacSha256Mac
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): HmacSha256Mac
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ScpStatementPledges {
  open fun switch(): ScpStatementType
  open fun prepare(value: ScpStatementPrepare = definedExternally): ScpStatementPrepare
  open fun confirm(value: ScpStatementConfirm = definedExternally): ScpStatementConfirm
  open fun externalize(value: ScpStatementExternalize = definedExternally): ScpStatementExternalize
  open fun nominate(value: ScpNomination = definedExternally): ScpNomination
  open fun value():
    dynamic /* ScpStatementPrepare | ScpStatementConfirm | ScpStatementExternalize | ScpNomination */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun scpStPrepare(value: ScpStatementPrepare): ScpStatementPledges
    fun scpStConfirm(value: ScpStatementConfirm): ScpStatementPledges
    fun scpStExternalize(value: ScpStatementExternalize): ScpStatementPledges
    fun scpStNominate(value: ScpNomination): ScpStatementPledges
    fun read(io: Buffer): ScpStatementPledges
    fun write(value: ScpStatementPledges, io: Buffer)
    fun isValid(value: ScpStatementPledges): Boolean
    fun toXDR(value: ScpStatementPledges): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpStatementPledges
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpStatementPledges
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AssetCode {
  open fun switch(): AssetType
  open fun assetCode4(value: Buffer = definedExternally): Buffer
  open fun assetCode12(value: Buffer = definedExternally): Buffer
  open fun value(): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun assetTypeCreditAlphanum4(value: Buffer): AssetCode
    fun assetTypeCreditAlphanum12(value: Buffer): AssetCode
    fun read(io: Buffer): AssetCode
    fun write(value: AssetCode, io: Buffer)
    fun isValid(value: AssetCode): Boolean
    fun toXDR(value: AssetCode): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AssetCode
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AssetCode
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class Asset {
  open fun switch(): AssetType
  open fun alphaNum4(value: AlphaNum4 = definedExternally): AlphaNum4
  open fun alphaNum12(value: AlphaNum12 = definedExternally): AlphaNum12
  open fun value(): dynamic /* AlphaNum4 | AlphaNum12 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun assetTypeNative(): Asset
    fun assetTypeCreditAlphanum4(value: AlphaNum4): Asset
    fun assetTypeCreditAlphanum12(value: AlphaNum12): Asset
    fun read(io: Buffer): Asset
    fun write(value: Asset, io: Buffer)
    fun isValid(value: Asset): Boolean
    fun toXDR(value: Asset): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Asset
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Asset
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AccountEntryExtensionV2Ext {
  open fun switch(): Number
  open fun v3(value: AccountEntryExtensionV3 = definedExternally): AccountEntryExtensionV3
  open fun value(): dynamic /* AccountEntryExtensionV3 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntryExtensionV2Ext
    fun write(value: AccountEntryExtensionV2Ext, io: Buffer)
    fun isValid(value: AccountEntryExtensionV2Ext): Boolean
    fun toXDR(value: AccountEntryExtensionV2Ext): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): AccountEntryExtensionV2Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntryExtensionV2Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AccountEntryExtensionV1Ext {
  open fun switch(): Number
  open fun v2(value: AccountEntryExtensionV2 = definedExternally): AccountEntryExtensionV2
  open fun value(): dynamic /* AccountEntryExtensionV2 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntryExtensionV1Ext
    fun write(value: AccountEntryExtensionV1Ext, io: Buffer)
    fun isValid(value: AccountEntryExtensionV1Ext): Boolean
    fun toXDR(value: AccountEntryExtensionV1Ext): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): AccountEntryExtensionV1Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntryExtensionV1Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AccountEntryExt {
  open fun switch(): Number
  open fun v1(value: AccountEntryExtensionV1 = definedExternally): AccountEntryExtensionV1
  open fun value(): dynamic /* AccountEntryExtensionV1 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AccountEntryExt
    fun write(value: AccountEntryExt, io: Buffer)
    fun isValid(value: AccountEntryExt): Boolean
    fun toXDR(value: AccountEntryExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AccountEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TrustLineAsset {
  open fun switch(): AssetType
  open fun alphaNum4(value: AlphaNum4 = definedExternally): AlphaNum4
  open fun alphaNum12(value: AlphaNum12 = definedExternally): AlphaNum12
  open fun liquidityPoolId(value: PoolId = definedExternally): PoolId
  open fun value(): dynamic /* AlphaNum4 | AlphaNum12 | PoolId | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun assetTypeNative(): TrustLineAsset
    fun assetTypeCreditAlphanum4(value: AlphaNum4): TrustLineAsset
    fun assetTypeCreditAlphanum12(value: AlphaNum12): TrustLineAsset
    fun assetTypePoolShare(value: PoolId): TrustLineAsset
    fun read(io: Buffer): TrustLineAsset
    fun write(value: TrustLineAsset, io: Buffer)
    fun isValid(value: TrustLineAsset): Boolean
    fun toXDR(value: TrustLineAsset): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TrustLineAsset
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineAsset
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TrustLineEntryExtensionV2Ext {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TrustLineEntryExtensionV2Ext
    fun write(value: TrustLineEntryExtensionV2Ext, io: Buffer)
    fun isValid(value: TrustLineEntryExtensionV2Ext): Boolean
    fun toXDR(value: TrustLineEntryExtensionV2Ext): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TrustLineEntryExtensionV2Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineEntryExtensionV2Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TrustLineEntryV1Ext {
  open fun switch(): Number
  open fun v2(value: TrustLineEntryExtensionV2 = definedExternally): TrustLineEntryExtensionV2
  open fun value(): dynamic /* TrustLineEntryExtensionV2 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TrustLineEntryV1Ext
    fun write(value: TrustLineEntryV1Ext, io: Buffer)
    fun isValid(value: TrustLineEntryV1Ext): Boolean
    fun toXDR(value: TrustLineEntryV1Ext): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TrustLineEntryV1Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineEntryV1Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TrustLineEntryExt {
  open fun switch(): Number
  open fun v1(value: TrustLineEntryV1 = definedExternally): TrustLineEntryV1
  open fun value(): dynamic /* TrustLineEntryV1 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TrustLineEntryExt
    fun write(value: TrustLineEntryExt, io: Buffer)
    fun isValid(value: TrustLineEntryExt): Boolean
    fun toXDR(value: TrustLineEntryExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TrustLineEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TrustLineEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class OfferEntryExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): OfferEntryExt
    fun write(value: OfferEntryExt, io: Buffer)
    fun isValid(value: OfferEntryExt): Boolean
    fun toXDR(value: OfferEntryExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): OfferEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): OfferEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class DataEntryExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): DataEntryExt
    fun write(value: DataEntryExt, io: Buffer)
    fun isValid(value: DataEntryExt): Boolean
    fun toXDR(value: DataEntryExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): DataEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): DataEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimPredicate {
  open fun switch(): ClaimPredicateType
  open fun andPredicates(value: Array<ClaimPredicate> = definedExternally): Array<ClaimPredicate>
  open fun orPredicates(value: Array<ClaimPredicate> = definedExternally): Array<ClaimPredicate>
  open fun notPredicate(value: ClaimPredicate? = definedExternally): ClaimPredicate?
  open fun absBefore(value: Int64 = definedExternally): Int64
  open fun relBefore(value: Int64 = definedExternally): Int64
  open fun value():
    dynamic /* Array<ClaimPredicate>? | Array<ClaimPredicate>? | ClaimPredicate? | Int64? | Unit? */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun claimPredicateUnconditional(): ClaimPredicate
    fun claimPredicateAnd(value: Array<ClaimPredicate>): ClaimPredicate
    fun claimPredicateOr(value: Array<ClaimPredicate>): ClaimPredicate
    fun claimPredicateNot(value: ClaimPredicate?): ClaimPredicate
    fun claimPredicateBeforeAbsoluteTime(value: Int64): ClaimPredicate
    fun claimPredicateBeforeRelativeTime(value: Int64): ClaimPredicate
    fun read(io: Buffer): ClaimPredicate
    fun write(value: ClaimPredicate, io: Buffer)
    fun isValid(value: ClaimPredicate): Boolean
    fun toXDR(value: ClaimPredicate): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimPredicate
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimPredicate
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class Claimant {
  open fun switch(): ClaimantType
  open fun v0(value: ClaimantV0 = definedExternally): ClaimantV0
  open fun value(): ClaimantV0
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun claimantTypeV0(value: ClaimantV0): Claimant
    fun read(io: Buffer): Claimant
    fun write(value: Claimant, io: Buffer)
    fun isValid(value: Claimant): Boolean
    fun toXDR(value: Claimant): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Claimant
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Claimant
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimableBalanceId {
  open fun switch(): ClaimableBalanceIdType
  open fun v0(value: Buffer = definedExternally): Buffer
  open fun value(): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun claimableBalanceIdTypeV0(value: Buffer): ClaimableBalanceId
    fun read(io: Buffer): ClaimableBalanceId
    fun write(value: ClaimableBalanceId, io: Buffer)
    fun isValid(value: ClaimableBalanceId): Boolean
    fun toXDR(value: ClaimableBalanceId): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimableBalanceId
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimableBalanceId
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimableBalanceEntryExtensionV1Ext {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimableBalanceEntryExtensionV1Ext
    fun write(value: ClaimableBalanceEntryExtensionV1Ext, io: Buffer)
    fun isValid(value: ClaimableBalanceEntryExtensionV1Ext): Boolean
    fun toXDR(value: ClaimableBalanceEntryExtensionV1Ext): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClaimableBalanceEntryExtensionV1Ext
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): ClaimableBalanceEntryExtensionV1Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimableBalanceEntryExt {
  open fun switch(): Number
  open fun v1(
    value: ClaimableBalanceEntryExtensionV1 = definedExternally
  ): ClaimableBalanceEntryExtensionV1
  open fun value(): dynamic /* ClaimableBalanceEntryExtensionV1 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ClaimableBalanceEntryExt
    fun write(value: ClaimableBalanceEntryExt, io: Buffer)
    fun isValid(value: ClaimableBalanceEntryExt): Boolean
    fun toXDR(value: ClaimableBalanceEntryExt): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClaimableBalanceEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimableBalanceEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LiquidityPoolEntryBody {
  open fun switch(): LiquidityPoolType
  open fun constantProduct(
    value: LiquidityPoolEntryConstantProduct = definedExternally
  ): LiquidityPoolEntryConstantProduct
  open fun value(): LiquidityPoolEntryConstantProduct
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun liquidityPoolConstantProduct(
      value: LiquidityPoolEntryConstantProduct
    ): LiquidityPoolEntryBody
    fun read(io: Buffer): LiquidityPoolEntryBody
    fun write(value: LiquidityPoolEntryBody, io: Buffer)
    fun isValid(value: LiquidityPoolEntryBody): Boolean
    fun toXDR(value: LiquidityPoolEntryBody): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolEntryBody
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolEntryBody
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerEntryExtensionV1Ext {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerEntryExtensionV1Ext
    fun write(value: LedgerEntryExtensionV1Ext, io: Buffer)
    fun isValid(value: LedgerEntryExtensionV1Ext): Boolean
    fun toXDR(value: LedgerEntryExtensionV1Ext): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerEntryExtensionV1Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerEntryExtensionV1Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerEntryData {
  open fun switch(): LedgerEntryType
  open fun account(value: AccountEntry = definedExternally): AccountEntry
  open fun trustLine(value: TrustLineEntry = definedExternally): TrustLineEntry
  open fun offer(value: OfferEntry = definedExternally): OfferEntry
  open fun data(value: DataEntry = definedExternally): DataEntry
  open fun claimableBalance(value: ClaimableBalanceEntry = definedExternally): ClaimableBalanceEntry
  open fun liquidityPool(value: LiquidityPoolEntry = definedExternally): LiquidityPoolEntry
  open fun value():
    dynamic /* AccountEntry | TrustLineEntry | OfferEntry | DataEntry | ClaimableBalanceEntry | LiquidityPoolEntry */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun account(value: AccountEntry): LedgerEntryData
    fun trustline(value: TrustLineEntry): LedgerEntryData
    fun offer(value: OfferEntry): LedgerEntryData
    fun data(value: DataEntry): LedgerEntryData
    fun claimableBalance(value: ClaimableBalanceEntry): LedgerEntryData
    fun liquidityPool(value: LiquidityPoolEntry): LedgerEntryData
    fun read(io: Buffer): LedgerEntryData
    fun write(value: LedgerEntryData, io: Buffer)
    fun isValid(value: LedgerEntryData): Boolean
    fun toXDR(value: LedgerEntryData): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerEntryData
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerEntryData
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerEntryExt {
  open fun switch(): Number
  open fun v1(value: LedgerEntryExtensionV1 = definedExternally): LedgerEntryExtensionV1
  open fun value(): dynamic /* LedgerEntryExtensionV1 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerEntryExt
    fun write(value: LedgerEntryExt, io: Buffer)
    fun isValid(value: LedgerEntryExt): Boolean
    fun toXDR(value: LedgerEntryExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerKey {
  open fun switch(): LedgerEntryType
  open fun account(value: LedgerKeyAccount = definedExternally): LedgerKeyAccount
  open fun trustLine(value: LedgerKeyTrustLine = definedExternally): LedgerKeyTrustLine
  open fun offer(value: LedgerKeyOffer = definedExternally): LedgerKeyOffer
  open fun data(value: LedgerKeyData = definedExternally): LedgerKeyData
  open fun claimableBalance(
    value: LedgerKeyClaimableBalance = definedExternally
  ): LedgerKeyClaimableBalance
  open fun liquidityPool(value: LedgerKeyLiquidityPool = definedExternally): LedgerKeyLiquidityPool
  open fun value():
    dynamic /* LedgerKeyAccount | LedgerKeyTrustLine | LedgerKeyOffer | LedgerKeyData | LedgerKeyClaimableBalance | LedgerKeyLiquidityPool */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun account(value: LedgerKeyAccount): LedgerKey
    fun trustline(value: LedgerKeyTrustLine): LedgerKey
    fun offer(value: LedgerKeyOffer): LedgerKey
    fun data(value: LedgerKeyData): LedgerKey
    fun claimableBalance(value: LedgerKeyClaimableBalance): LedgerKey
    fun liquidityPool(value: LedgerKeyLiquidityPool): LedgerKey
    fun read(io: Buffer): LedgerKey
    fun write(value: LedgerKey, io: Buffer)
    fun isValid(value: LedgerKey): Boolean
    fun toXDR(value: LedgerKey): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerKey
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerKey
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class StellarValueExt {
  open fun switch(): StellarValueType
  open fun lcValueSignature(
    value: LedgerCloseValueSignature = definedExternally
  ): LedgerCloseValueSignature
  open fun value(): dynamic /* LedgerCloseValueSignature | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun stellarValueBasic(): StellarValueExt
    fun stellarValueSigned(value: LedgerCloseValueSignature): StellarValueExt
    fun read(io: Buffer): StellarValueExt
    fun write(value: StellarValueExt, io: Buffer)
    fun isValid(value: StellarValueExt): Boolean
    fun toXDR(value: StellarValueExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): StellarValueExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): StellarValueExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerHeaderExtensionV1Ext {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerHeaderExtensionV1Ext
    fun write(value: LedgerHeaderExtensionV1Ext, io: Buffer)
    fun isValid(value: LedgerHeaderExtensionV1Ext): Boolean
    fun toXDR(value: LedgerHeaderExtensionV1Ext): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerHeaderExtensionV1Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerHeaderExtensionV1Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerHeaderExt {
  open fun switch(): Number
  open fun v1(value: LedgerHeaderExtensionV1 = definedExternally): LedgerHeaderExtensionV1
  open fun value(): dynamic /* LedgerHeaderExtensionV1 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerHeaderExt
    fun write(value: LedgerHeaderExt, io: Buffer)
    fun isValid(value: LedgerHeaderExt): Boolean
    fun toXDR(value: LedgerHeaderExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerHeaderExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerHeaderExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerUpgrade {
  open fun switch(): LedgerUpgradeType
  open fun newLedgerVersion(value: Number = definedExternally): Number
  open fun newBaseFee(value: Number = definedExternally): Number
  open fun newMaxTxSetSize(value: Number = definedExternally): Number
  open fun newBaseReserve(value: Number = definedExternally): Number
  open fun newFlags(value: Number = definedExternally): Number
  open fun value(): Number
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun ledgerUpgradeVersion(value: Number): LedgerUpgrade
    fun ledgerUpgradeBaseFee(value: Number): LedgerUpgrade
    fun ledgerUpgradeMaxTxSetSize(value: Number): LedgerUpgrade
    fun ledgerUpgradeBaseReserve(value: Number): LedgerUpgrade
    fun ledgerUpgradeFlags(value: Number): LedgerUpgrade
    fun read(io: Buffer): LedgerUpgrade
    fun write(value: LedgerUpgrade, io: Buffer)
    fun isValid(value: LedgerUpgrade): Boolean
    fun toXDR(value: LedgerUpgrade): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerUpgrade
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerUpgrade
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class BucketMetadataExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): BucketMetadataExt
    fun write(value: BucketMetadataExt, io: Buffer)
    fun isValid(value: BucketMetadataExt): Boolean
    fun toXDR(value: BucketMetadataExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): BucketMetadataExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): BucketMetadataExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class BucketEntry {
  open fun switch(): BucketEntryType
  open fun liveEntry(value: LedgerEntry = definedExternally): LedgerEntry
  open fun deadEntry(value: LedgerKey = definedExternally): LedgerKey
  open fun metaEntry(value: BucketMetadata = definedExternally): BucketMetadata
  open fun value(): dynamic /* LedgerEntry | LedgerKey | BucketMetadata */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun liveentry(value: LedgerEntry): BucketEntry
    fun initentry(value: LedgerEntry): BucketEntry
    fun deadentry(value: LedgerKey): BucketEntry
    fun metaentry(value: BucketMetadata): BucketEntry
    fun read(io: Buffer): BucketEntry
    fun write(value: BucketEntry, io: Buffer)
    fun isValid(value: BucketEntry): Boolean
    fun toXDR(value: BucketEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): BucketEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): BucketEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TxSetComponent {
  open fun switch(): TxSetComponentType
  open fun txsMaybeDiscountedFee(
    value: TxSetComponentTxsMaybeDiscountedFee = definedExternally
  ): TxSetComponentTxsMaybeDiscountedFee
  open fun value(): TxSetComponentTxsMaybeDiscountedFee
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun txsetCompTxsMaybeDiscountedFee(value: TxSetComponentTxsMaybeDiscountedFee): TxSetComponent
    fun read(io: Buffer): TxSetComponent
    fun write(value: TxSetComponent, io: Buffer)
    fun isValid(value: TxSetComponent): Boolean
    fun toXDR(value: TxSetComponent): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TxSetComponent
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TxSetComponent
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionPhase {
  open fun switch(): Number
  open fun v0Components(value: Array<TxSetComponent> = definedExternally): Array<TxSetComponent>
  open fun value(): Array<TxSetComponent>
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionPhase
    fun write(value: TransactionPhase, io: Buffer)
    fun isValid(value: TransactionPhase): Boolean
    fun toXDR(value: TransactionPhase): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionPhase
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionPhase
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class GeneralizedTransactionSet {
  open fun switch(): Number
  open fun v1TxSet(value: TransactionSetV1 = definedExternally): TransactionSetV1
  open fun value(): TransactionSetV1
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): GeneralizedTransactionSet
    fun write(value: GeneralizedTransactionSet, io: Buffer)
    fun isValid(value: GeneralizedTransactionSet): Boolean
    fun toXDR(value: GeneralizedTransactionSet): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): GeneralizedTransactionSet
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): GeneralizedTransactionSet
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionHistoryEntryExt {
  open fun switch(): Number
  open fun generalizedTxSet(
    value: GeneralizedTransactionSet = definedExternally
  ): GeneralizedTransactionSet
  open fun value(): dynamic /* GeneralizedTransactionSet | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionHistoryEntryExt
    fun write(value: TransactionHistoryEntryExt, io: Buffer)
    fun isValid(value: TransactionHistoryEntryExt): Boolean
    fun toXDR(value: TransactionHistoryEntryExt): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionHistoryEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionHistoryEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionHistoryResultEntryExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionHistoryResultEntryExt
    fun write(value: TransactionHistoryResultEntryExt, io: Buffer)
    fun isValid(value: TransactionHistoryResultEntryExt): Boolean
    fun toXDR(value: TransactionHistoryResultEntryExt): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionHistoryResultEntryExt
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): TransactionHistoryResultEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerHeaderHistoryEntryExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerHeaderHistoryEntryExt
    fun write(value: LedgerHeaderHistoryEntryExt, io: Buffer)
    fun isValid(value: LedgerHeaderHistoryEntryExt): Boolean
    fun toXDR(value: LedgerHeaderHistoryEntryExt): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LedgerHeaderHistoryEntryExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerHeaderHistoryEntryExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ScpHistoryEntry {
  open fun switch(): Number
  open fun v0(value: ScpHistoryEntryV0 = definedExternally): ScpHistoryEntryV0
  open fun value(): ScpHistoryEntryV0
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ScpHistoryEntry
    fun write(value: ScpHistoryEntry, io: Buffer)
    fun isValid(value: ScpHistoryEntry): Boolean
    fun toXDR(value: ScpHistoryEntry): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ScpHistoryEntry
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ScpHistoryEntry
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerEntryChange {
  open fun switch(): LedgerEntryChangeType
  open fun created(value: LedgerEntry = definedExternally): LedgerEntry
  open fun updated(value: LedgerEntry = definedExternally): LedgerEntry
  open fun removed(value: LedgerKey = definedExternally): LedgerKey
  open fun state(value: LedgerEntry = definedExternally): LedgerEntry
  open fun value(): dynamic /* LedgerEntry | LedgerKey */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun ledgerEntryCreated(value: LedgerEntry): LedgerEntryChange
    fun ledgerEntryUpdated(value: LedgerEntry): LedgerEntryChange
    fun ledgerEntryRemoved(value: LedgerKey): LedgerEntryChange
    fun ledgerEntryState(value: LedgerEntry): LedgerEntryChange
    fun read(io: Buffer): LedgerEntryChange
    fun write(value: LedgerEntryChange, io: Buffer)
    fun isValid(value: LedgerEntryChange): Boolean
    fun toXDR(value: LedgerEntryChange): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerEntryChange
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerEntryChange
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionMeta {
  open fun switch(): Number
  open fun operations(value: Array<OperationMeta> = definedExternally): Array<OperationMeta>
  open fun v1(value: TransactionMetaV1 = definedExternally): TransactionMetaV1
  open fun v2(value: TransactionMetaV2 = definedExternally): TransactionMetaV2
  open fun value(): dynamic /* Array<OperationMeta> | TransactionMetaV1 | TransactionMetaV2 */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionMeta
    fun write(value: TransactionMeta, io: Buffer)
    fun isValid(value: TransactionMeta): Boolean
    fun toXDR(value: TransactionMeta): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionMeta
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionMeta
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LedgerCloseMeta {
  open fun switch(): Number
  open fun v0(value: LedgerCloseMetaV0 = definedExternally): LedgerCloseMetaV0
  open fun v1(value: LedgerCloseMetaV1 = definedExternally): LedgerCloseMetaV1
  open fun value(): dynamic /* LedgerCloseMetaV0 | LedgerCloseMetaV1 */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): LedgerCloseMeta
    fun write(value: LedgerCloseMeta, io: Buffer)
    fun isValid(value: LedgerCloseMeta): Boolean
    fun toXDR(value: LedgerCloseMeta): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): LedgerCloseMeta
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LedgerCloseMeta
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class PeerAddressIp {
  open fun switch(): IpAddrType
  open fun ipv4(value: Buffer = definedExternally): Buffer
  open fun ipv6(value: Buffer = definedExternally): Buffer
  open fun value(): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun iPv4(value: Buffer): PeerAddressIp
    fun iPv6(value: Buffer): PeerAddressIp
    fun read(io: Buffer): PeerAddressIp
    fun write(value: PeerAddressIp, io: Buffer)
    fun isValid(value: PeerAddressIp): Boolean
    fun toXDR(value: PeerAddressIp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PeerAddressIp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PeerAddressIp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class SurveyResponseBody {
  open fun switch(): SurveyMessageCommandType
  open fun topologyResponseBody(
    value: TopologyResponseBody = definedExternally
  ): TopologyResponseBody
  open fun value(): TopologyResponseBody
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun surveyTopology(value: TopologyResponseBody): SurveyResponseBody
    fun read(io: Buffer): SurveyResponseBody
    fun write(value: SurveyResponseBody, io: Buffer)
    fun isValid(value: SurveyResponseBody): Boolean
    fun toXDR(value: SurveyResponseBody): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SurveyResponseBody
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SurveyResponseBody
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class StellarMessage {
  open fun switch(): MessageType
  open fun error(value: Error = definedExternally): Error
  open fun hello(value: Hello = definedExternally): Hello
  open fun auth(value: Auth = definedExternally): Auth
  open fun dontHave(value: DontHave = definedExternally): DontHave
  open fun peers(value: Array<PeerAddress> = definedExternally): Array<PeerAddress>
  open fun txSetHash(value: Buffer = definedExternally): Buffer
  open fun txSet(value: TransactionSet = definedExternally): TransactionSet
  open fun generalizedTxSet(
    value: GeneralizedTransactionSet = definedExternally
  ): GeneralizedTransactionSet
  open fun transaction(value: TransactionEnvelope = definedExternally): TransactionEnvelope
  open fun signedSurveyRequestMessage(
    value: SignedSurveyRequestMessage = definedExternally
  ): SignedSurveyRequestMessage
  open fun signedSurveyResponseMessage(
    value: SignedSurveyResponseMessage = definedExternally
  ): SignedSurveyResponseMessage
  open fun qSetHash(value: Buffer = definedExternally): Buffer
  open fun qSet(value: ScpQuorumSet = definedExternally): ScpQuorumSet
  open fun envelope(value: ScpEnvelope = definedExternally): ScpEnvelope
  open fun getScpLedgerSeq(value: Number = definedExternally): Number
  open fun sendMoreMessage(value: SendMore = definedExternally): SendMore
  open fun value():
    dynamic /* Error | Hello | Auth | DontHave | Array<PeerAddress> | Buffer | TransactionSet | GeneralizedTransactionSet | TransactionEnvelope | SignedSurveyRequestMessage | SignedSurveyResponseMessage | ScpQuorumSet | ScpEnvelope | Number | SendMore | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun errorMsg(value: Error): StellarMessage
    fun hello(value: Hello): StellarMessage
    fun auth(value: Auth): StellarMessage
    fun dontHave(value: DontHave): StellarMessage
    fun getPeers(): StellarMessage
    fun peers(value: Array<PeerAddress>): StellarMessage
    fun getTxSet(value: Buffer): StellarMessage
    fun txSet(value: TransactionSet): StellarMessage
    fun generalizedTxSet(value: GeneralizedTransactionSet): StellarMessage
    fun transaction(value: TransactionEnvelope): StellarMessage
    fun surveyRequest(value: SignedSurveyRequestMessage): StellarMessage
    fun surveyResponse(value: SignedSurveyResponseMessage): StellarMessage
    fun getScpQuorumset(value: Buffer): StellarMessage
    fun scpQuorumset(value: ScpQuorumSet): StellarMessage
    fun scpMessage(value: ScpEnvelope): StellarMessage
    fun getScpState(value: Number): StellarMessage
    fun sendMore(value: SendMore): StellarMessage
    fun read(io: Buffer): StellarMessage
    fun write(value: StellarMessage, io: Buffer)
    fun isValid(value: StellarMessage): Boolean
    fun toXDR(value: StellarMessage): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): StellarMessage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): StellarMessage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AuthenticatedMessage {
  open fun switch(): Number
  open fun v0(value: AuthenticatedMessageV0 = definedExternally): AuthenticatedMessageV0
  open fun value(): AuthenticatedMessageV0
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): AuthenticatedMessage
    fun write(value: AuthenticatedMessage, io: Buffer)
    fun isValid(value: AuthenticatedMessage): Boolean
    fun toXDR(value: AuthenticatedMessage): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AuthenticatedMessage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AuthenticatedMessage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LiquidityPoolParameters {
  open fun switch(): LiquidityPoolType
  open fun constantProduct(
    value: LiquidityPoolConstantProductParameters = definedExternally
  ): LiquidityPoolConstantProductParameters
  open fun value(): LiquidityPoolConstantProductParameters
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun liquidityPoolConstantProduct(
      value: LiquidityPoolConstantProductParameters
    ): LiquidityPoolParameters
    fun read(io: Buffer): LiquidityPoolParameters
    fun write(value: LiquidityPoolParameters, io: Buffer)
    fun isValid(value: LiquidityPoolParameters): Boolean
    fun toXDR(value: LiquidityPoolParameters): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolParameters
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolParameters
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class MuxedAccount {
  open fun switch(): CryptoKeyType
  open fun ed25519(value: Buffer = definedExternally): Buffer
  open fun med25519(value: MuxedAccountMed25519 = definedExternally): MuxedAccountMed25519
  open fun value(): dynamic /* Buffer | MuxedAccountMed25519 */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun keyTypeEd25519(value: Buffer): MuxedAccount
    fun keyTypeMuxedEd25519(value: MuxedAccountMed25519): MuxedAccount
    fun read(io: Buffer): MuxedAccount
    fun write(value: MuxedAccount, io: Buffer)
    fun isValid(value: MuxedAccount): Boolean
    fun toXDR(value: MuxedAccount): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): MuxedAccount
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): MuxedAccount
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ChangeTrustAsset {
  open fun switch(): AssetType
  open fun alphaNum4(value: AlphaNum4 = definedExternally): AlphaNum4
  open fun alphaNum12(value: AlphaNum12 = definedExternally): AlphaNum12
  open fun liquidityPool(
    value: LiquidityPoolParameters = definedExternally
  ): LiquidityPoolParameters
  open fun value(): dynamic /* AlphaNum4 | AlphaNum12 | LiquidityPoolParameters | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun assetTypeNative(): ChangeTrustAsset
    fun assetTypeCreditAlphanum4(value: AlphaNum4): ChangeTrustAsset
    fun assetTypeCreditAlphanum12(value: AlphaNum12): ChangeTrustAsset
    fun assetTypePoolShare(value: LiquidityPoolParameters): ChangeTrustAsset
    fun read(io: Buffer): ChangeTrustAsset
    fun write(value: ChangeTrustAsset, io: Buffer)
    fun isValid(value: ChangeTrustAsset): Boolean
    fun toXDR(value: ChangeTrustAsset): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ChangeTrustAsset
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ChangeTrustAsset
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class RevokeSponsorshipOp {
  open fun switch(): RevokeSponsorshipType
  open fun ledgerKey(value: LedgerKey = definedExternally): LedgerKey
  open fun signer(value: RevokeSponsorshipOpSigner = definedExternally): RevokeSponsorshipOpSigner
  open fun value(): dynamic /* LedgerKey | RevokeSponsorshipOpSigner */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun revokeSponsorshipLedgerEntry(value: LedgerKey): RevokeSponsorshipOp
    fun revokeSponsorshipSigner(value: RevokeSponsorshipOpSigner): RevokeSponsorshipOp
    fun read(io: Buffer): RevokeSponsorshipOp
    fun write(value: RevokeSponsorshipOp, io: Buffer)
    fun isValid(value: RevokeSponsorshipOp): Boolean
    fun toXDR(value: RevokeSponsorshipOp): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): RevokeSponsorshipOp
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): RevokeSponsorshipOp
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class OperationBody {
  open fun switch(): OperationType
  open fun createAccountOp(value: CreateAccountOp = definedExternally): CreateAccountOp
  open fun paymentOp(value: PaymentOp = definedExternally): PaymentOp
  open fun pathPaymentStrictReceiveOp(
    value: PathPaymentStrictReceiveOp = definedExternally
  ): PathPaymentStrictReceiveOp
  open fun manageSellOfferOp(value: ManageSellOfferOp = definedExternally): ManageSellOfferOp
  open fun createPassiveSellOfferOp(
    value: CreatePassiveSellOfferOp = definedExternally
  ): CreatePassiveSellOfferOp
  open fun setOptionsOp(value: SetOptionsOp = definedExternally): SetOptionsOp
  open fun changeTrustOp(value: ChangeTrustOp = definedExternally): ChangeTrustOp
  open fun allowTrustOp(value: AllowTrustOp = definedExternally): AllowTrustOp
  open fun destination(value: MuxedAccount = definedExternally): MuxedAccount
  open fun manageDataOp(value: ManageDataOp = definedExternally): ManageDataOp
  open fun bumpSequenceOp(value: BumpSequenceOp = definedExternally): BumpSequenceOp
  open fun manageBuyOfferOp(value: ManageBuyOfferOp = definedExternally): ManageBuyOfferOp
  open fun pathPaymentStrictSendOp(
    value: PathPaymentStrictSendOp = definedExternally
  ): PathPaymentStrictSendOp
  open fun createClaimableBalanceOp(
    value: CreateClaimableBalanceOp = definedExternally
  ): CreateClaimableBalanceOp
  open fun claimClaimableBalanceOp(
    value: ClaimClaimableBalanceOp = definedExternally
  ): ClaimClaimableBalanceOp
  open fun beginSponsoringFutureReservesOp(
    value: BeginSponsoringFutureReservesOp = definedExternally
  ): BeginSponsoringFutureReservesOp
  open fun revokeSponsorshipOp(value: RevokeSponsorshipOp = definedExternally): RevokeSponsorshipOp
  open fun clawbackOp(value: ClawbackOp = definedExternally): ClawbackOp
  open fun clawbackClaimableBalanceOp(
    value: ClawbackClaimableBalanceOp = definedExternally
  ): ClawbackClaimableBalanceOp
  open fun setTrustLineFlagsOp(value: SetTrustLineFlagsOp = definedExternally): SetTrustLineFlagsOp
  open fun liquidityPoolDepositOp(
    value: LiquidityPoolDepositOp = definedExternally
  ): LiquidityPoolDepositOp
  open fun liquidityPoolWithdrawOp(
    value: LiquidityPoolWithdrawOp = definedExternally
  ): LiquidityPoolWithdrawOp
  open fun value():
    dynamic /* CreateAccountOp | PaymentOp | PathPaymentStrictReceiveOp | ManageSellOfferOp | CreatePassiveSellOfferOp | SetOptionsOp | ChangeTrustOp | AllowTrustOp | MuxedAccount | ManageDataOp | BumpSequenceOp | ManageBuyOfferOp | PathPaymentStrictSendOp | CreateClaimableBalanceOp | ClaimClaimableBalanceOp | BeginSponsoringFutureReservesOp | RevokeSponsorshipOp | ClawbackOp | ClawbackClaimableBalanceOp | SetTrustLineFlagsOp | LiquidityPoolDepositOp | LiquidityPoolWithdrawOp | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun createAccount(value: CreateAccountOp): OperationBody
    fun payment(value: PaymentOp): OperationBody
    fun pathPaymentStrictReceive(value: PathPaymentStrictReceiveOp): OperationBody
    fun manageSellOffer(value: ManageSellOfferOp): OperationBody
    fun createPassiveSellOffer(value: CreatePassiveSellOfferOp): OperationBody
    fun setOptions(value: SetOptionsOp): OperationBody
    fun changeTrust(value: ChangeTrustOp): OperationBody
    fun allowTrust(value: AllowTrustOp): OperationBody
    fun accountMerge(value: MuxedAccount): OperationBody
    fun inflation(): OperationBody
    fun manageData(value: ManageDataOp): OperationBody
    fun bumpSequence(value: BumpSequenceOp): OperationBody
    fun manageBuyOffer(value: ManageBuyOfferOp): OperationBody
    fun pathPaymentStrictSend(value: PathPaymentStrictSendOp): OperationBody
    fun createClaimableBalance(value: CreateClaimableBalanceOp): OperationBody
    fun claimClaimableBalance(value: ClaimClaimableBalanceOp): OperationBody
    fun beginSponsoringFutureReserves(value: BeginSponsoringFutureReservesOp): OperationBody
    fun endSponsoringFutureReserves(): OperationBody
    fun revokeSponsorship(value: RevokeSponsorshipOp): OperationBody
    fun clawback(value: ClawbackOp): OperationBody
    fun clawbackClaimableBalance(value: ClawbackClaimableBalanceOp): OperationBody
    fun setTrustLineFlags(value: SetTrustLineFlagsOp): OperationBody
    fun liquidityPoolDeposit(value: LiquidityPoolDepositOp): OperationBody
    fun liquidityPoolWithdraw(value: LiquidityPoolWithdrawOp): OperationBody
    fun read(io: Buffer): OperationBody
    fun write(value: OperationBody, io: Buffer)
    fun isValid(value: OperationBody): Boolean
    fun toXDR(value: OperationBody): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): OperationBody
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): OperationBody
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class HashIdPreimage {
  open fun switch(): EnvelopeType
  open fun operationId(
    value: HashIdPreimageOperationId = definedExternally
  ): HashIdPreimageOperationId
  open fun revokeId(value: HashIdPreimageRevokeId = definedExternally): HashIdPreimageRevokeId
  open fun value(): dynamic /* HashIdPreimageOperationId | HashIdPreimageRevokeId */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun envelopeTypeOpId(value: HashIdPreimageOperationId): HashIdPreimage
    fun envelopeTypePoolRevokeOpId(value: HashIdPreimageRevokeId): HashIdPreimage
    fun read(io: Buffer): HashIdPreimage
    fun write(value: HashIdPreimage, io: Buffer)
    fun isValid(value: HashIdPreimage): Boolean
    fun toXDR(value: HashIdPreimage): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): HashIdPreimage
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): HashIdPreimage
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class Memo {
  open fun switch(): MemoType
  open fun text(value: String = definedExternally): dynamic /* String | Buffer */
  open fun text(): dynamic /* String | Buffer */
  open fun text(value: Buffer = definedExternally): dynamic /* String | Buffer */
  open fun id(value: Uint64 = definedExternally): Uint64
  open fun hash(value: Buffer = definedExternally): Buffer
  open fun retHash(value: Buffer = definedExternally): Buffer
  open fun value(): dynamic /* String | Buffer | Uint64 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun memoNone(): Memo
    fun memoText(value: String): Memo
    fun memoText(value: Buffer): Memo
    fun memoId(value: Uint64): Memo
    fun memoHash(value: Buffer): Memo
    fun memoReturn(value: Buffer): Memo
    fun read(io: Buffer): Memo
    fun write(value: Memo, io: Buffer)
    fun isValid(value: Memo): Boolean
    fun toXDR(value: Memo): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Memo
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Memo
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class Preconditions {
  open fun switch(): PreconditionType
  open fun timeBounds(value: TimeBounds = definedExternally): TimeBounds
  open fun v2(value: PreconditionsV2 = definedExternally): PreconditionsV2
  open fun value(): dynamic /* TimeBounds | PreconditionsV2 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun precondNone(): Preconditions
    fun precondTime(value: TimeBounds): Preconditions
    fun precondV2(value: PreconditionsV2): Preconditions
    fun read(io: Buffer): Preconditions
    fun write(value: Preconditions, io: Buffer)
    fun isValid(value: Preconditions): Boolean
    fun toXDR(value: Preconditions): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Preconditions
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): Preconditions
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionV0Ext {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionV0Ext
    fun write(value: TransactionV0Ext, io: Buffer)
    fun isValid(value: TransactionV0Ext): Boolean
    fun toXDR(value: TransactionV0Ext): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionV0Ext
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionV0Ext
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionExt
    fun write(value: TransactionExt, io: Buffer)
    fun isValid(value: TransactionExt): Boolean
    fun toXDR(value: TransactionExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class FeeBumpTransactionInnerTx {
  open fun switch(): EnvelopeType
  open fun v1(value: TransactionV1Envelope = definedExternally): TransactionV1Envelope
  open fun value(): TransactionV1Envelope
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun envelopeTypeTx(value: TransactionV1Envelope): FeeBumpTransactionInnerTx
    fun read(io: Buffer): FeeBumpTransactionInnerTx
    fun write(value: FeeBumpTransactionInnerTx, io: Buffer)
    fun isValid(value: FeeBumpTransactionInnerTx): Boolean
    fun toXDR(value: FeeBumpTransactionInnerTx): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): FeeBumpTransactionInnerTx
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): FeeBumpTransactionInnerTx
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class FeeBumpTransactionExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): FeeBumpTransactionExt
    fun write(value: FeeBumpTransactionExt, io: Buffer)
    fun isValid(value: FeeBumpTransactionExt): Boolean
    fun toXDR(value: FeeBumpTransactionExt): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): FeeBumpTransactionExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): FeeBumpTransactionExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionEnvelope {
  open fun switch(): EnvelopeType
  open fun v0(value: TransactionV0Envelope = definedExternally): TransactionV0Envelope
  open fun v1(value: TransactionV1Envelope = definedExternally): TransactionV1Envelope
  open fun feeBump(
    value: FeeBumpTransactionEnvelope = definedExternally
  ): FeeBumpTransactionEnvelope
  open fun value():
    dynamic /* TransactionV0Envelope | TransactionV1Envelope | FeeBumpTransactionEnvelope */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun envelopeTypeTxV0(value: TransactionV0Envelope): TransactionEnvelope
    fun envelopeTypeTx(value: TransactionV1Envelope): TransactionEnvelope
    fun envelopeTypeTxFeeBump(value: FeeBumpTransactionEnvelope): TransactionEnvelope
    fun read(io: Buffer): TransactionEnvelope
    fun write(value: TransactionEnvelope, io: Buffer)
    fun isValid(value: TransactionEnvelope): Boolean
    fun toXDR(value: TransactionEnvelope): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionEnvelope
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionEnvelope
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionSignaturePayloadTaggedTransaction {
  open fun switch(): EnvelopeType
  open fun tx(value: Transaction = definedExternally): Transaction
  open fun feeBump(value: FeeBumpTransaction = definedExternally): FeeBumpTransaction
  open fun value(): dynamic /* Transaction | FeeBumpTransaction */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun envelopeTypeTx(value: Transaction): TransactionSignaturePayloadTaggedTransaction
    fun envelopeTypeTxFeeBump(
      value: FeeBumpTransaction
    ): TransactionSignaturePayloadTaggedTransaction
    fun read(io: Buffer): TransactionSignaturePayloadTaggedTransaction
    fun write(value: TransactionSignaturePayloadTaggedTransaction, io: Buffer)
    fun isValid(value: TransactionSignaturePayloadTaggedTransaction): Boolean
    fun toXDR(value: TransactionSignaturePayloadTaggedTransaction): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionSignaturePayloadTaggedTransaction
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): TransactionSignaturePayloadTaggedTransaction
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimAtom {
  open fun switch(): ClaimAtomType
  open fun v0(value: ClaimOfferAtomV0 = definedExternally): ClaimOfferAtomV0
  open fun orderBook(value: ClaimOfferAtom = definedExternally): ClaimOfferAtom
  open fun liquidityPool(value: ClaimLiquidityAtom = definedExternally): ClaimLiquidityAtom
  open fun value(): dynamic /* ClaimOfferAtomV0 | ClaimOfferAtom | ClaimLiquidityAtom */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun claimAtomTypeV0(value: ClaimOfferAtomV0): ClaimAtom
    fun claimAtomTypeOrderBook(value: ClaimOfferAtom): ClaimAtom
    fun claimAtomTypeLiquidityPool(value: ClaimLiquidityAtom): ClaimAtom
    fun read(io: Buffer): ClaimAtom
    fun write(value: ClaimAtom, io: Buffer)
    fun isValid(value: ClaimAtom): Boolean
    fun toXDR(value: ClaimAtom): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClaimAtom
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimAtom
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class CreateAccountResult {
  open fun switch(): CreateAccountResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun createAccountSuccess(): CreateAccountResult
    fun createAccountMalformed(): CreateAccountResult
    fun createAccountUnderfunded(): CreateAccountResult
    fun createAccountLowReserve(): CreateAccountResult
    fun createAccountAlreadyExist(): CreateAccountResult
    fun read(io: Buffer): CreateAccountResult
    fun write(value: CreateAccountResult, io: Buffer)
    fun isValid(value: CreateAccountResult): Boolean
    fun toXDR(value: CreateAccountResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): CreateAccountResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): CreateAccountResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class PaymentResult {
  open fun switch(): PaymentResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun paymentSuccess(): PaymentResult
    fun paymentMalformed(): PaymentResult
    fun paymentUnderfunded(): PaymentResult
    fun paymentSrcNoTrust(): PaymentResult
    fun paymentSrcNotAuthorized(): PaymentResult
    fun paymentNoDestination(): PaymentResult
    fun paymentNoTrust(): PaymentResult
    fun paymentNotAuthorized(): PaymentResult
    fun paymentLineFull(): PaymentResult
    fun paymentNoIssuer(): PaymentResult
    fun read(io: Buffer): PaymentResult
    fun write(value: PaymentResult, io: Buffer)
    fun isValid(value: PaymentResult): Boolean
    fun toXDR(value: PaymentResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PaymentResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PaymentResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class PathPaymentStrictReceiveResult {
  open fun switch(): PathPaymentStrictReceiveResultCode
  open fun success(
    value: PathPaymentStrictReceiveResultSuccess = definedExternally
  ): PathPaymentStrictReceiveResultSuccess
  open fun noIssuer(value: Asset = definedExternally): Asset
  open fun value(): dynamic /* PathPaymentStrictReceiveResultSuccess | Asset | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun pathPaymentStrictReceiveSuccess(
      value: PathPaymentStrictReceiveResultSuccess
    ): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveMalformed(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveUnderfunded(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveSrcNoTrust(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveSrcNotAuthorized(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveNoDestination(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveNoTrust(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveNotAuthorized(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveLineFull(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveNoIssuer(value: Asset): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveTooFewOffers(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveOfferCrossSelf(): PathPaymentStrictReceiveResult
    fun pathPaymentStrictReceiveOverSendmax(): PathPaymentStrictReceiveResult
    fun read(io: Buffer): PathPaymentStrictReceiveResult
    fun write(value: PathPaymentStrictReceiveResult, io: Buffer)
    fun isValid(value: PathPaymentStrictReceiveResult): Boolean
    fun toXDR(value: PathPaymentStrictReceiveResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): PathPaymentStrictReceiveResult
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): PathPaymentStrictReceiveResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class PathPaymentStrictSendResult {
  open fun switch(): PathPaymentStrictSendResultCode
  open fun success(
    value: PathPaymentStrictSendResultSuccess = definedExternally
  ): PathPaymentStrictSendResultSuccess
  open fun noIssuer(value: Asset = definedExternally): Asset
  open fun value(): dynamic /* PathPaymentStrictSendResultSuccess | Asset | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun pathPaymentStrictSendSuccess(
      value: PathPaymentStrictSendResultSuccess
    ): PathPaymentStrictSendResult
    fun pathPaymentStrictSendMalformed(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendUnderfunded(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendSrcNoTrust(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendSrcNotAuthorized(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendNoDestination(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendNoTrust(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendNotAuthorized(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendLineFull(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendNoIssuer(value: Asset): PathPaymentStrictSendResult
    fun pathPaymentStrictSendTooFewOffers(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendOfferCrossSelf(): PathPaymentStrictSendResult
    fun pathPaymentStrictSendUnderDestmin(): PathPaymentStrictSendResult
    fun read(io: Buffer): PathPaymentStrictSendResult
    fun write(value: PathPaymentStrictSendResult, io: Buffer)
    fun isValid(value: PathPaymentStrictSendResult): Boolean
    fun toXDR(value: PathPaymentStrictSendResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): PathPaymentStrictSendResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PathPaymentStrictSendResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ManageOfferSuccessResultOffer {
  open fun switch(): ManageOfferEffect
  open fun offer(value: OfferEntry = definedExternally): OfferEntry
  open fun value(): dynamic /* OfferEntry | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun manageOfferCreated(value: OfferEntry): ManageOfferSuccessResultOffer
    fun manageOfferUpdated(value: OfferEntry): ManageOfferSuccessResultOffer
    fun manageOfferDeleted(): ManageOfferSuccessResultOffer
    fun read(io: Buffer): ManageOfferSuccessResultOffer
    fun write(value: ManageOfferSuccessResultOffer, io: Buffer)
    fun isValid(value: ManageOfferSuccessResultOffer): Boolean
    fun toXDR(value: ManageOfferSuccessResultOffer): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ManageOfferSuccessResultOffer
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageOfferSuccessResultOffer
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ManageSellOfferResult {
  open fun switch(): ManageSellOfferResultCode
  open fun success(value: ManageOfferSuccessResult = definedExternally): ManageOfferSuccessResult
  open fun value(): dynamic /* ManageOfferSuccessResult | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun manageSellOfferSuccess(value: ManageOfferSuccessResult): ManageSellOfferResult
    fun manageSellOfferMalformed(): ManageSellOfferResult
    fun manageSellOfferSellNoTrust(): ManageSellOfferResult
    fun manageSellOfferBuyNoTrust(): ManageSellOfferResult
    fun manageSellOfferSellNotAuthorized(): ManageSellOfferResult
    fun manageSellOfferBuyNotAuthorized(): ManageSellOfferResult
    fun manageSellOfferLineFull(): ManageSellOfferResult
    fun manageSellOfferUnderfunded(): ManageSellOfferResult
    fun manageSellOfferCrossSelf(): ManageSellOfferResult
    fun manageSellOfferSellNoIssuer(): ManageSellOfferResult
    fun manageSellOfferBuyNoIssuer(): ManageSellOfferResult
    fun manageSellOfferNotFound(): ManageSellOfferResult
    fun manageSellOfferLowReserve(): ManageSellOfferResult
    fun read(io: Buffer): ManageSellOfferResult
    fun write(value: ManageSellOfferResult, io: Buffer)
    fun isValid(value: ManageSellOfferResult): Boolean
    fun toXDR(value: ManageSellOfferResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ManageSellOfferResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageSellOfferResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ManageBuyOfferResult {
  open fun switch(): ManageBuyOfferResultCode
  open fun success(value: ManageOfferSuccessResult = definedExternally): ManageOfferSuccessResult
  open fun value(): dynamic /* ManageOfferSuccessResult | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun manageBuyOfferSuccess(value: ManageOfferSuccessResult): ManageBuyOfferResult
    fun manageBuyOfferMalformed(): ManageBuyOfferResult
    fun manageBuyOfferSellNoTrust(): ManageBuyOfferResult
    fun manageBuyOfferBuyNoTrust(): ManageBuyOfferResult
    fun manageBuyOfferSellNotAuthorized(): ManageBuyOfferResult
    fun manageBuyOfferBuyNotAuthorized(): ManageBuyOfferResult
    fun manageBuyOfferLineFull(): ManageBuyOfferResult
    fun manageBuyOfferUnderfunded(): ManageBuyOfferResult
    fun manageBuyOfferCrossSelf(): ManageBuyOfferResult
    fun manageBuyOfferSellNoIssuer(): ManageBuyOfferResult
    fun manageBuyOfferBuyNoIssuer(): ManageBuyOfferResult
    fun manageBuyOfferNotFound(): ManageBuyOfferResult
    fun manageBuyOfferLowReserve(): ManageBuyOfferResult
    fun read(io: Buffer): ManageBuyOfferResult
    fun write(value: ManageBuyOfferResult, io: Buffer)
    fun isValid(value: ManageBuyOfferResult): Boolean
    fun toXDR(value: ManageBuyOfferResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ManageBuyOfferResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageBuyOfferResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class SetOptionsResult {
  open fun switch(): SetOptionsResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun setOptionsSuccess(): SetOptionsResult
    fun setOptionsLowReserve(): SetOptionsResult
    fun setOptionsTooManySigners(): SetOptionsResult
    fun setOptionsBadFlags(): SetOptionsResult
    fun setOptionsInvalidInflation(): SetOptionsResult
    fun setOptionsCantChange(): SetOptionsResult
    fun setOptionsUnknownFlag(): SetOptionsResult
    fun setOptionsThresholdOutOfRange(): SetOptionsResult
    fun setOptionsBadSigner(): SetOptionsResult
    fun setOptionsInvalidHomeDomain(): SetOptionsResult
    fun setOptionsAuthRevocableRequired(): SetOptionsResult
    fun read(io: Buffer): SetOptionsResult
    fun write(value: SetOptionsResult, io: Buffer)
    fun isValid(value: SetOptionsResult): Boolean
    fun toXDR(value: SetOptionsResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SetOptionsResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SetOptionsResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ChangeTrustResult {
  open fun switch(): ChangeTrustResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun changeTrustSuccess(): ChangeTrustResult
    fun changeTrustMalformed(): ChangeTrustResult
    fun changeTrustNoIssuer(): ChangeTrustResult
    fun changeTrustInvalidLimit(): ChangeTrustResult
    fun changeTrustLowReserve(): ChangeTrustResult
    fun changeTrustSelfNotAllowed(): ChangeTrustResult
    fun changeTrustTrustLineMissing(): ChangeTrustResult
    fun changeTrustCannotDelete(): ChangeTrustResult
    fun changeTrustNotAuthMaintainLiabilities(): ChangeTrustResult
    fun read(io: Buffer): ChangeTrustResult
    fun write(value: ChangeTrustResult, io: Buffer)
    fun isValid(value: ChangeTrustResult): Boolean
    fun toXDR(value: ChangeTrustResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ChangeTrustResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ChangeTrustResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AllowTrustResult {
  open fun switch(): AllowTrustResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun allowTrustSuccess(): AllowTrustResult
    fun allowTrustMalformed(): AllowTrustResult
    fun allowTrustNoTrustLine(): AllowTrustResult
    fun allowTrustTrustNotRequired(): AllowTrustResult
    fun allowTrustCantRevoke(): AllowTrustResult
    fun allowTrustSelfNotAllowed(): AllowTrustResult
    fun allowTrustLowReserve(): AllowTrustResult
    fun read(io: Buffer): AllowTrustResult
    fun write(value: AllowTrustResult, io: Buffer)
    fun isValid(value: AllowTrustResult): Boolean
    fun toXDR(value: AllowTrustResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AllowTrustResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AllowTrustResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class AccountMergeResult {
  open fun switch(): AccountMergeResultCode
  open fun sourceAccountBalance(value: Int64 = definedExternally): Int64
  open fun value(): dynamic /* Int64 | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun accountMergeSuccess(value: Int64): AccountMergeResult
    fun accountMergeMalformed(): AccountMergeResult
    fun accountMergeNoAccount(): AccountMergeResult
    fun accountMergeImmutableSet(): AccountMergeResult
    fun accountMergeHasSubEntries(): AccountMergeResult
    fun accountMergeSeqnumTooFar(): AccountMergeResult
    fun accountMergeDestFull(): AccountMergeResult
    fun accountMergeIsSponsor(): AccountMergeResult
    fun read(io: Buffer): AccountMergeResult
    fun write(value: AccountMergeResult, io: Buffer)
    fun isValid(value: AccountMergeResult): Boolean
    fun toXDR(value: AccountMergeResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): AccountMergeResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): AccountMergeResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class InflationResult {
  open fun switch(): InflationResultCode
  open fun payouts(value: Array<InflationPayout> = definedExternally): Array<InflationPayout>
  open fun value(): dynamic /* Array<InflationPayout> | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun inflationSuccess(value: Array<InflationPayout>): InflationResult
    fun inflationNotTime(): InflationResult
    fun read(io: Buffer): InflationResult
    fun write(value: InflationResult, io: Buffer)
    fun isValid(value: InflationResult): Boolean
    fun toXDR(value: InflationResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): InflationResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): InflationResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ManageDataResult {
  open fun switch(): ManageDataResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun manageDataSuccess(): ManageDataResult
    fun manageDataNotSupportedYet(): ManageDataResult
    fun manageDataNameNotFound(): ManageDataResult
    fun manageDataLowReserve(): ManageDataResult
    fun manageDataInvalidName(): ManageDataResult
    fun read(io: Buffer): ManageDataResult
    fun write(value: ManageDataResult, io: Buffer)
    fun isValid(value: ManageDataResult): Boolean
    fun toXDR(value: ManageDataResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ManageDataResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ManageDataResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class BumpSequenceResult {
  open fun switch(): BumpSequenceResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun bumpSequenceSuccess(): BumpSequenceResult
    fun bumpSequenceBadSeq(): BumpSequenceResult
    fun read(io: Buffer): BumpSequenceResult
    fun write(value: BumpSequenceResult, io: Buffer)
    fun isValid(value: BumpSequenceResult): Boolean
    fun toXDR(value: BumpSequenceResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): BumpSequenceResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): BumpSequenceResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class CreateClaimableBalanceResult {
  open fun switch(): CreateClaimableBalanceResultCode
  open fun balanceId(value: ClaimableBalanceId = definedExternally): ClaimableBalanceId
  open fun value(): dynamic /* ClaimableBalanceId | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun createClaimableBalanceSuccess(value: ClaimableBalanceId): CreateClaimableBalanceResult
    fun createClaimableBalanceMalformed(): CreateClaimableBalanceResult
    fun createClaimableBalanceLowReserve(): CreateClaimableBalanceResult
    fun createClaimableBalanceNoTrust(): CreateClaimableBalanceResult
    fun createClaimableBalanceNotAuthorized(): CreateClaimableBalanceResult
    fun createClaimableBalanceUnderfunded(): CreateClaimableBalanceResult
    fun read(io: Buffer): CreateClaimableBalanceResult
    fun write(value: CreateClaimableBalanceResult, io: Buffer)
    fun isValid(value: CreateClaimableBalanceResult): Boolean
    fun toXDR(value: CreateClaimableBalanceResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): CreateClaimableBalanceResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): CreateClaimableBalanceResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClaimClaimableBalanceResult {
  open fun switch(): ClaimClaimableBalanceResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun claimClaimableBalanceSuccess(): ClaimClaimableBalanceResult
    fun claimClaimableBalanceDoesNotExist(): ClaimClaimableBalanceResult
    fun claimClaimableBalanceCannotClaim(): ClaimClaimableBalanceResult
    fun claimClaimableBalanceLineFull(): ClaimClaimableBalanceResult
    fun claimClaimableBalanceNoTrust(): ClaimClaimableBalanceResult
    fun claimClaimableBalanceNotAuthorized(): ClaimClaimableBalanceResult
    fun read(io: Buffer): ClaimClaimableBalanceResult
    fun write(value: ClaimClaimableBalanceResult, io: Buffer)
    fun isValid(value: ClaimClaimableBalanceResult): Boolean
    fun toXDR(value: ClaimClaimableBalanceResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClaimClaimableBalanceResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClaimClaimableBalanceResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class BeginSponsoringFutureReservesResult {
  open fun switch(): BeginSponsoringFutureReservesResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun beginSponsoringFutureReservesSuccess(): BeginSponsoringFutureReservesResult
    fun beginSponsoringFutureReservesMalformed(): BeginSponsoringFutureReservesResult
    fun beginSponsoringFutureReservesAlreadySponsored(): BeginSponsoringFutureReservesResult
    fun beginSponsoringFutureReservesRecursive(): BeginSponsoringFutureReservesResult
    fun read(io: Buffer): BeginSponsoringFutureReservesResult
    fun write(value: BeginSponsoringFutureReservesResult, io: Buffer)
    fun isValid(value: BeginSponsoringFutureReservesResult): Boolean
    fun toXDR(value: BeginSponsoringFutureReservesResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): BeginSponsoringFutureReservesResult
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): BeginSponsoringFutureReservesResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class EndSponsoringFutureReservesResult {
  open fun switch(): EndSponsoringFutureReservesResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun endSponsoringFutureReservesSuccess(): EndSponsoringFutureReservesResult
    fun endSponsoringFutureReservesNotSponsored(): EndSponsoringFutureReservesResult
    fun read(io: Buffer): EndSponsoringFutureReservesResult
    fun write(value: EndSponsoringFutureReservesResult, io: Buffer)
    fun isValid(value: EndSponsoringFutureReservesResult): Boolean
    fun toXDR(value: EndSponsoringFutureReservesResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): EndSponsoringFutureReservesResult
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): EndSponsoringFutureReservesResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class RevokeSponsorshipResult {
  open fun switch(): RevokeSponsorshipResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun revokeSponsorshipSuccess(): RevokeSponsorshipResult
    fun revokeSponsorshipDoesNotExist(): RevokeSponsorshipResult
    fun revokeSponsorshipNotSponsor(): RevokeSponsorshipResult
    fun revokeSponsorshipLowReserve(): RevokeSponsorshipResult
    fun revokeSponsorshipOnlyTransferable(): RevokeSponsorshipResult
    fun revokeSponsorshipMalformed(): RevokeSponsorshipResult
    fun read(io: Buffer): RevokeSponsorshipResult
    fun write(value: RevokeSponsorshipResult, io: Buffer)
    fun isValid(value: RevokeSponsorshipResult): Boolean
    fun toXDR(value: RevokeSponsorshipResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): RevokeSponsorshipResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): RevokeSponsorshipResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClawbackResult {
  open fun switch(): ClawbackResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun clawbackSuccess(): ClawbackResult
    fun clawbackMalformed(): ClawbackResult
    fun clawbackNotClawbackEnabled(): ClawbackResult
    fun clawbackNoTrust(): ClawbackResult
    fun clawbackUnderfunded(): ClawbackResult
    fun read(io: Buffer): ClawbackResult
    fun write(value: ClawbackResult, io: Buffer)
    fun isValid(value: ClawbackResult): Boolean
    fun toXDR(value: ClawbackResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ClawbackResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ClawbackResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ClawbackClaimableBalanceResult {
  open fun switch(): ClawbackClaimableBalanceResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun clawbackClaimableBalanceSuccess(): ClawbackClaimableBalanceResult
    fun clawbackClaimableBalanceDoesNotExist(): ClawbackClaimableBalanceResult
    fun clawbackClaimableBalanceNotIssuer(): ClawbackClaimableBalanceResult
    fun clawbackClaimableBalanceNotClawbackEnabled(): ClawbackClaimableBalanceResult
    fun read(io: Buffer): ClawbackClaimableBalanceResult
    fun write(value: ClawbackClaimableBalanceResult, io: Buffer)
    fun isValid(value: ClawbackClaimableBalanceResult): Boolean
    fun toXDR(value: ClawbackClaimableBalanceResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): ClawbackClaimableBalanceResult
    fun fromXDR(
      input: String,
      format: String /* "hex" | "base64" */
    ): ClawbackClaimableBalanceResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class SetTrustLineFlagsResult {
  open fun switch(): SetTrustLineFlagsResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun setTrustLineFlagsSuccess(): SetTrustLineFlagsResult
    fun setTrustLineFlagsMalformed(): SetTrustLineFlagsResult
    fun setTrustLineFlagsNoTrustLine(): SetTrustLineFlagsResult
    fun setTrustLineFlagsCantRevoke(): SetTrustLineFlagsResult
    fun setTrustLineFlagsInvalidState(): SetTrustLineFlagsResult
    fun setTrustLineFlagsLowReserve(): SetTrustLineFlagsResult
    fun read(io: Buffer): SetTrustLineFlagsResult
    fun write(value: SetTrustLineFlagsResult, io: Buffer)
    fun isValid(value: SetTrustLineFlagsResult): Boolean
    fun toXDR(value: SetTrustLineFlagsResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): SetTrustLineFlagsResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SetTrustLineFlagsResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LiquidityPoolDepositResult {
  open fun switch(): LiquidityPoolDepositResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun liquidityPoolDepositSuccess(): LiquidityPoolDepositResult
    fun liquidityPoolDepositMalformed(): LiquidityPoolDepositResult
    fun liquidityPoolDepositNoTrust(): LiquidityPoolDepositResult
    fun liquidityPoolDepositNotAuthorized(): LiquidityPoolDepositResult
    fun liquidityPoolDepositUnderfunded(): LiquidityPoolDepositResult
    fun liquidityPoolDepositLineFull(): LiquidityPoolDepositResult
    fun liquidityPoolDepositBadPrice(): LiquidityPoolDepositResult
    fun liquidityPoolDepositPoolFull(): LiquidityPoolDepositResult
    fun read(io: Buffer): LiquidityPoolDepositResult
    fun write(value: LiquidityPoolDepositResult, io: Buffer)
    fun isValid(value: LiquidityPoolDepositResult): Boolean
    fun toXDR(value: LiquidityPoolDepositResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolDepositResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolDepositResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class LiquidityPoolWithdrawResult {
  open fun switch(): LiquidityPoolWithdrawResultCode
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun liquidityPoolWithdrawSuccess(): LiquidityPoolWithdrawResult
    fun liquidityPoolWithdrawMalformed(): LiquidityPoolWithdrawResult
    fun liquidityPoolWithdrawNoTrust(): LiquidityPoolWithdrawResult
    fun liquidityPoolWithdrawUnderfunded(): LiquidityPoolWithdrawResult
    fun liquidityPoolWithdrawLineFull(): LiquidityPoolWithdrawResult
    fun liquidityPoolWithdrawUnderMinimum(): LiquidityPoolWithdrawResult
    fun read(io: Buffer): LiquidityPoolWithdrawResult
    fun write(value: LiquidityPoolWithdrawResult, io: Buffer)
    fun isValid(value: LiquidityPoolWithdrawResult): Boolean
    fun toXDR(value: LiquidityPoolWithdrawResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): LiquidityPoolWithdrawResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): LiquidityPoolWithdrawResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class OperationResultTr {
  open fun switch(): OperationType
  open fun createAccountResult(value: CreateAccountResult = definedExternally): CreateAccountResult
  open fun paymentResult(value: PaymentResult = definedExternally): PaymentResult
  open fun pathPaymentStrictReceiveResult(
    value: PathPaymentStrictReceiveResult = definedExternally
  ): PathPaymentStrictReceiveResult
  open fun manageSellOfferResult(
    value: ManageSellOfferResult = definedExternally
  ): ManageSellOfferResult
  open fun createPassiveSellOfferResult(
    value: ManageSellOfferResult = definedExternally
  ): ManageSellOfferResult
  open fun setOptionsResult(value: SetOptionsResult = definedExternally): SetOptionsResult
  open fun changeTrustResult(value: ChangeTrustResult = definedExternally): ChangeTrustResult
  open fun allowTrustResult(value: AllowTrustResult = definedExternally): AllowTrustResult
  open fun accountMergeResult(value: AccountMergeResult = definedExternally): AccountMergeResult
  open fun inflationResult(value: InflationResult = definedExternally): InflationResult
  open fun manageDataResult(value: ManageDataResult = definedExternally): ManageDataResult
  open fun bumpSeqResult(value: BumpSequenceResult = definedExternally): BumpSequenceResult
  open fun manageBuyOfferResult(
    value: ManageBuyOfferResult = definedExternally
  ): ManageBuyOfferResult
  open fun pathPaymentStrictSendResult(
    value: PathPaymentStrictSendResult = definedExternally
  ): PathPaymentStrictSendResult
  open fun createClaimableBalanceResult(
    value: CreateClaimableBalanceResult = definedExternally
  ): CreateClaimableBalanceResult
  open fun claimClaimableBalanceResult(
    value: ClaimClaimableBalanceResult = definedExternally
  ): ClaimClaimableBalanceResult
  open fun beginSponsoringFutureReservesResult(
    value: BeginSponsoringFutureReservesResult = definedExternally
  ): BeginSponsoringFutureReservesResult
  open fun endSponsoringFutureReservesResult(
    value: EndSponsoringFutureReservesResult = definedExternally
  ): EndSponsoringFutureReservesResult
  open fun revokeSponsorshipResult(
    value: RevokeSponsorshipResult = definedExternally
  ): RevokeSponsorshipResult
  open fun clawbackResult(value: ClawbackResult = definedExternally): ClawbackResult
  open fun clawbackClaimableBalanceResult(
    value: ClawbackClaimableBalanceResult = definedExternally
  ): ClawbackClaimableBalanceResult
  open fun setTrustLineFlagsResult(
    value: SetTrustLineFlagsResult = definedExternally
  ): SetTrustLineFlagsResult
  open fun liquidityPoolDepositResult(
    value: LiquidityPoolDepositResult = definedExternally
  ): LiquidityPoolDepositResult
  open fun liquidityPoolWithdrawResult(
    value: LiquidityPoolWithdrawResult = definedExternally
  ): LiquidityPoolWithdrawResult
  open fun value():
    dynamic /* CreateAccountResult | PaymentResult | PathPaymentStrictReceiveResult | ManageSellOfferResult | SetOptionsResult | ChangeTrustResult | AllowTrustResult | AccountMergeResult | InflationResult | ManageDataResult | BumpSequenceResult | ManageBuyOfferResult | PathPaymentStrictSendResult | CreateClaimableBalanceResult | ClaimClaimableBalanceResult | BeginSponsoringFutureReservesResult | EndSponsoringFutureReservesResult | RevokeSponsorshipResult | ClawbackResult | ClawbackClaimableBalanceResult | SetTrustLineFlagsResult | LiquidityPoolDepositResult | LiquidityPoolWithdrawResult */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun createAccount(value: CreateAccountResult): OperationResultTr
    fun payment(value: PaymentResult): OperationResultTr
    fun pathPaymentStrictReceive(value: PathPaymentStrictReceiveResult): OperationResultTr
    fun manageSellOffer(value: ManageSellOfferResult): OperationResultTr
    fun createPassiveSellOffer(value: ManageSellOfferResult): OperationResultTr
    fun setOptions(value: SetOptionsResult): OperationResultTr
    fun changeTrust(value: ChangeTrustResult): OperationResultTr
    fun allowTrust(value: AllowTrustResult): OperationResultTr
    fun accountMerge(value: AccountMergeResult): OperationResultTr
    fun inflation(value: InflationResult): OperationResultTr
    fun manageData(value: ManageDataResult): OperationResultTr
    fun bumpSequence(value: BumpSequenceResult): OperationResultTr
    fun manageBuyOffer(value: ManageBuyOfferResult): OperationResultTr
    fun pathPaymentStrictSend(value: PathPaymentStrictSendResult): OperationResultTr
    fun createClaimableBalance(value: CreateClaimableBalanceResult): OperationResultTr
    fun claimClaimableBalance(value: ClaimClaimableBalanceResult): OperationResultTr
    fun beginSponsoringFutureReserves(value: BeginSponsoringFutureReservesResult): OperationResultTr
    fun endSponsoringFutureReserves(value: EndSponsoringFutureReservesResult): OperationResultTr
    fun revokeSponsorship(value: RevokeSponsorshipResult): OperationResultTr
    fun clawback(value: ClawbackResult): OperationResultTr
    fun clawbackClaimableBalance(value: ClawbackClaimableBalanceResult): OperationResultTr
    fun setTrustLineFlags(value: SetTrustLineFlagsResult): OperationResultTr
    fun liquidityPoolDeposit(value: LiquidityPoolDepositResult): OperationResultTr
    fun liquidityPoolWithdraw(value: LiquidityPoolWithdrawResult): OperationResultTr
    fun read(io: Buffer): OperationResultTr
    fun write(value: OperationResultTr, io: Buffer)
    fun isValid(value: OperationResultTr): Boolean
    fun toXDR(value: OperationResultTr): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): OperationResultTr
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): OperationResultTr
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class OperationResult {
  open fun switch(): OperationResultCode
  open fun tr(value: OperationResultTr = definedExternally): OperationResultTr
  open fun value(): dynamic /* OperationResultTr | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun opInner(value: OperationResultTr): OperationResult
    fun opBadAuth(): OperationResult
    fun opNoAccount(): OperationResult
    fun opNotSupported(): OperationResult
    fun opTooManySubentries(): OperationResult
    fun opExceededWorkLimit(): OperationResult
    fun opTooManySponsoring(): OperationResult
    fun read(io: Buffer): OperationResult
    fun write(value: OperationResult, io: Buffer)
    fun isValid(value: OperationResult): Boolean
    fun toXDR(value: OperationResult): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): OperationResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): OperationResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class InnerTransactionResultResult {
  open fun switch(): TransactionResultCode
  open fun results(value: Array<OperationResult> = definedExternally): Array<OperationResult>
  open fun value(): dynamic /* Array<OperationResult> | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun txSuccess(value: Array<OperationResult>): InnerTransactionResultResult
    fun txFailed(value: Array<OperationResult>): InnerTransactionResultResult
    fun txTooEarly(): InnerTransactionResultResult
    fun txTooLate(): InnerTransactionResultResult
    fun txMissingOperation(): InnerTransactionResultResult
    fun txBadSeq(): InnerTransactionResultResult
    fun txBadAuth(): InnerTransactionResultResult
    fun txInsufficientBalance(): InnerTransactionResultResult
    fun txNoAccount(): InnerTransactionResultResult
    fun txInsufficientFee(): InnerTransactionResultResult
    fun txBadAuthExtra(): InnerTransactionResultResult
    fun txInternalError(): InnerTransactionResultResult
    fun txNotSupported(): InnerTransactionResultResult
    fun txBadSponsorship(): InnerTransactionResultResult
    fun txBadMinSeqAgeOrGap(): InnerTransactionResultResult
    fun txMalformed(): InnerTransactionResultResult
    fun read(io: Buffer): InnerTransactionResultResult
    fun write(value: InnerTransactionResultResult, io: Buffer)
    fun isValid(value: InnerTransactionResultResult): Boolean
    fun toXDR(value: InnerTransactionResultResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): InnerTransactionResultResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): InnerTransactionResultResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class InnerTransactionResultExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): InnerTransactionResultExt
    fun write(value: InnerTransactionResultExt, io: Buffer)
    fun isValid(value: InnerTransactionResultExt): Boolean
    fun toXDR(value: InnerTransactionResultExt): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): InnerTransactionResultExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): InnerTransactionResultExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionResultResult {
  open fun switch(): TransactionResultCode
  open fun innerResultPair(
    value: InnerTransactionResultPair = definedExternally
  ): InnerTransactionResultPair
  open fun results(value: Array<OperationResult> = definedExternally): Array<OperationResult>
  open fun value(): dynamic /* InnerTransactionResultPair | Array<OperationResult> | Unit */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun txFeeBumpInnerSuccess(value: InnerTransactionResultPair): TransactionResultResult
    fun txFeeBumpInnerFailed(value: InnerTransactionResultPair): TransactionResultResult
    fun txSuccess(value: Array<OperationResult>): TransactionResultResult
    fun txFailed(value: Array<OperationResult>): TransactionResultResult
    fun txTooEarly(): TransactionResultResult
    fun txTooLate(): TransactionResultResult
    fun txMissingOperation(): TransactionResultResult
    fun txBadSeq(): TransactionResultResult
    fun txBadAuth(): TransactionResultResult
    fun txInsufficientBalance(): TransactionResultResult
    fun txNoAccount(): TransactionResultResult
    fun txInsufficientFee(): TransactionResultResult
    fun txBadAuthExtra(): TransactionResultResult
    fun txInternalError(): TransactionResultResult
    fun txNotSupported(): TransactionResultResult
    fun txBadSponsorship(): TransactionResultResult
    fun txBadMinSeqAgeOrGap(): TransactionResultResult
    fun txMalformed(): TransactionResultResult
    fun read(io: Buffer): TransactionResultResult
    fun write(value: TransactionResultResult, io: Buffer)
    fun isValid(value: TransactionResultResult): Boolean
    fun toXDR(value: TransactionResultResult): Buffer
    fun fromXDR(
      input: Buffer,
      format: String /* "raw" */ = definedExternally
    ): TransactionResultResult
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionResultResult
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class TransactionResultExt {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): TransactionResultExt
    fun write(value: TransactionResultExt, io: Buffer)
    fun isValid(value: TransactionResultExt): Boolean
    fun toXDR(value: TransactionResultExt): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): TransactionResultExt
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): TransactionResultExt
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class ExtensionPoint {
  open fun switch(): Number
  open fun value()
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun read(io: Buffer): ExtensionPoint
    fun write(value: ExtensionPoint, io: Buffer)
    fun isValid(value: ExtensionPoint): Boolean
    fun toXDR(value: ExtensionPoint): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): ExtensionPoint
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): ExtensionPoint
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class PublicKey {
  open fun switch(): PublicKeyType
  open fun ed25519(value: Buffer = definedExternally): Buffer
  open fun value(): Buffer
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun publicKeyTypeEd25519(value: Buffer): PublicKey
    fun read(io: Buffer): PublicKey
    fun write(value: PublicKey, io: Buffer)
    fun isValid(value: PublicKey): Boolean
    fun toXDR(value: PublicKey): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): PublicKey
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): PublicKey
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}

external open class SignerKey {
  open fun switch(): SignerKeyType
  open fun ed25519(value: Buffer = definedExternally): Buffer
  open fun preAuthTx(value: Buffer = definedExternally): Buffer
  open fun hashX(value: Buffer = definedExternally): Buffer
  open fun ed25519SignedPayload(
    value: SignerKeyEd25519SignedPayload = definedExternally
  ): SignerKeyEd25519SignedPayload
  open fun value(): dynamic /* Buffer | SignerKeyEd25519SignedPayload */
  open fun toXDR(
    format: String /* "raw" | "hex" | "base64" */ = definedExternally
  ): dynamic /* Buffer | String */
  open fun toXDR(): Buffer

  companion object {
    fun signerKeyTypeEd25519(value: Buffer): SignerKey
    fun signerKeyTypePreAuthTx(value: Buffer): SignerKey
    fun signerKeyTypeHashX(value: Buffer): SignerKey
    fun signerKeyTypeEd25519SignedPayload(value: SignerKeyEd25519SignedPayload): SignerKey
    fun read(io: Buffer): SignerKey
    fun write(value: SignerKey, io: Buffer)
    fun isValid(value: SignerKey): Boolean
    fun toXDR(value: SignerKey): Buffer
    fun fromXDR(input: Buffer, format: String /* "raw" */ = definedExternally): SignerKey
    fun fromXDR(input: String, format: String /* "hex" | "base64" */): SignerKey
    fun validateXDR(input: Buffer, format: String /* "raw" */ = definedExternally): Boolean
    fun validateXDR(input: String, format: String /* "hex" | "base64" */): Boolean
  }
}
