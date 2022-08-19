package org.stellar.walletsdk.utils

import kotlin.test.assertFailsWith
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.ADDRESS_ACTIVE
import org.stellar.walletsdk.ADDRESS_INACTIVE
import org.stellar.walletsdk.OP_CLAIM_CLAIMABLE_BALANCE
import org.stellar.walletsdk.OP_CREATE_ACCOUNT

@DisplayName("sponsorOperation")
internal class SponsorOperationTest {
  @Test
  fun `is wrapped in Begin and End SponsoringFutureReservesOperation`() {
    val sponsoredOperation = sponsorOperation(ADDRESS_ACTIVE, ADDRESS_INACTIVE, OP_CREATE_ACCOUNT)

    kotlin.test.assertEquals(3, sponsoredOperation.size)
  }

  @Test
  fun `not allowed sponsored operation type throws an error`() {
    val error =
      assertFailsWith<Error>(
        block = { sponsorOperation(ADDRESS_ACTIVE, ADDRESS_ACTIVE, OP_CLAIM_CLAIMABLE_BALANCE) }
      )

    kotlin.test.assertTrue(
      error.toString().contains("ClaimClaimableBalanceOperation cannot be sponsored")
    )
  }
}
