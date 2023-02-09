package org.stellar.walletsdk.anchor

import io.ktor.client.*
import io.ktor.http.*
import mu.KotlinLogging
import okhttp3.internal.toImmutableMap
import org.stellar.walletsdk.InteractiveFlowResponse
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.Util.postJson

private val log = KotlinLogging.logger {}

/** Interactive flow for deposit and withdrawal using SEP-24. */
actual class Interactive
actual internal constructor(
  internal actual val anchor: Anchor,
  internal actual val httpClient: HttpClient,
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
  actual suspend fun withdraw(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>?,
    fundsAccountAddress: String?,
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
  actual suspend fun deposit(
    accountAddress: String,
    assetId: IssuedAssetId,
    authToken: AuthToken,
    extraFields: Map<String, String>?,
    fundsAccountAddress: String?,
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
    val info = anchor.getInfo()

    // Check if SEP-24 and SEP-10 are configured
    if (info.services.sep24 == null) {
      throw AnchorInteractiveFlowNotSupported
    } else if (!info.services.sep24.hasAuth) {
      throw AnchorAuthNotSupported
    }

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

    val url =
      URLBuilder(info.services.sep24.transferServerSep24)
        .appendPathSegments("transactions", type, "interactive")
        .build()
        .toString()

    // Get SEP-24 anchor response
    return httpClient.postJson(url, requestParams.toImmutableMap(), authToken)
  }
}
