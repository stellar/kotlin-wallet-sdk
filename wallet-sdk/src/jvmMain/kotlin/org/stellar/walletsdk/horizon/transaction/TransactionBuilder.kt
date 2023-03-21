package org.stellar.walletsdk.horizon.transaction

import org.stellar.sdk.*
import org.stellar.sdk.TransactionBuilder as SdkBuilder
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.Network
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.asset.toAsset
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.*
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.util.*
import org.stellar.walletsdk.util.JvmUtil.makeMemo

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
actual class TransactionBuilder
internal constructor(
  private val cfg: Config,
  sourceAccount: AccountResponse,
  memo: Pair<MemoType, String>?
) : CommonTransactionBuilder<TransactionBuilder>(sourceAccount.accountId) {
  private val network: Network = cfg.stellar.network
  private val maxBaseFeeInStroops: Int = cfg.stellar.maxBaseFeeStroops.toInt()
  override val operations: MutableList<Operation> = mutableListOf()

  // TODO: make timeout configurable
  private val builder: SdkBuilder =
    SdkBuilder(sourceAccount, network).setBaseFee(maxBaseFeeInStroops).setTimeout(180)

  init {
    memo?.also { builder.addMemo(it.first.makeMemo(it.second, cfg.app)) }
  }

  /**
   * Start a sponsoring block
   *
   * @param sponsorAccount account that will be used to sponsor all operations inside the block
   * @param sponsoredAccount account that is sponsored and will be used as a source account of all
   * operations inside the block. If not specified, defaults to builder's sourceAddress.
   * @param body main code block, that contains logic of operations sponsoring
   * @return [TransactionBuilder]
   * @receiver [SponsoringBuilder]
   */
  actual fun sponsoring(
    sponsorAccount: AccountKeyPair,
    sponsoredAccount: AccountKeyPair?,
    body: SponsoringBuilder.() -> SponsoringBuilder
  ): TransactionBuilder {
    SponsoringBuilder(sponsoredAccount?.address ?: sourceAddress, sponsorAccount, operations)
      .body()
      .stopSponsoring()
    return this
  }

  /**
   * Create an account in the network.
   *
   * @param newAccount Key pair of an account to be created.
   * @param startingBalance optional Starting account balance in XLM. Minimum for non-sponsored
   * accounts is 1 XLM. Default value is 1.
   * @throws [InvalidStartingBalanceException] on invalid starting balance
   */
  actual fun createAccount(newAccount: AccountKeyPair, startingBalance: UInt) = building {
    if (startingBalance < 1u) {
      throw InvalidStartingBalanceException
    }

    doCreateAccount(newAccount, startingBalance, sourceAddress)
  }

  /**
   * Creates transaction transferring asset, using Stellar's [payment operation]
   * (https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment) to
   * move asset between accounts.
   *
   * @param destinationAddress Stellar address of account receiving a transfer
   * @param assetId: Target asset id
   * @param amount amount of asset to transfer
   * @return formed transfer transaction
   */
  actual fun transfer(destinationAddress: String, assetId: StellarAssetId, amount: String) =
    building {
      PaymentOperation.Builder(destinationAddress, assetId.toAsset(), amount).build()
    }

  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  actual fun addOperation(operation: Operation) = building { operation }

  /**
   * Creates transaction
   *
   * @return **unsigned** transaction
   */
  actual fun build(): Transaction {
    operations.forEach { builder.addOperation(it) }
    return builder.build()
  }
}

suspend fun WithdrawalTransaction.toTransferTransaction(
  stellar: Stellar,
  assetId: StellarAssetId
): Transaction {
  this.requireStatus(TransactionStatus.PENDING_USER_TRANSFER_START)

  return stellar
    .transaction(this.from, this.withdrawalMemo.let { this.withdrawalMemoType to it })
    .transfer(this.withdrawAnchorAccount, assetId, this.amountIn)
    .build()
}

actual typealias Operation = org.stellar.sdk.Operation
