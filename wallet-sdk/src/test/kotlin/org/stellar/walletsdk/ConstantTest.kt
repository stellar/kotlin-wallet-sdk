package org.stellar.walletsdk

import org.stellar.sdk.ClaimClaimableBalanceOperation
import org.stellar.sdk.CreateAccountOperation
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.SigningKeyPair
import org.stellar.walletsdk.horizon.toPublicKeyPair

const val HORIZON_URL = "https://horizon-testnet.stellar.org"
const val NETWORK_PASSPHRASE = "Test SDF Network ; September 2015"
const val MAX_BASE_FEE = 500

// GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22
val ADDRESS_ACTIVE =
  SigningKeyPair.fromSecret("SAICIEF6C7ENEPUCFYU5YE7CF6Z2BXUCYQ4MNB3RK3MMZ4GECYFJBWZ3")
val ADDRESS_ACTIVE_TWO =
  "GD2YC3HSNQEHSOTRCGGPOUN4J3DETJQR4ENPKY5WLF67XBOSVG5OIEQT".toPublicKeyPair()
val ADDRESS_INACTIVE = "GAW7QECBN2OI4LS4UUA3FO65Y2QPGQS3SPMTMRYK5ZX4IRZXXJPRWOBN".toPublicKeyPair()

const val ADDRESS_BASIC = "GAFRGFVBQHOLZBTDF75FHLECSJB547NFUGZZ4WG756FALNUYITK7R4JB"
const val ADDRESS_FULL = "GC5UMI2LAP4XF677VIN2EG7WRLPPPUHCE24JA6GTKETFT5IRJCTXJIFS"

val ASSET_USDC = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

const val AUTH_ENDPOINT = "https://testanchor.stellar.org/auth"
const val AUTH_HOME_DOMAIN = "testanchor.stellar.org"
const val AUTH_CLIENT_DOMAIN = "demo-wallet-server.stellar.org"
const val AUTH_CLIENT_DOMAIN_URL = "https://demo-wallet-server.stellar.org/sign"

val ASSET_SRT = IssuedAssetId("SRT", "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")
const val ANCHOR_SERVICE_URL = "https://testanchor.stellar.org/sep24"
const val ANCHOR_HOME_DOMAIN = "testanchor.stellar.org"

val OP_CREATE_ACCOUNT: CreateAccountOperation =
  CreateAccountOperation.Builder(ADDRESS_ACTIVE.address, "1")
    .setSourceAccount(ADDRESS_INACTIVE.address)
    .build()
val OP_CLAIM_CLAIMABLE_BALANCE: ClaimClaimableBalanceOperation =
  ClaimClaimableBalanceOperation.Builder(
      "000000009c05cd4bfc4db9774d0895be09929b199c0b7625d963e6203e0cdc0c6bb3bbae"
    )
    .setSourceAccount(ADDRESS_ACTIVE.address)
    .build()

// Source account GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22
@Suppress("MaxLineLength")
const val TXN_XDR_CREATE_ACCOUNT =
  "AAAAAgAAAAAZCaG2HvD37MucM8Z4qhClE0XQWhEakEgovVIZfS+4JgAAAGQACQnlAAAACAAAAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAAAAAAAAAAAAAAAAt+BBBbpyOLlylAbK73cag80Jbk9k2Rwrub8RHN7pfGwAAAAAAmJaAAAAAAAAAAAA="

val TestWallet =
  Wallet(StellarConfiguration.Testnet, ApplicationConfiguration(InProcessWalletSigner()))
