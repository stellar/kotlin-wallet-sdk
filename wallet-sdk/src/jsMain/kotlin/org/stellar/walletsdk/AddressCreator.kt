package org.stellar.walletsdk

import deezer.kustomexport.KustomExport
import external.*
import external.operation.Operation
import kotlinx.coroutines.await

@KustomExport
class AddressCreator(private val secretKey: String) {
  suspend fun create(newAcc: Keypair): Result {
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
              override var destination: String = newAcc.publicKey()
              override var startingBalance = "1"
            }
          )
        )
        .build()

    tx.sign(key)

    try {
      val res = server.submitTransaction(tx).await()

      return Result(res.hash, newAcc)
    } catch (e: Throwable) {
      val resultCodes = JSON.stringify(e.asDynamic().response?.data?.extras?.result_codes)
      val message = e.message

      throw Exception("$message $resultCodes")
    }
  }
}

@KustomExport data class Result(val hash: String, val keypair: Keypair)
