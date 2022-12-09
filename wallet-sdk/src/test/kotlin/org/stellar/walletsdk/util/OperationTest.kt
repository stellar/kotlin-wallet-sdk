package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold

internal class OperationTest {
  @Nested
  @DisplayName("addSignerOperation")
  inner class AddSignerOperation {
    @Test
    fun `generates add signer operation`() {
      val signerOp =
        addSignerOperation(AccountSigner(address = ADDRESS_ACTIVE.address, weight = 10))

      assertEquals("SetOptionsOperation", signerOp::class.simpleName)
    }
  }

  @Nested
  @DisplayName("setThresholdsOperation")
  inner class SetThresholdsOperation {
    @Test
    fun `generates set thresholds operation`() {
      val thresholdsOp = setThresholdsOperation(AccountThreshold(low = 10, medium = 10, high = 10))

      assertEquals("SetOptionsOperation", thresholdsOp::class.simpleName)
    }
  }

  @Nested
  @DisplayName("setMasterKeyWeightOperation")
  inner class SetMasterKeyWeightOperation {
    @Test
    fun `generates set master key weight operation`() {
      val setMasterKeyOp = setMasterKeyWeightOperation(weight = 10)

      assertEquals("SetOptionsOperation", setMasterKeyOp::class.simpleName)
    }
  }

  @Nested
  @DisplayName("lockMasterKeyOperation")
  inner class LockMasterKeyOperation {
    @Test
    fun `generates lock master key operation`() {
      val setMasterKeyOp = lockMasterKeyOperation()

      assertEquals("SetOptionsOperation", setMasterKeyOp::class.simpleName)
    }
  }
}
