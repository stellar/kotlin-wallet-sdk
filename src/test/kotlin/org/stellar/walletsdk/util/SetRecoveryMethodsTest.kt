package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ADDRESS_ACTIVE_TWO
import org.stellar.walletsdk.RecoveryAccountSigner

internal class SetRecoveryMethodsTest {
  @Nested
  @DisplayName("getLatestRecoverySigner")
  inner class GetLatestRecoverySigner {
    @Test
    fun `return the latest signature from the list`() {
      val lastSigner =
        getLatestRecoverySigner(
          signers =
            listOf(
              RecoveryAccountSigner(key = ADDRESS_ACTIVE),
              RecoveryAccountSigner(key = ADDRESS_ACTIVE_TWO)
            )
        )

      assertEquals(ADDRESS_ACTIVE, lastSigner)
    }

    @Test
    fun `throws error if signers list is empty`() {
      assertThrows<Exception> { getLatestRecoverySigner(signers = listOf()) }
    }
  }
}
