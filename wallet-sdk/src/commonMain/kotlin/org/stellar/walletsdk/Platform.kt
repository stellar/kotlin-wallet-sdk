package org.stellar.walletsdk

expect class Network

expect internal fun Network.isPublic(): Boolean
expect internal val Network.passphrase: String