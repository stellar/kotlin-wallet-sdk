package org.stellar.walletsdk

import org.stellar.sdk.CreateAccountOperation

const val HORIZON_URL = "https://horizon-testnet.stellar.org"
const val NETWORK_PASSPHRASE = "Test SDF Network ; September 2015"

const val ADDRESS_ACTIVE = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
const val ADDRESS_INACTIVE = "GAW7QECBN2OI4LS4UUA3FO65Y2QPGQS3SPMTMRYK5ZX4IRZXXJPRWOBN"

val OP_CREATE_ACCOUNT: CreateAccountOperation =
  CreateAccountOperation.Builder(ADDRESS_ACTIVE, "1").setSourceAccount(ADDRESS_INACTIVE).build()
