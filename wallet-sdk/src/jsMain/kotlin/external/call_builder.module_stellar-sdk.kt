@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external

import kotlin.js.Promise
import org.w3c.dom.MessageEvent

external interface EventSourceOptions<T> {
  var onmessage: ((value: T) -> Unit)?
    get() = definedExternally
    set(value) = definedExternally
  var onerror: ((event: MessageEvent) -> Unit)?
    get() = definedExternally
    set(value) = definedExternally
  var reconnectTimeout: Number?
    get() = definedExternally
    set(value) = definedExternally
}

external open class CallBuilder<T>(serverUrl: URI, neighborRoot: String = definedExternally) {
  open var url: Any
  open var filter: Array<Array<String>>
  open var originalSegments: Array<String>
  open var neighborRoot: String
  open fun call(): Promise<T>
  open fun stream(options: EventSourceOptions<T> = definedExternally): () -> Unit
  open fun cursor(cursor: String): CallBuilder<T> /* this */
  open fun limit(recordsNumber: Number): CallBuilder<T> /* this */
  open fun order(direction: String /* "asc" | "desc" */): CallBuilder<T> /* this */
  open fun join(include: String /* "transactions" */): CallBuilder<T> /* this */
  open fun forEndpoint(endpoint: String, param: String): CallBuilder<T> /* this */
  open var checkFilter: Any
  open var _requestFnForLink: Any
  open var _parseRecord: Any
  open var _sendNormalRequest: Any
  open var _parseResponse: Any
  open var _toCollectionPage: Any
  open var _handleNetworkError: Any
}
