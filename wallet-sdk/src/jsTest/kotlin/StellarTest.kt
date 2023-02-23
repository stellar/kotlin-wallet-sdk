import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.AddressCreator
import kotlin.test.Test

class StellarTest {
  @Test
  fun test() = runTest {
    AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I").create()
  }
}
