package org.stellar.example

suspend fun main(args: Array<String>) {
  val acceptedString = "('deposit', 'withdrawal', 'transaction', 'info' value are accepted)"

  when (args.getOrNull(0)?.lowercase()) {
    "deposit" -> Deposit.main()
    "withdrawal" -> Withdrawal.main()
    "transaction" -> GetTransaction.main()
    "info" -> Info.main()
    null -> throw IllegalStateException("Provide type of job to run $acceptedString")
    else -> throw IllegalArgumentException("Unknown type $acceptedString ")
  }
}
