package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("convertLumensStroops")
internal class ConvertLumensStroops {
  @Nested
  @DisplayName("stroopsToLumens")
  inner class StroopsToLumens {
    @Test
    fun `converted stroops to lumens`() {
      val lumens = stroopsToLumens("100")

      assertEquals("0.00001", lumens)
    }
  }

  @Nested
  @DisplayName("lumensToStroops")
  inner class LumensToStroops {
    @Test
    fun `converted lumens to stroops`() {
      val lumens = lumensToStroops("0.00001")

      assertEquals("100.00000", lumens)
    }
  }
}
