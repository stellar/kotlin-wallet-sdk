package org.stellar.example

import org.stellar.walletsdk.anchor.getInfo
import org.stellar.walletsdk.anchor.interactive

object Info {
  @JvmStatic
  suspend fun main() {
    // Get info from the anchor server
    val info = anchor.getInfo()

    // Get SEP-24 info
    val servicesInfo = anchor.interactive().getServicesInfo()

    println("Info from anchor server: $info")
    println("SEP-24 info from anchor server: $servicesInfo")

    wallet.close()
  }
}
