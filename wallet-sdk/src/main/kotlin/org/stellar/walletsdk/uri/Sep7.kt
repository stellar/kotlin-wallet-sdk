package org.stellar.walletsdk.uri

/**
 * SEP-7 URI Support
 *
 * This package provides support for SEP-7 (URI Scheme to facilitate delegated signing).
 * @see [SEP-0007](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0007.md)
 *
 * Main components:
 * - [Sep7Pay]
 * - Represents payment operations
 * - [Sep7Tx]
 * - Represents transaction operations
 * - [Sep7Parser]
 * - Utilities for parsing and validating SEP-7 URIs
 *
 * Example usage:
 * ```kotlin
 * // Create a payment URI
 * val payUri = Sep7Pay.forDestination("GACCOUNT...")
 *     .amount("100")
 *     .assetCode("USDC")
 *     .callback("https://myapp.com/callback")
 *
 * // Parse a URI
 * val uri = Sep7Parser.parseSep7Uri("web+stellar:pay?destination=G...")
 * when (uri) {
 *     is Sep7Pay -> handlePayment(uri)
 *     is Sep7Tx -> handleTransaction(uri)
 * }
 *
 * // Validate a URI
 * val validation = Sep7Parser.isValidSep7Uri(uriString)
 * if (!validation.result) {
 *     println("Invalid URI: ${validation.reason}")
 * }
 * ```
 */
object Sep7 {
  /**
   * Parse a SEP-7 URI string into the appropriate Sep7 object
   * @param uriString The URI string to parse
   * @return Sep7Pay or Sep7Tx instance
   * @throws Sep7InvalidUriError if the URI is invalid
   */
  @JvmStatic fun parseUri(uriString: String): Sep7Base = Sep7Parser.parseSep7Uri(uriString)

  /**
   * Check if a URI string is a valid SEP-7 URI
   * @param uriString The URI string to validate
   * @return Validation result with success status and optional error reason
   */
  @JvmStatic
  fun isValidUri(uriString: String): IsValidSep7UriResult = Sep7Parser.isValidSep7Uri(uriString)

  /**
   * Create a new payment URI
   * @param destination The destination Stellar address
   * @return Sep7Pay instance
   */
  @JvmStatic
  fun createPaymentUri(destination: String): Sep7Pay = Sep7Pay.forDestination(destination)

  /**
   * Create a new transaction URI
   * @param transaction The transaction to encode
   * @return Sep7Tx instance
   */
  @JvmStatic
  fun createTransactionUri(transaction: org.stellar.sdk.Transaction): Sep7Tx =
    Sep7Tx.forTransaction(transaction)
}
