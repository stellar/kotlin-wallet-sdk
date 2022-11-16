package org.stellar.walletsdk.util

import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import org.stellar.sdk.Server
import org.stellar.walletsdk.*

/**
 * Interactive flow for deposit and withdrawal using SEP-24.
 *
 * @param type chosen flow (deposit or withdrawal)
 * @param accountAddress Stellar address of the account, used for authentication and by default for
 * depositing or withdrawing funds
 * @param fundsAccountAddress optional Stellar address of the account for depositing or withdrawing
 * funds, if different from the account address
 * @param homeDomain home domain of the anchor
 * @param assetCode Asset code to deposit or withdraw
 * @param authToken Auth token from the anchor (account's authentication using SEP-10)
 * @param extraFields Additional information to pass to the anchor
 * @param anchor instance of the [Anchor]
 * @param server Horizon [Server] instance
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
  fundsAccountAddress: String? = null,
  homeDomain: String,
  assetCode: String,
  authToken: String,
  extraFields: Map<String, Any>? = null,
  anchor: Anchor,
  server: Server,
  httpClient: OkHttpClient,
): InteractiveFlowResponse = coroutineScope {
  val sep24RequiredFields =
    listOf(
      StellarTomlFields.SIGNING_KEY.text,
      StellarTomlFields.TRANSFER_SERVER_SEP0024.text,
      StellarTomlFields.WEB_AUTH_ENDPOINT.text
    )
  val toml = StellarToml(homeDomain, server, httpClient)
  val tomlContent = toml.getToml()

  // Check toml for required SEP-24 fields
  val missingFields = mutableListOf<String>()

  sep24RequiredFields.forEach { field ->
    if (tomlContent[field] == null) {
      missingFields.add(field)
    }
  }

  if (missingFields.size > 0) {
    throw StellarTomlMissingFields(missingFields)
  }

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

  val requestParams = mutableMapOf<String, Any>()
  requestParams["account"] = fundsAccountAddress ?: accountAddress
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

    gson.fromJson(response.body!!.charStream(), InteractiveFlowResponse::class.java)
  }
}
