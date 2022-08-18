package org.stellar.walletsdk.utils

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.Constants

@DisplayName("sponsorOperation")
internal class SponsorOperationTest {
  @Test
  fun `is wrapped in Begin and End SponsoringFutureReservesOperation`() {
    val operation = Constants.OP_CREATE_ACCOUNT
    val sponsoredOperation =
      sponsorOperation(Constants.ADDRESS_ACTIVE, Constants.ADDRESS_INACTIVE, operation)

    kotlin.test.assertEquals(3, sponsoredOperation.size)
  }
}
