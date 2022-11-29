package org.stellar.walletsdk.util

import okhttp3.OkHttpClient
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.NetworkRequestFailedException
import org.stellar.walletsdk.exception.NoAccountSignersException

/**
 * Helper to register account with a recovery server using
 * [SEP-30](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md) and
 * [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md).
 *
 * @param endpoint Recovery server's endpoint URL
 * @param webAuthEndpoint Recovery server's web auth endpoint URL to use with SEP-10
 * @param homeDomain Recovery server's domain hosting stellar.toml (
 * [SEP-1](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0001.md)) file
 * containing `WEB_AUTH_ENDPOINT` URL and `SIGNING_KEY`
 * @param accountAddress Stellar address of the account that is registering with the recovery server
 * @param accountIdentity A list of account identities to be registered with the recovery server
 * @param walletSigner [WalletSigner] interface to define wallet client signing methods
 *
 * @return Stellar address of signer
 *
 * @throws [NetworkRequestFailedException] when request fails
 * @throws [NoAccountSignersException] if there are no signers on the recovery server for this
 * account
 */
suspend fun setRecoveryMethods(
  endpoint: String,
  webAuthEndpoint: String,
  homeDomain: String,
  accountAddress: String,
  accountIdentity: List<RecoveryAccountIdentity>,
  walletSigner: WalletSigner
): String {
  val gson = GsonUtils.instance!!
  val okHttpClient = OkHttpClient()

  val authToken =
    Auth(
        accountAddress = accountAddress,
        webAuthEndpoint = webAuthEndpoint,
        homeDomain = homeDomain,
        walletSigner = walletSigner
      )
      .authenticate()

  val requestUrl = "$endpoint/accounts/$accountAddress"
  val request =
    OkHttpUtils.buildJsonPostRequest(
      requestUrl,
      RecoveryIdentities(identities = accountIdentity),
      authToken
    )

  return okHttpClient.newCall(request).execute().use { response ->
    if (!response.isSuccessful) throw NetworkRequestFailedException(response)

    val jsonResponse = gson.fromJson(response.body!!.charStream(), RecoveryAccount::class.java)

    getLatestRecoverySigner(jsonResponse.signers)
  }
}

/**
 * Helper to get the last recovery signer
 *
 * @param signers A list of recovery account signers
 *
 * @return Stellar address of the last signer
 *
 * @throws [NoAccountSignersException] if there are no signers on the recovery server for this
 * account
 */
fun getLatestRecoverySigner(signers: List<RecoveryAccountSigner>): String {
  if (signers.isEmpty()) {
    throw NoAccountSignersException
  }

  return signers[0].key
}
