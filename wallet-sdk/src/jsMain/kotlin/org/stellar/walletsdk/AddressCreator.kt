package org.stellar.walletsdk

import external.*
import external.operation.Operation
import kotlinx.coroutines.await

class AddressCreator(private val secretKey: String) {
  suspend fun create() {
    val key = Keypair.fromSecret(secretKey)

    val server = Server("https://horizon-testnet.stellar.org")

    val acc = server.accounts().accountId(key.publicKey()).call().await()

    val tx =
      TransactionBuilder(
          Account(key.publicKey(), acc.sequence),
          object : TransactionBuilder.TransactionBuilderOptions {
            override var fee = "100"
            override var networkPassphrase: Networks? = Networks.TESTNET
          }
        )
        .setTimeout(0)
        .addMemo(Memo.text("test"))
        .addOperation(
          Operation.createAccount(
            object : OperationOptions.CreateAccount {
              override var destination: String = Keypair.random().publicKey()
              override var startingBalance = "1"
            }
          )
        )
        .build()

    tx.sign(key)

    try {
      val res = server.submitTransaction(tx).await()

      println(res.hash)
    } catch (e: Throwable) {
      val resultCodes = JSON.stringify(e.asDynamic().response?.data?.extras?.result_codes)
      val message = e.message

      throw Exception("$message $resultCodes")
    }
  }
}
