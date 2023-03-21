import external.Keypair
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.asset.IssuedAssetId
import org.stellar.walletsdk.horizon.SigningKeyPair

class AnchorTest {
  val anchor = Wallet.Testnet.anchor("testanchor.stellar.org")
  private val USDC = IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

  @Test
  fun testGetInfo() = runTest {
    val info = anchor.getInfo()

    println(info)

    val servicesInfo = anchor.getServicesInfo()

    println(servicesInfo)
  }

  @Test fun testAuth() = runTest { anchor.auth().authenticate(SigningKeyPair(Keypair.random())) }

  @Test
  fun testInteractive() = runTest {
    val key = SigningKeyPair(Keypair.random())

    val authToken = anchor.auth().authenticate(key)

    val interactive = anchor.interactive().deposit(key.address, USDC, authToken)

    println(interactive)
  }
}
