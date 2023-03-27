import external.Keypair
import external.Transaction__0
import kotlin.test.Test
import kotlinx.coroutines.await
import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.AddressCreator
import org.stellar.walletsdk.Signer

class StellarTest {
  @Test
  fun test() = runTest {
    println(
      AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I", TestSigner)
        .create(Keypair.random())
        .await()
    )
  }

  object TestSigner : Signer {
    override fun sign(t: Transaction__0, k: Keypair): Transaction__0 {
      t.sign(k)

      return t
    }
  }
}
