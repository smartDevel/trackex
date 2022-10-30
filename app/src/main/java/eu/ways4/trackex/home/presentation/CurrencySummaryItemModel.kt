package eu.ways4.trackex.home.presentation

import eu.ways4.trackex.data.model.Currency

data class CurrencySummaryItemModel(val currency: Currency, val amount: Double) {
    val amountText by lazy { "${"%.2f".format(amount)} ${currency.symbol}" }
    val currencyText by lazy { "(${currency.title} • ${currency.code})" }
}