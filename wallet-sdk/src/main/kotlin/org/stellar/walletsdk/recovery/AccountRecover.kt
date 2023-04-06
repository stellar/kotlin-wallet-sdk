package org.stellar.walletsdk.recovery

import io.ktor.client.*
import kotlin.io.encoding.Base64
import mu.KotlinLogging
import org.stellar.sdk.Transaction
import org.stellar.sdk.responses.AccountResponse
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.walletsdk.exception.NotAllSignaturesFetchedException
import org.stellar.walletsdk.exception.ServerRequestFailedException
import org.stellar.walletsdk.exception.ValidationException
import org.stellar.walletsdk.extension.accountOrNull
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.Stellar
import org.stellar.walletsdk.horizon.toPublicKeyPair
import org.stellar.walletsdk.util.Util.postJson

private val log = KotlinLogging.logger {}

interface AccountRecover {
  suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: AccountKeyPair,
    serverAuth: Map<RecoveryServerKey, RecoveryServerSigning>
  ): Transaction

  suspend fun replaceDeviceKey(
    account: AccountKeyPair,
    newKey: AccountKeyPair,
    serverAuth: Map<RecoveryServerKey, RecoveryServerSigning>,
    lostKey: AccountKeyPair? = null,
    sponsorAddress: AccountKeyPair? = null
  ): Transaction
}

internal class AccountRecoverImpl(
  private val stellar: Stellar,
  private val client: HttpClient,
  private val servers: Map<RecoveryServerKey, RecoveryServer>
) : AccountRecover {

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
  override suspend fun signWithRecoveryServers(
    transaction: Transaction,
    accountAddress: AccountKeyPair,
    serverAuth: Map<RecoveryServerKey, RecoveryServerSigning>
  ): Transaction {
    val signatures =
      serverAuth.map { getRecoveryServerTxnSignature(transaction, accountAddress.address, it) }

    signatures.forEach { transaction.addSignature(it) }

    return transaction
  }

  /**
   * Replace lost device key with a new key
   *
   * @param account target account
   * @param newKey a key to replace the lost key with
   * @param serverAuth list of servers to use
   * @param lostKey (optional) lost device key. If not specified, try to deduce key from account
   * signers list
   * @param sponsorAddress (optional) sponsor address of the transaction. Please note that not all
   * SEP-30 servers support signing sponsored transactions.
   * @return
   */
  override suspend fun replaceDeviceKey(
    account: AccountKeyPair,
    newKey: AccountKeyPair,
    serverAuth: Map<RecoveryServerKey, RecoveryServerSigning>,
    lostKey: AccountKeyPair?,
    sponsorAddress: AccountKeyPair?
  ): Transaction {
    val stellarAccount =
      stellar.server.accountOrNull(account.address)
        ?: throw ValidationException("Account doesn't exist")
    val lost: AccountKeyPair
    val weight: Int

    if (lostKey != null) {
      lost = lostKey
      weight =
        stellarAccount.signers.filter { it.key == lost.address }.map { it.weight }.firstOrNull()
          ?: throw ValidationException("Lost key doesn't belong to the account")
    } else {
      val deduced = deduceKey(stellarAccount, serverAuth)
      lost = deduced.key.toPublicKeyPair()
      weight = deduced.weight
    }

    val transaction =
      if (sponsorAddress != null) {
        stellar
          .transaction(account)
          .sponsoring(sponsorAddress) { removeAccountSigner(lost).addAccountSigner(newKey, weight) }
          .build()
      } else {
        stellar
          .transaction(account)
          .removeAccountSigner(lost)
          .addAccountSigner(newKey, weight)
          .build()
      }

    return signWithRecoveryServers(transaction, account, serverAuth)
  }

  /**
   * Try to deduce lost key. If any of these criteria matches, one of signers from the account will
   * be recognized as a device key:
   * 1. Only signer that's not in [serverAuth]
   * 2. All signers in [serverAuth] has the same weight, and potential signer is the only one with a
   * ```
   *    different weight.
   * ```
   */
  private fun deduceKey(
    stellarAccount: AccountResponse,
    serverAuth: Map<RecoveryServerKey, RecoveryServerSigning>
  ): AccountResponse.Signer {
    val recoverySigners = serverAuth.values.map { it.signerAddress }.toSet()

    val nonRecoverySigners =
      stellarAccount.signers.filter { !recoverySigners.contains(it.key) }.filter { it.weight != 0 }

    if (nonRecoverySigners.size > 1) {
      val groupedRecovery =
        stellarAccount.signers.filter { recoverySigners.contains(it.key) }.groupBy { it.weight }
      if (groupedRecovery.size == 1) {
        val recoveryWeight = groupedRecovery.entries.first().value.first().weight

        val filtered = nonRecoverySigners.filter { it.weight != recoveryWeight }

        if (filtered.size != 1) {
          throw ValidationException("Couldn't deduce lost key. Please provide lost key explicitly")
        }

        return filtered[0]
      } else {
        throw ValidationException("Couldn't deduce lost key. Please provide lost key explicitly")
      }
    } else {
      return nonRecoverySigners.getOrNull(0)
        ?: throw ValidationException("No device key is setup for this account")
    }
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
}
