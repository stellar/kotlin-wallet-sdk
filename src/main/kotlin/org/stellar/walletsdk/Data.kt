package org.stellar.walletsdk

data class RecoveryServerAuth(
  val endpoint: String,
  val signerAddress: String,
  val authToken: String,
)
