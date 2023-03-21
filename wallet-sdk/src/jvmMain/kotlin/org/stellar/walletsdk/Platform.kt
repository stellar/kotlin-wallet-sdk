package org.stellar.walletsdk

actual typealias Network = org.stellar.sdk.Network

actual fun Network.isPublic(): Boolean {
  return this == Network.PUBLIC
}

internal actual val Network.passphrase: String
  get() = this.networkPassphrase
