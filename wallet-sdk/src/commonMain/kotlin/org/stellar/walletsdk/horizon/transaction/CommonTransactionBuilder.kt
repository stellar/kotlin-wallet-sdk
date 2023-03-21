@file:JvmName("CommonTransactionBuilderJvm")

package org.stellar.walletsdk.horizon.transaction

import kotlin.jvm.JvmName
import mu.KotlinLogging
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.AccountKeyPair

private val log = KotlinLogging.logger {}

expect abstract class CommonTransactionBuilder<T>(sourceAddress: String) {

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
  fun addAccountSigner(signerAddress: AccountKeyPair, signerWeight: Int): T

  /**
   * Remove signer from the account.
   *
   * @param signerAddress Stellar address of the signer that is removed
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAccountSigner(signerAddress: AccountKeyPair): T

  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun lockAccountMasterKey(): T

  /**
   * Add an asset (trustline) to the account.
   *
   * @param asset Target asset
   * @param trustLimit optional The limit of the trustline. Default value is maximum supported.
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun addAssetSupport(
    asset: IssuedAssetId,
    trustLimit: String = defaultTrustLimit,
  ): T

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param asset Target asset
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAssetSupport(asset: IssuedAssetId): T

  fun setThreshold(low: Int, medium: Int, high: Int): T
}

internal expect val defaultTrustLimit: String
