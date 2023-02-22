import OperationOptions.CreateAccount
import external.*
import external.operation.Operation
import kotlin.test.Test
import kotlin.test.fail
import kotlinx.coroutines.await
import kotlinx.coroutines.test.runTest

class StellarTest {
  @Test
  fun test() = runTest {
    val key = Keypair.fromSecret("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")

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

      fail("$message $resultCodes")
    }
  }
}
