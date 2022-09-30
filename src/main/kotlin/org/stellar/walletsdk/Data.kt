package org.stellar.walletsdk

data class AccountSigner(val address: String, val weight: Int)

data class AccountThreshold(val low: Int, val medium: Int, val high: Int)

data class RecoveryServer(
  val endpoint: String,
  val authEndpoint: String,
  val stellarAddress: String,
  val homeDomain: String,
)

data class RecoveryServerAuth(
  val endpoint: String,
  val signerAddress: String,
  val authToken: String,
)

data class RecoveryAccount(
  val address: String,
  val identities: List<RecoveryAccountRole>,
  val signers: List<RecoveryAccountSigner>
)

data class RecoveryIdentities(val identities: List<RecoveryAccountIdentity>)

data class RecoveryAccountRole(val role: String, val authenticated: Boolean?)

data class RecoveryAccountSigner(val key: String)

data class RecoveryAccountAuthMethod(
  val type: String,
  val value: String,
)

data class RecoveryAccountIdentity(
  val role: String,
  val auth_methods: List<RecoveryAccountAuthMethod>,
)

data class SignerWeight(
  val master: Int,
  val recoveryServer: Int,
)
