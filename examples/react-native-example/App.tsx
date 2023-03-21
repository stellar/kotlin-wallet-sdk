import walletsdk = org.stellar.walletsdk;

if (typeof Buffer === 'undefined') global.Buffer = require('buffer').Buffer
import {Button, StyleSheet, Text, View} from 'react-native';
import {org} from "kotlin-wallet-sdk";
import {useState} from "react";
import {Keypair, Transaction} from 'stellar-sdk';
import * as Random from 'expo-random';
import Greeter = org.stellar.walletsdk.Greeter;
import Signer = org.stellar.walletsdk.Signer;
import IssuedAssetId = org.stellar.walletsdk.asset.IssuedAssetId;
import InteractiveFlowResponse = org.stellar.walletsdk.InteractiveFlowResponse;
import Wallet = org.stellar.walletsdk.js.Wallet;
import testnetWallet = org.stellar.walletsdk.js.testnetWallet;
import PublicKeyPair = org.stellar.walletsdk.horizon.PublicKeyPair;

export default function App() {
    const [res, setRes] = useState("Press button to create account")

    return (
        <View style={styles.container}>
            <Text>{res}</Text>
            <Button onPress={async () => {
                await test(setRes)
            }} title={"Create account"}></Button>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        alignItems: 'center',
        justifyContent: 'center',
    },
});

async function test(setRes: React.Dispatch<React.SetStateAction<string>>) {
    try {
        const controller = new AbortController()

        const creator = new walletsdk.AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I", new ExampleSigner())
        const bytes = Random.getRandomBytes(32)
        const kp = Keypair.fromRawEd25519Seed(Buffer.from(bytes));

        console.log("Creating account")

        const res = await creator.create(kp)

        Greeter.Companion.test()

        setRes(`Account created. \nTransaction hash: ${res.hash}. \nAddress: ${(res.keypair as Keypair).publicKey()}`)


        const anchor = testnetWallet().anchor("testanchor.stellar.org", null)
        const USDC =
            new IssuedAssetId("USDC", "GBBD47IF6LWK7P7MDEVSCWR7DPUWV3NY3DTQEVFL4NAT4AQH3ZLLFLA5")

        const auth = await anchor.auth(controller.signal)

        const authToken: string = await auth.authenticate(new PublicKeyPair(kp), null, null, null, controller.signal)

        const interactive: InteractiveFlowResponse = await anchor.interactive().deposit(kp.publicKey(), USDC, authToken, null, controller.signal)

        setRes(`Interactive flow url: ${interactive.url}`)
    } catch (e) {
        console.error(e)
    }
}

class ExampleSigner implements Signer {
    sign(t: Transaction<any, any>, k: Keypair): Transaction<any, any> {
        console.log("Using example signer")
        t.sign(k)
        return t;
    }
}