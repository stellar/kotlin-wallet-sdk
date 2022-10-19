package org.stellar.walletsdk.util

import org.stellar.sdk.KeyPair
import org.stellar.sdk.SetOptionsOperation
import org.stellar.sdk.Signer
import org.stellar.walletsdk.AccountSigner

fun addSignerOperation(signer: AccountSigner): SetOptionsOperation {
  val signerKeypair = KeyPair.fromAccountId(signer.address)
  val signerKey = Signer.ed25519PublicKey(signerKeypair)

  return SetOptionsOperation.Builder().setSigner(signerKey, signer.weight).build()
}

fun setThresholdsOperation(low: Int, medium: Int, high: Int): SetOptionsOperation {
  return SetOptionsOperation.Builder()
    .setLowThreshold(low)
    .setMediumThreshold(medium)
    .setHighThreshold(high)
    .build()
}

fun setMasterKeyWeightOperation(weight: Int): SetOptionsOperation {
  return SetOptionsOperation.Builder().setMasterKeyWeight(weight).build()
}

fun lockMasterKeyOperation(): SetOptionsOperation {
  return setMasterKeyWeightOperation(0)
}
