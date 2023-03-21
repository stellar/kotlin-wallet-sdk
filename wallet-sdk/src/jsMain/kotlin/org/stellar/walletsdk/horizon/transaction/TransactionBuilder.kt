package org.stellar.walletsdk.horizon.transaction

import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Transaction

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
actual class TransactionBuilder(sourceAddress: String) :
  CommonTransactionBuilder<TransactionBuilder>(sourceAddress) {
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
    TODO("Not yet implemented")
  }

  /**
   * Create an account in the network.
   *
   * @param newAccount Key pair of an account to be created.
   * @param startingBalance optional Starting account balance in XLM. Minimum for non-sponsored
   * accounts is 1 XLM. Default value is 1.
   * @throws [InvalidStartingBalanceException] on invalid starting balance
   */
  actual fun createAccount(newAccount: AccountKeyPair, startingBalance: UInt): TransactionBuilder {
    TODO("Not yet implemented")
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
  actual fun transfer(
    destinationAddress: String,
    assetId: StellarAssetId,
    amount: String
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  actual fun addOperation(operation: Operation): TransactionBuilder {
    TODO("Not yet implemented")
  }

  /**
   * Creates transaction
   *
   * @return **unsigned** transaction
   */
  actual fun build(): Transaction {
    TODO("Not yet implemented")
  }
}

actual abstract class Operation
