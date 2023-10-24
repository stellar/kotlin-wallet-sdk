import java.net.URL
import java.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.*
import org.stellar.sdk.Transaction
import org.stellar.walletsdk.ApplicationConfiguration
import org.stellar.walletsdk.StellarConfiguration
import org.stellar.walletsdk.Wallet
import org.stellar.walletsdk.fromJson
import org.stellar.walletsdk.horizon.AccountKeyPair
import org.stellar.walletsdk.horizon.SigningKeyPair

@Serializable class TransactionData(val id: String)

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecoveryTest {

  val wallet = Wallet(StellarConfiguration.Testnet, ApplicationConfiguration())
  val stellarService = wallet.stellar()

  lateinit var masterPair: AccountKeyPair
  lateinit var devicePair: AccountKeyPair

  fun linkHelper(key: String, value: String) {
    println("https://testnet.stellarchain.io/${key}/${value}")
  }

  // https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0030.md
  @BeforeAll
  fun testSetup() {
    masterPair = stellarService.account().createKeyPair()
    devicePair = stellarService.account().createKeyPair()
  }

  @Test
  @Order(1)
  @DisplayName("1.1 - Create a MASTER Keypair")
  fun shouldCreateMasterKeyPair() {
    println("address: ${masterPair.address}")
    linkHelper("accounts", masterPair.address)
    println("secret key: ${(masterPair as SigningKeyPair).secretKey}")
    // assertions here
  }

  @Test
  @Order(2)
  @DisplayName("1.2 - Create a DEVICE Keypair")
  fun shouldCreateDeviceKeyPair() {
    println("address: ${devicePair.address}")
    linkHelper("accounts", devicePair.address)
    println("secret key: ${(devicePair as SigningKeyPair).secretKey}")
    // assertions here
  }

  @Test
  @Order(3)
  @DisplayName("1.3 - Fund the Master Keypair")
  fun shouldFundMasterKeyPair() {
    friendbotHelper(masterPair.address)
  }

  @Test
  @Order(4)
  @DisplayName("1.4 - Fund the Device Keypair")
  fun shouldFundDeviceKeyPair() {
    friendbotHelper(devicePair.address)
  }

  fun friendbotHelper(address: String) {
    val friendbotUrl = java.lang.String.format("https://friendbot.stellar.org/?addr=%s", address)
    val response = URL(friendbotUrl).openStream()
    val body: String = Scanner(response, "UTF-8").useDelimiter("\\A").next()
    val json = body.fromJson<TransactionData>()
    println("SUCCESS! You have a new account :)")
    println("tx hash: ${json.id}")
    linkHelper("transactions", json.id)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @Test
  @Order(5)
  @DisplayName("1.5 - Add Device Keypair As Signer And Remove Master Keypair As Signer")
  fun addDeviceKeyPairAsSigner() = runTest { addDeviceRemoveMaster() }

  private suspend fun addDeviceRemoveMaster() {
    val tx: Transaction =
      stellarService
        .transaction(masterPair)
        .addAccountSigner(devicePair, 1)
        .lockAccountMasterKey()
        .build()
    println("addDeviceKey tx: ${tx.hashHex()}")
    val signedTx: Transaction = (masterPair as SigningKeyPair).sign(tx)
    println("the signed tx hash: ${signedTx.hashHex()}")
    linkHelper("transactions", signedTx.hashHex())
    stellarService.submitTransaction(signedTx)
  }
}
