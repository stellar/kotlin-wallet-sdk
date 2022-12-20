package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import org.stellar.sdk.Asset
import org.stellar.walletsdk.WalletOperationType
import org.stellar.walletsdk.helpers.sdkObjectFromJsonFile

internal class FormatAnchorTransactionTest {
  private val anchorTransactions =
    sdkObjectFromJsonFile<AnchorTransactionsJson>("anchor_transactions.json")
  private val asset = Asset.create("SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")

  @Test
  fun `deposit transaction`() {
    val operation = formatAnchorTransaction(anchorTransactions.deposit, asset)

    assertEquals(operation.id, "09d12492-2d6a-48e9-a085-4ea1404dd4a6")
    assertEquals(operation.date, "2022-05-25T20:32:54.751725Z")
    assertEquals(operation.amount, "99.00")
    assertEquals(operation.account, "")
    assertEquals(
      operation.asset[0].id,
      "SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
    )
    assertEquals(operation.type, WalletOperationType.DEPOSIT)
  }

  @Test
  fun `withdrawal transaction`() {
    val operation = formatAnchorTransaction(anchorTransactions.withdrawal, asset)

    assertEquals(operation.id, "b6debb71-0f5b-423e-94d1-adea51c760b2")
    assertEquals(operation.date, "2022-06-07T20:18:59.349856Z")
    assertEquals(operation.amount, "32.00")
    assertEquals(operation.account, "")
    assertEquals(
      operation.asset[0].id,
      "SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
    )
    assertEquals(operation.type, WalletOperationType.WITHDRAW)
  }
}
