package eu.ways4.trackex.home.presentation

import eu.ways4.trackex.util.extensions.*
import org.threeten.bp.LocalDate

enum class DateRange {

    TODAY {
        override fun contains(date: LocalDate): Boolean {
            return LocalDate.now().isEqual(date)
        }
    },

    THIS_WEEK {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now()
            val dayBeforeFirstDayOfWeek = now.firstDayOfWeek().yesterday()
            val dayAfterLastDayOfWeek = now.lastDayOfWeek().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfWeek) && date.isBefore(dayAfterLastDayOfWeek)
        }
    },

    THIS_MONTH {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now()
            val dayBeforeFirstDayOfMonth = now.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = now.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },

    ALL_TIME {
        override fun contains(date: LocalDate) = true
    },

    LAST_MONTH {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now().minusMonths(1)
            val dayBeforeFirstDayOfMonth = now.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = now.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },

    MONTH_MINUS02 {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now().minusMonths(2)
            val dayBeforeFirstDayOfMonth = now.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = now.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },
    MONTH_MINUS03 {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now().minusMonths(3)
            val dayBeforeFirstDayOfMonth = now.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = now.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },
    MONTH_MINUS12 {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now().minusMonths(12)
            val dayBeforeFirstDayOfMonth = now.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = now.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },
    OCTOBER {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now()
            val yr = now.year.toString()
            val first = LocalDate.parse("$yr-10-01")
            val last = LocalDate.parse("$yr-10-31")
            val dayBeforeFirstDayOfMonth = first.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = last.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },

    NOVEMBER {
        override fun contains(date: LocalDate): Boolean {
            val now = LocalDate.now()
            val yr = now.year.toString()
            val first = LocalDate.parse("$yr-11-01")
            val last = LocalDate.parse("$yr-11-30")
            val dayBeforeFirstDayOfMonth = first.firstDayOfMonth().yesterday()
            val dayAfterLastDayOfMonth = last.lastDayOfMonth().tomorrow()
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    };


    abstract fun contains(date: LocalDate): Boolean
}