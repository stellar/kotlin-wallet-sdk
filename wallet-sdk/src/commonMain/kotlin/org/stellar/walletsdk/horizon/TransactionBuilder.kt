@file:JvmName("TransactionBuilderJvm")
package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.anchor.requireStatus
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.exception.*
import kotlin.jvm.JvmName

private val log = KotlinLogging.logger {}

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
expect class TransactionBuilder {
  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun lockAccountMasterKey(sponsorAddress: String? = null): TransactionBuilder

  /**
   * Fund an account to activate it. This transaction can be sponsored.
   *
   * @param destinationAddress Stellar address of the account that is being funded
   * @param startingBalance optional Starting account balance in XLM. Minimum for non-sponsored
   * accounts is 1 XLM, sponsored accounts can leave it at 0 XLM. Default value is 1.
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [InvalidStartingBalanceException] when starting balance is less than 1 XLM for
   * non-sponsored account
   */
  fun fund(
    destinationAddress: String,
    startingBalance: String = "1",
    sponsorAddress: String? = null
  ): TransactionBuilder

  /**
   * Add an asset (trustline) to the account. This transaction can be sponsored.
   *
   * @param asset Target asset
   * @param trustLimit optional The limit of the trustline. Default value is maximum supported.
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun addAssetSupport(
    asset: IssuedAssetId,
    trustLimit: String = defaultTrustLimit,
    sponsorAddress: String? = null
  ): TransactionBuilder
  /**
   * Remove an asset (trustline) from the account.
   *
   * @param asset Target asset
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAssetSupport(asset: IssuedAssetId): TransactionBuilder

  /**
   * Add new signer to the account. Use caution when adding new signers, make sure you set the
   * correct signer weight. Otherwise, you might lock the account irreversibly.
   *
   * This transaction can be sponsored.
   *
   * @param signerAddress Stellar address of the signer that is added
   * @param signerWeight Signer weight
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun addAccountSigner(signerAddress: String, signerWeight: Int, sponsorAddress: String? = null) : TransactionBuilder

  /**
   * Remove signer from the account.
   *
   * @param signerAddress Stellar address of the signer that is removed
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAccountSigner(
    signerAddress: String,
    sponsorAddress: String? = null
  ): TransactionBuilder

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
  fun transfer(
    destinationAddress: String,
    assetId: StellarAssetId,
    amount: String,
    sponsorAddress: String? = null
  ): TransactionBuilder

  fun setThreshold(low: Int, medium: Int, high: Int, sponsorAddress: String? = null): TransactionBuilder

  fun build(): Transaction
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

internal expect val defaultTrustLimit: String
