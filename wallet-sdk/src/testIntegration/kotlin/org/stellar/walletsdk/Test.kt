package org.stellar.walletsdk

import kotlin.test.assertFalse
import org.junit.jupiter.api.Test

class Test {

  @Test
  fun testOk() {
    println("OK")
  }

  @Test
  fun testFail() {
    println("FAIL")
    assertFalse(true)
  }
}
