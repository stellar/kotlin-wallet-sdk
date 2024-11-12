package org.stellar.walletsdk.recovery

import io.ktor.client.*
import mu.KotlinLogging
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Transaction
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.sdk.xdr.Signature
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.Config
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.auth.Sep10
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
) : AccountRecover by AccountRecoverImpl(stellar, client, servers) {
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

      val authToken =
        sep10Auth(key)
          .authenticate(account, server.walletSigner, clientDomain = server.clientDomain)

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
  fun sep10Auth(key: RecoveryServerKey): Sep10 {
    val server = servers.getServer(key)
    return Sep10(cfg, server.authEndpoint, server.homeDomain, client)
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
   * @throws [RecoveryException] when error happens working with recovery servers
   * @throws [HorizonRequestFailedException] for Horizon exceptions
   */
  suspend fun createRecoverableWallet(config: RecoverableWalletConfig): RecoverableWallet {
    if (config.deviceAddress.address == config.accountAddress.address) {
      throw ValidationException("Device key must be different from master (account) key")
    }

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
        config.sponsorAddress,
        config.builderExtra
      ),
      recoverySigners
    )
  }

  suspend fun getAccountInfo(
    accountAddress: AccountKeyPair,
    auth: Map<RecoveryServerKey, AuthToken>
  ): Map<RecoveryServerKey, RecoverableAccountInfo> {
    return auth
      .map {
        val requestUrl = "${servers.getServer(it.key).endpoint}/accounts/${accountAddress.address}"

        it.key to client.authGet<RecoverableAccountInfo>(requestUrl, it.value)
      }
      .toMap()
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
    sponsorAddress: AccountKeyPair? = null,
    builderExtra: ((CommonTransactionBuilder<*>) -> Unit)? = null
  ): Transaction {
    val exists = stellar.server.accountOrNull(account.address) != null
    val source =
      if (exists) account
      else
        sponsorAddress ?: throw ValidationException("Account does not exist and is not sponsored.")

    val builder = stellar.transaction(source)

    if (sponsorAddress != null) {
      if (exists) {
        builder.sponsoring(sponsorAddress) {
          register(accountSigner, accountThreshold, builderExtra)
        }
      } else {
        builder.sponsoring(sponsorAddress, account) {
          createAccount(account)
          register(accountSigner, accountThreshold, builderExtra)
        }
      }
    } else {
      builder.register(accountSigner, accountThreshold, builderExtra)
    }

    return builder.build()
  }
}

private inline fun <reified T : CommonTransactionBuilder<*>> T.register(
  accountSigner: List<AccountSigner>,
  accountThreshold: AccountThreshold,
  noinline builderExtra: ((CommonTransactionBuilder<*>) -> Unit)?
): T {
  lockAccountMasterKey()
  accountSigner.forEach { this.addAccountSigner(it.address, it.weight) }
  this.setThreshold(accountThreshold.low, accountThreshold.medium, accountThreshold.high)
  builderExtra?.invoke(this)
  return this
}

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

internal fun Map<RecoveryServerKey, RecoveryServer>.getServer(
  key: RecoveryServerKey
): RecoveryServer {
  return this[key] ?: throw ValidationException("Server with key $key was not found")
}
