@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)
@file:JsModule("stellar-sdk")
@file:JsNonModule

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

external open class Server(serverURL: String, opts: Options = definedExternally) {
  open var serverURL: Any
  open fun fetchTimebounds(
    seconds: Number,
    _isRetry: Boolean = definedExternally
  ): Promise<Timebounds>
  open fun fetchBaseFee(): Promise<Number>
  open fun feeStats(): Promise<FeeStatsResponse>
  open fun submitTransaction(
    transaction: Transaction__0,
    opts: SubmitTransactionOptions = definedExternally
  ): Promise<SubmitTransactionResponse>
  open fun submitTransaction(transaction: Transaction__0): Promise<SubmitTransactionResponse>
  open fun submitTransaction(
    transaction: FeeBumpTransaction,
    opts: SubmitTransactionOptions = definedExternally
  ): Promise<SubmitTransactionResponse>
  open fun submitTransaction(transaction: FeeBumpTransaction): Promise<SubmitTransactionResponse>
  open fun accounts(): AccountCallBuilder
  //    open fun claimableBalances(): ClaimableBalanceCallBuilder
  //    open fun ledgers(): LedgerCallBuilder
  //    open fun transactions(): TransactionCallBuilder
  //    open fun offers(): OfferCallBuilder
  //    open fun orderbook(selling: Asset, buying: Asset): OrderbookCallBuilder
  //    open fun trades(): TradesCallBuilder
  //    open fun operations(): OperationCallBuilder
  //    open fun liquidityPools(): LiquidityPoolCallBuilder
  //    open fun strictReceivePaths(source: String, destinationAsset: Asset, destinationAmount:
  // String): PathCallBuilder
  //    open fun strictReceivePaths(source: Array<Asset>, destinationAsset: Asset,
  // destinationAmount: String): PathCallBuilder
  //    open fun strictSendPaths(sourceAsset: Asset, sourceAmount: String, destination: String):
  // PathCallBuilder
  //    open fun strictSendPaths(sourceAsset: Asset, sourceAmount: String, destination:
  // Array<Asset>): PathCallBuilder
  //    open fun payments(): PaymentCallBuilder
  //    open fun effects(): EffectCallBuilder
  //    open fun friendbot(address: String): FriendbotBuilder
  //    open fun assets(): AssetsCallBuilder
  open fun loadAccount(accountId: String): Promise<AccountResponse>
  //    open fun tradeAggregation(base: Asset, counter: Asset, start_time: Number, end_time: Number,
  // resolution: Number, offset: Number): TradeAggregationCallBuilder
  open fun checkMemoRequired(transaction: Transaction__0): Promise<Unit>
  open fun checkMemoRequired(transaction: FeeBumpTransaction): Promise<Unit>
  interface Options {
    var allowHttp: Boolean?
      get() = definedExternally
      set(value) = definedExternally
    var appName: String?
      get() = definedExternally
      set(value) = definedExternally
    var appVersion: String?
      get() = definedExternally
      set(value) = definedExternally
    var authToken: String?
      get() = definedExternally
      set(value) = definedExternally
  }
  interface Timebounds {
    var minTime: Number
    var maxTime: Number
  }
  interface SubmitTransactionOptions {
    var skipMemoRequiredCheck: Boolean?
      get() = definedExternally
      set(value) = definedExternally
  }
}
