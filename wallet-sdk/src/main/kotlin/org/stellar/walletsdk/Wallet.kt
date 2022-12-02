package org.stellar.walletsdk

import org.stellar.sdk.*
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.accountByAddress
import org.stellar.walletsdk.extension.accountOperations
import org.stellar.walletsdk.extension.buildTransaction
import org.stellar.walletsdk.extension.createTransactionBuilder
import org.stellar.walletsdk.recovery.Recovery
import org.stellar.walletsdk.util.*

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 *
 * [Learn about Stellar network fees](https://developers.stellar.org/docs/encyclopedia/fees-surge-pricing-fee-strategies)
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 * @property maxBaseFeeInStroops maximum base fee in stroops
 */
class Wallet(
  private val server: Server,
  private val network: Network,
  private val maxBaseFeeInStroops: Int = 100
) {
  /**
   * Generates new recovery service that share this wallet's configuration.
   * @return recovery service
   */
  fun recovery(): Recovery {
    return Recovery(server, network, maxBaseFeeInStroops)
  }

  /**
   * Generate new account keypair (public and secret key).
   *
   * @return public key and secret key
   */
  fun create(): AccountKeypair {
    return AccountKeypair(KeyPair.random())
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
   * @throws [AccountNotFoundException] when source account is not found
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

    return buildTransaction(sourceAddress, maxBaseFeeInStroops, server, network, operations)
  }

  /**
   * Add an asset (trustline) to the account. This transaction can be sponsored.
   *
   * @param sourceAddress Stellar address of the account that is opting-in for the asset
   * @param assetCode Identifying code of the asset
   * @param assetIssuer Stellar address of the asset issuer
   * @param trustLimit optional The limit of the trustline. Default value is maximum supported.
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [AccountNotFoundException] when source account is not found
   */
  suspend fun addAssetSupport(
    sourceAddress: String,
    assetCode: String,
    assetIssuer: String,
    trustLimit: String = Long.MAX_VALUE.toBigDecimal().movePointLeft(7).toPlainString(),
    sponsorAddress: String = ""
  ): Transaction {
    val isSponsored = sponsorAddress.isNotBlank()

    val asset = ChangeTrustAsset.createNonNativeAsset(assetCode, assetIssuer)
    val changeTrustOp: ChangeTrustOperation =
      ChangeTrustOperation.Builder(asset, trustLimit).setSourceAccount(sourceAddress).build()

    val operations: List<Operation> =
      if (isSponsored) {
        sponsorOperation(sponsorAddress, sourceAddress, listOf(changeTrustOp))
      } else {
        listOfNotNull(changeTrustOp)
      }

    return buildTransaction(sourceAddress, maxBaseFeeInStroops, server, network, operations)
  }

  /**
   * Remove an asset (trustline) from the account.
   *
   * @param sourceAddress Stellar address of the account that is opting-out of the asset
   * @param assetCode Identifying code of the asset
   * @param assetIssuer Stellar address of the asset issuer
   *
   * @return transaction
   *
   * @throws [AccountNotFoundException] when source account is not found
   */
  suspend fun removeAssetSupport(
    sourceAddress: String,
    assetCode: String,
    assetIssuer: String
  ): Transaction {
    return addAssetSupport(sourceAddress, assetCode, assetIssuer, "0")
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
   * @throws [AccountNotFoundException] when source account is not found
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

    return buildTransaction(sourceAddress, maxBaseFeeInStroops, server, network, operations)
  }

  /**
   * Creates transaction transferring asset, using Stellar's [payment operation]
   * (https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment) to
   * move asset between accounts.
   *
   * @param sourceAddress Stellar address of account making a transfer
   * @param destinationAddress Stellar address of account receiving a transfer
   * @param assetIssuer issuer of asset to transfer
   * @param assetCode code of asset to transfer
   * @param amount amount of asset to transfer
   * @param memo optional memo
   *
   * @return formed transfer transaction
   */
  private suspend fun transfer(
    sourceAddress: String,
    destinationAddress: String,
    assetIssuer: String,
    assetCode: String,
    amount: String,
    memo: Pair<MemoType, String>?
  ): Transaction {
    val transactionBuilder =
      createTransactionBuilder(sourceAddress, maxBaseFeeInStroops, server, network)

    val asset = Asset.create(null, assetCode, assetIssuer)

    val payment = PaymentOperation.Builder(destinationAddress, asset, amount).build()

    memo?.also { transactionBuilder.addMemo(it.first.mapper(it.second)) }

    transactionBuilder.addOperation(payment)

    return transactionBuilder.build()
  }

  /**
   * Creates transaction transferring asset, using Stellar's [payment operation]
   * (https://developers.stellar.org/docs/fundamentals-and-concepts/list-of-operations#payment) to
   * move asset between accounts.
   *
   * @param transaction anchor withdrawal transaction
   * @param assetIssuer issuer of asset to transfer
   * @param assetCode code of asset to transfer
   *
   * @return formed transfer transaction
   */
  suspend fun transfer(
    transaction: AnchorTransaction,
    assetIssuer: String,
    assetCode: String,
  ): Transaction {
    return transfer(
      transaction.from,
      transaction.withdraw_anchor_account,
      assetIssuer,
      assetCode,
      transaction.amount_in,
      transaction.withdraw_memo_type to transaction.withdraw_memo
    )
  }

  /**
   * Remove signer from the account.
   *
   * @param sourceAddress Stellar address of the account that is removing the signer
   * @param signerAddress Stellar address of the signer that is removed
   *
   * @return transaction
   *
   * @throws [AccountNotFoundException] when source account is not found
   */
  suspend fun removeAccountSigner(sourceAddress: String, signerAddress: String): Transaction {
    return addAccountSigner(sourceAddress, signerAddress, 0)
  }

  /**
   * Submit transaction to the Stellar network.
   *
   * @param signedTransaction Signed transaction that is submitted
   *
   * @return `true` if submitted successfully
   *
   * @throws [TransactionSubmitFailedException] when submission failed
   */
  suspend fun submitTransaction(
    signedTransaction: Transaction,
  ): Boolean {
    val response = server.submitTransaction(signedTransaction)

    if (response.isSuccess) {
      return true
    }

    throw TransactionSubmitFailedException(response)
  }

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
   * @throws [AccountNotFoundException] when account is not found
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

    return transactionBuilder.build()
  }

  /**
   * Get account information from the Stellar network.
   *
   * @param accountAddress Stellar address of the account
   * @param serverInstance optional Horizon server instance when default doesn't work
   *
   * @return formatted account information
   *
   * @throws [AccountNotFoundException] when account is not found
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun getInfo(accountAddress: String, serverInstance: Server = server): AccountInfo {
    val account = serverInstance.accountByAddress(accountAddress)
    val balances = formatAccountBalances(account, serverInstance)

    // TODO: add accountDetails

    return AccountInfo(
      publicKey = account.accountId,
      assets = balances.assets,
      liquidityPools = balances.liquidityPools,
      reservedNativeBalance = account.reservedBalance()
    )
  }

  /**
   * Get account operations for the specified Stellar address.
   *
   * @param accountAddress Stellar address of the account
   * @param limit optional how many operations to fetch, maximum is 200, default is 10
   * @param order optional data order, ascending or descending, defaults to descending
   * @param cursor optional cursor to specify a starting point
   * @param includeFailed optional flag to include failed operations, defaults to false
   *
   * @return a list of formatted operations
   *
   * @throws [OperationsLimitExceededException] when maximum limit of 200 is exceeded
   * @throws [AccountNotFoundException] when account is not found
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun getHistory(
    accountAddress: String,
    limit: Int? = null,
    order: Order? = Order.DESC,
    cursor: String? = null,
    includeFailed: Boolean? = null
  ): List<WalletOperation<OperationResponse>> {
    return server.accountOperations(accountAddress, limit, order, cursor, includeFailed).map {
      formatStellarOperation(accountAddress, it)
    }
  }
}
