@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package OperationOptions

import external.xdr.Asset
import external.xdr.Claimant
import kotlin.js.*

external interface BaseOptions {
  var source: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface AccountMerge : BaseOptions {
  var destination: String
}

external interface AllowTrust : BaseOptions {
  var trustor: String
  var assetCode: String
  var authorize: dynamic /* Boolean? | 0 | 1 | 2 */
    get() = definedExternally
    set(value) = definedExternally
}

external interface ChangeTrust : BaseOptions {
  var asset: dynamic /* Asset | LiquidityPoolAsset */
    get() = definedExternally
    set(value) = definedExternally
  var limit: String?
    get() = definedExternally
    set(value) = definedExternally
}

external interface CreateAccount : BaseOptions {
  var destination: String
  var startingBalance: String
}

external interface CreatePassiveSellOffer : BaseOptions {
  var selling: Asset
  var buying: Asset
  var amount: String
  var price: dynamic /* Number? | String? | Any? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface ManageSellOffer : CreatePassiveSellOffer {
  var offerId: dynamic /* Number? | String? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface ManageBuyOffer : BaseOptions {
  var selling: Asset
  var buying: Asset
  var buyAmount: String
  var price: dynamic /* Number? | String? | Any? */
    get() = definedExternally
    set(value) = definedExternally
  var offerId: dynamic /* Number? | String? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface Inflation : BaseOptions

external interface ManageData : BaseOptions {
  var name: String
  var value: dynamic /* String? | Buffer? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface PathPaymentStrictReceive : BaseOptions {
  var sendAsset: Asset
  var sendMax: String
  var destination: String
  var destAsset: Asset
  var destAmount: String
  var path: Array<Asset>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface PathPaymentStrictSend : BaseOptions {
  var sendAsset: Asset
  var sendAmount: String
  var destination: String
  var destAsset: Asset
  var destMin: String
  var path: Array<Asset>?
    get() = definedExternally
    set(value) = definedExternally
}

external interface Payment : BaseOptions {
  var amount: String
  var asset: Asset
  var destination: String
}

external interface SetOptions<T> : BaseOptions {
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
  var masterWeight: dynamic /* Number? | String? */
    get() = definedExternally
    set(value) = definedExternally
  var lowThreshold: dynamic /* Number? | String? */
    get() = definedExternally
    set(value) = definedExternally
  var medThreshold: dynamic /* Number? | String? */
    get() = definedExternally
    set(value) = definedExternally
  var highThreshold: dynamic /* Number? | String? */
    get() = definedExternally
    set(value) = definedExternally
  var homeDomain: String?
    get() = definedExternally
    set(value) = definedExternally
  var signer: T?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SetOptions__0 : SetOptions<Any>

external interface BumpSequence : BaseOptions {
  var bumpTo: String
}

external interface CreateClaimableBalance : BaseOptions {
  var asset: Asset
  var amount: String
  var claimants: Array<Claimant>
}

external interface ClaimClaimableBalance : BaseOptions {
  var balanceId: String
}

external interface BeginSponsoringFutureReserves : BaseOptions {
  var sponsoredId: String
}

external interface RevokeAccountSponsorship : BaseOptions {
  var account: String
}

external interface RevokeTrustlineSponsorship : BaseOptions {
  var account: String
  var asset: dynamic /* Asset | LiquidityPoolId */
    get() = definedExternally
    set(value) = definedExternally
}

external interface RevokeOfferSponsorship : BaseOptions {
  var seller: String
  var offerId: String
}

external interface RevokeDataSponsorship : BaseOptions {
  var account: String
  var name: String
}

external interface RevokeClaimableBalanceSponsorship : BaseOptions {
  var balanceId: String
}

external interface RevokeLiquidityPoolSponsorship : BaseOptions {
  var liquidityPoolId: String
}

external interface RevokeSignerSponsorship : BaseOptions {
  var account: String
  var signer:
    dynamic /* SignerKeyOptions.Ed25519PublicKey | SignerKeyOptions.Sha256Hash | SignerKeyOptions.PreAuthTx | SignerKeyOptions.Ed25519SignedPayload */
    get() = definedExternally
    set(value) = definedExternally
}

external interface Clawback : BaseOptions {
  var asset: Asset
  var amount: String
  var from: String
}

external interface ClawbackClaimableBalance : BaseOptions {
  var balanceId: String
}

external interface `T$100` {
  var authorized: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var authorizedToMaintainLiabilities: Boolean?
    get() = definedExternally
    set(value) = definedExternally
  var clawbackEnabled: Boolean?
    get() = definedExternally
    set(value) = definedExternally
}

external interface SetTrustLineFlags : BaseOptions {
  var trustor: String
  var asset: Asset
  var flags: `T$100`
}

external interface LiquidityPoolDeposit : BaseOptions {
  var liquidityPoolId: String
  var maxAmountA: String
  var maxAmountB: String
  var minPrice: dynamic /* Number? | String? | Any? */
    get() = definedExternally
    set(value) = definedExternally
  var maxPrice: dynamic /* Number? | String? | Any? */
    get() = definedExternally
    set(value) = definedExternally
}

external interface LiquidityPoolWithdraw : BaseOptions {
  var liquidityPoolId: String
  var amount: String
  var minAmountA: String
  var minAmountB: String
}
