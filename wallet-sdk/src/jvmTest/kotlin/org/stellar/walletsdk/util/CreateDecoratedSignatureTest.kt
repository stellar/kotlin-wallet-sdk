package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.stellar.sdk.xdr.DecoratedSignature
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.defaultBase64Decoder
import org.stellar.walletsdk.recovery.createDecoratedSignature

internal class CreateDecoratedSignatureTest {
  private val publicKey = ADDRESS_ACTIVE
  private val signatureBase64String =
    "SHBn0KzcvodqdPsdM1yK3nSe1QZihloIUfEA0DCdGdLshog1Zktu2dABcdcG83eGi8xbkiuzn5atDhQIPIQICA=="

  @Test
  fun `creates decorated signature`() {
    val decoratedSig =
      createDecoratedSignature(publicKey.address, defaultBase64Decoder(signatureBase64String))
    assertEquals(DecoratedSignature::class.java, decoratedSig::class.java)
  }

  @Test
  fun `throws exception if public key is invalid`() {
    assertThrows<Exception> {
      createDecoratedSignature("ABC", defaultBase64Decoder(signatureBase64String))
    }
  }
}
