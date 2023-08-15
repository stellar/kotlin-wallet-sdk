package org.stellar.walletsdk.horizon.transaction

import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*
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
import org.stellar.sdk.TransactionBuilder as SdkBuilder

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
class TransactionBuilder
internal constructor(
  cfg: Config,
  sourceAccount: AccountResponse,
  baseFee: UInt?,
  memo: Pair<MemoType, String>?,
  timeBounds: TimeBounds?
) : CommonTransactionBuilder<TransactionBuilder>(sourceAccount.accountId) {
  private val network: Network = cfg.stellar.network
  private val maxBaseFeeInStroops: Int = cfg.stellar.baseFee.toInt()
  override val operations: MutableList<Operation> = mutableListOf()

  private val builder: SdkBuilder =
    SdkBuilder(sourceAccount, network)
      .setBaseFee(baseFee?.toInt() ?: maxBaseFeeInStroops)
      .addPreconditions(
        TransactionPreconditions.builder()
          .timeBounds(timeBounds ?: cfg.stellar.defaultTimeout.toTimeBounds())
          .build()
      )

  init {
    memo?.also { builder.addMemo(it.first.mapper(it.second)) }
  }

  /**
   * Set memo to this builder
   *
   * @param memo memo to add
   */
  fun setMemo(memo: Pair<MemoType, String>): TransactionBuilder {
    builder.addMemo(memo.first.mapper(memo.second))
    return this
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
  fun sponsoring(
    sponsorAccount: AccountKeyPair,
    sponsoredAccount: AccountKeyPair? = null,
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
  fun createAccount(newAccount: AccountKeyPair, startingBalance: UInt = 1u) = building {
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
  fun transfer(destinationAddress: String, assetId: StellarAssetId, amount: String) = building {
    PaymentOperation.Builder(destinationAddress, assetId.toAsset(), amount).build()
  }

  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: Operation) = building { operation }

  /**
   * Creates transaction
   *
   * @return **unsigned** transaction
   */
  fun build(): Transaction {
    operations.forEach { builder.addOperation(it) }
    return builder.build()
  }
}

/**
 * Transforms this withdrawal transaction to the Stellar Transfer transaction that can be submitted
 * to the network.
 *
 * @param stellar instance of the Stellar service.
 * @param assetId asset that is being transferred.
 * @param sourceAddress (optional) origin account that will be used to transfer funds. If not
 * specified, `from` field of this transaction will be used.
 * @return Stellar transfer transaction.
 */
@Deprecated(
  "Deprecated in favor of TransactionBuilder function",
  replaceWith =
    ReplaceWith(
      "stellar.transaction(this.from).transferWithdrawalTransaction(this, assetId).build()"
    )
)
suspend fun WithdrawalTransaction.toStellarTransfer(
  stellar: Stellar,
  assetId: StellarAssetId,
  sourceAddress: AccountKeyPair? = null
): Transaction {
  this.requireStatus(TransactionStatus.PENDING_USER_TRANSFER_START)

  return stellar
    .transaction(
      sourceAddress
        ?: this.from
          ?: throw ValidationException(
          "Source account is not provided and from account is unknown"
        ),
      memo = this.withdrawalMemo?.let { this.withdrawalMemoType to it }
          ?: throw ValidationException("Missing withdrawal_memo in the transaction")
    )
    .transfer(this.withdrawAnchorAccount, assetId, this.amountIn)
    .build()
}

/**
 * Add a transfer to this builder from the withdrawal transaction
 *
 * @param transaction withdrawal transaction to fulfill
 * @param assetId target asset id
 */
suspend fun TransactionBuilder.transferWithdrawalTransaction(
  transaction: WithdrawalTransaction,
  assetId: StellarAssetId
): TransactionBuilder {
  transaction.requireStatus(TransactionStatus.PENDING_USER_TRANSFER_START)

  return this.setMemo(
      transaction.withdrawalMemo?.let { transaction.withdrawalMemoType to it }
        ?: throw ValidationException("Missing withdrawal_memo in the transaction")
    )
    .transfer(transaction.withdrawAnchorAccount, assetId, transaction.amountIn)
}
