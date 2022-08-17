package org.stellar.walletsdk

import org.stellar.sdk.CreateAccountOperation
import org.stellar.sdk.Network

class Constants {
  companion object {
    const val HORIZON_URL = "https://horizon-testnet.stellar.org"
    val NETWORK_PASSPHRASE = Network.TESTNET.toString()

    const val ADDRESS_ACTIVE = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
    const val ADDRESS_INACTIVE = "GAW7QECBN2OI4LS4UUA3FO65Y2QPGQS3SPMTMRYK5ZX4IRZXXJPRWOBN"

    val OP_CREATE_ACCOUNT: CreateAccountOperation =
      CreateAccountOperation.Builder(ADDRESS_ACTIVE, "1").setSourceAccount(ADDRESS_INACTIVE).build()
  }
}
