package org.stellar.walletsdk.horizon.transaction

import org.stellar.sdk.*
import org.stellar.sdk.responses.AccountResponse
import org.stellar.walletsdk.exception.InvalidSponsorOperationTypeException
import org.stellar.walletsdk.exception.InvalidSponsoredAccountException
import org.stellar.walletsdk.horizon.AccountKeyPair

class SponsoringBuilder
internal constructor(
  sourceAccount: AccountResponse,
  private val sponsorAccount: AccountKeyPair,
  override val operations: MutableList<Operation>,
) : CommonTransactionBuilder<SponsoringBuilder>(sourceAccount) {
  companion object {
    internal val allowedSponsoredOperations =
      setOf(
        ChangeTrustOperation::class,
        CreateAccountOperation::class,
        ManageDataOperation::class,
        ManageBuyOfferOperation::class,
        ManageSellOfferOperation::class,
        SetOptionsOperation::class
      )
  }

  private var accountCreated: AccountKeyPair? = null

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
  fun createAccount(newAccount: AccountKeyPair, startingBalance: UInt = 0u): SponsoringBuilder =
    building {
      // Because sponsoring is different for new account creation, it's required to remove default
      // sponsoring operation from the list and replace it with a "special" sponsoring operation for
      // account creation.
      if (operations.removeLast() !is BeginSponsoringFutureReservesOperation) {
        throw InvalidSponsoredAccountException
      }

      accountCreated = newAccount

      startSponsoring(newAccount.address)

      doCreateAccount(newAccount, startingBalance)
    }

  private fun startSponsoring(address: String) = building {
    BeginSponsoringFutureReservesOperation.Builder(address)
      .setSourceAccount(sponsorAccount.address)
      .build()
  }

  internal fun stopSponsoring() = building {
    val created = accountCreated
    // Because of special sponsoring operation unique to account creation, other types of operations
    // are prohibited in the sponsoring block.
    if (created != null) {
      val last = operations.last()
      val secondLast = operations[operations.size - 2]

      if (
        last !is CreateAccountOperation && secondLast !is BeginSponsoringFutureReservesOperation
      ) {
        throw InvalidSponsoredAccountException
      }

      EndSponsoringFutureReservesOperation(created.address)
    } else {
      EndSponsoringFutureReservesOperation(sourceAddress)
    }
  }

  /**
   * Adds operation to this builder
   *
   * @param operation operation that can be created using SDK
   */
  fun addOperation(operation: Operation) = building {
    if (!allowedSponsoredOperations.contains(operation::class)) {
      throw InvalidSponsorOperationTypeException(operation)
    }

    operation
  }
}
