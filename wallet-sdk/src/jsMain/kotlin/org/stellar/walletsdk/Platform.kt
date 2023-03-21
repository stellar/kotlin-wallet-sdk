package org.stellar.walletsdk

actual data class Network(val passphrase: String) {
  override fun toString(): String {
    return passphrase
  }

  companion object {
    val PUBLIC = Network("Public Global Stellar Network ; September 2015")
    val TESTNET = Network("Test SDF Network ; September 2015")
  }
}

actual fun Network.isPublic(): Boolean {
  return this == Network.PUBLIC
}

internal actual val Network.passphrase: String
  get() = passphrase