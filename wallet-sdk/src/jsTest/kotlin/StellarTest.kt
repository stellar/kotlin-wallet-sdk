import external.*
import kotlin.test.Test

class StellarTest {
  @Test
  fun test() {
    val key = Keypair.fromSecret("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")

    val tx =
      TransactionBuilder(
          Account(key.publicKey(), "0"),
          object : TransactionBuilder.TransactionBuilderOptions {
            override var fee = "100"
            override var networkPassphrase: Networks? = Networks.TESTNET
          }
        )
        .setTimeout(0)
        .addMemo(Memo.text("test"))
        .build()

    tx.sign(key)

    println(tx.toXDR())
  }
}
