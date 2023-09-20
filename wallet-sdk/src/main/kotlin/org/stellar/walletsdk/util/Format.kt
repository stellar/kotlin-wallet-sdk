@file:Suppress("TooManyFunctions")

package org.stellar.walletsdk.util

import java.math.BigDecimal
import org.stellar.walletsdk.*
import org.stellar.walletsdk.anchor.*

/**
 * Format amount to consistent string.
 *
 * @param amount Amount string to format
 * @return formatted amount
 */
@Deprecated("To be removed with wrapper")
fun formatAmount(amount: String): String {
  // TODO: how to always show 7 decimal points (1.0000000)?
  return amount
}

/**
 * Convert amount in [stroops](https://developers.stellar.org/docs/glossary#stroop) to amount in
 * lumens (XLM).
 *
 * @param stroops Amount in stroops to convert to lumens
 * @return amount in lumens (XLM)
 */
@Deprecated("To be removed with wrapper")
fun stroopsToLumens(stroops: String): String {
  return BigDecimal(stroops).divide(BigDecimal(XLM_PRECISION)).toPlainString()
}

/**
 * Convert amount in lumens (XLM) to amount in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop).
 *
 * @param lumens Amount in lumens (XLM) to convert to stroops
 * @return amount in stroops
 */
@Deprecated("To be removed with wrapper")
fun lumensToStroops(lumens: String): String {
  return BigDecimal(lumens).multiply(BigDecimal(XLM_PRECISION)).toPlainString()
}
