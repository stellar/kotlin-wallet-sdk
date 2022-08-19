package org.stellar.walletsdk.utils

import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
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

    assertEquals(3, sponsoredOperation.size)
    assertEquals("BeginSponsoringFutureReservesOperation", sponsoredOperation[0]::class.simpleName)
    assertEquals("EndSponsoringFutureReservesOperation", sponsoredOperation[2]::class.simpleName)
  }

  @Test
  fun `not allowed sponsored operation type throws an error`() {
    val error =
      assertFailsWith<Error>(
        block = { sponsorOperation(ADDRESS_ACTIVE, ADDRESS_ACTIVE, OP_CLAIM_CLAIMABLE_BALANCE) }
      )

    assertTrue(error.toString().contains("ClaimClaimableBalanceOperation cannot be sponsored"))
  }
}
