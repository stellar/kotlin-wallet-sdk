package org.stellar.walletsdk.auth

import org.stellar.sdk.Network
import org.stellar.walletsdk.horizon.Transaction

internal actual fun ChallengeResponse.toTransaction(): Transaction {
  return Transaction.fromEnvelopeXdr(this.transaction, Network(this.networkPassphrase))
    as Transaction
}

internal actual fun Transaction.hasClientDomain(): Boolean {
  return this.operations.any {
    it.toXdr().body?.manageDataOp?.dataName?.string64.toString() == "client_domain"
  }
}
