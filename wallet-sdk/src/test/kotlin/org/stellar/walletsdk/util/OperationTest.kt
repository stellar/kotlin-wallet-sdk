package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold

@DisplayName("operation")
internal class OperationTest {
  @Test
  fun `generates add signer operation`() {
    val signerOp = addSignerOperation(AccountSigner(address = ADDRESS_ACTIVE.address, weight = 10))

    assertEquals("SetOptionsOperation", signerOp::class.simpleName)
  }

  @Test
  fun `generates set thresholds operation`() {
    val thresholdsOp = setThresholdsOperation(AccountThreshold(low = 10, medium = 10, high = 10))

    assertEquals("SetOptionsOperation", thresholdsOp::class.simpleName)
  }

  @Test
  fun `generates set master key weight operation`() {
    val setMasterKeyOp = setMasterKeyWeightOperation(weight = 10)

    assertEquals("SetOptionsOperation", setMasterKeyOp::class.simpleName)
  }

  @Test
  fun `generates lock master key operation`() {
    val setMasterKeyOp = lockMasterKeyOperation()

    assertEquals("SetOptionsOperation", setMasterKeyOp::class.simpleName)
  }
}
