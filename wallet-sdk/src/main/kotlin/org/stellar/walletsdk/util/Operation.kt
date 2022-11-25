package org.stellar.walletsdk.util

import org.stellar.sdk.KeyPair
import org.stellar.sdk.SetOptionsOperation
import org.stellar.sdk.Signer
import org.stellar.walletsdk.AccountSigner
import org.stellar.walletsdk.AccountThreshold

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
