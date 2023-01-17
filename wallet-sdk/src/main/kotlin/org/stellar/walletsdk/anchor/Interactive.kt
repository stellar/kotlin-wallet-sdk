package org.stellar.walletsdk.anchor

import mu.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.internal.toImmutableMap
import org.stellar.walletsdk.*
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.toml.StellarToml
import org.stellar.walletsdk.toml.parseToml
import org.stellar.walletsdk.util.OkHttpUtils

private val log = KotlinLogging.logger {}

/**
 * Interactive flow for deposit and withdrawal using SEP-24.
 *
 * @param anchor instance of the [Anchor]
 * @param httpClient HTTP client
 * @return response object from the anchor
 * @throws [AnchorAssetException] if asset was refused by the anchor
 * @throws [ServerRequestFailedException] if network request fails
 */
class Interactive
internal constructor(
  private val homeDomain: String,
  private val anchor: Anchor,
  private val cfg: Config,
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
   * @param assetId Asset code to deposit or withdraw
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   * @return response object from the anchor
   * @throws [AnchorAssetException] if asset was refused by the anchor
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun withdraw(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>? = null,
    fundsAccountAddress: String? = null,
  ): InteractiveFlowResponse {
    return flow(accountAddress, assetId, authToken, extraFields, fundsAccountAddress, "withdraw") {
      it.withdraw[assetId.code]
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
   * @param assetId Asset code to deposit or withdraw
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   * @return response object from the anchor
   * @throws [AnchorAssetException] if asset was refused by the anchor
   * @throws [ServerRequestFailedException] if network request fails
   */
  suspend fun deposit(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>? = null,
    fundsAccountAddress: String? = null,
  ): InteractiveFlowResponse {
    return flow(accountAddress, assetId, authToken, extraFields, fundsAccountAddress, "deposit") {
      it.deposit[assetId.code]
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
   *
   * It should regularly encode InteractiveRequest (typed) + append all fields from sep9 (currently
   * string)
   * [documentation](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md#request)
   */
  @Suppress("LongParameterList", "ThrowsCount")
  private suspend fun flow(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>?,
    fundsAccountAddress: String?,
    type: String,
    assetGet: (AnchorServiceInfo) -> AnchorServiceAsset?
  ): InteractiveFlowResponse {
    val toml = StellarToml(cfg.scheme, homeDomain, httpClient)
    val tomlInfo = parseToml(toml.getToml())

    // Check if SEP-24 and SEP-10 are configured
    if (tomlInfo.services.sep24 == null) {
      throw AnchorInteractiveFlowNotSupported
    } else if (!tomlInfo.services.sep24.hasAuth) {
      throw AnchorAuthNotSupported
    }

    val transferServerEndpoint = tomlInfo.services.sep24.transferServerSep24

    val serviceInfo = anchor.getServicesInfo()

    // Check if deposit/withdraw is enabled for the asset

    val asset = assetGet(serviceInfo) ?: throw AssetNotAcceptedForDepositException(assetId)

    if (!asset.enabled) {
      throw AssetNotEnabledForDepositException(assetId)
    }

    val requestParams = mutableMapOf<String, String>()
    val account = fundsAccountAddress ?: accountAddress
    requestParams["account"] = fundsAccountAddress ?: accountAddress
    requestParams["asset_code"] = assetId.code
    requestParams["asset_issuer"] = assetId.issuer

    if (extraFields != null) {
      requestParams += extraFields
    }

    log.debug { "Interactive $type request: account = $account, asset_code = ${assetId.code}" }

    // Get SEP-24 anchor response
    val requestUrl = "$transferServerEndpoint/transactions/${type}/interactive"
    val request = OkHttpUtils.makePostRequest(requestUrl, requestParams.toImmutableMap(), authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw ServerRequestFailedException(response)

      response.toJson()
    }
  }
}
