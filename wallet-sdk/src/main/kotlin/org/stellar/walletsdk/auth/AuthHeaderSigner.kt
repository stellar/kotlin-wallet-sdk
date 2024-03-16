package org.stellar.walletsdk.auth

import io.jsonwebtoken.Jwts
import kotlinx.datetime.Clock
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.Util.toJava
import java.time.Instant
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/** Header signer to sign JWT for GET /Auth request. */
interface AuthHeaderSigner {
  fun createToken(
    url: String,
    subject: String,
    domainIssuer: String?,
    issuer: AccountKeyPair?
  ): String
}

/** Header signer signing JWT for GET /Auth with a main custodial key */
open class DefaultAuthHeaderSigner(private val expiration: Duration = 15.minutes) :
  AuthHeaderSigner {
  override fun createToken(
    url: String,
    subject: String,
    domainIssuer: String?,
    issuer: AccountKeyPair?
  ): String {
    if (issuer == null) {
      throw IllegalArgumentException(
        "Default signer can't sign headers for client domain. Use DomainSigner instead."
      )
    }
    if (issuer !is SigningKeyPair) {
      throw IllegalArgumentException(
        "SigningKeyPair must be provided to .auth() method in order to sign headers"
      )
    }

    val timeExp = Instant.ofEpochSecond(Clock.System.now().plus(expiration).epochSeconds)
    val token: String =
      Jwts.builder()
        .id(UUID.randomUUID().toString())
        .subject(subject)
        .issuer(issuer.address)
        .expiration(Date.from(timeExp))
        .claim("url", url)
        .signWith(issuer.toJava().private, Jwts.SIG.EdDSA)
        .compact()

    return token
  }
}
