@file:Suppress(
  "INTERFACE_WITH_SUPERCLASS",
  "OVERRIDING_FINAL_MEMBER",
  "RETURN_TYPE_MISMATCH_ON_OVERRIDE",
  "CONFLICTING_OVERLOADS"
)

package external

import kotlin.js.*
import org.khronos.webgl.*
import org.w3c.dom.*
import org.w3c.dom.events.*
import org.w3c.dom.parsing.*
import org.w3c.dom.svg.*
import org.w3c.dom.url.*
import org.w3c.fetch.*
import org.w3c.files.*
import org.w3c.notifications.*
import org.w3c.performance.*
import org.w3c.workers.*
import org.w3c.xhr.*

typealias QueryDataMap = Any

external interface HTMLElement

@Suppress("NESTED_CLASS_IN_EXTERNAL_INTERFACE")
external interface URI {
  fun absoluteTo(path: String): URI
  fun absoluteTo(path: URI): URI
  fun addFragment(fragment: String): URI
  fun addQuery(qry: String): URI
  fun addQuery(qry: QueryDataMap): URI
  fun addQuery(qry: String, value: Any): URI
  fun addSearch(qry: String): URI
  fun addSearch(qry: QueryDataMap): URI
  fun addSearch(key: String, value: Any): URI
  fun authority(): String
  fun authority(authority: String): URI
  fun clone(): URI
  fun directory(dir: Boolean = definedExternally): String
  fun directory(): String
  fun directory(dir: String): URI
  fun domain(domain: Boolean = definedExternally): String
  fun domain(): String
  fun domain(domain: String): URI
  fun duplicateQueryParameters(param_val: Boolean): URI
  fun equals(url: String = definedExternally): Boolean
  fun equals(): Boolean
  fun equals(url: URI = definedExternally): Boolean
  fun escapeQuerySpace(param_val: Boolean): URI
  fun filename(file: Boolean = definedExternally): String
  fun filename(): String
  fun filename(file: String): URI
  fun fragment(): String
  fun fragment(fragment: String): URI
  fun fragmentPrefix(prefix: String): URI
  fun hash(): String
  fun hash(hash: String): URI
  fun host(): String
  fun host(host: String): URI
  fun hostname(): String
  fun hostname(hostname: String): URI
  fun href(): String
  fun href(url: String)
  fun `is`(
    qry:
      String /* "relative" | "absolute" | "urn" | "url" | "domain" | "name" | "sld" | "idn" | "punycode" | "ip" | "ip4" | "ipv4" | "inet4" | "ip6" | "ipv6" | "inet6" */
  ): Boolean
  fun iso8859(): URI
  fun normalize(): URI
  fun normalizeFragment(): URI
  fun normalizeHash(): URI
  fun normalizeHostname(): URI
  fun normalizePath(): URI
  fun normalizePathname(): URI
  fun normalizePort(): URI
  fun normalizeProtocol(): URI
  fun normalizeQuery(): URI
  fun normalizeSearch(): URI
  fun origin(): String
  fun origin(uri: String): URI
  fun origin(uri: URI): URI
  fun password(): String
  fun password(pw: String): URI
  fun path(path: Boolean = definedExternally): String
  fun path(): String
  fun path(path: String): URI
  fun pathname(path: Boolean = definedExternally): String
  fun pathname(): String
  fun pathname(path: String): URI
  fun port(): String
  fun port(port: String): URI
  fun protocol(): String
  fun protocol(protocol: String): URI
  fun preventInvalidHostname(param_val: Boolean): URI
  fun query(): String
  fun query(qry: String): URI
  fun query(qry: QueryDataMap): URI
  fun query(qry: (qryObject: QueryDataMap) -> Any): URI
  fun query(v: Boolean): QueryDataMap
  fun readable(): String
  fun relativeTo(path: String): URI
  fun removeQuery(qry: String): URI
  fun removeQuery(qry: QueryDataMap): URI
  fun removeQuery(name: String, value: String): URI
  fun removeSearch(qry: String): URI
  fun removeSearch(qry: QueryDataMap): URI
  fun removeSearch(name: String, value: String): URI
  fun resource(): String
  fun resource(resource: String): URI
  fun scheme(): String
  fun scheme(protocol: String): URI
  fun search(): String
  fun search(qry: String): URI
  fun search(qry: QueryDataMap): URI
  fun search(qry: (qryObject: QueryDataMap) -> Any): URI
  fun search(v: Boolean): QueryDataMap
  fun segment(): Array<String>
  fun segment(segments: Array<String>): URI
  fun segment(segments: String): URI
  fun segment(position: Number): String?
  fun segment(position: Number, level: String): URI
  fun segmentCoded(): Array<String>
  fun segmentCoded(segments: Array<String>): URI
  fun segmentCoded(segments: String): URI
  fun segmentCoded(position: Number): String
  fun segmentCoded(position: Number, level: String): URI
  fun setQuery(key: String, value: Any): URI
  fun setQuery(qry: QueryDataMap): URI
  fun setSearch(key: String, value: Any): URI
  fun setSearch(qry: QueryDataMap): URI
  fun hasQuery(
    name: Any,
    value: String = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any): Boolean
  fun hasQuery(name: Any, value: String = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: Number = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: Number = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: Boolean = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: Boolean = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: Array<String> = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: Array<String> = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: Array<Number> = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: Array<Number> = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: Array<Boolean> = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: Array<Boolean> = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: RegExp = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: RegExp = definedExternally): Boolean
  fun hasQuery(
    name: Any,
    value: (args: Any) -> Any = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasQuery(name: Any, value: (args: Any) -> Any = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: String = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any): Boolean
  fun hasSearch(name: Any, value: String = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: Number = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: Number = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: Boolean = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: Boolean = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: Array<String> = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: Array<String> = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: Array<Number> = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: Array<Number> = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: Array<Boolean> = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: Array<Boolean> = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: RegExp = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: RegExp = definedExternally): Boolean
  fun hasSearch(
    name: Any,
    value: (args: Any) -> Any = definedExternally,
    withinArray: Boolean = definedExternally
  ): Boolean
  fun hasSearch(name: Any, value: (args: Any) -> Any = definedExternally): Boolean
  fun subdomain(): String
  fun subdomain(subdomain: String): URI
  fun suffix(suffix: Boolean = definedExternally): String
  fun suffix(): String
  fun suffix(suffix: String): URI
  fun tld(tld: Boolean = definedExternally): String
  fun tld(): String
  fun tld(tld: String): URI
  fun unicode(): URI
  fun userinfo(): String
  fun userinfo(userinfo: String): URI
  fun username(): String
  fun username(uname: String): URI
  fun valueOf(): String
  interface URIOptions {
    var protocol: String?
      get() = definedExternally
      set(value) = definedExternally
    var username: String?
      get() = definedExternally
      set(value) = definedExternally
    var password: String?
      get() = definedExternally
      set(value) = definedExternally
    var hostname: String?
      get() = definedExternally
      set(value) = definedExternally
    var port: String?
      get() = definedExternally
      set(value) = definedExternally
    var path: String?
      get() = definedExternally
      set(value) = definedExternally
    var query: String?
      get() = definedExternally
      set(value) = definedExternally
    var fragment: String?
      get() = definedExternally
      set(value) = definedExternally
    var urn: Boolean?
      get() = definedExternally
      set(value) = definedExternally
  }
  interface Parts : URIOptions {
    var duplicateQueryParameters: Boolean
    var escapeQuerySpace: Boolean
    var preventInvalidHostname: Boolean
  }
  interface ReadonlyURI {
    fun clone(): URI
    fun authority(): String
    fun directory(dir: Boolean = definedExternally): String
    fun domain(domain: Boolean = definedExternally): String
    fun filename(file: Boolean = definedExternally): String
    fun fragment(): String
    fun hash(): String
    fun host(): String
    fun hostname(): String
    fun href(): String
    fun origin(): String
    fun password(): String
    fun path(path: Boolean = definedExternally): String
    fun pathname(path: Boolean = definedExternally): String
    fun port(): String
    fun protocol(): String
    fun query(): String
    fun query(v: Boolean): QueryDataMap
    fun readable(): String
    fun resource(): String
    fun scheme(): String
    fun search(): String
    fun search(v: Boolean): QueryDataMap
    fun segment(): Array<String>
    fun segment(position: Number): String?
    fun segmentCoded(): Array<String>
    fun segmentCoded(position: Number): String
    fun subdomain(): String
    fun suffix(suffix: Boolean = definedExternally): String
    fun tld(tld: Boolean = definedExternally): String
    fun userinfo(): String
    fun username(): String
    fun valueOf(): String
    fun equals(url: String = definedExternally): Boolean
    fun equals(): Boolean
    fun equals(url: ReadonlyURI = definedExternally): Boolean
    fun equals(url: URI = definedExternally): Boolean
    fun `is`(
      qry:
        String /* "relative" | "absolute" | "urn" | "url" | "domain" | "name" | "sld" | "idn" | "punycode" | "ip" | "ip4" | "ipv4" | "inet4" | "ip6" | "ipv6" | "inet6" */
    ): Boolean
    fun hasQuery(
      name: Any,
      value: String = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any): Boolean
    fun hasQuery(name: Any, value: String = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: Number = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: Number = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: Boolean = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: Boolean = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: Array<String> = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: Array<String> = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: Array<Number> = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: Array<Number> = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: Array<Boolean> = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: Array<Boolean> = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: RegExp = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: RegExp = definedExternally): Boolean
    fun hasQuery(
      name: Any,
      value: (args: Any) -> Any = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasQuery(name: Any, value: (args: Any) -> Any = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: String = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any): Boolean
    fun hasSearch(name: Any, value: String = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: Number = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: Number = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: Boolean = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: Boolean = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: Array<String> = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: Array<String> = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: Array<Number> = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: Array<Number> = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: Array<Boolean> = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: Array<Boolean> = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: RegExp = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: RegExp = definedExternally): Boolean
    fun hasSearch(
      name: Any,
      value: (args: Any) -> Any = definedExternally,
      withinArray: Boolean = definedExternally
    ): Boolean
    fun hasSearch(name: Any, value: (args: Any) -> Any = definedExternally): Boolean
  }
}
