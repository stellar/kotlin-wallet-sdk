package org.stellar.walletsdk.util

import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Test
import org.stellar.walletsdk.anchor.*
import org.stellar.walletsdk.exception.InvalidJsonException
import org.stellar.walletsdk.helpers.sdkObjectFromJsonFile
import org.stellar.walletsdk.json.fromJson

@Serializable
data class AnchorTransactionsJson(
  val deposit: AnchorTransaction,
  val withdrawal: AnchorTransaction,
  val incompleteDeposit: AnchorTransaction,
  val incompleteWithdrawal: AnchorTransaction
)

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

  @Test
  fun `missing kind does not leak response body in exception message`() {
    val sensitive = "eyJ.SENSITIVE_TOKEN_PAYLOAD.SIGNATURE"
    val payload =
      """{"status": "completed", "more_info_url": "https://anchor.example/?token=$sensitive"}"""

    val ex = assertFailsWith<InvalidJsonException> { payload.fromJson<AnchorTransaction>() }

    val message = ex.message ?: ""
    assertFalse(
      message.contains(sensitive),
      "exception message must not leak response body; got: $message"
    )
  }

  @Test
  fun `missing status does not leak response body in exception message`() {
    val sensitive = "FIRST_NAME=Alice;LAST_NAME=Smith;DOB=1990-01-01"
    val payload = """{"kind": "deposit", "metadata": "$sensitive"}"""

    val ex = assertFailsWith<InvalidJsonException> { payload.fromJson<AnchorTransaction>() }

    val message = ex.message ?: ""
    assertFalse(
      message.contains(sensitive),
      "exception message must not leak response body; got: $message"
    )
  }

  @Test
  fun `invalid kind value does not leak response body in exception message`() {
    val sensitive = "another_secret_value_12345"
    val payload = """{"kind": "bogus", "status": "completed", "memo": "$sensitive"}"""

    val ex = assertFailsWith<InvalidJsonException> { payload.fromJson<AnchorTransaction>() }

    val message = ex.message ?: ""
    assertFalse(
      message.contains(sensitive),
      "exception message must not leak response body; got: $message"
    )
  }
}
