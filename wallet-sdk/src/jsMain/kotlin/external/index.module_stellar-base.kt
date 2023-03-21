@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)
@file:JsModule("stellar-sdk")
@file:JsNonModule

package external

import external.xdr.ChangeTrustAsset
import external.xdr.ClaimPredicate
import external.xdr.TransactionEnvelope
import external.xdr.TrustLineAsset
import kotlin.js.Date

external open class Account(accountId: String, sequence: String) {
  open fun accountId(): String
  open fun sequenceNumber(): String
  open fun incrementSequenceNumber()
}

external open class MuxedAccount(account: Account, sequence: String) {
  open fun accountId(): String
  open fun sequenceNumber(): String
  open fun incrementSequenceNumber()
  open fun baseAccount(): Account
  open fun id(): String
  open fun setId(id: String): MuxedAccount
  //  open fun toXDRObject(): xdr.MuxedAccount
  open fun equals(otherMuxedAccount: MuxedAccount): Boolean

  companion object {
    fun fromAddress(mAddress: String, sequenceNum: String): MuxedAccount
    fun parseBaseAddress(mAddress: String): String
  }
}

external open class Asset(code: String, issuer: String = definedExternally) {
  open fun getCode(): String
  open fun getIssuer(): String
  open fun getAssetType():
    String /* "native" | "credit_alphanum4" | "credit_alphanum12" | "liquidity_pool_shares" */
  open fun isNative(): Boolean
  open fun equals(other: Asset): Boolean
  open fun toXDRObject(): external.xdr.Asset
  open fun toChangeTrustXDRObject(): ChangeTrustAsset
  open fun toTrustLineXDRObject(): TrustLineAsset
  open var code: String
  open var issuer: String

  companion object {
    fun native(): Asset
    fun fromOperation(xdr: external.xdr.Asset): Asset
    fun compare(assetA: Asset, assetB: Asset): dynamic /* "-1" | 0 | 1 */
  }
}

external open class LiquidityPoolAsset(assetA: Asset, assetB: Asset, fee: Number) {
  open fun toXDRObject(): ChangeTrustAsset
  //  open fun getLiquidityPoolParameters(): ConstantProduct
  open fun getAssetType(): String /* "liquidity_pool_shares" */
  open fun equals(other: LiquidityPoolAsset): Boolean
  open var assetA: Asset
  open var assetB: Asset
  open var fee: Number

  companion object {
    fun fromOperation(xdr: ChangeTrustAsset): LiquidityPoolAsset
  }
}

external open class LiquidityPoolId(liquidityPoolId: String) {
  open fun toXDRObject(): TrustLineAsset
  open fun getLiquidityPoolId(): String
  open fun equals(other: LiquidityPoolId): Boolean
  open var liquidityPoolId: String

  companion object {
    fun fromOperation(xdr: TrustLineAsset): LiquidityPoolId
  }
}

external open class Claimant(destination: String, predicate: ClaimPredicate = definedExternally) {
  open var destination: String
  open var predicate: ClaimPredicate
  open fun toXDRObject(): external.xdr.Claimant

  companion object {
    fun fromXDR(claimantXdr: external.xdr.Claimant): Claimant
    fun predicateUnconditional(): ClaimPredicate
    fun predicateAnd(left: ClaimPredicate, right: ClaimPredicate): ClaimPredicate
    fun predicateOr(left: ClaimPredicate, right: ClaimPredicate): ClaimPredicate
    fun predicateNot(predicate: ClaimPredicate): ClaimPredicate
    fun predicateBeforeAbsoluteTime(absBefore: String): ClaimPredicate
    fun predicateBeforeRelativeTime(seconds: String): ClaimPredicate
  }
}

external var FastSigning: Boolean

external interface `T$98` {
  var type: String /* "ed25519" */
  var secretKey: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
  var publicKey: dynamic /* String? | Buffer? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface `T$99` {
  var type: String /* "ed25519" */
  var publicKey: dynamic /* String | Buffer */
    get() = definedExternally
    set(value) = definedExternally
}

external open class Keypair {
  constructor(keys: `T$98`)
  constructor(keys: `T$99`)
  open var type: String /* "ed25519" */
  open fun publicKey(): String
  open fun secret(): String
  open fun rawPublicKey(): Buffer
  open fun rawSecretKey(): Buffer
  open fun canSign(): Boolean
  open fun sign(data: Buffer): Buffer
  open fun signDecorated(data: Buffer): dynamic
  open fun signPayloadDecorated(data: Buffer): dynamic
  open fun signatureHint(): Buffer
  open fun verify(data: Buffer, signature: Buffer): Boolean
  open fun xdrAccountId(): dynamic
  //  open fun xdrAccountId(): AccountId
  open fun xdrPublicKey(): dynamic
  //  open fun xdrPublicKey(): PublicKey
  open fun xdrMuxedAccount(id: String): dynamic
  //  open fun xdrMuxedAccount(id: String): xdr.MuxedAccount

  companion object {
    fun fromRawEd25519Seed(secretSeed: Buffer): Keypair
    fun fromSecret(secretKey: String): Keypair
    fun master(networkPassphrase: String): Keypair
    fun fromPublicKey(publicKey: String): Keypair
    fun random(): Keypair
  }
}

external var LiquidityPoolFeeV18: Any

// external fun getLiquidityPoolId(
//  liquidityPoolType: String /* "constant_product" */,
//  liquidityPoolParameters: ConstantProduct
// ): Buffer

external open class Memo<T> {

  open var type: T
  open var value: Any
  open fun toXDRObject(): dynamic

  companion object {
    fun fromXDRObject(memo: dynamic): Memo<Any>
    fun hash(hash: String): Memo<Any>
    fun id(id: String): Memo<Any>
    fun none(): Memo<Any>
    fun `return`(hash: String): Memo<Any>
    fun text(text: String): Memo<Any>
  }
}

external class Networks {
  companion object  {
    val PUBLIC: Networks = definedExternally/* = 'Public Global Stellar Network ; September 2015' */
    val TESTNET: Networks = definedExternally/* = 'Test SDF Network ; September 2015' */
  }
}

external var AuthRequiredFlag: Number /* 1 */

external var AuthRevocableFlag: Number /* 2 */

external var AuthImmutableFlag: Number /* 4 */

external var AuthClawbackEnabledFlag: Number /* 8 */

external open class TransactionI {
  open fun addSignature(publicKey: String, signature: String)
  open fun addDecoratedSignature(signature: dynamic)
  open var fee: String
  open fun getKeypairSignature(keypair: Keypair): String
  open fun hash(): Buffer
  open var networkPassphrase: String
  open fun sign(vararg keypairs: Keypair)
  open fun signatureBase(): Buffer
  open var signatures: Array<dynamic>
  open fun signHashX(preimage: Buffer)
  open fun signHashX(preimage: String)
  open fun toEnvelope(): dynamic
  open fun toXDR(): String
}

external open class FeeBumpTransaction : TransactionI {
  constructor(envelope: String, networkPassphrase: String)
  //  constructor(envelope: TransactionEnvelope, networkPassphrase: String)
  open var feeSource: String
  open var innerTransaction: Transaction<*, *>
}

external interface `T$101` {
  var minTime: String
  var maxTime: String
}

external interface `T$102` {
  var minLedger: Number
  var maxLedger: Number
}

external open class Transaction<
  TMemo : Memo<Any>,
  TOps : Array<
    dynamic /* Operation.CreateAccount | Operation.Payment | Operation.PathPaymentStrictReceive |
 Operation.PathPaymentStrictSend | Operation.CreatePassiveSellOffer | Operation.ManageSellOffer |
 Operation.ManageBuyOffer | SetOptions__0 | Operation.ChangeTrust | Operation.AllowTrust |
 Operation.AccountMerge | Operation.Inflation | Operation.ManageData | Operation.BumpSequence |
 Operation.CreateClaimableBalance | Operation.ClaimClaimableBalance |
 Operation.BeginSponsoringFutureReserves | Operation.EndSponsoringFutureReserves |
 Operation.RevokeAccountSponsorship | Operation.RevokeTrustlineSponsorship |
 Operation.RevokeOfferSponsorship | Operation.RevokeDataSponsorship |
 Operation.RevokeClaimableBalanceSponsorship | Operation.RevokeLiquidityPoolSponsorship |
 Operation.RevokeSignerSponsorship | Operation.Clawback | Operation.ClawbackClaimableBalance |
 Operation.SetTrustLineFlags | Operation.LiquidityPoolDeposit | Operation.LiquidityPoolWithdraw */>
> : TransactionI {
  constructor(envelope: String, networkPassphrase: String)
  //  constructor(envelope: TransactionEnvelope, networkPassphrase: String)
  open var memo: TMemo
  open var operations: TOps
  open var sequence: String
  open var source: String
  open var timeBounds: `T$101`
  open var ledgerBounds: `T$102`
  open var minAccountSequence: String
  open var minAccountSequenceAge: Number
  open var minAccountSequenceLedgerGap: Number
  open var extraSigners: Array<String>
  open fun getClaimableBalanceId(opIndex: Number): String
}

external var BASE_FEE: Any

external var TimeoutInfinite: Any

@JsName("TransactionBuilder")
external open class TransactionBuilder(
  sourceAccount: Account,
  options: TransactionBuilderOptions = definedExternally
) {
  //  open fun addOperation(operation: dynamic): TransactionBuilder /* this */
  open fun addOperation(operation: Operation2__0): TransactionBuilder /* this */
  open fun addMemo(memo: Memo<Any>): TransactionBuilder /* this */
  open fun setTimeout(timeoutInSeconds: Number): TransactionBuilder /* this */
  open fun setTimebounds(min: Date, max: Date): TransactionBuilder /* this */
  open fun setTimebounds(min: Date, max: Number): TransactionBuilder /* this */
  open fun setTimebounds(min: Number, max: Date): TransactionBuilder /* this */
  open fun setTimebounds(min: Number, max: Number): TransactionBuilder /* this */
  open fun setLedgerbounds(minLedger: Number, maxLedger: Number): TransactionBuilder /* this */
  open fun setMinAccountSequence(minAccountSequence: String): TransactionBuilder /* this */
  open fun setMinAccountSequenceAge(durationInSeconds: Number): TransactionBuilder /* this */
  open fun setMinAccountSequenceLedgerGap(gap: Number): TransactionBuilder /* this */
  open fun setExtraSigners(extraSigners: Array<String>): TransactionBuilder /* this */
  open fun build(): Transaction<*, *>
  open fun setNetworkPassphrase(networkPassphrase: String): TransactionBuilder /* this */
  interface `T$103` {
    var minTime: dynamic /* Date? | Number? | String? */
      get() = definedExternally
      set(value) = definedExternally
    var maxTime: dynamic /* Date? | Number? | String? */
      get() = definedExternally
      set(value) = definedExternally
  }
  interface `T$104` {
    var minLedger: Number?
      get() = definedExternally
      set(value) = definedExternally
    var maxLedger: Number?
      get() = definedExternally
      set(value) = definedExternally
  }
  interface TransactionBuilderOptions {
    var fee: String
    var memo: Memo<Any>?
      get() = definedExternally
      set(value) = definedExternally
    var networkPassphrase: Networks?
      get() = definedExternally
      set(value) = definedExternally
    var timebounds: `T$103`?
      get() = definedExternally
      set(value) = definedExternally
    var ledgerbounds: `T$104`?
      get() = definedExternally
      set(value) = definedExternally
    var minAccountSequence: String?
      get() = definedExternally
      set(value) = definedExternally
    var minAccountSequenceAge: Number?
      get() = definedExternally
      set(value) = definedExternally
    var minAccountSequenceLedgerGap: Number?
      get() = definedExternally
      set(value) = definedExternally
    var extraSigners: Array<String>?
      get() = definedExternally
      set(value) = definedExternally
  }

  companion object {
    fun buildFeeBumpTransaction(
      feeSource: Keypair,
      baseFee: String,
      innerTx: Transaction__0,
      networkPassphrase: String
    ): FeeBumpTransaction
    fun buildFeeBumpTransaction(
      feeSource: String,
      baseFee: String,
      innerTx: Transaction__0,
      networkPassphrase: String
    ): FeeBumpTransaction
    fun fromXDR(
      envelope: String,
      networkPassphrase: String
    ): dynamic /* Transaction__0 | FeeBumpTransaction */
    fun fromXDR(
      envelope: TransactionEnvelope,
      networkPassphrase: String
    ): dynamic /* Transaction__0 | FeeBumpTransaction */
  }
}

external fun hash(data: Buffer): Buffer

external fun sign(data: Buffer, rawSecret: Buffer): Buffer

external fun verify(data: Buffer, signature: Buffer, rawPublicKey: Buffer): Boolean

external fun decodeAddressToMuxedAccount(
  address: String,
  supportMuxing: Boolean
): external.xdr.MuxedAccount

external fun encodeMuxedAccountToAddress(
  account: external.xdr.MuxedAccount,
  supportMuxing: Boolean
): String

external fun encodeMuxedAccount(gAddress: String, id: String): external.xdr.MuxedAccount

external fun extractBaseAddress(address: String): String
