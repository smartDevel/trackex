package eu.ways4.trackex.home.presentation

import android.content.Context
import eu.ways4.trackex.R
import eu.ways4.trackex.data.model.Currency
import eu.ways4.trackex.home.presentation.DateRange.*

class SummaryItemModel(
    context: Context,
    val currencySummaries: List<Pair<Currency, Double>>,
    val dateRange: DateRange
) : HomeItemModel {

    var itemModels: List<CurrencySummaryItemModel>
    var dateRangeText: String
    var dateRangeChange: ((DateRange) -> Unit)? = null

    init {
        itemModels = createItemModels(currencySummaries)
        dateRangeText = createDateRangeText(context, dateRange)
    }

    private fun createItemModels(currencySummaries: List<Pair<Currency, Double>>) =
            currencySummaries.map { createCurrencySummaryItemModel(it.first, it.second) }

    private fun createCurrencySummaryItemModel(currency: Currency, amount: Double) =
            CurrencySummaryItemModel(currency, amount)

    private fun createDateRangeText(context: Context, dateRange: DateRange): String {
        return when (dateRange) {
            TODAY -> context.getString(R.string.today)
            THIS_WEEK -> context.getString(R.string.this_week)
            THIS_MONTH -> context.getString(R.string.this_month)
            LAST_MONTH -> context.getString(R.string.last_month)
            MONTH_MINUS02 -> context.getString(R.string.month_minus02)
            MONTH_MINUS03 -> context.getString(R.string.month_minus03)
            MONTH_MINUS12 -> context.getString(R.string.month_minus12)
            ALL_TIME -> context.getString(R.string.all_time)
            OCTOBER -> context.getString(R.string.october)
            NOVEMBER -> context.getString(R.string.november)
        }
    }

    fun onTodayClick() = dateRangeChange?.invoke(TODAY)

    fun onThisWeekClick() = dateRangeChange?.invoke(DateRange.THIS_WEEK)

    fun onThisMonthClick() = dateRangeChange?.invoke(DateRange.THIS_MONTH)

    fun onLastMonthClick() = dateRangeChange?.invoke(DateRange.LAST_MONTH)

    fun onMonthMinus02Click() = dateRangeChange?.invoke(DateRange.MONTH_MINUS02)

    fun onMonthMinus03Click() = dateRangeChange?.invoke(DateRange.MONTH_MINUS03)

    fun onMonthMinus12Click() = dateRangeChange?.invoke(DateRange.MONTH_MINUS12)

    fun onOctoberClick() = dateRangeChange?.invoke(DateRange.OCTOBER)

    fun onNovemberClick() = dateRangeChange?.invoke(DateRange.NOVEMBER)

    fun onAllTimeClick() = dateRangeChange?.invoke(DateRange.ALL_TIME)
}