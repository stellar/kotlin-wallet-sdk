package org.stellar.walletsdk.helpers

import java.io.FileReader
import shadow.com.moandjiezana.toml.Toml

fun mapFromTomlFile(fileName: String): Map<String, Any> {
  val filePath = getFileFromResource(fileName)
  val tomlContent = FileReader(filePath).use { f -> f.readText() }

  return Toml().read(tomlContent).toMap()
}
