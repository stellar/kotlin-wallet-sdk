package org.stellar.walletsdk.uri

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.http.*
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.ByteBuffer
import java.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.stellar.sdk.KeyPair
import org.stellar.sdk.Network
import org.stellar.walletsdk.ClientConfigFn
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.toml.StellarToml

/**
 * A base abstract class containing common functions that should be used by both Sep7Tx and Sep7Pay
 * classes for parsing or constructing SEP-0007 Stellar URIs.
 *
 * @see [SEP-0007
 * Specification](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0007.md#specification)
 */
abstract class Sep7Base {
  protected var uri: URI

  /**
   * Creates a new instance of the Sep7 class.
   *
   * @param uriStr URI to initialize the Sep7 instance (URL or string)
   * @throws Sep7LongMsgError if the 'msg' param is longer than 300 characters
   */
  constructor(uriStr: String) {
    this.uri = URI(uriStr)

    msg?.let {
      if (it.length > URI_MSG_MAX_LENGTH) {
        throw Sep7LongMsgError(URI_MSG_MAX_LENGTH)
      }
    }
  }

  /**
   * Creates a new instance of the Sep7 class from a URI object.
   *
   * @param uri URI object to initialize the Sep7 instance
   * @throws Sep7LongMsgError if the 'msg' param is longer than 300 characters
   */
  constructor(uri: URI) {
    this.uri = URI(uri.toString())

    msg?.let {
      if (it.length > URI_MSG_MAX_LENGTH) {
        throw Sep7LongMsgError(URI_MSG_MAX_LENGTH)
      }
    }
  }

  /**
   * Should return a deep clone of this instance.
   *
   * @return a deep clone of the Sep7Base extended instance
   */
  abstract fun clone(): Sep7Base

  /**
   * Returns a stringified URL-decoded version of the 'uri' object.
   *
   * @return the uri decoded string value
   */
  override fun toString(): String {
    // When URI was created with the 3-arg constructor, it properly encodes the SSP
    // When created from a string, we need to return the original format
    val uriStr = uri.toString()

    // If it's already in the correct format, return it
    if (uriStr.startsWith("web+stellar:")) {
      return uriStr
    }

    // Otherwise build it manually (shouldn't happen with current implementation)
    val operation = uri.schemeSpecificPart?.substringBefore("?") ?: uri.path ?: ""
    val query = uri.schemeSpecificPart?.substringAfter("?", "") ?: uri.query ?: ""

    return buildString {
      append("web+stellar:")
      append(operation)
      if (query.isNotEmpty()) {
        append("?")
        append(query)
      }
      uri.fragment?.let {
        append("#")
        append(it)
      }
    }
  }

  /**
   * Returns uri's path as the operation type.
   *
   * @return the operation type, either "tx" or "pay"
   */
  val operationType: Sep7OperationType
    get() {
      // For web+stellar: scheme, the path is empty and we need to check the scheme-specific part
      val path = uri.schemeSpecificPart?.removePrefix("//")?.substringBefore("?") ?: uri.path
      return when (path) {
        "tx" -> Sep7OperationType.TX
        "pay" -> Sep7OperationType.PAY
        else -> throw Sep7UriTypeNotSupportedError(path ?: "")
      }
    }

  /**
   * Returns a URL-decoded version of the uri 'callback' param without the 'url:' prefix.
   *
   * The URI handler should send the signed XDR to this callback url, if this value is omitted then
   * the URI handler should submit it to the network.
   */
  var callback: String?
    get() {
      val value = getParam("callback")
      return value?.removePrefix("url:")
    }
    set(value) {
      if (value == null) {
        setParam("callback", null)
      } else if (value.startsWith("url:")) {
        setParam("callback", value)
      } else {
        setParam("callback", "url:$value")
      }
    }

  /**
   * Returns a URL-decoded version of the uri 'msg' param.
   *
   * This message should indicate any additional information that the website or application wants
   * to show the user in her wallet.
   */
  var msg: String?
    get() = getParam("msg")
    set(value) {
      value?.let {
        if (it.length > URI_MSG_MAX_LENGTH) {
          throw Sep7LongMsgError(URI_MSG_MAX_LENGTH)
        }
      }
      setParam("msg", value)
    }

  /**
   * Returns uri 'network_passphrase' param, if not present returns the PUBLIC Network value by
   * default.
   */
  var networkPassphrase: Network
    get() {
      val passphrase = getParam("network_passphrase")
      return when (passphrase) {
        Network.PUBLIC.networkPassphrase -> Network.PUBLIC
        Network.TESTNET.networkPassphrase -> Network.TESTNET
        null -> Network.PUBLIC
        else -> Network(passphrase)
      }
    }
    set(value) {
      setParam("network_passphrase", value.networkPassphrase)
    }

  /**
   * Returns a URL-decoded version of the uri 'origin_domain' param.
   *
   * This should be a fully qualified domain name that specifies the originating domain of the URI
   * request.
   */
  var originDomain: String?
    get() = getParam("origin_domain")
    set(value) {
      setParam("origin_domain", value)
    }

  /**
   * Returns a URL-decoded version of the uri 'signature' param.
   *
   * This should be a signature of the hash of the URI request (excluding the 'signature' field and
   * value itself).
   *
   * Wallets should use the URI_REQUEST_SIGNING_KEY specified in the origin_domain's stellar.toml
   * file to validate this signature.
   */
  val signature: String?
    get() = getParam("signature")

  /**
   * Signs the URI with the given keypair, which means it sets the 'signature' param.
   *
   * This should be the last step done before generating the URI string, otherwise the signature
   * will be invalid for the URI.
   *
   * @param keypair The keypair (including secret key), used to sign the request. This should be the
   * keypair found in the URI_REQUEST_SIGNING_KEY field of the origin_domains' stellar.toml.
   * @return the generated 'signature' param
   */
  fun addSignature(keypair: KeyPair): String {
    val payload = createSignaturePayload()
    val signature = Base64.getEncoder().encodeToString(keypair.sign(payload))
    setParam("signature", signature)
    return signature
  }

  /**
   * Verifies that the signature added to the URI is valid using the provided Wallet's HTTP client.
   *
   * @param wallet the Wallet instance whose HTTP client will be used to fetch the stellar.toml
   * @return true if the signature is valid for the current URI and origin_domain. Returns false if
   * signature verification fails, or if there is a problem looking up the stellar.toml associated
   * with the origin_domain.
   */
  suspend fun verifySignature(wallet: Wallet): Boolean =
    verifySignature(wallet.cfg.app.defaultClientConfig)

  /**
   * Verifies that the signature added to the URI is valid.
   *
   * @param clientConfig optional configuration for the HTTP client used to fetch the stellar.toml
   * @return true if the signature is valid for the current URI and origin_domain. Returns false if
   * signature verification fails, or if there is a problem looking up the stellar.toml associated
   * with the origin_domain.
   */
  suspend fun verifySignature(clientConfig: ClientConfigFn = {}): Boolean =
    withContext(Dispatchers.IO) {
      val domain = originDomain
      val sig = signature

      // We can fail fast if neither of them are set
      if (domain == null || sig == null) {
        return@withContext false
      }

      try {
        // Create HTTP client for fetching TOML with optional configuration
        val httpClient = HttpClient(OkHttp) { clientConfig() }

        val baseUrl = Url("https://$domain")

        val toml = StellarToml.getToml(baseUrl, httpClient)
        val signingKey =
          toml.services.sep7?.signingKey ?: toml.uriRequestSigningKey ?: return@withContext false

        val keypair = KeyPair.fromAccountId(signingKey)
        val payload = createSignaturePayload()
        val signatureBytes = Base64.getDecoder().decode(sig)

        keypair.verify(payload, signatureBytes)
      } catch (e: Exception) {
        // If something fails we assume signature verification failed
        false
      }
    }

  /**
   * Finds the uri param related to the inputted 'key', if any, and returns a URL-decoded version of
   * it. Returns null if key param not found.
   *
   * @param key the uri param key
   * @return URL-decoded value of the uri param if found
   */
  protected fun getParam(key: String): String? {
    // Get the raw URI string to preserve encoding
    val uriString = toString() // Use our toString() which maintains proper encoding

    // Extract the query part from the raw string
    val queryStart = uriString.indexOf('?')
    if (queryStart == -1) return null

    val queryEnd = uriString.indexOf('#', queryStart)
    val rawQuery =
      if (queryEnd == -1) {
        uriString.substring(queryStart + 1)
      } else {
        uriString.substring(queryStart + 1, queryEnd)
      }

    // Parse the query parameters from the raw encoded string
    val params = rawQuery.split("&")

    for (param in params) {
      val parts = param.split("=", limit = 2)
      if (parts.size == 2 && parts[0] == key) {
        // URLDecoder.decode treats + as space, which breaks Base64.
        // We need to handle %2B specially, then let URLDecoder handle everything else.
        return parts[1]
          .replace("%2B", "+")
          .replace("%2b", "+") // Handle lowercase
          .let {
            // For any remaining percent-encoded sequences, use URLDecoder
            // but first protect any + characters (both literal and the ones we just decoded)
            if (it.contains("%")) {
              it
                .replace("+", "\u0000") // Temporarily replace + with null char
                .let { str -> URLDecoder.decode(str, "UTF-8") }
                .replace("\u0000", "+") // Restore + characters
            } else {
              it
            }
          }
      }
    }

    return null
  }

  /**
   * Sets and URL-encodes a 'key=value' uri param.
   *
   * Deletes the uri param if 'value' set as null.
   *
   * @param key the uri param key
   * @param value the uri param value to be set
   */
  protected fun setParam(key: String, value: String?) {
    // For web+stellar: URIs, query is in the scheme-specific part
    val currentQuery = uri.query ?: uri.schemeSpecificPart?.substringAfter("?", "") ?: ""

    // Parse existing params into a map to preserve decoded values
    val paramsMap = mutableMapOf<String, String>()
    if (currentQuery.isNotEmpty()) {
      currentQuery.split("&").forEach { param ->
        val parts = param.split("=", limit = 2)
        if (parts.size == 2) {
          // Store the decoded value
          paramsMap[parts[0]] = parts[1]
        }
      }
    }

    // Update or remove the param
    if (value != null) {
      paramsMap[key] = value
    } else {
      paramsMap.remove(key)
    }

    // Rebuild the URI with properly encoded params
    // Use URLEncoder but replace + with %20 for proper URI encoding
    val newQuery =
      if (paramsMap.isNotEmpty()) {
        paramsMap.entries.joinToString("&") { (k, v) ->
          "$k=${URLEncoder.encode(v, "UTF-8").replace("+", "%20")}"
        }
      } else null

    // Extract operation type from current URI
    val operation =
      uri.schemeSpecificPart?.removePrefix("//")?.substringBefore("?") ?: uri.path ?: ""

    val uriStr = buildString {
      append("web+stellar:")
      append(operation)
      if (!newQuery.isNullOrEmpty()) {
        append("?")
        append(newQuery)
      }
      uri.fragment?.let {
        append("#")
        append(it)
      }
    }

    // Use single-arg constructor to preserve our URL encoding
    // The 3-arg constructor would double-encode our already encoded parameters
    this.uri = URI(uriStr)
  }

  /**
   * Converts the URI request into the payload that will be signed by the 'addSignature' method.
   *
   * @return array of bytes to be signed with given keypair
   */
  private fun createSignaturePayload(): ByteArray {
    var data = toString()

    // Remove signature if present
    signature?.let { sig ->
      val encodedSig = URLEncoder.encode(sig, "UTF-8").replace("+", "%20")
      data = data.replace("&signature=$encodedSig", "")
    }

    // The first 35 bytes of the payload are all 0, the 36th byte is 4.
    // Then we concatenate the URI request with the prefix 'stellar.sep.7 - URI Scheme'
    val prefix =
      ByteBuffer.allocate(36)
        .apply {
          put(ByteArray(35) { 0 })
          put(4.toByte())
        }
        .array()

    val message = "stellar.sep.7 - URI Scheme$data".toByteArray()

    return prefix + message
  }
}
