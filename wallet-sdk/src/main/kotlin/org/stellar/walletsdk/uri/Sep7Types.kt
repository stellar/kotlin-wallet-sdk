package org.stellar.walletsdk.uri

/** SEP-7 URI scheme constant */
const val WEB_STELLAR_SCHEME = "web+stellar:"

/** Maximum length for the 'msg' parameter in SEP-7 URIs */
const val URI_MSG_MAX_LENGTH = 300

/** SEP-7 operation types */
enum class Sep7OperationType(val value: String) {
  TX("tx"),
  PAY("pay");

  override fun toString() = value
}

/**
 * SEP-7 replacement data structure for field substitution
 * @property id The identifier for this replacement
 * @property path The txrep path to the field that needs replacement
 * @property hint A user-friendly hint about what this replacement is for
 */
data class Sep7Replacement(val id: String, val path: String, val hint: String)

/**
 * Result of SEP-7 URI validation
 * @property result Whether the URI is valid
 * @property reason Optional reason why the URI is invalid
 */
data class IsValidSep7UriResult(val result: Boolean, val reason: String? = null)
