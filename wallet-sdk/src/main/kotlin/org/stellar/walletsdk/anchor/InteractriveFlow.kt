package org.stellar.walletsdk.anchor

import io.ktor.http.*
import kotlin.io.encoding.Base64
import mu.KotlinLogging
import okhttp3.internal.toImmutableMap
import org.stellar.walletsdk.InteractiveFlowResponse
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.NativeAssetId
import org.stellar.walletsdk.asset.StellarAssetId
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.*
import org.stellar.walletsdk.util.Util.postJson

private val log = KotlinLogging.logger {}

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
@Suppress("LongParameterList", "ThrowsCount", "TooGenericExceptionCaught", "SwallowedException")
internal suspend fun Interactive.flow(
  assetId: StellarAssetId,
  authToken: AuthToken,
  extraFields: Map<String, String>?,
  account: String?,
  memo: Pair<String, MemoType>?,
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

  val serviceInfo = getServicesInfo()

  // Check if deposit/withdraw is enabled for the asset

  val asset = assetGet(serviceInfo) ?: throw AssetNotAcceptedForDepositException(assetId)

  if (!asset.enabled) {
    throw AssetNotEnabledForDepositException(assetId)
  }

  val requestParams = mutableMapOf<String, String>()
  account?.run { requestParams["account"] = this }

  if (memo != null) {
    requestParams["memo"] = memo.first
    requestParams["memo_type"] = memo.second.serialName

    if (memo.second == MemoType.HASH) {
      try {
        Base64.decode(memo.first)
      } catch (e: Exception) {
        throw ValidationException("Hash memo must be base64 encoded")
      }
    }
  }
  when (assetId) {
    is IssuedAssetId -> {
      requestParams["asset_code"] = assetId.code
      requestParams["asset_issuer"] = assetId.issuer
    }
    is NativeAssetId -> {
      requestParams["asset_code"] = NativeAssetId.id
    }
  }

  if (extraFields != null) {
    requestParams += extraFields
  }

  log.debug {
    "Interactive $type request: account = $account, asset_code = ${requestParams["asset_code"]}"
  }

  val url =
    URLBuilder(info.services.sep24.transferServerSep24)
      .appendPathSegments("transactions", type, "interactive")
      .build()
      .toString()

  // Get SEP-24 anchor response
  return httpClient.postJson(url, requestParams.toImmutableMap(), authToken)
}
