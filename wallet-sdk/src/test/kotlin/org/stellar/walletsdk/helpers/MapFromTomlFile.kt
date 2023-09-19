package org.stellar.walletsdk.helpers

import com.moandjiezana.toml.Toml
import java.io.FileReader

fun mapFromTomlFile(fileName: String): Map<String, Any> {
  val filePath = getFileFromResource(fileName)
  val tomlContent = FileReader(filePath).use { f -> f.readText() }

  return Toml().read(tomlContent).toMap()
}
