package org.stellar.walletsdk.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object OkHttpUtils {
  private val gson = GsonUtils.instance!!
  private const val jsonContentType = "application/json; charset=utf-8"
  private val jsonContentMediaType = jsonContentType.toMediaType()

  fun buildStringGetRequest(url: String): Request {
    return Request.Builder().url(url).build()
  }

  fun <T> buildJsonPostRequest(url: String, requestParams: T): Request {
    return Request.Builder()
      .url(url)
      .header("Content-Type", jsonContentType)
      .post(gson.toJson(requestParams).toRequestBody(jsonContentMediaType))
      .build()
  }
}
