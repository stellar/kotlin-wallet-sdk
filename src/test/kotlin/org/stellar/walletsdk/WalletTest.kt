package org.stellar.walletsdk

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.stellar.sdk.Network

class WalletTest {
  private val wallet = Wallet("https://horizon-testnet.stellar.org", Network.TESTNET.toString())

  @Test
  fun `create() generates Stellar public and secret keys`() {
    val accountKeys = wallet.create()
    val publicKey = accountKeys.publicKey
    val secretKey = accountKeys.secretKey

    // Public key
    assertEquals(56, publicKey.length)
    assertTrue(publicKey.startsWith("G"))

    // Secret key
    assertEquals(56, secretKey.length)
    assertTrue(secretKey.startsWith("S"))
  }
}
