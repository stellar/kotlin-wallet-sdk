package org.stellar.walletsdk.recovery

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.stellar.sdk.Transaction
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

/**
 * A list of identities that if any one is authenticated will be able to gain control of this
 * account.
 */
@Serializable data class RecoveryIdentities(val identities: List<RecoveryAccountIdentity>)

@Serializable
data class RecoveryAccountRole(val role: RecoveryRole, val authenticated: Boolean? = null)

@Serializable data class RecoveryAccountSigner(val key: String)

@Serializable
enum class RecoveryType {
  @SerialName("stellar_address") STELLAR_ADDRESS,
  @SerialName("phone_number") PHONE_NUMBER,
  @SerialName("email") EMAIL
}

@Serializable
data class RecoveryAccountAuthMethod(
  val type: RecoveryType,
  val value: String,
)

/**
 * @constructor Create empty Recovery account identity
 * @property role see [RecoveryRole]
 * @property authMethods
 */
@Serializable
data class RecoveryAccountIdentity(
  val role: RecoveryRole,
  @SerialName("auth_methods") val authMethods: List<RecoveryAccountAuthMethod>,
)

/**
 * The role of the identity. This value is not used by the server and is stored and echoed back in
 * responses as a way for a client to know conceptually who each identity represents
 */
@Serializable
enum class RecoveryRole {
  @SerialName("owner") OWNER,
  @SerialName("sender") SENDER,
  @SerialName("receiver") RECEIVER,
}

data class SignerWeight(
  val device: Int,
  val recoveryServer: Int,
)

data class RecoverableWallet(val transaction: Transaction, val signers: List<String>)

internal data class AccountSigner(val address: AccountKeyPair, val weight: Int)

