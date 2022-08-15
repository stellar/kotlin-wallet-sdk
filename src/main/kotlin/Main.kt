import org.stellar.sdk.Network
import org.stellar.walletsdk.Wallet

fun main() {
  // Create wallet instance with Horizon
  val wallet = Wallet("https://horizon-testnet.stellar.org", Network.TESTNET.toString())

  // Create account, generate keypairs
  //  val newAccountKeypair = wallet.create()
  //  println(newAccountKeypair)
}
