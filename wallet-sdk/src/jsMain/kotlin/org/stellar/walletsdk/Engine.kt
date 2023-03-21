package org.stellar.walletsdk

import io.ktor.client.engine.*

// internal actual val engine: HttpClientEngineFactory<ClientConfigType>
//    get() = TODO("Not yet implemented")

actual class ClientConfigType : HttpClientEngineConfig()

actual object Engine : HttpClientEngineFactory<ClientConfigType> {
  override fun create(block: ClientConfigType.() -> Unit): HttpClientEngine {
    TODO("Not yet implemented")
  }
}
