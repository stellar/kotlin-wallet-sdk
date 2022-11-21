package org.stellar.walletsdk.util

import org.stellar.sdk.BeginSponsoringFutureReservesOperation
import org.stellar.sdk.EndSponsoringFutureReservesOperation
import org.stellar.sdk.Operation
import org.stellar.walletsdk.exception.InvalidSponsorOperationTypeException

/**
 * Sponsor operation(s) for another account.
 *
 * [Learn about operation sponsorship](https://developers.stellar.org/docs/encyclopedia/sponsored-reserves#begin-and-end-sponsorships)
 *
 * @param sponsorAddress Stellar address of the account sponsoring operation(s)
 * @param accountAddress Stellar address of the account that is sponsored
 * @param operation A list of operation(s) to sponsor
 *
 * @return a list of operations
 *
 * @throws [InvalidSponsorOperationTypeException] when an operation cannot be sponsored
 */
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
      throw InvalidSponsorOperationTypeException(opType.toString(), allowedOperations)
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
