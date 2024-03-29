@file:UseSerializers(AccountAsStringSerializer::class, InstantIso8601Serializer::class)

package org.stellar.walletsdk.recovery

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.PublicKeyPair
import org.stellar.walletsdk.horizon.transaction.CommonTransactionBuilder
import org.stellar.walletsdk.json.AccountAsStringSerializer

@Serializable internal data class TransactionRequest(val transaction: String)

@Serializable internal data class AuthSignature(val signature: String)

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
  val sponsorAddress: AccountKeyPair? = null,
  val builderExtra: ((CommonTransactionBuilder<*>) -> Unit)? = null
)

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
  val walletSigner: WalletSigner? = null,
  val clientDomain: String? = null
)

data class RecoveryServerSigning(
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

@Serializable
data class RecoverableAccountInfo(
  val address: PublicKeyPair,
  val identities: List<RecoverableIdentity>,
  val signers: List<RecoverableSigner>
)

@Serializable data class RecoverableIdentity(val role: String, val authenticated: Boolean? = null)

@Serializable
data class RecoverableSigner(
  val key: PublicKeyPair,
  @SerialName("added_at") val added: Instant? = null
)

@JvmInline value class RecoveryServerKey(val name: String)
