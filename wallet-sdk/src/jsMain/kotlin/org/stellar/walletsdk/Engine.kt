package org.stellar.walletsdk

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual class ClientConfigType : HttpClientEngineConfig()

actual object Engine : HttpClientEngineFactory<ClientConfigType> {
  override fun create(block: ClientConfigType.() -> Unit): HttpClientEngine {
    return Js.create(block as HttpClientEngineConfig.() -> Unit)
  }
}
