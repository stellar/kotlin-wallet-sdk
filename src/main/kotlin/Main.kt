import org.stellar.walletsdk.Wallet

fun main() {
  // Create wallet instance with Horizon
  val wallet = Wallet("https://horizon-testnet.stellar.org", "Test SDF Network ; September 2015")

  // Create account, generate keypairs
  //  val newAccountKeypair = wallet.create()
  //  println(newAccountKeypair)
}
