package org.stellar.walletsdk.customer

import io.ktor.client.*
import io.ktor.http.*
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.CustomerNotFoundException
import org.stellar.walletsdk.exception.CustomerUpdateException
import org.stellar.walletsdk.util.Util.authDelete
import org.stellar.walletsdk.util.Util.authGet
import org.stellar.walletsdk.util.Util.putJson

class Sep12
internal constructor(
  private val token: AuthToken,
  private val baseUrl: String,
  private val httpClient: HttpClient,
) {
  /**
   * Get customer information by customer ID.
   *
   * @param id customer id
   * @param type customer type
   * @return a customer data object
   */
  suspend fun getByIdAndType(id: String, type: String): GetCustomerResponse {
    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    urlBuilder.parameters.append("id", id)
    urlBuilder.parameters.append("type", type)
    val urlString = urlBuilder.buildString()

    val response = httpClient.authGet<GetCustomerResponse>(urlString, token)

    if (response.id == null) {
      throw CustomerNotFoundException(account = token.principalAccount)
    }

    return response
  }

  /**
   * Create a new customer.
   *
   * @param sep9Info (optional) map of customer SEP-9 information. To create a new customer fields
   * first_name, last_name, and email_address are required
   * @param type (optional) the type of action the customer is being KYC for. See the Type
   * Specification on SEP-12 definition.
   * @return a customer with id information
   */
  suspend fun add(
    sep9Info: Map<String, String>,
    type: String? = null,
  ): AddCustomerResponse {

    var customer: Map<String, String> = mapOf()

    if (type != null) {
      customer = customer + mapOf("type" to type)
    }

    if (sep9Info.isNotEmpty()) {
      customer = customer + sep9Info
    }

    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    val urlString = urlBuilder.buildString()

    return httpClient.putJson(urlString, customer, token)
  }

  /**
   * Update a customer.
   *
   * @param sep9Info map of customer SEP-9 information
   * @param id the ID of the customer as returned in the response of a previous PUT request. If the
   * customer has not been registered, they do not yet have an id.
   * @param type (optional) the type of action the customer is being KYC for. See the Type
   * Specification on SEP-12 definition.
   * @return a customer with id information
   */
  suspend fun update(
    sep9Info: Map<String, String>,
    id: String,
    type: String? = null,
  ): AddCustomerResponse {

    var customer: Map<String, String> = mapOf()
    customer = customer + mapOf("id" to id)

    if (type != null) {
      customer = customer + mapOf("type" to type)
    }

    if (sep9Info.isEmpty()) {
      throw CustomerUpdateException()
    }
    customer = customer + sep9Info

    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    val urlString = urlBuilder.buildString()

    return httpClient.putJson(urlString, customer, token)
  }

  /**
   * Delete a customer using account address.
   *
   * @param account account address
   */
  suspend fun delete(account: String?, memo: String? = null) {
    val customerAccount = account ?: token.principalAccount
    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    urlBuilder.appendPathSegments(customerAccount)
    val urlString = urlBuilder.buildString()

    httpClient.authDelete(urlString, memo, token)
  }
}
