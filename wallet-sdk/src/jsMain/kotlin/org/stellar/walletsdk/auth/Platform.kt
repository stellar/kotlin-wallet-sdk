package org.stellar.walletsdk.auth

import org.stellar.walletsdk.horizon.Transaction

internal actual fun ChallengeResponse.toTransaction(): Transaction {
  return Transaction(this.transaction, this.networkPassphrase)
}

internal actual fun Transaction.hasClientDomain(): Boolean {
  // TODO
  return false
}
