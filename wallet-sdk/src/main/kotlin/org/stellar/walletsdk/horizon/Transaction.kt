package org.stellar.walletsdk.horizon

import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.anchor.WithdrawalTransaction
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.asset.toAsset
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.*
import org.stellar.walletsdk.util.*

private val log = KotlinLogging.logger {}

class TransactionBuilder
internal constructor(
  private val cfg: Config,
) {
  private val server: Server = cfg.stellar.server
  private val network: Network = cfg.stellar.network
  private val maxBaseFeeInStroops: Int = cfg.stellar.maxBaseFeeStroops.toInt()

  /**
   * Lock the master key of the account (set its weight to 0). Use caution when locking account's
   * master key. Make sure you have set the correct signers and weights. Otherwise, you might lock
   * the account irreversibly.
   *
   * @param accountAddress Stellar address of the account whose master key to lock
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun lockAccountMasterKey(
    accountAddress: String,
    sponsorAddress: String = ""
  ): Transaction {
    val isSponsored = sponsorAddress.isNotBlank()
    val transactionBuilder =
      createTransactionBuilder(
        sourceAddress = accountAddress,
        maxBaseFeeInStroops = maxBaseFeeInStroops,
        server = server,
        network = network
      )

    val lockOp = listOf(lockMasterKeyOperation())

    val operations: List<Operation> =
      if (isSponsored) {
        sponsorOperation(sponsorAddress, accountAddress, lockOp)
      } else {
        lockOp
      }

    transactionBuilder.addOperations(operations)

    log.debug {
      "Lock master key txn: accountAddress = $accountAddress, sponsorAddress = $sponsorAddress"
    }

    return transactionBuilder.build()
  }

  /**
   * Fund an account to activate it. This transaction can be sponsored.
   *
   * @param sourceAddress Stellar address of the account that is funding the account
   * @param destinationAddress Stellar address of the account that is being funded
   * @param startingBalance optional Starting account balance in XLM. Minimum for non-sponsored
   * accounts is 1 XLM, sponsored accounts can leave it at 0 XLM. Default value is 1.
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [InvalidStartingBalanceException] when starting balance is less than 1 XLM for
   * non-sponsored account
   */
  suspend fun fund(
    sourceAddress: String,
    destinationAddress: String,
    startingBalance: String = "1",
    sponsorAddress: String = ""
  ): Transaction {
    val isSponsored = sponsorAddress.isNotBlank()

    if (!isSponsored && startingBalance.toInt() < 1) {
      throw InvalidStartingBalanceException
    }

    val startBalance = if (isSponsored) "0" else startingBalance

    val createAccountOp: CreateAccountOperation =
      CreateAccountOperation.Builder(destinationAddress, startBalance)
        .setSourceAccount(sourceAddress)
        .build()

    val operations: List<Operation> =
      if (isSponsored) {
        sponsorOperation(sponsorAddress, destinationAddress, listOf(createAccountOp))
      } else {
        listOfNotNull(createAccountOp)
      }

    log.debug {
      "Fund txn: sourceAddress = $sourceAddress, destinationAddress = $destinationAddress, " +
        "startBalance = $startBalance, sponsorAddress = $sponsorAddress"
    }

    return buildTransaction(sourceAddress, maxBaseFeeInStroops, server, network, operations)
  }

  /**
   * Add an asset (trustline) to the account. This transaction can be sponsored.
   *
   * @param sourceAddress Stellar address of the account that is opting-in for the asset
   * @param asset Target asset
   * @param trustLimit optional The limit of the trustline. Default value is maximum supported.
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun addAssetSupport(
    sourceAddress: String,
    asset: IssuedAssetId,
    trustLimit: String = Long.MAX_VALUE.toBigDecimal().movePointLeft(7).toPlainString(),
    sponsorAddress: String = ""
  ): Transaction {
    val isSponsored = sponsorAddress.isNotBlank()

    val stellarAsset = ChangeTrustAsset.createNonNativeAsset(asset.code, asset.issuer)
    val changeTrustOp: ChangeTrustOperation =
      ChangeTrustOperation.Builder(stellarAsset, trustLimit).setSourceAccount(sourceAddress).build()

    val operations: List<Operation> =
      if (isSponsored) {
        sponsorOperation(sponsorAddress, sourceAddress, listOf(changeTrustOp))
      } else {
        listOfNotNull(changeTrustOp)
      }

    log.debug {
      "${if (trustLimit == "0") "Remove" else "Add"} asset txn: sourceAddress = $sourceAddress, asset=$asset, trustLimit = $trustLimit, sponsorAddress = $sponsorAddress"
    }

    return buildTransaction(sourceAddress, maxBaseFeeInStroops, server, network, operations)
  }

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param sourceAddress Stellar address of the account that is opting-out of the asset
   * @param assetId Target asset
   *
   * @return transaction
   *
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun removeAssetSupport(sourceAddress: String, assetId: IssuedAssetId): Transaction {
    return addAssetSupport(sourceAddress, assetId, "0")
  }

  /**
   * Add new signer to the account. Use caution when adding new signers, make sure you set the
   * correct signer weight. Otherwise, you might lock the account irreversibly.
   *
   * This transaction can be sponsored.
   *
   * @param sourceAddress Stellar address of the account that is adding the signer
   * @param signerAddress Stellar address of the signer that is added
   * @param signerWeight Signer weight
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun addAccountSigner(
    sourceAddress: String,
    signerAddress: String,
    signerWeight: Int,
    sponsorAddress: String = ""
  ): Transaction {
    val isSponsored = sponsorAddress.isNotBlank()
    val keyPair = KeyPair.fromAccountId(signerAddress)
    val signer = Signer.ed25519PublicKey(keyPair)

    val addSignerOp: SetOptionsOperation =
      SetOptionsOperation.Builder().setSigner(signer, signerWeight).build()

    val operations: List<Operation> =
      if (isSponsored) {
        sponsorOperation(sponsorAddress, sourceAddress, listOf(addSignerOp))
      } else {
        listOfNotNull(addSignerOp)
      }

    log.debug {
      "${if (signerWeight == 0) "Remove" else "Add"} account signer txn: sourceAddress = $sourceAddress, signerAddress = " +
        "$signerAddress, signerWeight = $signerWeight, sponsorAddress = $sponsorAddress"
    }

    return buildTransaction(sourceAddress, maxBaseFeeInStroops, server, network, operations)
  }

  /**
   * Remove signer from the account.
   *
   * @param sourceAddress Stellar address of the account that is removing the signer
   * @param signerAddress Stellar address of the signer that is removed
   *
   * @return transaction
   *
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun removeAccountSigner(sourceAddress: String, signerAddress: String): Transaction {
    return addAccountSigner(sourceAddress, signerAddress, 0)
  }

  /**
   * Creates transaction transferring asset, using Stellar's [payment operation]
   * (https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment) to
   * move asset between accounts.
   *
   * @param sourceAddress Stellar address of account making a transfer
   * @param destinationAddress Stellar address of account receiving a transfer
   * @param assetId: Target asset id
   * @param amount amount of asset to transfer
   * @param memo optional memo
   *
   * @return formed transfer transaction
   */
  private suspend fun transfer(
    sourceAddress: String,
    destinationAddress: String,
    assetId: StellarAssetId,
    amount: String,
    memo: Pair<MemoType, String>?
  ): Transaction {
    val transactionBuilder =
      createTransactionBuilder(sourceAddress, maxBaseFeeInStroops, server, network)

    val payment = PaymentOperation.Builder(destinationAddress, assetId.toAsset(), amount).build()

    memo?.also { transactionBuilder.addMemo(it.first.mapper(it.second, cfg.app)) }

    transactionBuilder.addOperation(payment)

    return transactionBuilder.build()
  }

  /**
   * Creates transaction transferring asset, using Stellar's
   * [payment operation](https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment)
   * to move asset between accounts.
   *
   * @param transaction anchor withdrawal transaction
   * @param assetIssuer issuer of asset to transfer
   * @param assetCode code of asset to transfer
   *
   * @return formed transfer transaction
   * @throws IncorrectTransactionStatusException if transaction can't be sent due to having
   * incorrect state
   */
  suspend fun transfer(
    transaction: WithdrawalTransaction,
    assetId: StellarAssetId,
  ): Transaction {
    transaction.requireStatus("pending_user_transfer_start")

    return transfer(
      transaction.from,
      transaction.withdrawAnchorAccount,
      assetId,
      transaction.amountIn,
      transaction.withdrawalMemo.let { transaction.withdrawalMemoType to it }
    )
  }
}
