package org.stellar.walletsdk.uri

/** Exception thrown when a SEP-7 URI is invalid */
class Sep7InvalidUriError(reason: String?) :
  Exception("Invalid Stellar Sep-7 URI, reason: $reason")

/** Exception thrown when a SEP-7 URI type is not supported */
class Sep7UriTypeNotSupportedError(type: String) :
  Exception("Stellar Sep-7 URI operation type '$type' is not currently supported")

/** Exception thrown when the 'msg' parameter exceeds the maximum length */
class Sep7LongMsgError(maxLength: Int) :
  Exception("'msg' should be no longer than $maxLength characters")
