package org.stellar.walletsdk.horizon.transaction

import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Transaction

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
expect class TransactionBuilder : CommonTransactionBuilder<TransactionBuilder> {
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
  ): TransactionBuilder

  /**
   * Create an account in the network.
   *
   * @param newAccount Key pair of an account to be created.
   * @param startingBalance optional Starting account balance in XLM. Minimum for non-sponsored
   * accounts is 1 XLM. Default value is 1.
   * @throws [InvalidStartingBalanceException] on invalid starting balance
   */
  fun createAccount(newAccount: AccountKeyPair, startingBalance: UInt = 1u): TransactionBuilder

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
    amount: String
  ): TransactionBuilder

  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: Operation): TransactionBuilder

  /**
   * Creates transaction
   *
   * @return **unsigned** transaction
   */
  fun build(): Transaction
}

expect abstract class Operation
