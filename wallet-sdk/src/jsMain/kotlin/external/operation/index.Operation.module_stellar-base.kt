@file:JsModule("stellar-sdk")
@file:JsNonModule
@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external.operation

import OperationOptions.BaseOptions
import OperationOptions.`T$100`
import external.Buffer
import external.xdr.Asset
import external.xdr.Claimant
import external.xdr.hidden.Operation
import external.xdr.hidden.Operation2
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

external object Operation {
  interface BaseOperation<T : String> {
    var type: T
    var source: String?
      get() = definedExternally
      set(value) = definedExternally
  }

  interface AccountMerge : BaseOperation<String /* "accountMerge" */> {
    var destination: String
  }

  fun accountMerge(options: OperationOptions.AccountMerge): Operation2<AccountMerge>

  interface AllowTrust : BaseOperation<String /* "allowTrust" */> {
    var trustor: String
    var assetCode: String
    var authorize: dynamic /* Boolean? | 0 | 1 | 2 */
      get() = definedExternally
      set(value) = definedExternally
  }

  fun allowTrust(options: OperationOptions.AllowTrust): Operation2<AllowTrust>

  interface ChangeTrust : BaseOperation<String /* "changeTrust" */> {
    var line: dynamic /* Asset | LiquidityPoolAsset */
      get() = definedExternally
      set(value) = definedExternally
    var limit: String
  }

  fun changeTrust(options: OperationOptions.ChangeTrust): Operation2<ChangeTrust>

  interface CreateAccount : BaseOperation<String /* "createAccount" */> {
    var destination: String
    var startingBalance: String
  }

  fun createAccount(options: OperationOptions.CreateAccount): Operation2<CreateAccount>

  interface CreatePassiveSellOffer : BaseOperation<String /* "createPassiveSellOffer" */> {
    var selling: Asset
    var buying: Asset
    var amount: String
    var price: String
  }

  fun createPassiveSellOffer(
    options: OperationOptions.CreatePassiveSellOffer
  ): Operation2<CreatePassiveSellOffer>

  interface Inflation : BaseOperation<String /* "inflation" */>

  fun inflation(options: OperationOptions.Inflation): Operation2<Inflation>

  interface ManageData : BaseOperation<String /* "manageData" */> {
    var name: String
    var value: Buffer?
      get() = definedExternally
      set(value) = definedExternally
  }

  fun manageData(options: OperationOptions.ManageData): Operation2<ManageData>

  interface ManageSellOffer : BaseOperation<String /* "manageSellOffer" */> {
    var selling: Asset
    var buying: Asset
    var amount: String
    var price: String
    var offerId: String
  }

  fun manageSellOffer(options: OperationOptions.ManageSellOffer): Operation2<ManageSellOffer>

  interface ManageBuyOffer : BaseOperation<String /* "manageBuyOffer" */> {
    var selling: Asset
    var buying: Asset
    var buyAmount: String
    var price: String
    var offerId: String
  }

  fun manageBuyOffer(options: OperationOptions.ManageBuyOffer): Operation2<ManageBuyOffer>

  interface PathPaymentStrictReceive : BaseOperation<String /* "pathPaymentStrictReceive" */> {
    var sendAsset: Asset
    var sendMax: String
    var destination: String
    var destAsset: Asset
    var destAmount: String
    var path: Array<Asset>
  }

  fun pathPaymentStrictReceive(
    options: OperationOptions.PathPaymentStrictReceive
  ): Operation2<PathPaymentStrictReceive>

  interface PathPaymentStrictSend : BaseOperation<String /* "pathPaymentStrictSend" */> {
    var sendAsset: Asset
    var sendAmount: String
    var destination: String
    var destAsset: Asset
    var destMin: String
    var path: Array<Asset>
  }

  fun pathPaymentStrictSend(
    options: OperationOptions.PathPaymentStrictSend
  ): Operation2<PathPaymentStrictSend>

  interface Payment : BaseOperation<String /* "payment" */> {
    var amount: String
    var asset: Asset
    var destination: String
  }

  fun payment(options: OperationOptions.Payment): Operation2<Payment>

  interface SetOptions<T> : BaseOperation<String /* "setOptions" */> {
    var inflationDest: String?
      get() = definedExternally
      set(value) = definedExternally
    var clearFlags:
      dynamic /* AuthFlag.required? | AuthFlag.immutable? | AuthFlag.revocable? | AuthFlag.clawbackEnabled? */
      get() = definedExternally
      set(value) = definedExternally
    var setFlags:
      dynamic /* AuthFlag.required? | AuthFlag.immutable? | AuthFlag.revocable? | AuthFlag.clawbackEnabled? */
      get() = definedExternally
      set(value) = definedExternally
    var masterWeight: Number?
      get() = definedExternally
      set(value) = definedExternally
    var lowThreshold: Number?
      get() = definedExternally
      set(value) = definedExternally
    var medThreshold: Number?
      get() = definedExternally
      set(value) = definedExternally
    var highThreshold: Number?
      get() = definedExternally
      set(value) = definedExternally
    var homeDomain: String?
      get() = definedExternally
      set(value) = definedExternally
    var signer: Any
  }

  interface SetOptions__0 :
    SetOptions<
      dynamic /* SignerOptions.Ed25519PublicKey | SignerOptions.Sha256Hash | SignerOptions.PreAuthTx | SignerOptions.Ed25519SignedPayload */
    >

  fun <T> setOptions(options: OperationOptions.SetOptions<T>): Operation2<SetOptions<T>>

  interface BumpSequence : BaseOperation<String /* "bumpSequence" */> {
    var bumpTo: String
  }

  fun bumpSequence(options: OperationOptions.BumpSequence): Operation2<BumpSequence>

  interface CreateClaimableBalance : BaseOperation<String /* "createClaimableBalance" */> {
    var amount: String
    var asset: Asset
    var claimants: Array<Claimant>
  }

  fun createClaimableBalance(
    options: OperationOptions.CreateClaimableBalance
  ): Operation2<CreateClaimableBalance>

  interface ClaimClaimableBalance : BaseOperation<String /* "claimClaimableBalance" */> {
    var balanceId: String
  }

  fun claimClaimableBalance(
    options: OperationOptions.ClaimClaimableBalance
  ): Operation2<ClaimClaimableBalance>

  interface BeginSponsoringFutureReserves :
    BaseOperation<String /* "beginSponsoringFutureReserves" */> {
    var sponsoredId: String
  }

  fun beginSponsoringFutureReserves(
    options: OperationOptions.BeginSponsoringFutureReserves
  ): Operation2<BeginSponsoringFutureReserves>

  interface EndSponsoringFutureReserves : BaseOperation<String /* "endSponsoringFutureReserves" */>

  fun endSponsoringFutureReserves(options: BaseOptions): Operation2<EndSponsoringFutureReserves>

  interface RevokeAccountSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var account: String
  }

  fun revokeAccountSponsorship(
    options: OperationOptions.RevokeAccountSponsorship
  ): Operation2<RevokeAccountSponsorship>

  interface RevokeTrustlineSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var account: String
    var asset: dynamic /* Asset | LiquidityPoolId */
      get() = definedExternally
      set(value) = definedExternally
  }

  fun revokeTrustlineSponsorship(
    options: OperationOptions.RevokeTrustlineSponsorship
  ): Operation2<RevokeTrustlineSponsorship>

  interface RevokeOfferSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var seller: String
    var offerId: String
  }

  fun revokeOfferSponsorship(
    options: OperationOptions.RevokeOfferSponsorship
  ): Operation2<RevokeOfferSponsorship>

  interface RevokeDataSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var account: String
    var name: String
  }

  fun revokeDataSponsorship(
    options: OperationOptions.RevokeDataSponsorship
  ): Operation2<RevokeDataSponsorship>

  interface RevokeClaimableBalanceSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var balanceId: String
  }

  fun revokeClaimableBalanceSponsorship(
    options: OperationOptions.RevokeClaimableBalanceSponsorship
  ): Operation2<RevokeClaimableBalanceSponsorship>

  interface RevokeLiquidityPoolSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var liquidityPoolId: String
  }

  fun revokeLiquidityPoolSponsorship(
    options: OperationOptions.RevokeLiquidityPoolSponsorship
  ): Operation2<RevokeLiquidityPoolSponsorship>

  interface RevokeSignerSponsorship : BaseOperation<String /* "revokeSponsorship" */> {
    var account: String
    var signer:
      dynamic /* SignerKeyOptions.Ed25519PublicKey | SignerKeyOptions.Sha256Hash | SignerKeyOptions.PreAuthTx | SignerKeyOptions.Ed25519SignedPayload */
      get() = definedExternally
      set(value) = definedExternally
  }

  fun revokeSignerSponsorship(
    options: OperationOptions.RevokeSignerSponsorship
  ): Operation2<RevokeSignerSponsorship>

  interface Clawback : BaseOperation<String /* "clawback" */> {
    var asset: Asset
    var amount: String
    var from: String
  }

  fun clawback(options: OperationOptions.Clawback): Operation2<Clawback>

  interface ClawbackClaimableBalance : BaseOperation<String /* "clawbackClaimableBalance" */> {
    var balanceId: String
  }

  fun clawbackClaimableBalance(
    options: OperationOptions.ClawbackClaimableBalance
  ): Operation2<ClawbackClaimableBalance>

  interface SetTrustLineFlags : BaseOperation<String /* "setTrustLineFlags" */> {
    var trustor: String
    var asset: Asset
    var flags: `T$100`
  }

  fun setTrustLineFlags(options: OperationOptions.SetTrustLineFlags): Operation2<SetTrustLineFlags>

  interface LiquidityPoolDeposit : BaseOperation<String /* "liquidityPoolDeposit" */> {
    var liquidityPoolId: String
    var maxAmountA: String
    var maxAmountB: String
    var minPrice: String
    var maxPrice: String
  }

  fun liquidityPoolDeposit(
    options: OperationOptions.LiquidityPoolDeposit
  ): Operation2<LiquidityPoolDeposit>

  interface LiquidityPoolWithdraw : BaseOperation<String /* "liquidityPoolWithdraw" */> {
    var liquidityPoolId: String
    var amount: String
    var minAmountA: String
    var minAmountB: String
  }

  fun liquidityPoolWithdraw(
    options: OperationOptions.LiquidityPoolWithdraw
  ): Operation2<LiquidityPoolWithdraw>

  fun <T : Operation> fromXDRObject(xdrOperation: Operation2<T>): T
}
