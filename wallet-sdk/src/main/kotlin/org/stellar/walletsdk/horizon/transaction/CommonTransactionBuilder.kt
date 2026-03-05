package org.stellar.walletsdk.horizon.transaction

import java.math.BigDecimal
import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.sdk.operations.*
import org.stellar.walletsdk.DECIMAL_POINT_PRECISION
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.exception.HorizonRequestFailedException
import org.stellar.walletsdk.horizon.AccountKeyPair

private val log = KotlinLogging.logger {}

abstract class CommonTransactionBuilder<T>(protected val sourceAddress: String) {
  abstract val operations: MutableList<Operation>

  @Suppress("UNCHECKED_CAST")
  internal inline fun building(body: () -> Operation): T {
    operations.add(body())
    return this as T
  }

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
  fun addAccountSigner(signerAddress: AccountKeyPair, signerWeight: Int) = building {
    log.debug {
      "${if (signerWeight == 0) "Remove" else "Add"} account signer txn: sourceAddress = " +
        "$sourceAddress, signerAddress = $signerAddress, signerWeight = $signerWeight"
    }

    val signerKey = SignerKey.fromEd25519PublicKey(signerAddress.keyPair.accountId)

    SetOptionsOperation.builder()
      .sourceAccount(sourceAddress)
      .signer(signerKey)
      .signerWeight(signerWeight)
      .build()
  }

  /**
   * Remove signer from the account.
   *
   * @param signerAddress Stellar address of the signer that is removed
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAccountSigner(signerAddress: AccountKeyPair): T {
    require(signerAddress.address != sourceAddress) {
      "This method can't be used to remove master signer key, " +
        "call ${this::lockAccountMasterKey.name} method instead"
    }

    return addAccountSigner(signerAddress, 0)
  }

  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun lockAccountMasterKey() = building {
    log.debug { "Lock master key tx: accountAddress = $sourceAddress" }

    SetOptionsOperation.builder().sourceAccount(sourceAddress).masterKeyWeight(0).build()
  }

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
    trustLimit: String =
      Long.MAX_VALUE.toBigDecimal().movePointLeft(DECIMAL_POINT_PRECISION).toPlainString(),
  ) = building {
    log.debug {
      "${if (trustLimit == "0") "Remove" else "Add"} asset txn: sourceAddress = $sourceAddress, " +
        "asset=$asset, trustLimit = $trustLimit"
    }

    val stellarAsset = ChangeTrustAsset(Asset.createNonNativeAsset(asset.code, asset.issuer))

    ChangeTrustOperation.builder()
      .asset(stellarAsset)
      .limit(BigDecimal(trustLimit))
      .sourceAccount(sourceAddress)
      .build()
  }

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param asset Target asset
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAssetSupport(asset: IssuedAssetId): T {
    return addAssetSupport(asset, "0")
  }

  fun setThreshold(low: Int, medium: Int, high: Int) = building {
    SetOptionsOperation.builder()
      .sourceAccount(sourceAddress)
      .lowThreshold(low)
      .mediumThreshold(medium)
      .highThreshold(high)
      .build()
  }

  protected fun doCreateAccount(
    newAccount: AccountKeyPair,
    startingBalance: ULong,
    sourceAddress: String
  ): CreateAccountOperation {
    log.debug {
      "Fund tx: sourceAddress = $sourceAddress, destinationAddress = ${newAccount.address}, " +
        "startBalance = $startingBalance"
    }

    return CreateAccountOperation.builder()
      .destination(newAccount.address)
      .startingBalance(BigDecimal(startingBalance.toString()))
      .sourceAccount(sourceAddress)
      .build()
  }
}
