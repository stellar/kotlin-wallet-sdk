## MoneyGram integration example
### Code structure
This module contains an example for integration with the MoneyGram.  
The code is split between 4 parts:
1. [Deposit](src/main/kotlin/org/stellar/example/Deposit.kt) shows an example of making a deposit transaction
2. [Withdrawal](src/main/kotlin/org/stellar/example/Withdrawal.kt) shows an example of making a withdrawal transaction
3. [Get transaction](src/main/kotlin/org/stellar/example/GetTransaction.kt) shows an example of getting transaction information
4. [Info](src/main/kotlin/org/stellar/example/Info.kt) shows an example of getting MGI Anchor info

There is also a main [Runner](src/main/kotlin/org/stellar/example/Runner.kt) class that you can use 
to run either of these services. Pass either `deposit`, `withdrawal`, `transaction` or `info` as 
an argument to run the desired service.

### Environmental variables
You will need to define following env variables to successfully run the code:
1. `CLIENT_DOMAIN` [SEP-10](https://github.com/stellar/stellar-protocol/blob/master/ecosystem/sep-0010.md) client domain
2. `CLIENT_PRIVATE` private key from SEP-1 client domain file. Alternatively, you may want to implement your own `WalletSigner`.
3. (Optional) `STELLAR_KEY` private key of the user account with established USDC trustline. If not specified, default will be used. (Please do not drain the funds of the example account)

In addition, when calling `trasacntion` service, you will need auth token and transaction id. (Auth token is printed during withdrawal/deposit flow in the console):
1. `AUTH_TOKEN` SEP-10 Auth Token.
2. `TRANSACTION_ID` id of transaction you want to fetch data for, 