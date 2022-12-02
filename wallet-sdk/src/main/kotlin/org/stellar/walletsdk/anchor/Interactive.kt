package org.stellar.walletsdk.anchor

import okhttp3.OkHttpClient
import org.stellar.sdk.Server
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.GsonUtils
import org.stellar.walletsdk.util.OkHttpUtils
import org.stellar.walletsdk.util.StellarToml

/**
 * Interactive flow for deposit and withdrawal using SEP-24.
 *
 * @param anchor instance of the [Anchor]
 * @param server Horizon [Server] instance
 * @param httpClient HTTP client
 *
 * @return response object from the anchor
 *
 * @throws [AnchorAssetException] if asset was refused by the anchor
 * @throws [AnchorRequestFailedException] if network request fails
 */
class Interactive(
  private val homeDomain: String,
  private val anchor: Anchor,
  private val server: Server,
  private val httpClient: OkHttpClient,
) {
  /**
   * Initiates interactive withdrawal using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param accountAddress Stellar address of the account, used for authentication and by default
   * for depositing or withdrawing funds
   * @param fundsAccountAddress optional Stellar address of the account for depositing or
   * withdrawing funds, if different from the account address
   * @param assetCode Asset code to deposit or withdraw
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   *
   * @return response object from the anchor
   *
   * @throws [AnchorAssetException] if asset was refused by the anchor
   * @throws [AnchorRequestFailedException] if network request fails
   */
  suspend fun withdraw(
    accountAddress: String,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, Any>? = null,
    fundsAccountAddress: String? = null,
  ): InteractiveFlowResponse {
    return flow(
      accountAddress,
      assetCode,
      authToken,
      extraFields,
      fundsAccountAddress,
      "withdraw"
    ) { it.withdraw[assetCode] }
  }

  /**
   * Initiates interactive deposit using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param accountAddress Stellar address of the account, used for authentication and by default
   * for depositing or withdrawing funds
   * @param fundsAccountAddress optional Stellar address of the account for depositing or
   * withdrawing funds, if different from the account address
   * @param assetCode Asset code to deposit or withdraw
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   *
   * @return response object from the anchor
   *
   * @throws [AnchorAssetException] if asset was refused by the anchor
   * @throws [AnchorRequestFailedException] if network request fails
   */
  suspend fun deposit(
    accountAddress: String,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, Any>? = null,
    fundsAccountAddress: String? = null,
  ): InteractiveFlowResponse {
    return flow(accountAddress, assetCode, authToken, extraFields, fundsAccountAddress, "deposit") {
      it.deposit[assetCode]
    }
  }

  private suspend fun flow(
    accountAddress: String,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, Any>?,
    fundsAccountAddress: String?,
    type: String,
    assetGet: (AnchorServiceInfo) -> AnchorServiceAsset?
  ): InteractiveFlowResponse {
    val sep24RequiredFields =
      listOf(
        StellarTomlField.SIGNING_KEY.text,
        StellarTomlField.TRANSFER_SERVER_SEP0024.text,
        StellarTomlField.WEB_AUTH_ENDPOINT.text
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
      tomlContent[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()

    val serviceInfo = anchor.getServicesInfo(transferServerEndpoint)

    // Check if deposit/withdraw is enabled for the asset

    val asset = assetGet(serviceInfo) ?: throw AssetNotAcceptedForDepositException(assetCode)

    if (!asset.enabled) {
      throw AssetNotEnabledForDepositException(assetCode)
    }

    val requestParams = mutableMapOf<String, Any>()
    requestParams["account"] = fundsAccountAddress ?: accountAddress
    requestParams["asset_code"] = assetCode

    if (extraFields != null) {
      requestParams += extraFields
    }

    // Get SEP-24 anchor response
    val gson = GsonUtils.instance!!

    val requestUrl = "$transferServerEndpoint/transactions/${type}/interactive"
    val request = OkHttpUtils.buildJsonPostRequest(requestUrl, requestParams, authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw AnchorRequestFailedException(response)

      gson.fromJson(response.body!!.charStream(), InteractiveFlowResponse::class.java)
    }
  }
}
