package org.stellar.walletsdk.utils

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ADDRESS_INACTIVE
import org.stellar.walletsdk.OP_CREATE_ACCOUNT

@DisplayName("sponsorOperation")
internal class SponsorOperationTest {
  @Test
  fun `is wrapped in Begin and End SponsoringFutureReservesOperation`() {
    val operation = OP_CREATE_ACCOUNT
    val sponsoredOperation = sponsorOperation(ADDRESS_ACTIVE, ADDRESS_INACTIVE, operation)

    kotlin.test.assertEquals(3, sponsoredOperation.size)
  }
}
