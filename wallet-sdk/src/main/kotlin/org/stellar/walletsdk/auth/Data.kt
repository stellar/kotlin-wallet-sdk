package org.stellar.walletsdk.auth

import kotlinx.serialization.Serializable

@Serializable data class ChallengeResponse(val transaction: String, val network_passphrase: String)

@Serializable internal data class AuthToken(val token: String)

@Serializable internal data class AuthTransaction(val transaction: String)
