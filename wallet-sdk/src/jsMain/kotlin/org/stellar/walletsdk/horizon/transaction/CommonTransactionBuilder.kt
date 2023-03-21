package org.stellar.walletsdk.horizon.transaction

import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.AccountKeyPair

actual abstract class CommonTransactionBuilder<T> actual constructor(sourceAddress: String) {
  /**
   * Add new signer to the account. Use caution when adding new signers, make sure you set the
   * correct signer weight. Otherwise, you might lock the account irreversibly.
   *
   * This transaction can be sponsored.
   *
   * @param signerAddress Stellar address of the signer that is added
   * @param signerWeight Signer weight
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun addAccountSigner(signerAddress: AccountKeyPair, signerWeight: Int): T {
    TODO("Not yet implemented")
  }

  /**
   * Remove signer from the account.
   *
   * @param signerAddress Stellar address of the signer that is removed
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun removeAccountSigner(signerAddress: AccountKeyPair): T {
    TODO("Not yet implemented")
  }

  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun lockAccountMasterKey(): T {
    TODO("Not yet implemented")
  }

  /**
   * Add an asset (trustline) to the account.
   *
   * @param asset Target asset
   * @param trustLimit optional The limit of the trustline. Default value is maximum supported.
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun addAssetSupport(asset: IssuedAssetId, trustLimit: String): T {
    TODO("Not yet implemented")
  }

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param asset Target asset
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  actual fun removeAssetSupport(asset: IssuedAssetId): T {
    TODO("Not yet implemented")
  }

  actual fun setThreshold(low: Int, medium: Int, high: Int): T {
    TODO("Not yet implemented")
  }
}

internal actual val defaultTrustLimit: String
  get() = TODO("Not yet implemented")
