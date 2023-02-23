import external.Keypair
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.AddressCreator

class StellarTest {
  @Test
  fun test() = runTest {
    AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")
      .create(Keypair.random())
  }
}
