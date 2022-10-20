package org.stellar.walletsdk.util

import java.math.BigDecimal

/**
 * Convert amount in [stroops](https://developers.stellar.org/docs/glossary#stroop) to amount in
 * lumens (XLM).
 *
 * @param stroops Amount in stroops to convert to lumens
 *
 * @return amount in lumens (XLM)
 */
fun stroopsToLumens(stroops: String): String {
  return BigDecimal(stroops).divide(BigDecimal(1e7)).toPlainString()
}

/**
 * Convert amount in lumens (XLM) to amount in
 * [stroops](https://developers.stellar.org/docs/glossary#stroop).
 *
 * @param lumens Amount in lumens (XLM) to convert to stroops
 *
 * @return amount in stroops
 */
fun lumensToStroops(lumens: String): String {
  return BigDecimal(lumens).multiply(BigDecimal(1e7)).toPlainString()
}
