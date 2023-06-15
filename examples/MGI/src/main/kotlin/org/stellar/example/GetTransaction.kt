package org.stellar.example

import org.stellar.walletsdk.auth.AuthToken

object GetTransaction {
  @JvmStatic
  suspend fun main() {
    val token: String = System.getenv("AUTH_TOKEN")
    val id: String = System.getenv("TRANSACTION_ID")

    val transaction = anchor.getTransaction(id, AuthToken.from(token))

    println(transaction)
  }
}
