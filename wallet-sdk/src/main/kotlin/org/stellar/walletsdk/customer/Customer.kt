package org.stellar.walletsdk.customer

import io.ktor.client.*
import io.ktor.http.*
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
   * @param customerSep12Info map of customer SEP-12 information
   * @param customerSep9Info map of customer SEP-9 information
   * @return a customer with id information
   */
  suspend fun add(
    customerSep12Info: Map<String, String>,
    customerSep9Info: Map<String, String>? = null
  ): AddCustomerResponse {
    var customer = customerSep12Info
    if (customerSep9Info != null) {
      customer = customer + customerSep9Info
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
