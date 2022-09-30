package org.stellar.walletsdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.stellar.sdk.*
import org.stellar.walletsdk.util.*

class Wallet(
  private val horizonUrl: String = "https://horizon-testnet.stellar.org",
  private val networkPassphrase: String = Network.TESTNET.toString(),
  private val maxBaseFeeInStroops: Int = 100
) {
  private val server = Server(this.horizonUrl)
  private val network = Network(this.networkPassphrase)

  data class AccountKeypair(val publicKey: String, val secretKey: String)

  // Create account keys, generate new keypair
  fun create(): AccountKeypair {
    val keypair: KeyPair = KeyPair.random()
    return AccountKeypair(keypair.accountId, String(keypair.secretSeed))
  }

  // Fund (activate) account
  // TODO: ??? do we need to include add trustline operation here?
  suspend fun fund(
    sourceAddress: String,
    destinationAddress: String,
    startingBalance: String = "1",
    sponsorAddress: String = ""
  ): Transaction {
    val isSponsored = sponsorAddress.isNotBlank()

    if (!isSponsored && startingBalance.toInt() < 1) {
      throw Exception("Starting balance must be at least 1 XLM for non-sponsored accounts")
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

  // Add trustline
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

  // Remove trustline
  suspend fun removeAssetSupport(
    sourceAddress: String,
    assetCode: String,
    assetIssuer: String
  ): Transaction {
    return addAssetSupport(sourceAddress, assetCode, assetIssuer, "0")
  }

  // Add signer
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

  // Remove signer
  suspend fun removeAccountSigner(sourceAddress: String, signerAddress: String): Transaction {
    return addAccountSigner(sourceAddress, signerAddress, 0)
  }

  // Submit transaction
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

        throw Exception(errorMessage)
      }
      .await()
  }

  //  Sign transaction with recovery servers
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
            return@async getRecoveryServerTxnSignatures(
              transaction = transaction,
              accountAddress = accountAddress,
              recoveryServer = it,
              base64Decoder = base64Decoder
            )
          }
          .await()
      }

    if (recoveryServers.size != signatures.size) {
      throw Exception("Didn't get all recovery server signatures")
    }

    signatures.forEach { transaction.addSignature(it) }

    return transaction
  }

  // TODO: create account helper to handle 409 Conflict > fetch account data from RS and return
  //  signers[0].key
  // TODO: handle update RS account info (PUT request)

  //  Add new account signers and threshold weights to account, register with recovery servers
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

  //  Register account with recovery server
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
          throw Exception("Could not register with all recovery servers")
        }

        return@async signers
      }
      .await()
  }

  //  Add signers to account and set threshold weights
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

  //  Lock account master key (set weight to 0)
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
