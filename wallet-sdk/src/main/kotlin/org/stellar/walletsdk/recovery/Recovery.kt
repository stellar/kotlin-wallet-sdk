package org.stellar.walletsdk.recovery

import io.ktor.client.*
import kotlin.io.encoding.Base64
import mu.KotlinLogging
import org.stellar.sdk.*
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.sdk.xdr.Signature
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.extension.accountOrNull
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.horizon.toPublicKeyPair
import org.stellar.walletsdk.horizon.transaction.CommonTransactionBuilder
import org.stellar.walletsdk.util.Util.authGet
import org.stellar.walletsdk.util.Util.postJson

private val log = KotlinLogging.logger {}

class Recovery
internal constructor(
  private val cfg: Config,
  private val stellar: Stellar,
  private val client: HttpClient
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
    accountAddress: AccountKeyPair,
    recoveryServers: List<RecoveryServerAuth>
  ): Transaction {
    val signatures =
      recoveryServers.map { getRecoveryServerTxnSignature(transaction, accountAddress.address, it) }

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

    log.debug {
      "Recovery server signature request: accountAddress = $accountAddress, " +
        "signerAddress = ${it.signerAddress}, authToken = ${it.authToken.prettify()}..."
    }

    val authResponse: AuthSignature = client.postJson(requestUrl, requestParams, it.authToken)

    return createDecoratedSignature(it.signerAddress, Base64.decode(authResponse.signature))
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
  private suspend fun enrollWithRecoveryServer(
    recoveryServers: List<RecoveryServer>,
    account: AccountKeyPair,
    accountIdentity: List<RecoveryAccountIdentity>
  ): List<String> {
    return recoveryServers.map {
      val authToken = sep10Auth(it).authenticate(account, it.walletSigner)

      val requestUrl = "${it.endpoint}/accounts/${account.address}"
      val resp: RecoveryAccount =
        client.postJson(requestUrl, RecoveryIdentities(accountIdentity), authToken)

      log.debug {
        "Recovery server enroll request: accountAddress = ${account.address}, homeDomain =" +
          " ${it.homeDomain}, authToken = ${authToken.prettify()}..."
      }

      getLatestRecoverySigner(resp.signers)
    }
  }

  /**
   * Create new auth object to authenticate account with the recovery server using SEP-10.
   *
   * @return auth object
   */
  fun sep10Auth(server: RecoveryServer): Auth {
    return Auth(cfg, server.authEndpoint, server.homeDomain, client)
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
   * **Warning**: This transaction will lock master key of the account. Make sure you have access to
   * specified [RecoverableWalletConfig.deviceAddress]
   *
   * This transaction can be sponsored.
   *
   * @param config: [RecoverableWalletConfig]
   * @return transaction
   * @throws [ServerRequestFailedException] when request fails
   * @throws [RecoveryException] when error happens working with recovery servers
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun createRecoverableWallet(config: RecoverableWalletConfig): RecoverableWallet {
    val recoverySigners =
      enrollWithRecoveryServer(
        config.recoveryServers,
        config.accountAddress,
        config.accountIdentity,
      )

    val signer =
      recoverySigners
        .map { rs -> AccountSigner(rs.toPublicKeyPair(), config.signerWeight.recoveryServer) }
        .toMutableList()

    signer.add(AccountSigner(config.deviceAddress, config.signerWeight.device))

    return RecoverableWallet(
      registerRecoveryServerSigners(
        config.accountAddress,
        signer,
        config.accountThreshold,
        config.sponsorAddress
      ),
      recoverySigners
    )
  }

  suspend fun getAccountInfo(
    accountAddress: AccountKeyPair,
    recoveryServers: List<RecoveryServerAuth>
  ): List<RecoverableAccountInfo> {
    return recoveryServers.map {
      val requestUrl = "${it.endpoint}/accounts/${accountAddress.address}"

      client.authGet(requestUrl, it.authToken)
    }
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
  internal suspend fun registerRecoveryServerSigners(
    account: AccountKeyPair,
    accountSigner: List<AccountSigner>,
    accountThreshold: AccountThreshold,
    sponsorAddress: AccountKeyPair? = null
  ): Transaction {
    val exists = stellar.server.accountOrNull(account.address) != null
    val source =
      if (exists) account
      else
        sponsorAddress ?: throw ValidationException("Account does not exist and is not sponsored.")

    val builder = stellar.transaction(source)

    if (sponsorAddress != null) {
      if (exists) {
        builder.sponsoring(sponsorAddress) { register(accountSigner, accountThreshold) }
      } else {
        builder.sponsoring(sponsorAddress, account) {
          createAccount(account)
          register(accountSigner, accountThreshold)
        }
      }
    } else {
      builder.register(accountSigner, accountThreshold)
    }

    return builder.build()
  }
}

private inline fun <reified T : CommonTransactionBuilder<*>> T.register(
  accountSigner: List<AccountSigner>,
  accountThreshold: AccountThreshold
): T {
  lockAccountMasterKey()
  accountSigner.forEach { this.addAccountSigner(it.address, it.weight) }
  this.setThreshold(accountThreshold.low, accountThreshold.medium, accountThreshold.high)
  return this
}

/**
 * Configuration for recoverable wallet
 *
 * @param accountAddress Stellar address of the account that is registering
 * @param deviceAddress Stellar address of the device that is added as a primary signer. It will
 * replace the master key of [accountAddress]
 * @param accountThreshold Low, medium, and high thresholds to set on the account
 * @param accountIdentity A list of account identities to be registered with the recovery servers
 * @param recoveryServers A list of recovery servers to register with
 * @param signerWeight Signer weight of the device and recovery keys to set
 * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
 */
data class RecoverableWalletConfig(
  val accountAddress: AccountKeyPair,
  val deviceAddress: AccountKeyPair,
  val accountThreshold: AccountThreshold,
  val accountIdentity: List<RecoveryAccountIdentity>,
  val recoveryServers: List<RecoveryServer>,
  val signerWeight: SignerWeight,
  val sponsorAddress: AccountKeyPair? = null
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
