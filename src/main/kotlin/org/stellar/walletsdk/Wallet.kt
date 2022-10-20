package org.stellar.walletsdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.stellar.sdk.*
import org.stellar.walletsdk.util.*

/**
 * Wallet SDK main entry point. It provides methods to build wallet applications on the Stellar
 * network.
 *
 * [Learn about Stellar network fees](https://developers.stellar.org/docs/encyclopedia/fees-surge-pricing-fee-strategies)
 *
 * @property horizonUrl Horizon server URL
 * @property networkPassphrase Stellar network passphrase
 * @property maxBaseFeeInStroops maximum base fee in stroops
 */
class Wallet(
  private val horizonUrl: String = "https://horizon-testnet.stellar.org",
  private val networkPassphrase: String = Network.TESTNET.toString(),
  private val maxBaseFeeInStroops: Int = 100
) {
  private val server = Server(this.horizonUrl)
  private val network = Network(this.networkPassphrase)

  data class AccountKeypair(val publicKey: String, val secretKey: String)

  /**
   * Generate new account keypair (public and secret key).
   *
   * @return public key and secret key
   */
  fun create(): AccountKeypair {
    val keypair: KeyPair = KeyPair.random()
    return AccountKeypair(keypair.accountId, String(keypair.secretSeed))
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
      throw InvalidStartingBalanceException()
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
   * @param serverInstance optional Horizon server instance when default doesn't work
   *
   * @return `true` if submitted successfully
   *
   * @throws [TransactionSubmitFailedException] when submission failed
   */
  suspend fun submitTransaction(
    signedTransaction: Transaction,
    serverInstance: Server = server
  ): Boolean {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val response = serverInstance.submitTransaction(signedTransaction)

        if (response.isSuccess) {
          return@async true
        }

        var errorMessage = "Transaction failed"

        if (!response.extras?.resultCodes?.transactionResultCode.isNullOrBlank()) {
          errorMessage += ": ${response.extras.resultCodes.transactionResultCode}"
        }

        throw TransactionSubmitFailedException(response, errorMessage)
      }
      .await()
  }

  /**
   * Sign transaction with recovery servers. It is used to recover an account using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
   *
   * @param transaction Transaction with new signer to be signed by recovery servers
   * @param accountAddress Stellar address of the account that is recovered
   * @param recoveryServers List of recovery servers to use
   * @param base64Decoder optional base64Decoder. Default `java.util.Base64` decoder works with
   * Android API 23+. To support Android API older than API 23, custom base64Decoder needs to be
   * provided. For example, `android.util.Base64`.
   *
   * @return transaction with recovery server signatures
   *
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [RecoveryNotAllSignaturesFetchedException] when all recovery servers don't return
   * signatures
   */
  suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: String,
    recoveryServers: List<RecoveryServerAuth>,
    base64Decoder: ((String) -> ByteArray)? = null
  ): Transaction {
    val signatures =
      recoveryServers.map {
        CoroutineScope(Dispatchers.IO)
          .async {
            return@async getRecoveryServerTxnSignature(
              transaction = transaction,
              accountAddress = accountAddress,
              recoveryServer = it,
              base64Decoder = base64Decoder
            )
          }
          .await()
      }

    if (recoveryServers.size != signatures.size) {
      throw RecoveryNotAllSignaturesFetchedException()
    }

    signatures.forEach { transaction.addSignature(it) }

    return transaction
  }

  /**
   * Register account with recovery servers using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
   *
   * @param recoveryServers A list of recovery servers to register with
   * @param accountAddress Stellar address of the account that is registering
   * @param accountIdentity A list of account identities to be registered with the recovery servers
   * @param walletSigner [WalletSigner] interface to sign transaction with the account
   *
   * @return a list of recovery servers' signatures
   *
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [RecoveryNoAccountSignersOnServerException] if there are no signers on the recovery
   * server for this account
   * @throws [RecoveryNotRegisteredWithAllServersException] when all recovery servers were not
   * registered
   */
  suspend fun enrollWithRecoveryServer(
    recoveryServers: List<RecoveryServer>,
    accountAddress: String,
    accountIdentity: List<RecoveryAccountIdentity>,
    walletSigner: WalletSigner
  ): List<String> {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val signers =
          recoveryServers.map { rs ->
            setRecoveryMethods(
              endpoint = rs.endpoint,
              authEndpoint = rs.authEndpoint,
              homeDomain = rs.homeDomain,
              accountAddress = accountAddress,
              accountIdentity = accountIdentity,
              walletSigner = walletSigner
            )
          }

        if (recoveryServers.size != signers.size) {
          throw RecoveryNotRegisteredWithAllServersException()
        }

        return@async signers
      }
      .await()
  }

  /**
   * Add recovery servers and device account as new account signers, and set new threshold weights
   * on the account.
   *
   * This transaction can be sponsored.
   *
   * @param accountAddress Stellar address of the account that is registering
   * @param accountSigner A list of account signers and their weights
   * @param accountThreshold Low, medium, and high thresholds to set on the account
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [AccountNotFoundException] when account is not found
   */
  suspend fun registerRecoveryServerSigners(
    accountAddress: String,
    accountSigner: List<AccountSigner>,
    accountThreshold: AccountThreshold,
    sponsorAddress: String = ""
  ): Transaction {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val isSponsored = sponsorAddress.isNotBlank()
        val transactionBuilder =
          createTransactionBuilder(
            sourceAddress = accountAddress,
            maxBaseFeeInStroops = maxBaseFeeInStroops,
            server = server,
            network = network,
          )

        val setOptionsOp =
          listOf(
            *accountSigner.map { signer -> addSignerOperation(signer) }.toTypedArray(),
            setThresholdsOperation(
              low = accountThreshold.low,
              medium = accountThreshold.medium,
              high = accountThreshold.high
            )
          )

        val operations: List<Operation> =
          if (isSponsored) {
            sponsorOperation(sponsorAddress, accountAddress, setOptionsOp)
          } else {
            setOptionsOp
          }

        transactionBuilder.addOperations(operations)

        return@async transactionBuilder.build()
      }
      .await()
  }

  // TODO: create account helper to handle 409 Conflict > fetch account data from RS and return
  //  signers[0].key
  // TODO: handle update RS account info (PUT request)
  /**
   * Create new recoverable wallet using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md). It
   * registers the account with recovery servers, adds recovery servers and device account as new
   * account signers, and sets threshold weights on the account.
   *
   * This transaction can be sponsored.
   *
   * Uses [enrollWithRecoveryServer] and [registerRecoveryServerSigners] internally.
   *
   * @param accountAddress Stellar address of the account that is registering
   * @param deviceAddress Stellar address of the device that is added as a signer
   * @param accountThreshold Low, medium, and high thresholds to set on the account
   * @param accountIdentity A list of account identities to be registered with the recovery servers
   * @param recoveryServers A list of recovery servers to register with
   * @param accountWalletSigner [WalletSigner] interface to sign transaction with the account
   * @param signerWeight Signer weight to set
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   *
   * @return transaction
   *
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [RecoveryNoAccountSignersOnServerException] if there are no signers on the recovery
   * server for this account
   * @throws [RecoveryNotRegisteredWithAllServersException] when all recovery servers were not
   * registered
   * @throws [AccountNotFoundException] when account is not found
   */
  suspend fun createRecoverableWallet(
    accountAddress: String,
    deviceAddress: String,
    accountThreshold: AccountThreshold,
    accountIdentity: List<RecoveryAccountIdentity>,
    recoveryServers: List<RecoveryServer>,
    accountWalletSigner: WalletSigner,
    signerWeight: SignerWeight,
    sponsorAddress: String = ""
  ): Transaction {
    return CoroutineScope(Dispatchers.IO)
      .async {
        val recoverySigners =
          enrollWithRecoveryServer(
            recoveryServers = recoveryServers,
            accountAddress = accountAddress,
            accountIdentity = accountIdentity,
            walletSigner = accountWalletSigner
          )

        val recoveryServerSigners =
          recoverySigners
            .map { rs -> AccountSigner(address = rs, weight = signerWeight.recoveryServer) }
            .toTypedArray()

        val signer =
          listOf(
            *recoveryServerSigners,
            AccountSigner(address = deviceAddress, weight = signerWeight.master)
          )

        return@async registerRecoveryServerSigners(
          accountAddress = accountAddress,
          accountSigner = signer,
          accountThreshold = accountThreshold,
          sponsorAddress = sponsorAddress
        )
      }
      .await()
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
    return CoroutineScope(Dispatchers.IO)
      .async {
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

        return@async transactionBuilder.build()
      }
      .await()
  }
}
