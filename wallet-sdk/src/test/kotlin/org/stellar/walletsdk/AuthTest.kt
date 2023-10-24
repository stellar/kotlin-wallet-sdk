package org.stellar.walletsdk

import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.Auth

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
}
