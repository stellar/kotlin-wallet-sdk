package org.stellar.walletsdk

import org.stellar.sdk.ClaimClaimableBalanceOperation
import org.stellar.sdk.CreateAccountOperation

const val HORIZON_URL = "https://horizon-testnet.stellar.org"
const val NETWORK_PASSPHRASE = "Test SDF Network ; September 2015"
const val MAX_BASE_FEE = 500

const val ADDRESS_ACTIVE = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
const val ADDRESS_ACTIVE_SECRET = "SAICIEF6C7ENEPUCFYU5YE7CF6Z2BXUCYQ4MNB3RK3MMZ4GECYFJBWZ3"
const val ADDRESS_ACTIVE_TWO = "GD2YC3HSNQEHSOTRCGGPOUN4J3DETJQR4ENPKY5WLF67XBOSVG5OIEQT"
const val ADDRESS_INACTIVE = "GAW7QECBN2OI4LS4UUA3FO65Y2QPGQS3SPMTMRYK5ZX4IRZXXJPRWOBN"

const val USDC_ASSET_CODE = "USDC"
const val USDC_ASSET_ISSUER = "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5"

const val AUTH_ENDPOINT = "https://testanchor.stellar.org/auth"
const val AUTH_HOME_DOMAIN = "testanchor.stellar.org"
const val AUTH_CLIENT_DOMAIN = "demo-wallet-server.stellar.org"
const val AUTH_CLIENT_DOMAIN_URL = "https://demo-wallet-server.stellar.org/sign"

val OP_CREATE_ACCOUNT: CreateAccountOperation =
  CreateAccountOperation.Builder(ADDRESS_ACTIVE, "1").setSourceAccount(ADDRESS_INACTIVE).build()
val OP_CLAIM_CLAIMABLE_BALANCE: ClaimClaimableBalanceOperation =
  ClaimClaimableBalanceOperation.Builder(
      "000000009c05cd4bfc4db9774d0895be09929b199c0b7625d963e6203e0cdc0c6bb3bbae"
    )
    .setSourceAccount(ADDRESS_ACTIVE)
    .build()

// Source account GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22
const val TXN_XDR_CREATE_ACCOUNT =
  "AAAAAgAAAAAZCaG2HvD37MucM8Z4qhClE0XQWhEakEgovVIZfS+4JgAAAGQACQnlAAAACAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAt+BBBbpyOLlylAbK73cag80Jbk9k2Rwrub8RHN7pfGwAAAAAAmJaAAAAAAAAAAAA="
