package org.stellar.walletsdk

import io.ktor.client.engine.*

expect class Network

expect internal fun Network.isPublic(): Boolean

expect internal val Network.passphrase: String

expect class ClientConfigType : HttpClientEngineConfig

expect object Engine : HttpClientEngineFactory<ClientConfigType>
