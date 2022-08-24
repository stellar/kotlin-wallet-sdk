package org.stellar.walletsdk.helpers

import com.google.gson.Gson
import java.io.File

fun <T> objectFromJsonFile(filePath: String, t: Class<T>): T {
  val jsonString =
    try {
      File(filePath).readText()
    } catch (e: Exception) {
      throw Error("File not found")
    }

  return Gson().fromJson(jsonString, t)
}
