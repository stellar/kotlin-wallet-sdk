package org.stellar.walletsdk.js

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlin.js.Promise
import mu.KotlinLogging
import org.stellar.walletsdk.auth.Auth
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.auth.WalletSigner
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.util.UtilJS.promise

private val log = KotlinLogging.logger {}

@JsExport
@JsName("Auth")
class AuthJS internal constructor(private val auth: Auth) {

  fun authenticate(
    accountAddress: AccountKeyPair,
    walletSigner: WalletSigner? = null,
    memoId: String? = null,
    clientDomain: String? = null
  ): Promise<AuthToken> = promise { auth.authenticate(accountAddress, walletSigner) }
}
