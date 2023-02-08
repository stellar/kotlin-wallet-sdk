package org.stellar.walletsdk.recovery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.auth.WalletSigner

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
