package org.stellar.walletsdk.util

import org.stellar.sdk.BeginSponsoringFutureReservesOperation
import org.stellar.sdk.EndSponsoringFutureReservesOperation
import org.stellar.sdk.Operation

fun sponsorOperation(
  sponsorAddress: String,
  accountAddress: String,
  operation: List<Operation>
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

  operation.forEach { op ->
    val opType = op::class.simpleName

    if (!allowedOperations.contains(opType)) {
      throw Exception(
        "$opType cannot be sponsored. Allowed operations are: ${allowedOperations
          .joinToString(", ")}."
      )
    }
  }

  return listOfNotNull(
    // Start reserve sponsoring
    BeginSponsoringFutureReservesOperation.Builder(accountAddress)
      .setSourceAccount(sponsorAddress)
      .build(),
    // Operation(s) to sponsor
    *operation.toTypedArray(),
    // End reserve sponsoring
    EndSponsoringFutureReservesOperation(accountAddress)
  )
}
