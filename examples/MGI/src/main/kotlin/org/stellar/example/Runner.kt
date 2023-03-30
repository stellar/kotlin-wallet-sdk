package org.stellar.example

suspend fun main(args: Array<String>) {
  val acceptedString = "('deposit', 'withdrawal' or 'transaction' value are accepted)"

  when (args.getOrNull(0)?.lowercase()) {
    "deposit" -> Deposit.main(emptyArray())
    "withdrawal" -> Withdrawal.main()
    "transaction" -> GetTransaction.main(emptyArray())
    null -> throw IllegalStateException("Provide type of job to run $acceptedString")
    else -> throw IllegalArgumentException("Unknown type $acceptedString ")
  }
}
