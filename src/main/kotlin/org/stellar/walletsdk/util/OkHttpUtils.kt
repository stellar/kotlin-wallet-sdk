package org.stellar.walletsdk.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object OkHttpUtils {
  private val gson = GsonUtils.instance!!
  private const val jsonContentType = "application/json; charset=utf-8"
  private val jsonContentMediaType = jsonContentType.toMediaType()

  fun buildStringGetRequest(url: String, authToken: String? = null): Request {
    val request = Request.Builder().url(url)

    if (authToken != null) {
      request.addHeader("Authorization", "Bearer $authToken")
    }

    return request.build()
  }

  fun <T> buildJsonPostRequest(url: String, requestParams: T, authToken: String? = null): Request {
    val request = Request.Builder().url(url).header("Content-Type", jsonContentType)

    if (authToken != null) {
      request.addHeader("Authorization", "Bearer $authToken")
    }

    return request.post(gson.toJson(requestParams).toRequestBody(jsonContentMediaType)).build()
  }
}
