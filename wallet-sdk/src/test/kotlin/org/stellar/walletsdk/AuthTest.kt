package org.stellar.walletsdk

import io.jsonwebtoken.Jwts
import io.ktor.http.*
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.Auth
import org.stellar.walletsdk.auth.DefaultAuthHeaderSigner
import org.stellar.walletsdk.auth.createAuthSignToken
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.toJava

internal class AuthTest : SuspendTest() {
  private val cfg = TestWallet.cfg

  // NOTE: making real network calls for now. Mocking SEP-10 server is tricky, so we will need to
  // spend more time on this later.

  @Test
  fun `auth token`() {
    val authToken = runBlocking {
      Auth(
          cfg,
          webAuthEndpoint = AUTH_ENDPOINT,
          homeDomain = AUTH_HOME_DOMAIN,
          cfg.app.defaultClient
        )
        .authenticate(ADDRESS_ACTIVE)
    }

    assertNotNull(authToken)
  }

  @Test
  fun `auth token with client domain`() {
    val authToken = runBlocking {
      Auth(cfg, AUTH_ENDPOINT, AUTH_HOME_DOMAIN, cfg.app.defaultClient)
        .authenticate(ADDRESS_ACTIVE, clientDomain = AUTH_CLIENT_DOMAIN)
    }

    assertNotNull(authToken)
  }

  @Test
  fun `auth token has correct account`() {
    val authToken = runBlocking {
      Auth(cfg, AUTH_ENDPOINT, AUTH_HOME_DOMAIN, cfg.app.defaultClient)
        .authenticate(ADDRESS_ACTIVE, clientDomain = AUTH_CLIENT_DOMAIN)
    }

    assertEquals(authToken.account, ADDRESS_ACTIVE.address)
  }

  @Test
  fun `auth token has correct account and memo`() {
    val memo = 18446744073709551615U
    val authToken = runBlocking {
      Auth(cfg, AUTH_ENDPOINT, AUTH_HOME_DOMAIN, cfg.app.defaultClient)
        .authenticate(ADDRESS_ACTIVE, memoId = memo, clientDomain = AUTH_CLIENT_DOMAIN)
    }

    assertEquals(authToken.account, ADDRESS_ACTIVE.address)
    assertEquals(authToken.memo, memo)
  }

  @Test
  fun headerSignerTest() {
    val kp = SigningKeyPair.fromSecret("SBPPLU2KO3PDBLSDFIWARQSW5SAOIHTJDUQIWN3BQS7KPNMVUDSU37QO")
    val signer = DefaultAuthHeaderSigner()
    val token = signer.createToken(mapOf("testkey" to "test"), null, kp)

    val claims = Jwts.parser().verifyWith(kp.toJava().public).build().parseSignedClaims(token)

    assertEquals("test", claims.payload["testkey"])
  }

  @Test
  fun `create token test custodial`() {
    val signer = DefaultAuthHeaderSigner()
    val accountKp =
      SigningKeyPair.fromSecret("SBPPLU2KO3PDBLSDFIWARQSW5SAOIHTJDUQIWN3BQS7KPNMVUDSU37QO")
    val memo = "1234567"
    val url = "https://example.com/sep10/auth?account=${accountKp.address}&memo=$memo"
    val builder = URLBuilder(url)
    val params = mapOf("account" to accountKp.address, "memo" to memo)

    val token =
      createAuthSignToken(
        accountKp,
        "https://example.com/sep10/auth",
        params,
        authHeaderSigner = signer
      )

    val claims =
      Jwts.parser().verifyWith(accountKp.toJava().public).build().parseSignedClaims(token)

    assertEquals("https://example.com/sep10/auth", claims.payload["web_auth_endpoint"])
    assertEquals(accountKp.address, claims.payload["account"])
    assertEquals(memo, claims.payload["memo"])

    println(url)
    println(token)
  }

  @Test
  fun `create token test non custodial`() {
    val accountKp =
      SigningKeyPair.fromSecret("SC6UWRT6SMC7DSGJR67JTYHJQ6GPSGCKYJ66V5AVCLRGSGKXUNW5OXX7")
    val domainKp =
      SigningKeyPair.fromSecret("SCYVDFYEHNDNTB2UER2FCYSZAYQFAAZ6BDYXL3BWRQWNL327GZUXY7D7")
    // Signing with a domain signer
    val signer =
      object : DefaultAuthHeaderSigner() {
        override fun createToken(
          claims: Map<String, String>,
          clientDomain: String?,
          issuer: AccountKeyPair?
        ): String {
          val timeExp = Instant.ofEpochSecond(Clock.System.now().plus(expiration).epochSeconds)
          val builder = createBuilder(timeExp, claims)

          builder.signWith(domainKp.toJava().private, Jwts.SIG.EdDSA)

          return builder.compact()
        }
      }
    val clientDomain = "example-wallet.stellar.org"
    val url =
      "https://example.com/sep10/auth?account=${accountKp.address}&client_domain=${clientDomain}"
    val builder = URLBuilder(url)
    val params = mapOf("account" to accountKp.address, "client_domain" to clientDomain)

    val token =
      createAuthSignToken(
        accountKp,
        "https://example.com/sep10/auth",
        params,
        authHeaderSigner = signer
      )
    val claims = Jwts.parser().verifyWith(domainKp.toJava().public).build().parseSignedClaims(token)

    assertEquals("https://example.com/sep10/auth", claims.payload["web_auth_endpoint"])
    assertEquals(accountKp.address, claims.payload["account"])
    assertEquals(clientDomain, claims.payload["client_domain"])

    println(url)
    println(token)
  }
}
