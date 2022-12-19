package org.stellar.walletsdk.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.stellar.walletsdk.auth.AuthTokenValue
import org.stellar.walletsdk.json.toJson

/** Helpers for [OkHttpClient] */
object OkHttpUtils {
  private const val jsonContentType = "application/json; charset=utf-8"
  private val jsonContentMediaType = jsonContentType.toMediaType()

  fun buildStringGetRequest(url: String, authToken: AuthTokenValue? = null): Request {
    val request = Request.Builder().url(url)

    if (authToken != null) {
      request.addHeader("Authorization", "Bearer $authToken")
    }

    return request.build()
  }
  internal inline fun <reified T : Any> makePostRequest(
    url: String,
    requestParams: T,
    authToken: AuthTokenValue? = null
  ): Request {
    val request = Request.Builder().url(url).header("Content-Type", jsonContentType)

    if (authToken != null) {
      request.addHeader("Authorization", "Bearer $authToken")
    }

    return request.post(requestParams.toJson().toRequestBody(jsonContentMediaType)).build()
  }
}
