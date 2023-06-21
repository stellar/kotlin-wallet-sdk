package org.stellar.walletsdk.customer

import io.ktor.client.*
import io.ktor.http.*
import org.stellar.walletsdk.anchor.MemoType
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.CustomerNotFoundException
import org.stellar.walletsdk.exception.UnauthorizedCustomerDeletionException
import org.stellar.walletsdk.json.toJson
import org.stellar.walletsdk.util.Util.authDelete
import org.stellar.walletsdk.util.Util.authGet
import org.stellar.walletsdk.util.Util.putJson

class Customer
internal constructor(
  private val token: AuthToken,
  private val baseUrl: String,
  private val httpClient: HttpClient
) {
  /**
   * Get customer information by customer ID.
   *
   * @param id customer id
   * @return a customer data object
   */
  suspend fun getById(id: String, type: String): GetCustomerResponse {
    val endpoint = getEndpoint()
    val urlString = "$baseUrl/$endpoint/customer?id=$id&type=$type"

    return httpClient.authGet<GetCustomerResponse>(urlString, token)
  }

  /**
   * Create a new customer.
   *
   * @param sep9Info map of customer SEP-9 information
   * @param id (optional) the ID of the customer as returned in the response of a previous PUT
   * request. If the customer has not been registered, they do not yet have an id.
   * @param account (deprecated, optional) the server should infer the account from the sub value in
   * the SEP-10 JWT to identify the customer. The account parameter is only used for backwards
   * compatibility, and if explicitly provided in the request body it should match the sub value of
   * the decoded SEP-10 JWT.
   * @param memo (optional) the client-generated memo that uniquely identifies the customer. If a
   * memo is present in the decoded SEP-10 JWT's sub value, it must match this parameter value. If a
   * muxed account is used as the JWT's sub value, memos sent in requests must match the 64-bit
   * integer subaccount ID of the muxed account. See the Shared Accounts section for more
   * information.
   * @param type (optional) the type of action the customer is being KYC for. See the Type
   * Specification on SEP-12 definition.
   * @param lang (optional) Defaults to en. Language code specified using ISO 639-1. Human-readable
   * descriptions, choices, and messages should be in this language.
   * @return a customer with id information
   */
  suspend fun add(
    sep9Info: Map<String, String>,
    id: String? = null,
    account: String? = null,
    memo: Pair<String, MemoType>? = null,
    type: String? = null,
    lang: String = "en"
  ): AddCustomerResponse {

    var customer =
      mapOf(
        "id" to id,
        "account" to account,
        "memo" to memo?.first,
        "memo_type" to memo?.second?.serialName,
        "type" to type,
        "lang" to lang
      )

    if (sep9Info.isNotEmpty()) {
      customer = customer + sep9Info
    }

    val body = customer.toJson()
    val endpoint = getEndpoint()
    val urlString = "$baseUrl/$endpoint/customer"

    return httpClient.putJson<String, AddCustomerResponse>(urlString, body, token)
  }

  /**
   * Delete a customer using account address.
   *
   * @param account account address
   */
  suspend fun delete(account: String, memo: String? = null) {
    val endpoint = getEndpoint()
    val urlString = "$baseUrl/$endpoint/customer/$account"

    val statusCode = httpClient.authDelete(urlString, memo, token)

    if (statusCode == HttpStatusCode.Unauthorized || statusCode == HttpStatusCode.Forbidden) {
      throw UnauthorizedCustomerDeletionException(
        "Unauthorized to delete customer account $account"
      )
    }
    if (statusCode == HttpStatusCode.NotFound) {
      throw CustomerNotFoundException("Customer not found")
    }
  }

  private fun getEndpoint(): String {
    val isTestRunning = System.getProperty("IS_TEST_RUNNING").toBoolean()
    return if (isTestRunning) "kyc" else "sep12"
  }
}
