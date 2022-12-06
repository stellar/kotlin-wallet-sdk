package org.stellar.walletsdk.helpers

import java.io.FileReader
import org.stellar.walletsdk.json.fromJson

internal inline fun <reified T> stellarObjectFromJsonFile(fileName: String): T {
  val filePath = getFileFromResource(fileName)
  val jsonString = FileReader(filePath).use { f -> f.readText() }
  val gson = GsonUtils.instance ?: throw Exception("Gson instance not found")

  return gson.fromJson(jsonString, T::class.java)
}

internal inline fun <reified T> sdkObjectFromJsonFile(fileName: String): T {
  val filePath = getFileFromResource(fileName)
  val jsonString = FileReader(filePath).use { f -> f.readText() }

  return jsonString.fromJson()
}
