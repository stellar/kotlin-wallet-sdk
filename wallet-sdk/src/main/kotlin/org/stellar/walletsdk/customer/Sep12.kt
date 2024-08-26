package org.stellar.walletsdk.customer

import io.ktor.client.*
import io.ktor.http.*
import org.stellar.walletsdk.auth.AuthToken
import org.stellar.walletsdk.exception.CustomerNotFoundException
import org.stellar.walletsdk.exception.CustomerUpdateException
import org.stellar.walletsdk.exception.ValidationException
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
   * Get customer information.
   *
   * @param id (optional) The ID of the customer as returned in the response of a previous PUT
   * request. If the customer has not been registered, they do not yet have an id.
   * @param memo (optional) the client-generated memo that uniquely identifies the customer. If a
   * memo is present in the decoded SEP-10 JWT's sub value, it must match this parameter value.
   * @param type (optional) the type of action the customer is being KYCd for.
   * @param lang (optional) Defaults to en. Language code specified using ISO 639-1. Human-readable
   * descriptions, choices, and messages should be in this language.
   * @param transactionId (optional) the ID of the transaction that the customer is being KYC'ed
   * for.
   * @return a customer data object
   */
  suspend fun get(
    id: String? = null,
    memo: ULong? = null,
    type: String? = null,
    lang: String? = null,
    transactionId: String? = null,
  ): GetCustomerResponse {
    validateMemo(memo)

    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    urlBuilder.addParameter("id", id)
    urlBuilder.addParameter("memo", memo?.toString())
    urlBuilder.addParameter("type", type)
    urlBuilder.addParameter("lang", lang)
    urlBuilder.addParameter("transaction_id", transactionId)
    val urlString = urlBuilder.buildString()

    val response = httpClient.authGet<GetCustomerResponse>(urlString, token)

    if (response.id == null) {
      throw CustomerNotFoundException(account = token.account)
    }

    return response
  }

  private fun URLBuilder.addParameter(name: String, value: String?) {
    if (value != null) {
      this.parameters.append(name, value)
    }
  }

  /**
   * Create a new customer.
   *
   * @param sep9Info (optional) map of customer SEP-9 information. To create a new customer fields
   * first_name, last_name, and email_address are required
   * @param type (optional) the type of action the customer is being KYC for. See the Type
   * Specification on SEP-12 definition.
   * @param memo (optional) the client-generated memo of type ID that uniquely identifies the
   * customer. If a memo is present in the decoded SEP-10 JWT's sub value, it must match this
   * parameter value.
   * @param transactionId (optional) the ID of the transaction that the customer is being KYC'ed
   * for.
   * @return a customer with id information
   */
  suspend fun add(
    sep9Info: Map<String, String>,
    memo: ULong? = null,
    type: String? = null,
    transactionId: String? = null,
  ): AddCustomerResponse {
    val customer: MutableMap<String, String> = mutableMapOf()

    populateMap(type, transactionId, customer, memo, sep9Info)

    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    val urlString = urlBuilder.buildString()

    return httpClient.putJson(urlString, customer.toMap(), token)
  }

  /**
   * Update a customer.
   *
   * @param sep9Info map of customer SEP-9 information
   * @param id the ID of the customer as returned in the response of a previous PUT request. If the
   * customer has not been registered, they do not yet have an id.
   * @param type (optional) the type of action the customer is being KYC for. See the Type
   * Specification on SEP-12 definition.
   * @param memo (optional) the client-generated memo of type ID that uniquely identifies the
   * customer. If a memo is present in the decoded SEP-10 JWT's sub value, it must match this
   * parameter value.
   * @param transactionId (optional) the ID of the transaction that the customer is being KYC'ed
   * for.
   * @return a customer with id information
   */
  suspend fun update(
    sep9Info: Map<String, String>,
    id: String,
    type: String? = null,
    memo: ULong? = null,
    transactionId: String? = null,
  ): AddCustomerResponse {
    val customer: MutableMap<String, String> = mutableMapOf("id" to id)

    if (sep9Info.isEmpty()) {
      throw CustomerUpdateException()
    }

    populateMap(type, transactionId, customer, memo, sep9Info)

    val urlBuilder = URLBuilder(baseUrl)
    urlBuilder.appendPathSegments("customer")
    val urlString = urlBuilder.buildString()

    return httpClient.putJson(urlString, customer.toMap(), token)
  }

  private fun populateMap(
    type: String?,
    transactionId: String?,
    customer: MutableMap<String, String>,
    memo: ULong?,
    sep9Info: Map<String, String>
  ) {
    if (type != null) {
      customer["type"] = type
    }
    if (transactionId != null) {
      customer["transaction_id"] = transactionId
    }
    validateMemo(memo) { customer["memo"] = it.toString() }

    customer.putAll(sep9Info)
  }

  private fun validateMemo(memo: ULong?, onMemo: (memo: ULong) -> (Unit) = {}) {
    if (memo != null) {
      if (token.memo != null && token.memo != memo) {
        throw ValidationException("Passed memo $memo doesn't match token memo ${token.memo}")
      }
      onMemo(memo)
    }
  }

  /**
   * Delete a customer using account address.
   *
   * @param account account address
   */
  suspend fun delete(account: String = token.account, memo: ULong? = null) {
    val urlBuilder = URLBuilder(baseUrl)
    validateMemo(memo)
    urlBuilder.appendPathSegments("customer")
    urlBuilder.appendPathSegments(account)
    val urlString = urlBuilder.buildString()

    httpClient.authDelete(urlString, memo?.toString(), token)
  }
}
