// A very basic example of how to use TypeScript Wallet SDK
import * as TypeScriptWalletSdk from "kotlin-wallet-sdk";

// TODO: This will change, `org.stellar.walletsdk` won't be required
const WalletSdk = TypeScriptWalletSdk.org.stellar.walletsdk;

function basicWallet() {
  const config = new WalletSdk.StellarConfiguration();
  const wallet = new WalletSdk.Wallet(config);
  const anchor = wallet.anchor("https://testanchor.stellar.org");
  const info = anchor.getInfo();

  console.log("info: ", info);
}

basicWallet();
