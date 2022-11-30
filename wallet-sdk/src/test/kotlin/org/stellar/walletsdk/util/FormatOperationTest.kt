package org.stellar.walletsdk.util

import kotlin.test.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.stellar.sdk.Asset
import org.stellar.sdk.responses.operations.*
import org.stellar.walletsdk.WalletAsset
import org.stellar.walletsdk.WalletOperationType
import org.stellar.walletsdk.anchor.AnchorTransaction
import org.stellar.walletsdk.helpers.objectFromJsonFile

data class StellarOperationsJson(
  val createAccount: CreateAccountOperationResponse,
  val paymentNative: PaymentOperationResponse,
  val paymentAsset: PaymentOperationResponse,
  val pathPaymentStrictReceive: PathPaymentStrictReceiveOperationResponse,
  val pathPaymentStrictReceiveSwap: PathPaymentStrictReceiveOperationResponse,
  val pathPaymentStrictSend: PathPaymentStrictSendOperationResponse,
  val pathPaymentStrictSendSwap: PathPaymentStrictSendOperationResponse,
  val changeTrust: ChangeTrustOperationResponse,
  val claimClaimableBalance: ClaimClaimableBalanceOperationResponse
)

data class AnchorTransactionsJson(
  val deposit: AnchorTransaction,
  val withdrawal: AnchorTransaction
)

internal class FormatOperationTest {
  @Nested
  @DisplayName("formatStellarOperation")
  inner class FormatStellarOperation {
    private val stellarOperations =
      objectFromJsonFile("stellar_operations.json", StellarOperationsJson::class.java)

    @Test
    fun `create account for account creator`() {
      val operation =
        formatStellarOperation(
          "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22",
          stellarOperations.createAccount
        )

      assertEquals(operation.id, "34832184791041")
      assertEquals(operation.date, "2022-09-14T21:19:29Z")
      assertEquals(operation.amount, "10000.0000000")
      assertEquals(operation.account, "GAIH3ULLFQ4DGSECF2AR555KZ4KNDGEKN4AFI4SU2M7B43MGK3QJZNSR")
      assertEquals(operation.asset[0].id, "XLM:Native")
      assertEquals(operation.type, WalletOperationType.SEND)
    }

    @Test
    fun `create account for created account`() {
      val operation =
        formatStellarOperation(
          "GAIH3ULLFQ4DGSECF2AR555KZ4KNDGEKN4AFI4SU2M7B43MGK3QJZNSR",
          stellarOperations.createAccount
        )

      assertEquals(operation.id, "34832184791041")
      assertEquals(operation.date, "2022-09-14T21:19:29Z")
      assertEquals(operation.amount, "10000.0000000")
      assertEquals(operation.account, "GAMQTINWD3YPP3GLTQZ4M6FKCCSRGROQLIIRVECIFC6VEGL5F64CND22")
      assertEquals(operation.asset[0].id, "XLM:Native")
      assertEquals(operation.type, WalletOperationType.RECEIVE)
    }

    @Test
    fun `native payment for sender`() {
      val operation =
        formatStellarOperation(
          "GAKEBVZB3RQO5GOFMLKATWKIQ2Z7SAPBG6E3LY7MELNRREYFV43MG7FQ",
          stellarOperations.paymentNative
        )

      assertEquals(operation.id, "163322432148766721")
      assertEquals(operation.date, "2021-10-28T18:35:53Z")
      assertEquals(operation.amount, "0.0100000")
      assertEquals(operation.account, "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42")
      assertEquals(operation.asset[0].id, "XLM:Native")
      assertEquals(operation.type, WalletOperationType.SEND)
    }

    @Test
    fun `native payment for receiver`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.paymentNative
        )

      assertEquals(operation.id, "163322432148766721")
      assertEquals(operation.date, "2021-10-28T18:35:53Z")
      assertEquals(operation.amount, "0.0100000")
      assertEquals(operation.account, "GAKEBVZB3RQO5GOFMLKATWKIQ2Z7SAPBG6E3LY7MELNRREYFV43MG7FQ")
      assertEquals(operation.asset[0].id, "XLM:Native")
      assertEquals(operation.type, WalletOperationType.RECEIVE)
    }

    @Test
    fun `asset payment for sender`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.paymentAsset
        )

      assertEquals(operation.id, "163328062850232321")
      assertEquals(operation.date, "2021-10-28T20:42:42Z")
      assertEquals(operation.amount, "10.0000000")
      assertEquals(operation.account, "GAKEBVZB3RQO5GOFMLKATWKIQ2Z7SAPBG6E3LY7MELNRREYFV43MG7FQ")
      assertEquals(
        operation.asset[0].id,
        "QUIET:GAXE7IP22GTI4H373KDVVSXZTPM4GMRDUKSXM4MFZMYYRB4HYQHLJCBO"
      )
      assertEquals(operation.type, WalletOperationType.SEND)
    }

    @Test
    fun `asset payment for receiver`() {
      val operation =
        formatStellarOperation(
          "GAKEBVZB3RQO5GOFMLKATWKIQ2Z7SAPBG6E3LY7MELNRREYFV43MG7FQ",
          stellarOperations.paymentAsset
        )

      assertEquals(operation.id, "163328062850232321")
      assertEquals(operation.date, "2021-10-28T20:42:42Z")
      assertEquals(operation.amount, "10.0000000")
      assertEquals(operation.account, "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42")
      assertEquals(
        operation.asset[0].id,
        "QUIET:GAXE7IP22GTI4H373KDVVSXZTPM4GMRDUKSXM4MFZMYYRB4HYQHLJCBO"
      )
      assertEquals(operation.type, WalletOperationType.RECEIVE)
    }

    @Test
    fun `path payment strict receive for sender`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.pathPaymentStrictReceive
        )

      assertEquals(operation.id, "124018825644490753")
      assertEquals(operation.date, "2020-03-26T19:33:55Z")
      assertEquals(operation.amount, "0.1000000")
      assertEquals(operation.account, "GBZH7S5NC57XNHKHJ75C5DGMI3SP6ZFJLIKW74K6OSMA5E5DFMYBDD2Z")
      assertEquals(
        operation.asset[0].id,
        "USD:GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"
      )
      assertEquals(
        operation.asset[1].id,
        "BRL:GDVKY2GU2DRXWTBEYJJWSFXIGBZV6AZNBVVSUHEPZI54LIS6BA7DVVSP"
      )
      assertEquals(operation.type, WalletOperationType.SEND)
    }

    @Test
    fun `path payment strict receive for receiver`() {
      val operation =
        formatStellarOperation(
          "GBZH7S5NC57XNHKHJ75C5DGMI3SP6ZFJLIKW74K6OSMA5E5DFMYBDD2Z",
          stellarOperations.pathPaymentStrictReceive
        )

      assertEquals(operation.id, "124018825644490753")
      assertEquals(operation.date, "2020-03-26T19:33:55Z")
      assertEquals(operation.amount, "0.1000000")
      assertEquals(operation.account, "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42")
      assertEquals(
        operation.asset[0].id,
        "USD:GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"
      )
      assertEquals(
        operation.asset[1].id,
        "BRL:GDVKY2GU2DRXWTBEYJJWSFXIGBZV6AZNBVVSUHEPZI54LIS6BA7DVVSP"
      )
      assertEquals(operation.type, WalletOperationType.RECEIVE)
    }

    @Test
    fun `path payment strict receive swap`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.pathPaymentStrictReceiveSwap
        )

      assertEquals(operation.id, "124018825644490753")
      assertEquals(operation.date, "2020-03-26T19:33:55Z")
      assertEquals(operation.amount, "0.1000000")
      assertEquals(operation.account, "")
      assertEquals(
        operation.asset[0].id,
        "USD:GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"
      )
      assertEquals(
        operation.asset[1].id,
        "BRL:GDVKY2GU2DRXWTBEYJJWSFXIGBZV6AZNBVVSUHEPZI54LIS6BA7DVVSP"
      )
      assertEquals(operation.type, WalletOperationType.SWAP)
    }

    @Test
    fun `path payment strict send for sender`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.pathPaymentStrictSend
        )

      assertEquals(operation.id, "124624072438579201")
      assertEquals(operation.date, "2020-04-04T13:47:50Z")
      assertEquals(operation.amount, "26.5544244")
      assertEquals(operation.account, "GBZH7S5NC57XNHKHJ75C5DGMI3SP6ZFJLIKW74K6OSMA5E5DFMYBDD2Z")
      assertEquals(
        operation.asset[0].id,
        "USD:GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"
      )
      assertEquals(
        operation.asset[1].id,
        "BRL:GDVKY2GU2DRXWTBEYJJWSFXIGBZV6AZNBVVSUHEPZI54LIS6BA7DVVSP"
      )
      assertEquals(operation.type, WalletOperationType.SEND)
    }

    @Test
    fun `path payment strict send for receiver`() {
      val operation =
        formatStellarOperation(
          "GBZH7S5NC57XNHKHJ75C5DGMI3SP6ZFJLIKW74K6OSMA5E5DFMYBDD2Z",
          stellarOperations.pathPaymentStrictSend
        )

      assertEquals(operation.id, "124624072438579201")
      assertEquals(operation.date, "2020-04-04T13:47:50Z")
      assertEquals(operation.amount, "26.5544244")
      assertEquals(operation.account, "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42")
      assertEquals(
        operation.asset[0].id,
        "USD:GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"
      )
      assertEquals(
        operation.asset[1].id,
        "BRL:GDVKY2GU2DRXWTBEYJJWSFXIGBZV6AZNBVVSUHEPZI54LIS6BA7DVVSP"
      )
      assertEquals(operation.type, WalletOperationType.RECEIVE)
    }

    @Test
    fun `path payment strict send for swap`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.pathPaymentStrictSendSwap
        )

      assertEquals(operation.id, "124624072438579201")
      assertEquals(operation.date, "2020-04-04T13:47:50Z")
      assertEquals(operation.amount, "26.5544244")
      assertEquals(operation.account, "")
      assertEquals(
        operation.asset[0].id,
        "USD:GDUKMGUGDZQK6YHYA5Z6AY2G4XDSZPSZ3SW5UN3ARVMO6QSRDWP5YLEX"
      )
      assertEquals(
        operation.asset[1].id,
        "BRL:GDVKY2GU2DRXWTBEYJJWSFXIGBZV6AZNBVVSUHEPZI54LIS6BA7DVVSP"
      )
      assertEquals(operation.type, WalletOperationType.SWAP)
    }

    @Test
    fun `change trust should be formatted as other`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.changeTrust
        )

      assertEquals(operation.id, "163325782222753793")
      assertEquals(operation.date, "2021-10-28T19:50:44Z")
      assertEquals(operation.amount, "")
      assertEquals(operation.account, "")
      assertEquals(operation.asset, listOf<WalletAsset>())
      assertEquals(operation.type, WalletOperationType.OTHER)
    }

    @Test
    fun `claim claimable balance should be formatted as other`() {
      val operation =
        formatStellarOperation(
          "GCOK6S3AUTVOEBSTFFDRMMFBWVH7BIWC56U5HUW7OH5EF5YE75WKVL42",
          stellarOperations.claimClaimableBalance
        )

      assertEquals(operation.id, "163325782222753794")
      assertEquals(operation.date, "2021-10-29T10:51:44Z")
      assertEquals(operation.amount, "")
      assertEquals(operation.account, "")
      assertEquals(operation.asset, listOf<WalletAsset>())
      assertEquals(operation.type, WalletOperationType.OTHER)
    }
  }

  @Nested
  @DisplayName("formatAnchorTransaction")
  inner class FormatAnchorTransaction {
    private val anchorTransactions =
      objectFromJsonFile("anchor_transactions.json", AnchorTransactionsJson::class.java)
    private val asset = Asset.create("SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B")

    @Test
    fun `deposit transaction`() {
      val operation = formatAnchorTransaction(anchorTransactions.deposit, asset)

      assertEquals(operation.id, "09d12492-2d6a-48e9-a085-4ea1404dd4a6")
      assertEquals(operation.date, "2022-05-25T20:32:54.751725Z")
      assertEquals(operation.amount, "99.00")
      assertEquals(operation.account, "")
      assertEquals(
        operation.asset[0].id,
        "SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
      )
      assertEquals(operation.type, WalletOperationType.DEPOSIT)
    }

    @Test
    fun `withdrawal transaction`() {
      val operation = formatAnchorTransaction(anchorTransactions.withdrawal, asset)

      assertEquals(operation.id, "b6debb71-0f5b-423e-94d1-adea51c760b2")
      assertEquals(operation.date, "2022-06-07T20:18:59.349856Z")
      assertEquals(operation.amount, "32.00")
      assertEquals(operation.account, "")
      assertEquals(
        operation.asset[0].id,
        "SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
      )
      assertEquals(operation.type, WalletOperationType.WITHDRAW)
    }
  }

  @Nested
  @DisplayName("formatAsset")
  inner class FormatAsset {
    @Test
    fun `native asset`() {
      val asset = Asset.create("native")

      assertEquals(
        formatAsset(asset),
        WalletAsset(id = "XLM:Native", code = "XLM", issuer = "Native")
      )
    }

    @Test
    fun `credit_alphanum4 asset`() {
      val asset =
        Asset.create(
          "credit_alphanum4",
          "SRT",
          "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
        )

      assertEquals(
        formatAsset(asset),
        WalletAsset(
          id = "SRT:GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B",
          code = "SRT",
          issuer = "GCDNJUBQSX7AJWLJACMJ7I4BC3Z47BQUTMHEICZLE6MU4KQBRYG5JY6B"
        )
      )
    }

    @Test
    fun `credit_alphanum12 asset`() {
      val asset =
        Asset.create(
          "credit_alphanum12",
          "STELLAR",
          "GC62NIRU3XI5HI3O3X5JH2YDG2YNXVYVPDGS3THUJ4BVWOOOO2ZJDDLO"
        )

      assertEquals(
        formatAsset(asset),
        WalletAsset(
          id = "STELLAR:GC62NIRU3XI5HI3O3X5JH2YDG2YNXVYVPDGS3THUJ4BVWOOOO2ZJDDLO",
          code = "STELLAR",
          issuer = "GC62NIRU3XI5HI3O3X5JH2YDG2YNXVYVPDGS3THUJ4BVWOOOO2ZJDDLO"
        )
      )
    }
  }
}
