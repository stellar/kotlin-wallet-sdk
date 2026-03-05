package org.stellar.walletsdk.uri

import java.net.URI
import java.net.URLDecoder
import org.stellar.sdk.Network
import org.stellar.sdk.StrKey
import org.stellar.sdk.Transaction
import org.stellar.sdk.xdr.TransactionEnvelope

/** Parser and validator for SEP-7 URIs */
object Sep7Parser {

  /**
   * Returns true if the given URI is a SEP-7 compliant URI, false otherwise.
   *
   * Currently this checks whether it starts with 'web+stellar:tx' or 'web+stellar:pay' and has its
   * required parameters: 'xdr=' and 'destination=' respectively.
   *
   * @param uriString The URI string to check
   * @return returns result with success status and optional reason for failure
   */
  @JvmStatic
  fun isValidSep7Uri(uriString: String): IsValidSep7UriResult {
    if (!uriString.startsWith(WEB_STELLAR_SCHEME)) {
      return IsValidSep7UriResult(
        result = false,
        reason = "it must start with '$WEB_STELLAR_SCHEME'"
      )
    }

    val uri =
      try {
        URI(uriString)
      } catch (e: Exception) {
        return IsValidSep7UriResult(result = false, reason = "invalid URI format: ${e.message}")
      }

    // For web+stellar: URIs, the operation type is in the scheme-specific part
    val type = uri.schemeSpecificPart?.removePrefix("//")?.substringBefore("?") ?: uri.path
    val xdr = getQueryParam(uri, "xdr")
    val networkPassphrase =
      getQueryParam(uri, "network_passphrase") ?: Network.PUBLIC.networkPassphrase
    val destination = getQueryParam(uri, "destination")
    val msg = getQueryParam(uri, "msg")

    // Check valid operation types
    if (type != "tx" && type != "pay") {
      return IsValidSep7UriResult(
        result = false,
        reason = "operation type '$type' is not currently supported"
      )
    }

    // Validate tx operation
    if (type == "tx" && xdr == null) {
      return IsValidSep7UriResult(
        result = false,
        reason = "operation type '$type' must have a 'xdr' parameter"
      )
    }

    if (type == "tx" && xdr != null) {
      try {
        // The xdr is already URL-decoded by getQueryParam
        val envelope = TransactionEnvelope.fromXdrBase64(xdr)
        val network =
          when (networkPassphrase) {
            Network.PUBLIC.networkPassphrase -> Network.PUBLIC
            Network.TESTNET.networkPassphrase -> Network.TESTNET
            else -> Network(networkPassphrase)
          }
        Transaction.fromEnvelopeXdr(envelope, network)
      } catch (e: Exception) {
        return IsValidSep7UriResult(
          result = false,
          reason =
            "the provided 'xdr' parameter is not a valid transaction envelope on the '$networkPassphrase' network: ${e.message}"
        )
      }
    }

    // Validate pay operation
    if (type == "pay" && destination == null) {
      return IsValidSep7UriResult(
        result = false,
        reason = "operation type '$type' must have a 'destination' parameter"
      )
    }

    if (type == "pay" && destination != null) {
      // Checks if it's a valid "G", "M" or "C" Stellar address
      val isValidStellarAddress =
        try {
          when {
            destination.startsWith("G") -> StrKey.isValidEd25519PublicKey(destination)
            destination.startsWith("M") ->
              try {
                StrKey.isValidMed25519PublicKey(destination)
                true
              } catch (e: Exception) {
                false
              }
            destination.startsWith("C") -> StrKey.isValidContract(destination)
            else -> false
          }
        } catch (e: Exception) {
          false
        }

      if (!isValidStellarAddress) {
        return IsValidSep7UriResult(
          result = false,
          reason =
            "the provided 'destination' parameter is not a valid Stellar address (got: '$destination')"
        )
      }
    }

    // Validate message length
    if (msg != null && msg.length > URI_MSG_MAX_LENGTH) {
      return IsValidSep7UriResult(
        result = false,
        reason = "the 'msg' parameter should be no longer than $URI_MSG_MAX_LENGTH characters"
      )
    }

    return IsValidSep7UriResult(result = true)
  }

  /**
   * Try parsing a SEP-7 URI string and returns a Sep7Tx or Sep7Pay instance, depending on the type.
   *
   * @param uriString The URI string to parse
   * @return a uri parsed Sep7Tx or Sep7Pay instance
   * @throws Sep7InvalidUriError if the inputted uri is not a valid SEP-7 URI
   * @throws Sep7UriTypeNotSupportedError if the inputted uri does not have a supported SEP-7 type
   */
  @JvmStatic
  fun parseSep7Uri(uriString: String): Sep7Base {
    val isValid = isValidSep7Uri(uriString)
    if (!isValid.result) {
      throw Sep7InvalidUriError(isValid.reason)
    }

    val uri = URI(uriString)

    // For web+stellar: URIs, the operation type is in the scheme-specific part
    val type = uri.schemeSpecificPart?.removePrefix("//")?.substringBefore("?") ?: uri.path

    return when (type) {
      "tx" -> Sep7Tx(uri)
      "pay" -> Sep7Pay(uri)
      else -> throw Sep7UriTypeNotSupportedError(type ?: "")
    }
  }

  /** String delimiters shared by the parsing functions. */
  private const val HINT_DELIMITER = ";"
  private const val ID_DELIMITER = ":"
  private const val LIST_DELIMITER = ","

  /**
   * Takes a Sep-7 URL-decoded 'replace' string param and parses it to a list of Sep7Replacement
   * objects for ease of use.
   *
   * This string identifies the fields to be replaced in the XDR using the 'Txrep (SEP-0011)'
   * representation, which should be specified in the format of:
   * txrep_tx_field_name_1:reference_identifier_1,txrep_tx_field_name_2:reference_identifier_2;reference_identifier_1:hint_1,reference_identifier_2:hint_2
   *
   * @param replacements a replacements string in the 'Txrep (SEP-0011)' representation
   * @return a list of parsed Sep7Replacement objects
   */
  @JvmStatic
  fun sep7ReplacementsFromString(replacements: String?): List<Sep7Replacement> {
    if (replacements.isNullOrEmpty()) {
      return emptyList()
    }

    val parts = replacements.split(HINT_DELIMITER)
    val txrepString = parts[0]
    val hintsString = parts.getOrNull(1) ?: ""

    // Parse hints map
    val hintsMap = mutableMapOf<String, String>()
    if (hintsString.isNotEmpty()) {
      hintsString.split(LIST_DELIMITER).forEach { item ->
        val hintParts = item.split(ID_DELIMITER, limit = 2)
        if (hintParts.size == 2) {
          hintsMap[hintParts[0]] = hintParts[1]
        }
      }
    }

    // Parse txrep list
    return txrepString.split(LIST_DELIMITER).mapNotNull { item ->
      val txrepParts = item.split(ID_DELIMITER, limit = 2)
      if (txrepParts.size == 2) {
        Sep7Replacement(
          id = txrepParts[1],
          path = txrepParts[0],
          hint = hintsMap[txrepParts[1]] ?: ""
        )
      } else {
        null
      }
    }
  }

  /**
   * Takes a list of Sep7Replacement objects and parses it to a string that could be URL-encoded and
   * used as a Sep-7 URI 'replace' param.
   *
   * This string identifies the fields to be replaced in the XDR using the 'Txrep (SEP-0011)'
   * representation, which should be specified in the format of:
   * txrep_tx_field_name_1:reference_identifier_1,txrep_tx_field_name_2:reference_identifier_2;reference_identifier_1:hint_1,reference_identifier_2:hint_2
   *
   * @param replacements a list of Sep7Replacement objects
   * @return a string that identifies the fields to be replaced in the XDR using the 'Txrep
   * (SEP-0011)' representation
   */
  @JvmStatic
  fun sep7ReplacementsToString(replacements: List<Sep7Replacement>?): String {
    if (replacements.isNullOrEmpty()) {
      return ""
    }

    val hintsMap = mutableMapOf<String, String>()

    val txrepString =
      replacements.joinToString(LIST_DELIMITER) { replacement ->
        if (replacement.hint.isNotEmpty()) {
          hintsMap[replacement.id] = replacement.hint
        }
        "${replacement.path}$ID_DELIMITER${replacement.id}"
      }

    if (hintsMap.isEmpty()) {
      return txrepString
    }

    val hintsString =
      hintsMap.entries.joinToString(LIST_DELIMITER) { (id, hint) -> "$id$ID_DELIMITER$hint" }

    return "$txrepString$HINT_DELIMITER$hintsString"
  }

  /** Helper function to extract query parameters from URI */
  private fun getQueryParam(uri: URI, key: String): String? {
    // For web+stellar: URIs, query is in the scheme-specific part
    val query = uri.query ?: uri.schemeSpecificPart?.substringAfter("?", "") ?: return null
    if (query.isEmpty()) return null
    val params = query.split("&")

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
}
