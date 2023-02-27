import external.Keypair
import external.Transaction
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.AddressCreator
import org.stellar.walletsdk.Signer

class StellarTest {
  @Test
  fun test() = runTest {
    AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I", TestSigner)
      .create(Keypair.random())
  }

  object TestSigner : Signer {
    override fun sign(t: Transaction<*, *>, k: Keypair): Transaction<*, *> {
      t.sign(k)

      return t
    }
  }
}
