package eu.ways4.trackex.data.preference

import android.content.Context
import androidx.preference.PreferenceManager
import eu.ways4.trackex.R
import eu.ways4.trackex.common.presentation.Theme
import eu.ways4.trackex.data.model.Currency
import eu.ways4.trackex.home.presentation.DateRange

class PreferenceDataSource {

    fun getDefaultCurrency(context: Context): Currency {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getDefaultCurrencyKey(context)
        return preferences.getString(key, null)?.let { Currency.valueOf(it) } ?: Currency.EUR
    }

    fun setDefaultCurrency(context: Context, defaultCurrency: Currency) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getDefaultCurrencyKey(context)
        preferences.edit().putString(key, defaultCurrency.name).apply()
    }

    fun getDateRange(context: Context): DateRange {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getDateRangeKey(context)
        return preferences.getString(key, null)?.let { DateRange.valueOf(it) } ?: DateRange.ALL_TIME
    }

    fun setDateRange(context: Context, dateRange: DateRange) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getDateRangeKey(context)
        preferences.edit().putString(key, dateRange.name).apply()
    }

    fun getIsUserOnboarded(context: Context): Boolean {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getIsUserOnboardedKey(context)
        return preferences.getBoolean(key, false)
    }

    fun setIsUserOnboarded(context: Context, isUserOnboarded: Boolean) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getIsUserOnboardedKey(context)
        preferences.edit().putBoolean(key, isUserOnboarded).apply()
    }

    fun getTheme(context: Context): Theme {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getThemeKey(context)
        return preferences.getString(key, null)?.let { Theme.valueOf(it) } ?: Theme.SYSTEM_DEFAULT
    }

    fun setTheme(context: Context, theme: Theme) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val key = getThemeKey(context)
        preferences.edit().putString(key, theme.name).apply()
    }

    private fun getDefaultCurrencyKey(context: Context) =
        context.getString(R.string.key_default_currency)

    private fun getDateRangeKey(context: Context) =
        context.getString(R.string.key_date_range)

    private fun getIsUserOnboardedKey(context: Context) =
        context.getString(R.string.key_is_user_onboarded)

    private fun getThemeKey(context: Context) =
        context.getString(R.string.key_theme)
}