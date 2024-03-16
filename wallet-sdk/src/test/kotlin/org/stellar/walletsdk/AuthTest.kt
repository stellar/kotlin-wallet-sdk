package org.stellar.walletsdk

import io.jsonwebtoken.Jwts
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.Auth
import org.stellar.walletsdk.auth.DefaultAuthHeaderSigner
import org.stellar.walletsdk.auth.createAuthSignToken
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.util.Util.toJava
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
    val token = signer.createToken("test", "subject", null, kp)

    val claims = Jwts.parser().verifyWith(kp.toJava().public).build().parseSignedClaims(token)

    assertEquals("test", claims.payload["url"])
    assertEquals("subject", claims.payload["sub"])
    assertEquals(kp.address, claims.payload["iss"])
  }

  @Test
  fun `create token test custodial`() {
    val signer = DefaultAuthHeaderSigner()
    val kp = SigningKeyPair.fromSecret("SBPPLU2KO3PDBLSDFIWARQSW5SAOIHTJDUQIWN3BQS7KPNMVUDSU37QO")
    val url =
      "https://auth.example.com/?account=GCXXH6AYJUVTDGIHT42OZNMF3LHCV4DOKCX6HHDKWECUZYXDZSWZN6HS&memo=1234567"
    val token = createAuthSignToken(kp, url, memoId = "1234567", authHeaderSigner = signer)

    val claims = Jwts.parser().verifyWith(kp.toJava().public).build().parseSignedClaims(token)

    println(token)
  }

  // In real impl custodial case is not supported, must be signed with domain signer
  @Test
  fun `create token test non custodial`() {
    val signer = DefaultAuthHeaderSigner()
    val kp = SigningKeyPair.fromSecret("SBPPLU2KO3PDBLSDFIWARQSW5SAOIHTJDUQIWN3BQS7KPNMVUDSU37QO")
    val url =
      "https://auth.example.com/?account=GCIBUCGPOHWMMMFPFTDWBSVHQRT4DIBJ7AD6BZJYDITBK2LCVBYW7HUQ&client_domain=example-wallet.stellar.org"
    val token = createAuthSignToken(kp, url, authHeaderSigner = signer)
    println(token)
  }
}
