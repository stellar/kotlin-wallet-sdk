package org.stellar.walletsdk.horizon.transaction

import org.stellar.sdk.*
import org.stellar.walletsdk.horizon.AccountKeyPair

class SponsoringBuilder
internal constructor(
  sourceAccount: String,
  private val sponsorAccount: AccountKeyPair,
  override val operations: MutableList<Operation>,
) : CommonTransactionBuilder<SponsoringBuilder>(sourceAccount) {
  init {
    startSponsoring(sourceAddress)
  }

  /**
   * Create an account in the network.
   *
   * @param newAccount Key pair of an account to be created.
   * @param startingBalance optional Starting account balance in XLM. Default value is 0.
   * @throws [InvalidSponsoredAccountException]
   */
  fun createAccount(newAccount: AccountKeyPair, startingBalance: ULong = 0u): SponsoringBuilder =
    building {
      doCreateAccount(newAccount, startingBalance, sponsorAccount.address)
    }

  private fun startSponsoring(address: String) = building {
    BeginSponsoringFutureReservesOperation.Builder(address)
      .setSourceAccount(sponsorAccount.address)
      .build()
  }

  internal fun stopSponsoring() = building { EndSponsoringFutureReservesOperation(sourceAddress) }

  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: ManageDataOperation) = building { operation }
  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: ManageBuyOfferOperation) = building { operation }
  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: ManageSellOfferOperation) = building { operation }
  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: SetOptionsOperation) = building { operation }
}
