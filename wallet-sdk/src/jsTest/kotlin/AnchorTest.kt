import kotlinx.coroutines.test.runTest
import org.stellar.walletsdk.Wallet
import kotlin.test.Test

class AnchorTest {
    @Test
    fun testGetInfo() = runTest {
        val anchor = Wallet.Testnet.anchor("testanchor.stellar.org")
        val info = anchor.getInfo()

        println(info)

        val servicesInfo = anchor.getServicesInfo()

        println(servicesInfo)
    }
}