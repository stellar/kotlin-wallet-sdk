import "./shim";
import {Button, StyleSheet, Text, View} from 'react-native';
import {org} from "kotlin-wallet-sdk";
// import AddressCreator = org.stellar.walletsdk.js.AddressCreator;
import {useState} from "react";
import Greeter = org.stellar.walletsdk.js.Greeter;
// import {Keypair} from 'stellar-sdk';


export default function App() {
    const [res, setRes] = useState("Loading...")

    return (
        <View style={styles.container}>
            <Text>{res}</Text>
            <Button onPress={async () => {
                await test(setRes)
            }} title={"Test"}></Button>
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
    // const controller = new AbortController()
    // const creator = new AddressCreator("SDYGC4TW5HHR5JA6CB2XLTTBF2DZRH2KDPBDPV3D5TXM6GF7FBPRZF3I")
    // // const randomBytes = Random.getRandomBytes(32);
    // const kp = Keypair.fromSecret("SDBQCGUDMHQXZ55QPGL5CDAYDT7B6UOVSLZUUSSP74QTVT3DBLCVCGYE");
    //
    // const res = await creator.create(kp, controller.signal)
    //
    // setRes(  JSON.stringify(res))
    const g = new Greeter()
    setRes(g.greet())
}