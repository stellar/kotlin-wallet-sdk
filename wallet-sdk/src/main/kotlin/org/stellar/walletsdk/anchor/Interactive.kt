package org.stellar.walletsdk.anchor

import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.internal.toImmutableMap
import org.stellar.sdk.Server
import org.stellar.walletsdk.*
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.OkHttpUtils
import org.stellar.walletsdk.util.StellarToml
import org.stellar.walletsdk.util.toJson

private val log = KotlinLogging.logger {}

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
 * @throws [ServerRequestFailedException] if network request fails
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
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun withdraw(
    accountAddress: String,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, String>? = null,
    fundsAccountAddress: String? = null,
  ): InteractiveFlowResponse {
    return flow(
      accountAddress,
      assetCode,
      authToken,
      extraFields,
      fundsAccountAddress,
      "withdraw"
    ) {
      it.withdraw[assetCode]
    }
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
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun deposit(
    accountAddress: String,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, String>? = null,
    fundsAccountAddress: String? = null,
  ): InteractiveFlowResponse {
    return flow(accountAddress, assetCode, authToken, extraFields, fundsAccountAddress, "deposit") {
      it.deposit[assetCode]
    }
  }

  /**
   * TODO: there should be a custom serializer Signature of method:
   * ```kotlin
   * fun flow(
   *  request: InteractiveRequest,
   *  requestMapper: (InteractiveRequest) -> (Unit) = {},
   *  extraSEP9: Map<String, String>
   * )
   * ```
   * It should regularly encode InteractiveRequest (typed) + append all fields from sep9 (currently
   * string)
   * [documentation](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md#request)
   */
  private suspend fun flow(
    accountAddress: String,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, String>?,
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

    val requestParams = mutableMapOf<String, String>()
    val account = fundsAccountAddress ?: accountAddress
    requestParams["account"] = account
    requestParams["asset_code"] = assetCode

    if (extraFields != null) {
      requestParams += extraFields
    }

    log.debug { "Interactive $type request: account = $account, asset_code = $assetCode" }

    // Get SEP-24 anchor response
    val requestUrl = "$transferServerEndpoint/transactions/${type}/interactive"
    val request = OkHttpUtils.makePostRequest(requestUrl, requestParams.toImmutableMap(), authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson()
    }
  }
}
