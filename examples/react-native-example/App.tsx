if (typeof Buffer === 'undefined') global.Buffer = require('buffer').Buffer
import {Button, StyleSheet, Text, View} from 'react-native';
import {org} from "kotlin-wallet-sdk";
import AddressCreator = org.stellar.walletsdk.js.AddressCreator;
import {useState} from "react";
import Greeter = org.stellar.walletsdk.js.Greeter;
import {Keypair} from 'stellar-sdk';


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
    // const randomBytes = Random.getRandomBytes(32);
    try {
        const controller = new AbortController()
        const creator = new AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")
        const kp = Keypair.fromSecret("SDJYH364ZYXV6RWVPHJ7YXRS6VNPFLEAZIT3S6LPCKTFJPMAWBJ5LV55");

        console.log("Creating account")

        const res = await creator.create(kp, controller.signal).catch(reason => console.error(reason))

        setRes(JSON.stringify(res))
    } catch (e) {
        console.log(e)
    }

}