package org.stellar.walletsdk.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.*

/**
 * Interactive flow for deposit and withdrawal using SEP-24.
 *
 * @param type chosen flow (deposit or withdrawal)
 * @param accountAddress Stellar address of the account
 * @param assetCode Asset code to use
 * @param assetIssuer Asset issuer to use
 * @param memoId optional memo ID to distinguish the account
 * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
 * @param extraFields Additional information to pass to the anchor
 * @param walletSigner interface to define wallet client and domain (if using `clientDomain`)
 * signing methods
 * @param anchor instance of the [Anchor]
 * @param server Horizon [Server] instance
 * @param network Stellar [Network] instance
 * @param httpClient HTTP client
 *
 * @return response object from the anchor
 *
 * @throws [AssetNotAcceptedForDepositException] if asset is not accepted for deposits
 * @throws [AssetNotEnabledForDepositException] if asset is not enabled for deposits by the anchor
 * @throws [AssetNotAcceptedForWithdrawalException] if asset is not accepted for withdrawals
 * @throws [AssetNotEnabledForWithdrawalException] if asset is not enabled for withdrawals by the
 * anchor
 * @throws [NetworkRequestFailedException] if network request fails
 */
suspend fun interactiveFlow(
  type: InteractiveFlowType,
  accountAddress: String,
  assetCode: String,
  assetIssuer: String,
  memoId: String? = null,
  clientDomain: String? = null,
  extraFields: Map<String, Any>? = null,
  walletSigner: WalletSigner,
  anchor: Anchor,
  server: Server,
  network: Network,
  httpClient: OkHttpClient,
): InteractiveFlowResponse {
  return CoroutineScope(Dispatchers.IO)
    .async {
      val sep24RequiredFields =
        listOf(
          StellarTomlFields.SIGNING_KEY.text,
          StellarTomlFields.TRANSFER_SERVER_SEP0024.text,
          StellarTomlFields.WEB_AUTH_ENDPOINT.text
        )
      val toml = StellarToml(stellarAddress = assetIssuer, server, httpClient)
      val tomlContent = toml.getToml()

      // Check toml for required SEP-24 fields
      toml.hasFields(fields = sep24RequiredFields, tomlContent = tomlContent)

      val transferServerEndpoint =
        tomlContent[StellarTomlFields.TRANSFER_SERVER_SEP0024.text].toString()

      val serviceInfo = anchor.getServicesInfo(transferServerEndpoint)

      // Check if deposit/withdraw is enabled for the asset
      if (type == InteractiveFlowType.DEPOSIT) {
        val asset =
          serviceInfo.deposit[assetCode] ?: throw AssetNotAcceptedForDepositException(assetCode)

        if (!asset.enabled) {
          throw AssetNotEnabledForDepositException(assetCode)
        }
      } else {
        val asset =
          serviceInfo.withdraw[assetCode] ?: throw AssetNotAcceptedForWithdrawalException(assetCode)

        if (!asset.enabled) {
          throw AssetNotEnabledForWithdrawalException(assetCode)
        }
      }

      val homeDomain = toml.getHomeDomain()

      // SEP-10 auth
      val auth =
        Auth(
          accountAddress = accountAddress,
          webAuthEndpoint = tomlContent[StellarTomlFields.WEB_AUTH_ENDPOINT.text].toString(),
          homeDomain = homeDomain,
          clientDomain = clientDomain,
          memoId = memoId,
          networkPassPhrase = network.networkPassphrase,
          walletSigner = walletSigner,
          httpClient = httpClient
        )
      val authToken = auth.authenticate()

      val requestParams = mutableMapOf<String, Any>()
      requestParams["account"] = accountAddress
      requestParams["asset_code"] = assetCode

      if (extraFields != null) {
        requestParams += extraFields
      }

      // Get SEP-24 anchor response
      val gson = GsonUtils.instance!!

      val requestUrl = "$transferServerEndpoint/transactions/${type.value}/interactive"
      val request = OkHttpUtils.buildJsonPostRequest(requestUrl, requestParams, authToken)

      httpClient.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw NetworkRequestFailedException(response)

        return@async gson.fromJson(
          response.body!!.charStream(),
          InteractiveFlowResponse::class.java
        )
      }
    }
    .await()
}
