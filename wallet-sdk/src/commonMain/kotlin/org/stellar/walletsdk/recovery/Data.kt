package org.stellar.walletsdk.recovery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.AccountKeyPair

@Serializable internal data class TransactionRequest(val transaction: String)

@Serializable internal data class AuthSignature(val signature: String)

/**
 * Recovery server configuration
 *
 * @constructor Create empty Recovery server
 * @property endpoint main endpoint (root domain) of SEP-30 recovery server. E.g.
 * `https://testanchor.stellar.org`
 * @property authEndpoint SEP-10 auth endpoint to be used. Should be in format
 * `<https://domain/auth>`. E.g. `https://testanchor.stellar.org/auth`)
 * @property homeDomain is a SEP-10 home domain. E.g. `testanchor.stellar.org`
 * @property walletSigner optional [WalletSigner] used to sign authentication
 */
data class RecoveryServer(
  val endpoint: String,
  val authEndpoint: String,
  val homeDomain: String,
  val walletSigner: WalletSigner? = null
)

data class RecoveryServerAuth(
  val endpoint: String,
  val signerAddress: String,
  val authToken: AuthToken,
)

@Serializable
data class RecoveryAccount(
  val address: String,
  val identities: List<RecoveryAccountRole>,
  val signers: List<RecoveryAccountSigner>
)

@Serializable data class RecoveryIdentities(val identities: List<RecoveryAccountIdentity>)

@Serializable data class RecoveryAccountRole(val role: String, val authenticated: Boolean? = null)

@Serializable data class RecoveryAccountSigner(val key: String)

@Serializable
data class RecoveryAccountAuthMethod(
  val type: String,
  val value: String,
)

@Serializable
data class RecoveryAccountIdentity(
  val role: String,
  @SerialName("auth_methods") val authMethods: List<RecoveryAccountAuthMethod>,
)

data class SignerWeight(
  val master: Int,
  val recoveryServer: Int,
)

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
  val sponsorAddress: AccountKeyPair? = null
)
