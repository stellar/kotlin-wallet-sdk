package org.stellar.walletsdk

import external.*
import external.operation.Operation
import kotlin.js.Promise
import kotlinx.coroutines.*

@JsExport
class AddressCreator(private val secretKey: String, private val signer: Signer) {
  fun create(newAcc: Keypair): Promise<Result> =
    CoroutineScope(Dispatchers.Main).promise {
      val key = Keypair.fromSecret(secretKey)

      val server = Server("https://horizon-testnet.stellar.org")

      val acc = server.accounts().accountId(key.publicKey()).call().await()

      val tx =
        TransactionBuilder(
            Account(key.publicKey(), acc.sequence),
            object : TransactionBuilder.TransactionBuilderOptions {
              override var fee = "100"
              override var networkPassphrase: String? = Network.TESTNET.passphrase
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

      signer.sign(tx, key)

      try {
        val res = server.submitTransaction(tx).await()

        return@promise Result(res.hash, newAcc)
      } catch (e: Throwable) {
        val resultCodes = JSON.stringify(e.asDynamic().response?.data?.extras?.result_codes)
        val message = e.message

        throw Exception("$message $resultCodes")
      }
    }
}

@JsExport
external interface Signer {
  fun sign(t: Transaction__0, k: Keypair): Transaction__0
}

@JsExport data class Result(val hash: String, val keypair: Keypair)
