package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.sdk.TransactionBuilder as SdkBuilder
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.anchor.TransactionStatus
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.asset.toAsset
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.*
import org.stellar.walletsdk.util.*

private val log = KotlinLogging.logger {}

/** Class that allows to construct Stellar transactions, containing one or more operations */
@Suppress("TooManyFunctions")
class TransactionBuilder
internal constructor(
  private val cfg: Config,
  private val sourceAccount: AccountResponse,
  memo: Pair<MemoType, String>?,
  private val defaultSponsorAddress: String?
) {
  private val sourceAddress = sourceAccount.accountId
  private val network: Network = cfg.stellar.network
  private val maxBaseFeeInStroops: Int = cfg.stellar.baseFee.toInt()
  // TODO: make timeout configurable
  private val builder =
    SdkBuilder(sourceAccount, network).setBaseFee(maxBaseFeeInStroops).setTimeout(180)

  init {
    memo?.also { builder.addMemo(it.first.mapper(it.second, cfg.app)) }
  }

  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun lockAccountMasterKey(sponsorAddress: String? = null) =
    building(sponsorAddress) {
      log.debug { "Lock master key tx: accountAddress = $sourceAccount" }

      SetOptionsOperation.Builder().setMasterKeyWeight(0).build()
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
  fun fund(
    destinationAddress: String,
    startingBalance: String = "1",
    sponsorAddress: String? = null
  ) =
    building(sponsorAddress) {
      val isSponsored = getSponsor(sponsorAddress) != null

      if (!isSponsored && startingBalance.toInt() < 1 || startingBalance.toInt() < 0) {
        throw InvalidStartingBalanceException
      }

      log.debug {
        "Fund tx: sourceAddress = $sourceAddress, destinationAddress = $destinationAddress, " +
          "startBalance = $startingBalance"
      }

      CreateAccountOperation.Builder(destinationAddress, startingBalance)
        .setSourceAccount(sourceAddress)
        .build()
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
  fun addAssetSupport(
    asset: IssuedAssetId,
    trustLimit: String =
      Long.MAX_VALUE.toBigDecimal().movePointLeft(DECIMAL_POINT_PRECISION).toPlainString(),
    sponsorAddress: String? = null
  ) =
    building(sponsorAddress) {
      log.debug {
        "${if (trustLimit == "0") "Remove" else "Add"} asset txn: sourceAddress = $sourceAddress, " +
          "asset=$asset, trustLimit = $trustLimit, sponsorAddress = $sponsorAddress"
      }

      val stellarAsset = ChangeTrustAsset.createNonNativeAsset(asset.code, asset.issuer)

      ChangeTrustOperation.Builder(stellarAsset, trustLimit).setSourceAccount(sourceAddress).build()
    }

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param asset Target asset
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  fun removeAssetSupport(asset: IssuedAssetId): TransactionBuilder {
    return addAssetSupport(asset, "0")
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
  fun addAccountSigner(signerAddress: String, signerWeight: Int, sponsorAddress: String? = null) =
    building(sponsorAddress) {
      log.debug {
        "${if (signerWeight == 0) "Remove" else "Add"} account signer txn: sourceAddress = " +
          "$sourceAddress, signerAddress = $signerAddress, signerWeight = $signerWeight, " +
          "sponsorAddress = $sponsorAddress"
      }

      val keyPair = KeyPair.fromAccountId(signerAddress)
      val signer = Signer.ed25519PublicKey(keyPair)

      SetOptionsOperation.Builder()
        .setSourceAccount(sourceAddress)
        .setSigner(signer, signerWeight)
        .build()
    }

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
  ): TransactionBuilder {
    require(signerAddress != sourceAddress) {
      "This method can't be used to remove master signer key, call ${this::lockAccountMasterKey.name} method instead"
    }

    return addAccountSigner(signerAddress, 0, sponsorAddress)
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
  fun transfer(
    destinationAddress: String,
    assetId: StellarAssetId,
    amount: String,
    sponsorAddress: String? = null
  ) =
    building(sponsorAddress) {
      PaymentOperation.Builder(destinationAddress, assetId.toAsset(), amount).build()
    }

  fun setThreshold(low: Int, medium: Int, high: Int, sponsorAddress: String? = null) =
    building(sponsorAddress) {
      SetOptionsOperation.Builder()
        .setLowThreshold(low)
        .setMediumThreshold(medium)
        .setHighThreshold(high)
        .build()
    }

  /**
   * Creates transaction
   *
   * @return **unsigned** transaction
   */
  fun build(): Transaction {
    return builder.build()
  }

  private fun getSponsor(sponsorAddress: String?): String? {
    return sponsorAddress ?: defaultSponsorAddress
  }

  private fun List<Operation>.sponsor(sponsorAddress: String?): List<Operation> {
    val sponsor = getSponsor(sponsorAddress)

    if (sponsor != null) {
      return sponsorOperation(sponsor, sourceAccount.accountId, this)
    }

    return this
  }

  private fun Operation.sponsor(sponsorAddress: String?): List<Operation> {
    return listOf(this).sponsor(sponsorAddress)
  }

  private inline fun building(sponsorAddress: String?, body: () -> Operation): TransactionBuilder {
    builder.addOperations(body().sponsor(sponsorAddress))
    return this
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
