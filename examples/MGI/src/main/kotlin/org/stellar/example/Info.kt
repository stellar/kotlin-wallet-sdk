package org.stellar.example

object Info {
  @JvmStatic
  suspend fun main(args: Array<String>) {
    // Get info from the anchor server
    val info = anchor.getInfo()

    // Get SEP-24 info
    val servicesInfo = anchor.getServicesInfo()

    println("Info from anchor server: $info")
    println("SEP-24 info from anchor server: $servicesInfo")

    wallet.close()
  }
}
