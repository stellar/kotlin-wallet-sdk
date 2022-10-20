package org.stellar.walletsdk

import kotlin.test.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class AuthTest : SuspendTest() {

  // NOTE: making real network calls for now. Mocking SEP-10 server is tricky, so we will need to
  // spend more time on this later.

  @Test
  fun `auth token`() {
    val authToken = runBlocking {
      Auth(
          accountAddress = ADDRESS_ACTIVE,
          webAuthEndpoint = AUTH_ENDPOINT,
          homeDomain = AUTH_HOME_DOMAIN,
          walletSigner = InProcessWalletSigner()
        )
        .authenticate()
    }

    assertNotNull(authToken)
  }

  @Test
  fun `auth token with client domain`() {
    val authToken = runBlocking {
      Auth(
          ADDRESS_ACTIVE,
          AUTH_ENDPOINT,
          AUTH_HOME_DOMAIN,
          clientDomain = AUTH_CLIENT_DOMAIN,
          walletSigner = InProcessWalletSigner()
        )
        .authenticate()
    }

    assertNotNull(authToken)
  }

  @Test
  fun `throw exception if both memo and clientDomain are provided`() {
    assertThrows<Exception> {
      runBlocking {
        Auth(
            ADDRESS_ACTIVE,
            AUTH_ENDPOINT,
            AUTH_HOME_DOMAIN,
            memoId = "123",
            clientDomain = AUTH_CLIENT_DOMAIN,
            walletSigner = InProcessWalletSigner()
          )
          .authenticate()
      }
    }
  }

  @Test
  fun `throw exception if Memo ID is not a positive integer`() {
    assertThrows<Exception> {
      runBlocking {
        Auth(
            ADDRESS_ACTIVE,
            AUTH_ENDPOINT,
            AUTH_HOME_DOMAIN,
            memoId = "abc",
            walletSigner = InProcessWalletSigner()
          )
          .authenticate()
      }
    }
  }
}
