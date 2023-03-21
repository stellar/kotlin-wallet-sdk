import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.Wallet

class AnchorTest {
  val anchor = Wallet.Testnet.anchor("testanchor.stellar.org")

  @Test
  fun testGetInfo() = runTest {
    val info = anchor.getInfo()

    println(info)

    val servicesInfo = anchor.getServicesInfo()

    println(servicesInfo)
  }

  //  @Test fun testAuth() = runTest { anchor.auth().authenticate(SigningKeyPair(Keypair.random()))
  // }
}
