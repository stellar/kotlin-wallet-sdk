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
import org.stellar.walletsdk.auth.AuthToken
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
  private val client: HttpClient,
  private val servers: Map<RecoveryServerKey, RecoveryServer>
) {
  /**
   * Sign transaction with recovery servers. It is used to recover an account using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
   *
   * @param transaction Transaction with new signer to be signed by recovery servers
   * @param accountAddress Stellar address of the account that is recovered
   * @param serverAuth List of recovery servers to use
   * @return transaction with recovery server signatures
   * @throws [ServerRequestFailedException] when request fails
   * @throws [NotAllSignaturesFetchedException] when all recovery servers don't return signatures
   */
  suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: AccountKeyPair,
    serverAuth: Map<RecoveryServerKey, RecoveryServerSigning>
  ): Transaction {
    val signatures =
      serverAuth.map { getRecoveryServerTxnSignature(transaction, accountAddress.address, it) }

    signatures.forEach { transaction.addSignature(it) }

    return transaction
  }

  private suspend fun getRecoveryServerTxnSignature(
    transaction: Transaction,
    accountAddress: String,
    it: Map.Entry<RecoveryServerKey, RecoveryServerSigning>
  ): DecoratedSignature {
    val server = servers.getServer(it.key)
    val auth = it.value

    val requestUrl = "${server.endpoint}/accounts/$accountAddress/sign/${auth.signerAddress}"
    val requestParams = TransactionRequest(transaction.toEnvelopeXdrBase64())

    log.debug {
      "Recovery server signature request: accountAddress = $accountAddress, " +
        "signerAddress = ${auth.signerAddress}, authToken = ${auth.authToken.prettify()}..."
    }

    val authResponse: AuthSignature = client.postJson(requestUrl, requestParams, auth.authToken)

    return createDecoratedSignature(auth.signerAddress, Base64.decode(authResponse.signature))
  }

  /**
   * Register account with recovery servers using
   * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md).
   */
  private suspend fun enrollWithRecoveryServer(
    account: AccountKeyPair,
    identityMap: Map<RecoveryServerKey, List<RecoveryAccountIdentity>>
  ): List<String> {
    return servers.map { entry ->
      val server = entry.value
      val key = entry.key
      val accountIdentity =
        identityMap[key]
          ?: throw ValidationException("Account identity for server $key was not specified")

      val authToken = sep10Auth(key).authenticate(account, server.walletSigner)

      val requestUrl = "${server.endpoint}/accounts/${account.address}"
      val resp: RecoveryAccount =
        client.postJson(requestUrl, RecoveryIdentities(accountIdentity), authToken)

      log.debug {
        "Recovery server enroll request: accountAddress = ${account.address}, homeDomain =" +
          " ${server.homeDomain}, authToken = ${authToken.prettify()}..."
      }

      getLatestRecoverySigner(resp.signers)
    }
  }

  /**
   * Create new auth object to authenticate account with the recovery server using SEP-10.
   *
   * @return auth object
   */
  fun sep10Auth(key: RecoveryServerKey): Auth {
    val server = servers.getServer(key)
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
    auth: Map<RecoveryServerKey, AuthToken>
  ): List<RecoverableAccountInfo> {
    return auth.map {
      val requestUrl = "${servers.getServer(it.key).endpoint}/accounts/${accountAddress.address}"

      client.authGet(requestUrl, it.value)
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

  private fun Map<RecoveryServerKey, RecoveryServer>.getServer(
    key: RecoveryServerKey
  ): RecoveryServer {
    return this[key] ?: throw ValidationException("Server with key $key was not found")
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
 * @param signerWeight Signer weight of the device and recovery keys to set
 * @param sponsorAddress optional Stellar address of the account sponsoring this transaction
 */
data class RecoverableWalletConfig(
  val accountAddress: AccountKeyPair,
  val deviceAddress: AccountKeyPair,
  val accountThreshold: AccountThreshold,
  val accountIdentity: Map<RecoveryServerKey, List<RecoveryAccountIdentity>>,
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
