package org.stellar.walletsdk

import io.jsonwebtoken.Jwts
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.Duration.Companion.days
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.Auth
import org.stellar.walletsdk.auth.DefaultAuthHeaderSigner
import org.stellar.walletsdk.auth.createAuthSignToken
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.Util.toJava

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
    val token = signer.createToken("test", null, kp)

    val claims = Jwts.parser().verifyWith(kp.toJava().public).build().parseSignedClaims(token)

    assertEquals("test", claims.payload["url"])
  }

  @Test
  fun `create token test custodial`() {
    val signer = DefaultAuthHeaderSigner(100000.days)
    val accountKp =
      SigningKeyPair.fromSecret("SBPPLU2KO3PDBLSDFIWARQSW5SAOIHTJDUQIWN3BQS7KPNMVUDSU37QO")
    val url =
      "https://auth.example.com/?account=GCXXH6AYJUVTDGIHT42OZNMF3LHCV4DOKCX6HHDKWECUZYXDZSWZN6HS&memo=1234567"
    val token = createAuthSignToken(accountKp, url, authHeaderSigner = signer)

    val claims =
      Jwts.parser().verifyWith(accountKp.toJava().public).build().parseSignedClaims(token)

    assertEquals(url, claims.payload["url"])

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
      object : DefaultAuthHeaderSigner(100000.days) {
        override fun createToken(
          url: String,
          clientDomain: String?,
          issuer: AccountKeyPair?
        ): String {
          val timeExp = Instant.ofEpochSecond(Clock.System.now().plus(expiration).epochSeconds)
          val builder = createBuilder(timeExp, url)

          builder.signWith(domainKp.toJava().private, Jwts.SIG.EdDSA)

          return builder.compact()
        }
      }
    val url =
      "https://auth.example.com/?account=GCIBUCGPOHWMMMFPFTDWBSVHQRT4DIBJ7AD6BZJYDITBK2LCVBYW7HUQ&client_domain=example-wallet.stellar.org"
    val token = createAuthSignToken(accountKp, url, authHeaderSigner = signer)
    val claims = Jwts.parser().verifyWith(domainKp.toJava().public).build().parseSignedClaims(token)

    assertEquals(url, claims.payload["url"])

    println(token)
  }
}
