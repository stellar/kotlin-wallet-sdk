package org.stellar.walletsdk.auth

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import java.time.Instant
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.Util.toJava

/** Header signer to sign JWT for GET /Auth request. */
interface AuthHeaderSigner {
  fun createToken(url: String, clientDomain: String?, issuer: AccountKeyPair?): String
}

/** Header signer signing JWT for GET /Auth with a main custodial key */
open class DefaultAuthHeaderSigner(val expiration: Duration = 15.minutes) : AuthHeaderSigner {
  override fun createToken(url: String, clientDomain: String?, issuer: AccountKeyPair?): String {
    if (issuer == null) {
      throw IllegalArgumentException("Default signer can't sign headers for client domain.")
    }
    if (issuer !is SigningKeyPair) {
      throw IllegalArgumentException(
        "SigningKeyPair must be provided to .auth() method in order to sign headers"
      )
    }

    val timeExp = Instant.ofEpochSecond(Clock.System.now().plus(expiration).epochSeconds)
    val builder = createBuilder(timeExp, url)

    builder.signWith(issuer.toJava().private, Jwts.SIG.EdDSA)

    return builder.compact()
  }

  fun createBuilder(timeExp: Instant, url: String): JwtBuilder {
    return Jwts.builder()
      .id(UUID.randomUUID().toString())
      .expiration(Date.from(timeExp))
      .claim("url", url)
  }
}
