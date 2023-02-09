package org.stellar.walletsdk.horizon

import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.StellarAssetId

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
actual class TransactionBuilder {
  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun lockAccountMasterKey(sponsorAddress: String?): TransactionBuilder {
    TODO("Not yet implemented")
  }

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
  actual fun fund(
    destinationAddress: String,
    startingBalance: String,
    sponsorAddress: String?
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  /**
   * Add an asset (trustline) to the account. This transaction can be sponsored.
   *
   * @param asset Target asset
   * @param trustLimit optional The limit of the trustline. Default value is maximum supported.
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun addAssetSupport(
    asset: IssuedAssetId,
    trustLimit: String,
    sponsorAddress: String?
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param asset Target asset
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun removeAssetSupport(asset: IssuedAssetId): TransactionBuilder {
    TODO("Not yet implemented")
  }

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
  actual fun addAccountSigner(
    signerAddress: String,
    signerWeight: Int,
    sponsorAddress: String?
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  /**
   * Remove signer from the account.
   *
   * @param signerAddress Stellar address of the signer that is removed
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun removeAccountSigner(
    signerAddress: String,
    sponsorAddress: String?
  ): TransactionBuilder {
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
    amount: String,
    sponsorAddress: String?
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  actual fun setThreshold(
    low: Int,
    medium: Int,
    high: Int,
    sponsorAddress: String?
  ): TransactionBuilder {
    TODO("Not yet implemented")
  }

  actual fun build(): Transaction {
    TODO("Not yet implemented")
  }
}

internal actual val defaultTrustLimit: String
  get() = TODO("Not yet implemented")
