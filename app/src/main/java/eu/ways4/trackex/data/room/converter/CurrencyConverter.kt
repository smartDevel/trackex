package eu.ways4.trackex.data.room.converter

import androidx.room.TypeConverter
import eu.ways4.trackex.data.model.Currency

class CurrencyConverter {

    companion object {

        @JvmStatic
        @TypeConverter
        fun toCurrency(string: String) = Currency.fromCode(string)

        @JvmStatic
        @TypeConverter
        fun toString(currency: Currency) = currency.toString()
    }
}