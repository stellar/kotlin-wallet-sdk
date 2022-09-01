package org.stellar.walletsdk.util

import java.math.BigDecimal

fun stroopsToLumens(stroops: String): String {
  return BigDecimal(stroops).divide(BigDecimal(1e7)).toPlainString()
}

fun lumensToStroops(lumens: String): String {
  return BigDecimal(lumens).multiply(BigDecimal(1e7)).toPlainString()
}
