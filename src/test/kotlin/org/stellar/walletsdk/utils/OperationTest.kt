package org.stellar.walletsdk.utils

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.Constants
import org.stellar.walletsdk.utils.Operation.Companion.createSponsoredOperation

internal class OperationTest {
  @Nested
  @DisplayName("createSponsoredOperation")
  inner class CreateSponsoredOperation {
    @Test
    fun `is wrapped in Begin and End SponsoringFutureReservesOperation`() {
      val operation = Constants.OP_CREATE_ACCOUNT
      val sponsoredOperation =
        createSponsoredOperation(Constants.ADDRESS_ACTIVE, Constants.ADDRESS_INACTIVE, operation)

      assertEquals(3, sponsoredOperation.size)
    }
  }
}
