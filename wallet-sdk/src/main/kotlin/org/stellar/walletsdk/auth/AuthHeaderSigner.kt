package org.stellar.walletsdk.auth

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.Jwts
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import java.time.Instant
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.Util.postJson
import org.stellar.walletsdk.util.toJava

/** Header signer to sign JWT for GET /Auth request. */
interface AuthHeaderSigner {
  suspend fun createToken(
    claims: Map<String, String>,
    clientDomain: String?,
    issuer: AccountKeyPair?
  ): String
}

/** Header signer signing JWT for GET /Auth with a main custodial key */
open class DefaultAuthHeaderSigner(open val expiration: Duration = 15.minutes) : AuthHeaderSigner {
  override suspend fun createToken(
    claims: Map<String, String>,
    clientDomain: String?,
    issuer: AccountKeyPair?
  ): String {
    require(issuer != null) { "Default signer can't sign headers for client domain." }
    require(issuer is SigningKeyPair) {
      "SigningKeyPair must be provided to .auth() method in order to sign headers"
    }

    val timeExp = Instant.ofEpochSecond(Clock.System.now().plus(expiration).epochSeconds)
    val builder = createBuilder(timeExp, claims)

    builder.signWith(issuer.toJava().private, Jwts.SIG.EdDSA)

    return builder.compact()
  }

  fun createBuilder(timeExp: Instant, claims: Map<String, String>): JwtBuilder {
    return Jwts.builder().issuedAt(Date.from(Instant.now())).expiration(Date.from(timeExp)).also {
      builder ->
      claims.forEach { builder.claim(it.key, it.value) }
    }
  }
}

/**
 * Auth header signer using remote server to form and sign the JWT. On calling [createToken] it will
 * send [JWTSignData] to the specified [url], expecting [SignedJWT] in response.
 */
open class DomainAuthHeaderSigner(
  val url: String,
  val requestTransformer: HttpRequestBuilder.() -> Unit = {},
  override val expiration: Duration = 15.minutes
) : DefaultAuthHeaderSigner(expiration) {
  val client = HttpClient() { install(ContentNegotiation) { json() } }

  @Serializable
  data class JWTSignData(
    val claims: Map<String, String>,
    val clientDomain: String,
    val exp: Long,
    val iat: Long
  )

  @Serializable data class SignedJWT(val token: String)

  override suspend fun createToken(
    claims: Map<String, String>,
    clientDomain: String?,
    issuer: AccountKeyPair?
  ): String {
    require(clientDomain != null) {
      "This signed should only be used for remote signing. For local signing use " +
        "${DefaultAuthHeaderSigner::class.simpleName} instead"
    }
    val now = Clock.System.now()
    val timeExp = now.plus(expiration).epochSeconds
    return signToken(claims, clientDomain, timeExp, now.epochSeconds)
  }

  /**
   * Method to create and sign token on a remote server
   *
   * @return signed base-64 encoded JWT token
   */
  open suspend fun signToken(
    claims: Map<String, String>,
    clientDomain: String,
    expiration: Long,
    issuedAt: Long
  ): String {
    val response: SignedJWT =
      client.postJson(url, JWTSignData(claims, clientDomain, expiration, issuedAt)) {
        requestTransformer()
      }

    return response.token
  }
}
