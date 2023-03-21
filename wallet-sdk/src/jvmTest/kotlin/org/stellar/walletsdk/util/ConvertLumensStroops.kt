package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class ConvertLumensStroops {
  @Test
  fun `converted stroops to lumens`() {
    val lumens = stroopsToLumens("100")

    assertEquals("0.00001", lumens)
  }

  @Test
  fun `converted lumens to stroops`() {
    val lumens = lumensToStroops("0.00001")

    assertEquals("100.00000", lumens)
  }
}
