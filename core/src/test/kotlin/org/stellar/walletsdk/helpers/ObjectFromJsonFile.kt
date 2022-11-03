package org.stellar.walletsdk.helpers

import java.io.FileReader
import org.stellar.walletsdk.util.GsonUtils

fun <T> objectFromJsonFile(fileName: String, t: Class<T>): T {
  val filePath = getFileFromResource(fileName)
  val jsonString = FileReader(filePath).use { f -> f.readText() }
  val gson = GsonUtils.instance ?: throw Exception("Gson instance not found")

  return gson.fromJson(jsonString, t)
}
