package org.stellar.walletsdk.util

import org.stellar.sdk.*

private val allowedSponsoredOperations =
  setOf(
    ChangeTrustOperation::class,
    CreateAccountOperation::class,
    ManageDataOperation::class,
    ManageBuyOfferOperation::class,
    ManageSellOfferOperation::class,
    SetOptionsOperation::class
  )
