package org.stellar.walletsdk.utils

import org.stellar.sdk.BeginSponsoringFutureReservesOperation
import org.stellar.sdk.EndSponsoringFutureReservesOperation
import org.stellar.sdk.Operation

fun sponsorOperation(
  sponsorAddress: String,
  accountAddress: String,
  operation: Operation
): List<Operation> {
  return listOfNotNull(
    // Start reserve sponsoring
    BeginSponsoringFutureReservesOperation.Builder(accountAddress)
      .setSourceAccount(sponsorAddress)
      .build(),
    // Operation to sponsor
    operation,
    // End reserve sponsoring
    EndSponsoringFutureReservesOperation(accountAddress)
  )
}
