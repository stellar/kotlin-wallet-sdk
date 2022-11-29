package org.stellar.walletsdk

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.stellar.sdk.Asset
import org.stellar.sdk.Network
import org.stellar.sdk.Server
import org.stellar.walletsdk.util.*

/**
 * Build on/off ramps with anchors.
 *
 * @property server Horizon [Server] instance
 * @property network Stellar [Network] instance
 * @property homeDomain home domain of the anchor
 * @property httpClient optional custom HTTP client, uses [OkHttpClient] by default
 */
class Anchor(
  private val server: Server,
  private val network: Network,
  private val homeDomain: String,
  private val httpClient: OkHttpClient = OkHttpClient()
) {
  private val gson = GsonUtils.instance!!

  /**
   * Get anchor information from a TOML file.
   *
   * @return TOML file content
   */
  suspend fun getInfo(): Map<String, Any> {
    val toml = StellarToml(homeDomain, server, httpClient)

    return toml.getToml()
  }

  /**
   * Authenticate account with the anchor using SEP-10.
   *
   * @param accountAddress Stellar address of the account to authenticate
   * @param clientDomain optional domain hosting stellar.toml file containing `SIGNING_KEY`
   * @param memoId optional memo ID to distinguish the account
   * @param toml Anchor's stellar.toml file containing `WEB_AUTH_ENDPOINT`
   * @param walletSigner interface to define wallet client and domain (if using `clientDomain`)
   * signing methods
   *
   * @return JWT auth token
   */
  suspend fun getAuthToken(
    accountAddress: String,
    clientDomain: String? = null,
    memoId: String? = null,
    toml: Map<String, Any>,
    walletSigner: WalletSigner
  ): String {
    return Auth(
        accountAddress = accountAddress,
        webAuthEndpoint = toml[StellarTomlField.WEB_AUTH_ENDPOINT.text].toString(),
        homeDomain = homeDomain,
        clientDomain = clientDomain,
        memoId = memoId,
        networkPassPhrase = network.networkPassphrase,
        walletSigner = walletSigner,
        httpClient = httpClient
      )
      .authenticate()
  }

  /**
   * Available anchor services and information about them. For example, limits, currency, fees,
   * payment methods.
   *
   * @param serviceUrl URL where `/info` endpoint is hosted
   *
   * @return a list of available anchor services
   *
   * @throws [NetworkRequestFailedException] if network request fails
   * @throws [InvalidAnchorServiceUrl] if provided service URL is not a valid URL
   */
  suspend fun getServicesInfo(serviceUrl: String): AnchorServiceInfo {
    val url: HttpUrl

    try {
      url = serviceUrl.toHttpUrl()
    } catch (e: Exception) {
      throw InvalidAnchorServiceUrl(e)
    }

    val urlBuilder = HttpUrl.Builder().scheme(url.scheme).host(url.host).port(url.port)

    url.pathSegments.forEach { urlBuilder.addPathSegment(it) }
    urlBuilder.addPathSegment("info")

    val infoUrl = urlBuilder.build().toString()

    val request = Request.Builder().url(infoUrl).build()
    val gson = GsonUtils.instance!!

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      val infoResponse = response.body!!.charStream()
      gson.fromJson(infoResponse, AnchorServiceInfo::class.java)
    }
  }

  /**
   * Interactive deposit flow using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param accountAddress Stellar address of the account, used for authentication and by default
   * for depositing funds
   * @param fundsAccountAddress optional Stellar address of the account for depositing funds, if
   * different from the account address
   * @param assetCode Asset code to deposit
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   *
   * @return response object from the anchor
   *
   * @throws [AssetNotAcceptedForDepositException] if asset is not accepted for deposits
   * @throws [AssetNotEnabledForDepositException] if asset is not enabled for deposits by the anchor
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getInteractiveDeposit(
    accountAddress: String,
    fundsAccountAddress: String? = null,
    assetCode: String,
    authToken: String,
  ): InteractiveFlowResponse {
    return interactiveFlow(
      type = InteractiveFlowType.DEPOSIT,
      accountAddress = accountAddress,
      fundsAccountAddress = fundsAccountAddress,
      homeDomain = homeDomain,
      assetCode = assetCode,
      authToken = authToken,
      anchor = this,
      server = server,
      httpClient = httpClient
    )
  }

  /**
   * Interactive withdrawal flow using
   * [SEP-24](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0024.md).
   *
   * @param accountAddress Stellar address of the account, used for authentication and by default
   * for withdrawing funds
   * @param fundsAccountAddress optional Stellar address of the account for withdrawing funds, if
   * different from the account address
   * @param assetCode Asset code to deposit
   * @param authToken Auth token from the anchor (account's authentication using SEP-10)
   * @param extraFields Additional information to pass to the anchor
   *
   * @return response object from the anchor
   *
   * @throws [AssetNotAcceptedForWithdrawalException] if asset is not accepted for withdrawals
   * @throws [AssetNotEnabledForWithdrawalException] if asset is not enabled for withdrawals by the
   * anchor
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getInteractiveWithdrawal(
    accountAddress: String,
    fundsAccountAddress: String? = null,
    assetCode: String,
    authToken: String,
    extraFields: Map<String, Any>? = null,
  ): InteractiveFlowResponse {
    return interactiveFlow(
      type = InteractiveFlowType.WITHDRAW,
      accountAddress = accountAddress,
      fundsAccountAddress = fundsAccountAddress,
      homeDomain = homeDomain,
      assetCode = assetCode,
      authToken = authToken,
      extraFields = extraFields,
      anchor = this,
      server = server,
      httpClient = httpClient
    )
  }

  // TODO: is this for SEP-24 only?
  // TODO: handle extra fields
  /**
   * Get single transaction's current status and details.
   *
   * @param transactionId transaction ID
   * @param authToken auth token of the account authenticated with the anchor
   * @param toml Anchor's stellar.toml file containing `WEB_AUTH_ENDPOINT`
   *
   * @return transaction object
   *
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getTransactionStatus(
    transactionId: String,
    authToken: String,
    toml: Map<String, Any>
  ): AnchorTransaction {
    val transferServerEndpoint = toml[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointUrl = "$transferServerEndpoint/transaction?id=$transactionId"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      gson
        .fromJson(response.body!!.charStream(), AnchorTransactionStatusResponse::class.java)
        .transaction
    }
  }

  // TODO: is this for SEP-24 only?
  // TODO: handle extra fields
  /**
   * Get all account's transactions by specified asset.
   *
   * @param assetCode asset's code
   * @param authToken auth token of the account authenticated with the anchor
   * @param toml Anchor's stellar.toml file containing `WEB_AUTH_ENDPOINT`
   *
   * @return transaction object
   *
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getAllTransactionStatus(
    assetCode: String,
    authToken: String,
    toml: Map<String, Any>
  ): List<AnchorTransaction> {
    val transferServerEndpoint = toml[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointUrl = "$transferServerEndpoint/transactions?asset_code=$assetCode"
    val request = OkHttpUtils.buildStringGetRequest(endpointUrl, authToken)

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      gson
        .fromJson(response.body!!.charStream(), AnchorAllTransactionsResponse::class.java)
        .transactions
    }
  }

  /**
   * Get account transactions for specified asset. Optional field implementation depends on anchor.
   *
   * @param assetCode asset's code
   * @param authToken auth token of the account authenticated with the anchor
   * @param toml Anchor's stellar.toml file containing `CURRENCIES` list of supported assets
   * @param limit optional how many transactions to fetch
   * @param pagingId optional return transactions prior to this ID
   * @param noOlderThan optional return transactions starting on or after this date and time
   * @param lang optional language code specified using
   * [RFC 4646](https://www.rfc-editor.org/rfc/rfc4646), default is `en`
   *
   * @return a list of formatted operations
   *
   * @throws [NetworkRequestFailedException] if network request fails
   */
  suspend fun getHistory(
    assetCode: String,
    authToken: String,
    toml: Map<String, Any>,
    limit: Int? = null,
    pagingId: String? = null,
    noOlderThan: String? = null,
    lang: String? = null
  ): List<WalletOperation<AnchorTransaction>> {
    val anchorCurrency =
      ((toml as HashMap)["CURRENCIES"] as List<*>).filterIsInstance<HashMap<*, *>>().find {
        it["code"] == assetCode
      }
      // TODO: custom exception
      ?: throw Exception("Anchor does not support $assetCode asset")
    val asset = Asset.create("$assetCode:$anchorCurrency[\"issuer\"]")

    val transferServerEndpoint = toml[StellarTomlField.TRANSFER_SERVER_SEP0024.text].toString()
    val endpointHttpUrl = transferServerEndpoint.toHttpUrl()
    val endpointUrl = HttpUrl.Builder().scheme("https").host(endpointHttpUrl.host)

    // Add path segments, if there are any
    endpointHttpUrl.pathSegments.forEach { endpointUrl.addPathSegment(it) }

    // Add transactions path segment
    endpointUrl.addPathSegment("transactions")

    // Add query params
    val queryParams = mutableMapOf<String, String>()
    queryParams["asset_code"] = assetCode
    queryParams["limit"] = limit?.toString() ?: ""
    queryParams["paging_id"] = pagingId ?: ""
    queryParams["no_older_than"] = noOlderThan ?: ""
    queryParams["lang"] = lang ?: "en"

    queryParams
      .filter { it.value.isNotBlank() }
      .forEach { endpointUrl.addQueryParameter(it.key, it.value) }

    val request = OkHttpUtils.buildStringGetRequest(endpointUrl.build().toString(), authToken)
    val finalStatusList = listOf("completed", "refunded")

    return httpClient.newCall(request).execute().use { response ->
      if (!response.isSuccessful) throw NetworkRequestFailedException(response)

      gson
        .fromJson(response.body!!.charStream(), AnchorAllTransactionsResponse::class.java)
        .transactions
        .filter { finalStatusList.contains(it.status) }
        .map { formatAnchorTransaction(it, asset) }
    }
  }
}
