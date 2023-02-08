package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.stellar.sdk.Asset
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.asset.NativeAssetId
import org.stellar.walletsdk.asset.toAssetId

internal class FormatAssetTest {
  @Test
  fun `native asset`() {
    val asset = Asset.create("native")

    assertEquals(asset.toAssetId(), NativeAssetId)
  }

  @Test
  fun `credit_alphanum4 asset`() {
    val asset =
      Asset.create(
        "credit_alphanum4",
        "SRT",
        "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
      )

    assertEquals(
      asset.toAssetId(),
        IssuedAssetId(
            code = "SRT",
            issuer = "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
        )
    )
  }

  @Test
  fun `credit_alphanum12 asset`() {
    val asset =
      Asset.create(
        "credit_alphanum12",
        "STELLAR",
        "GC62NIRU3XI5HI3O3X5JH2YDG2YNXVYVPDGS3THUJ4BVWOOOO2ZJDDLO"
      )

    assertEquals(
      asset.toAssetId(),
        IssuedAssetId(
            code = "STELLAR",
            issuer = "GC62NIRU3XI5HI3O3X5JH2YDG2YNXVYVPDGS3THUJ4BVWOOOO2ZJDDLO"
        )
    )
  }
}
