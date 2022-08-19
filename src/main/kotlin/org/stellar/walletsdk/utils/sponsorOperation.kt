package org.stellar.walletsdk.utils

import org.stellar.sdk.BeginSponsoringFutureReservesOperation
import org.stellar.sdk.EndSponsoringFutureReservesOperation
import org.stellar.sdk.Operation

fun sponsorOperation(
  sponsorAddress: String,
  accountAddress: String,
  operation: Operation
): List<Operation> {
  val allowedOperations =
    listOf(
      "ChangeTrustOperation",
      "CreateAccountOperation",
      "ManageDataOperation",
      "ManageBuyOfferOperation",
      "ManageSellOfferOperation",
      "SetOptionsOperation",
    )

  val operationType = operation::class.simpleName

  if (!allowedOperations.contains(operationType)) {
    throw Error(
      "$operationType cannot be sponsored. Allowed operations are: ${allowedOperations
        .joinToString(", ")}."
    )
  }

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
