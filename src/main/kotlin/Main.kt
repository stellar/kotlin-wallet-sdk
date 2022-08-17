import org.stellar.sdk.Network
import org.stellar.walletsdk.Wallet

fun main() {
  // Create wallet instance with Horizon
  val wallet = Wallet("https://horizon-testnet.stellar.org", Network.TESTNET.toString())

  // Create account, generate keypairs
  //  val newAccountKeypair = wallet.create()
  //  println(newAccountKeypair)

  val sourceAddress = "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22"
  val destinationAddress = "GDEIYYWIVK24CCQ3Y4QNGEIBEFABTTCFBRTVNQZ43VPOUNQARO7ZEKJY"

  // Fund account
  //  try {
  //    val fundTxn = wallet.fund(sourceAddress, destinationAddress)
  //    val fundSponsoredTxn =
  //      wallet.fund(sourceAddress, destinationAddress, sponsorAddress = sourceAddress)
  //  } catch (e: Error) {
  //    println(e.toString())
  //  }
}
