package org.stellar.walletsdk.util

import org.stellar.sdk.*
import org.stellar.walletsdk.exception.InvalidSponsorOperationTypeException

@Suppress("MaxLineLength")
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
@Deprecated(
  "To be removed"
) // TODO: optimize this function in transaction builder. Only add begin/end ops once (not every
// time)
fun sponsorOperation(
  sponsorAddress: String,
  accountAddress: String,
  operation: List<Operation>
): List<Operation> {
  val invalid = operation.filter { !allowedSponsoredOperations.contains(it::class) }

  if (invalid.isNotEmpty()) {
    throw InvalidSponsorOperationTypeException(invalid, allowedSponsoredOperations)
  }

  // Start reserve sponsoring
  val ops =
    mutableListOf<Operation>(
      BeginSponsoringFutureReservesOperation.Builder(accountAddress)
        .setSourceAccount(sponsorAddress)
        .build()
    )
  // Operation(s) to sponsor
  ops.addAll(operation)
  // End reserve sponsoring
  ops.add(EndSponsoringFutureReservesOperation(accountAddress))

  return ops
}

private val allowedSponsoredOperations =
  setOf(
    ChangeTrustOperation::class,
    CreateAccountOperation::class,
    ManageDataOperation::class,
    ManageBuyOfferOperation::class,
    ManageSellOfferOperation::class,
    SetOptionsOperation::class
  )
