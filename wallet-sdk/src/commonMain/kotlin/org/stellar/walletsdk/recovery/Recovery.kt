package org.stellar.walletsdk.recovery

import mu.KotlinLogging
import okhttp3.OkHttpClient
import org.stellar.sdk.*
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.sdk.xdr.Signature
import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.util.*

private val log = KotlinLogging.logger {}

class Recovery
internal constructor(
  private val cfg: Config,
  private val stellar: Stellar,
  private val client: OkHttpClient
) {
  /**
   * Sign transaction with recovery servers. It is used to recover an account using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
   *
   * @param transaction Transaction with new signer to be signed by recovery servers
   * @param accountAddress Stellar address of the account that is recovered
   * @param recoveryServers List of recovery servers to use
   * @return transaction with recovery server signatures
   * @throws [ServerRequestFailedException] when request fails
   * @throws [NotAllSignaturesFetchedException] when all recovery servers don't return signatures
   */
  suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: String,
    recoveryServers: List<RecoveryServerAuth>
  ): Transaction {
    val signatures =
      recoveryServers.map { getRecoveryServerTxnSignature(transaction, accountAddress, it) }

    signatures.forEach { transaction.addSignature(it) }

    return transaction
  }

  private suspend fun getRecoveryServerTxnSignature(
    transaction: Transaction,
    accountAddress: String,
    it: RecoveryServerAuth
  ): DecoratedSignature {
    val requestUrl = "${it.endpoint}/accounts/$accountAddress/sign/${it.signerAddress}"
    val requestParams = TransactionRequest(transaction.toEnvelopeXdrBase64())
    val request = OkHttpUtils.makePostRequest(requestUrl, requestParams, it.authToken)

    log.debug {
      "Recovery server signature request: accountAddress = $accountAddress, " +
        "signerAddress = ${it.signerAddress}, authToken = ${it.authToken.prettify()}..."
    }

    return client.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      val authResponse: AuthSignature = response.toJson()

      createDecoratedSignature(it.signerAddress, cfg.app.base64Decoder(authResponse.signature))
    }
  }

  /**
   * Register account with recovery servers using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
   *
   * @param recoveryServers A list of recovery servers to register with
   * @param accountIdentity A list of account identities to be registered with the recovery servers
   * @return a list of recovery servers' signatures
   * @throws [ServerRequestFailedException] when request fails
   * @throws [RecoveryException] when error happens working with recovery servers
   */
  // TODO: can be private?
  suspend fun enrollWithRecoveryServer(
    recoveryServers: List<RecoveryServer>,
    account: AccountKeyPair,
    accountIdentity: List<RecoveryAccountIdentity>
  ): List<String> {
    return recoveryServers.map {
      // TODO: pass auth token as an argument?
      val authToken =
        Auth(cfg, it.authEndpoint, it.homeDomain, client).authenticate(account, it.walletSigner)

      val requestUrl = "${it.endpoint}/accounts/${account.address}"
      val request =
        OkHttpUtils.makePostRequest(
          requestUrl,
          RecoveryIdentities(identities = accountIdentity),
          authToken
        )

      log.debug {
        "Recovery server enroll request: accountAddress = ${account.address}, homeDomain =" +
          " ${it.homeDomain}, authToken = ${authToken.prettify()}..."
      }

      client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw ServerRequestFailedException(response)

        val jsonResponse = response.toJson<RecoveryAccount>()

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
   * @return transaction
   * @throws [ServerRequestFailedException] when request fails
   * @throws [RecoveryException] when error happens working with recovery servers
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun createRecoverableWallet(config: RecoverableWalletConfig): Transaction {
    val recoverySigners =
      enrollWithRecoveryServer(
        config.recoveryServers,
        config.accountAddress,
        config.accountIdentity,
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
   * @param accountSigner A list of account signers and their weights
   * @param accountThreshold Low, medium, and high thresholds to set on the account
   * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
   * @return transaction
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  // TODO: can be private?
  suspend fun registerRecoveryServerSigners(
    account: AccountKeyPair,
    accountSigner: List<AccountSigner>,
    accountThreshold: AccountThreshold,
    sponsorAddress: String? = null
  ): Transaction {
    val builder = stellar.transaction(account, defaultSponsorAddress = sponsorAddress)

    accountSigner.forEach { builder.addAccountSigner(it.address, it.weight) }
    builder.setThreshold(accountThreshold.low, accountThreshold.medium, accountThreshold.high)

    return builder.build()
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
 * @param signerWeight Signer weight to set
 * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
 */
data class RecoverableWalletConfig(
  val accountAddress: AccountKeyPair,
  val deviceAddress: String,
  val accountThreshold: AccountThreshold,
  val accountIdentity: List<RecoveryAccountIdentity>,
  val recoveryServers: List<RecoveryServer>,
  val signerWeight: SignerWeight,
  val sponsorAddress: String? = null
)

internal fun createDecoratedSignature(
  signatureAddress: String,
  decodedSignature: ByteArray
): DecoratedSignature {
  val signature = Signature()

  signature.signature = decodedSignature

  val decoratedSig = DecoratedSignature()
  decoratedSig.signature = signature
  decoratedSig.hint = KeyPair.fromAccountId(signatureAddress).signatureHint

  return decoratedSig
}
