package org.stellar.walletsdk.horizon.transaction

import org.stellar.walletsdk.horizon.AccountKeyPair

actual class SponsoringBuilder {
  /**
   * Create an account in the network.
   *
   * @param newAccount Key pair of an account to be created.
   * @param startingBalance optional Starting account balance in XLM. Default value is 0.
   * @throws [InvalidSponsoredAccountException]
   */
  actual fun createAccount(newAccount: AccountKeyPair, startingBalance: UInt): SponsoringBuilder {
    TODO("Not yet implemented")
  }
}
