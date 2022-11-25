package org.stellar.walletsdk.recovery

import okhttp3.OkHttpClient
import org.stellar.sdk.*
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.sdk.xdr.Signature
import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.Auth
import org.stellar.walletsdk.WalletSigner
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.*

class Recovery(
  private val server: Server,
  private val network: Network,
  private val maxBaseFeeInStroops: Int = 100
) {
  private val gson = GsonUtils.instance!!
  private val client = OkHttpClient()

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
   * @throws [NotAllSignaturesFetchedException] when all recovery servers don't return signatures
   */
  suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: String,
    recoveryServers: List<RecoveryServerAuth>,
    base64Decoder: Base64Decoder = defaultBase64Decoder
  ): Transaction {
    val signatures =
      recoveryServers.map {
        getRecoveryServerTxnSignature(transaction, accountAddress, it, base64Decoder)
      }

    signatures.forEach { transaction.addSignature(it) }

    return transaction
  }

  private suspend fun getRecoveryServerTxnSignature(
    transaction: Transaction,
    accountAddress: String,
    it: RecoveryServerAuth,
    base64Decoder: Base64Decoder
  ): DecoratedSignature {
    val requestUrl = "${it.endpoint}/accounts/$accountAddress/sign/${it.signerAddress}"
    val requestParams = TransactionRequest(transaction.toEnvelopeXdrBase64())
    val request = OkHttpUtils.buildJsonPostRequest(requestUrl, requestParams, it.authToken)

    return client.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      val authResponse: AuthSignature =
        gson.fromJson(response.body!!.charStream(), AuthSignature::class.java)

      createDecoratedSignature(accountAddress, authResponse.signature, base64Decoder)
    }
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
   * @throws [RecoveryException] when error happens working with recovery servers
   */
  // TODO: can be private?
  suspend fun enrollWithRecoveryServer(
    recoveryServers: List<RecoveryServer>,
    accountAddress: String,
    accountIdentity: List<RecoveryAccountIdentity>,
    walletSigner: WalletSigner
  ): List<String> {
    return recoveryServers.map {
      // TODO: pass auth token as an argument?
      val authToken =
        Auth(it.authEndpoint, it.homeDomain, walletSigner).authenticate(accountAddress)

      val requestUrl = "${it.homeDomain}/accounts/$accountAddress"
      val request =
        OkHttpUtils.buildJsonPostRequest(
          requestUrl,
          RecoveryIdentities(identities = accountIdentity),
          authToken
        )

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw NetworkRequestFailedException(response)

        val jsonResponse = gson.fromJson(response.body!!.charStream(), RecoveryAccount::class.java)

        getLatestRecoverySigner(jsonResponse.signers)
      }
    }
  }

  private fun getLatestRecoverySigner(signers: List<RecoveryAccountSigner>): String {
    if (signers.isEmpty()) {
      throw NoAccountSignersException
    }

    return signers[0].key
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
   * @param config: RecoverableWalletConfig
   *
   * @return transaction
   *
   * @throws [NetworkRequestFailedException] when request fails
   * @throws [RecoveryException] when error happens working with recovery servers
   * @throws [AccountNotFoundException] when account is not found
   */
  suspend fun createRecoverableWallet(config: RecoverableWalletConfig): Transaction {
    val recoverySigners =
      enrollWithRecoveryServer(
        config.recoveryServers,
        config.accountAddress,
        config.accountIdentity,
        config.accountWalletSigner
      )

    val signer =
      recoverySigners
        .map { rs -> AccountSigner(rs, config.signerWeight.recoveryServer) }
        .toMutableList()

    signer.add(AccountSigner(config.deviceAddress, config.signerWeight.master))

    return registerRecoveryServerSigners(
      config.accountAddress,
      signer,
      config.accountThreshold,
      config.sponsorAddress
    )
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
  // TODO: can be private?
  suspend fun registerRecoveryServerSigners(
    accountAddress: String,
    accountSigner: List<AccountSigner>,
    accountThreshold: AccountThreshold,
    sponsorAddress: String? = null
  ): Transaction {
    val transactionBuilder =
      createTransactionBuilder(
        sourceAddress = accountAddress,
        maxBaseFeeInStroops = maxBaseFeeInStroops,
        server = server,
        network = network,
      )

    val setOptionsOp = accountSigner.map { signer -> addSignerOperation(signer) }.toMutableList()
    setOptionsOp.add(setThresholdsOperation(accountThreshold))

    val operations: List<Operation> =
      if (sponsorAddress != null) {
        sponsorOperation(sponsorAddress, accountAddress, setOptionsOp)
      } else {
        setOptionsOp
      }

    transactionBuilder.addOperations(operations)

    return transactionBuilder.build()
  }
}

/**
 * Configuration for recoverable wallet
 *
 * @param accountAddress Stellar address of the account that is registering
 * @param deviceAddress Stellar address of the device that is added as a signer
 * @param accountThreshold Low, medium, and high thresholds to set on the account
 * @param accountIdentity A list of account identities to be registered with the recovery servers
 * @param recoveryServers A list of recovery servers to register with
 * @param accountWalletSigner [WalletSigner] interface to sign transaction with the account
 * @param signerWeight Signer weight to set
 * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
 */
data class RecoverableWalletConfig(
  val accountAddress: String,
  val deviceAddress: String,
  val accountThreshold: AccountThreshold,
  val accountIdentity: List<RecoveryAccountIdentity>,
  val recoveryServers: List<RecoveryServer>,
  val accountWalletSigner: WalletSigner,
  val signerWeight: SignerWeight,
  val sponsorAddress: String? = null
)

internal fun createDecoratedSignature(
  accountAddress: String,
  signatureBase64String: String,
  base64Decoder: Base64Decoder
): DecoratedSignature {
  val signature = Signature()

  signature.signature = base64Decoder(signatureBase64String)

  val decoratedSig = DecoratedSignature()
  decoratedSig.signature = signature
  decoratedSig.hint = KeyPair.fromAccountId(accountAddress).signatureHint

  return decoratedSig
}
