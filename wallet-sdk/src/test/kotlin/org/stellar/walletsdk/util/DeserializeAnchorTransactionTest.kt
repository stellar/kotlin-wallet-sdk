package org.stellar.walletsdk.util

import kotlin.test.assertIs
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.helpers.sdkObjectFromJsonFile

@Serializable
data class AnchorTransactionsJson(
  val deposit: AnchorTransaction,
  val withdrawal: AnchorTransaction,
  val incompleteDeposit: AnchorTransaction,
  val incompleteWithdrawal: AnchorTransaction
)

@DisplayName("deserializeAnchorTransaction")
internal class DeserializeAnchorTransactionTest {
  private val anchorTransactions =
    sdkObjectFromJsonFile<AnchorTransactionsJson>("anchor_transactions.json")

  @Test
  fun deposit() {
    assertIs<DepositTransaction>(anchorTransactions.deposit)
  }

  @Test
  fun withdrawal() {
    assertIs<WithdrawalTransaction>(anchorTransactions.withdrawal)
  }

  @Test
  fun `incomplete deposit`() {
    assertIs<IncompleteDepositTransaction>(anchorTransactions.incompleteDeposit)
  }

  @Test
  fun `incomplete withdrawal`() {
    assertIs<IncompleteWithdrawalTransaction>(anchorTransactions.incompleteWithdrawal)
  }
}
