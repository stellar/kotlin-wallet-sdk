package org.stellar.walletsdk.auth

import org.stellar.walletsdk.Config
import org.stellar.walletsdk.horizon.AccountKeyPair

/**
 * Authenticate to an external server using
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md).
 *
 * @property webAuthEndpoint Authentication endpoint URL
 * @property homeDomain Domain hosting stellar.toml (
 * [SEP-1](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)) file
 * containing `WEB_AUTH_ENDPOINT` URL and `SIGNING_KEY`
 * @property network Stellar network
 * @property defaultSigner interface to define wallet client and domain (if using `clientDomain`)
 * signing methods
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
actual class Auth internal actual constructor(
    cfg: Config,
    webAuthEndpoint: String,
    homeDomain: String
) {
    internal actual val cfg: Config
        get() = TODO("Not yet implemented")
    internal actual val webAuthEndpoint: String
        get() = TODO("Not yet implemented")
    internal actual val homeDomain: String
        get() = TODO("Not yet implemented")

    /**
     * Authenticates to an external server.
     *
     * @param accountAddress Stellar address of the account authenticating
     * @param walletSigner overriding [Auth.defaultSigner] to use in this authentication
     * @param memoId optional memo ID to distinguish the account
     * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
     * @return authentication token (JWT)
     * @throws [ValidationException] when some of the request arguments are not valid
     * @throws [ServerRequestFailedException] when request fails
     * @throws [InvalidResponseException] when JSON response is malformed
     */
    actual suspend fun authenticate(
        accountAddress: AccountKeyPair,
        walletSigner: WalletSigner?,
        memoId: String?,
        clientDomain: String?
    ): AuthToken {
        TODO("Not yet implemented")
    }

}