package org.stellar.walletsdk.helpers

import java.io.File
import java.net.URL

fun getFileFromResource(fileName: String): File {
  val classLoader: ClassLoader = {}.javaClass.classLoader
  val resource: URL? = classLoader.getResource(fileName)

  return if (resource == null) {
    throw IllegalArgumentException("file not found! $fileName")
  } else {
    File(resource.toURI())
  }
}
