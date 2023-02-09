package org.stellar.walletsdk.anchor

import org.stellar.walletsdk.Config
import org.stellar.walletsdk.InteractiveFlowResponse
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.auth.AuthToken

/**
 * Interactive flow for deposit and withdrawal using SEP-24.
 *
 * @param anchor instance of the [Anchor]
 * @param httpClient HTTP client
 * @return response object from the anchor
 * @throws [AnchorAssetException] if asset was refused by the anchor
 * @throws [ServerRequestFailedException] if network request fails
 */
expect class Interactive internal constructor(homeDomain: String, anchor: Anchor, cfg: Config) {
  internal val homeDomain: String
  internal val anchor: Anchor
  internal val cfg: Config

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
  ): InteractiveFlowResponse

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
  ): InteractiveFlowResponse
}
