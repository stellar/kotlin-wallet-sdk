package org.stellar.walletsdk.util

import org.stellar.sdk.*
import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold
import org.stellar.walletsdk.exception.InvalidSponsorOperationTypeException

/**
 * Operation to add new signer.
 *
 * @param signer New account signer
 *
 * @return operation
 */
fun addSignerOperation(signer: AccountSigner): SetOptionsOperation {
  val signerKeypair = KeyPair.fromAccountId(signer.address)
  val signerKey = Signer.ed25519PublicKey(signerKeypair)

  return SetOptionsOperation.Builder().setSigner(signerKey, signer.weight).build()
}

/**
 * Operation to set account threshold weights.
 *
 * @param accountThreshold account threshold
 *
 * @return operation
 */
fun setThresholdsOperation(accountThreshold: AccountThreshold): SetOptionsOperation {
  return SetOptionsOperation.Builder()
    .setLowThreshold(accountThreshold.low)
    .setMediumThreshold(accountThreshold.medium)
    .setHighThreshold(accountThreshold.high)
    .build()
}

/**
 * Operation to set account's master key weight.
 *
 * @param weight Master key weight to set
 *
 * @return operation
 */
fun setMasterKeyWeightOperation(weight: Int): SetOptionsOperation {
  return SetOptionsOperation.Builder().setMasterKeyWeight(weight).build()
}

/**
 * Operation to lock account's master key.
 *
 * @return operation
 */
fun lockMasterKeyOperation(): SetOptionsOperation {
  return setMasterKeyWeightOperation(0)
}

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
