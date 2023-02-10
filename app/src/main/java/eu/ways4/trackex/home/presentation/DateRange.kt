package eu.ways4.trackex.home.presentation

import eu.ways4.trackex.util.extensions.*
import org.threeten.bp.LocalDate
import java.util.concurrent.atomic.AtomicInteger

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
    SEPTEMBER {
        // Check if the given date is within the range of September
        override fun contains(date: LocalDate): Boolean {
            // First day of September
            val first = LocalDate.of(date.year, 9, 1)
            // Last day of September
            val last = LocalDate.of(date.year, 9, 30)
            // Day before the first day of September
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of September
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of September
            // and before the day after the last day of September
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },
    JANUARY {
        override fun contains(date: LocalDate): Boolean {
            // First day of January
            val first = LocalDate.of(date.year, 1, 1)
            // Last day of January
            val last = LocalDate.of(date.year, 1, 31)
            // Day before the first day of January
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of January
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of January
            // and before the day after the last day of January
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },
    FEBRUARY {
        override fun contains(date: LocalDate): Boolean {
            // First day of February
            val first = LocalDate.of(date.year, 2, 1)
            // Last day of February
            val last = LocalDate.of(date.year, 2, 28)
            // Day before the first day of February
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of February
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of February
            // and before the day after the last day of February
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },
    MARCH {
        override fun contains(date: LocalDate): Boolean {
            // First day of March
            val first = LocalDate.of(date.year, 3, 1)
            // Last day of March
            val last = LocalDate.of(date.year, 3, 31)
            // Day before the first day of March
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of March
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of March
            // and before the day after the last day of March
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },

    OCTOBER {
        override fun contains(date: LocalDate): Boolean {
            // First day of October
            val first = LocalDate.of(date.year, 10, 1)
            // Last day of October
            val last = LocalDate.of(date.year, 10, 31)
            // Day before the first day of October
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of October
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of October
            // and before the day after the last day of October
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },

    NOVEMBER {
        override fun contains(date: LocalDate): Boolean {
            // First day of November
            val first = LocalDate.of(date.year, 11, 1)
            // Last day of November
            val last = LocalDate.of(date.year, 11, 30)
            // Day before the first day of November
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of November
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of November
            // and before the day after the last day of November
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    },

    DECEMBER {
        // Check if the given date is within the range of December
        override fun contains(date: LocalDate): Boolean {
            // First day of December
            val first = LocalDate.of(date.year, 12, 1)
            // Last day of December
            val last = LocalDate.of(date.year, 12, 31)
            // Day before the first day of December
            val dayBeforeFirstDayOfMonth = first.minusDays(1)
            // Day after the last day of December
            val dayAfterLastDayOfMonth = last.plusDays(1)
            // Return whether the given date is after the day before the first day of December
            // and before the day after the last day of December
            return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
        }
    };



    /* DECEMBER {
         override fun contains(date: LocalDate): Boolean {
             val now = LocalDate.now()
             val yr = now.year.toString()
             val first = LocalDate.parse("$yr-12-01")
             val last = LocalDate.parse("$yr-12-31")
             val dayBeforeFirstDayOfMonth = first.firstDayOfMonth().yesterday()
             val dayAfterLastDayOfMonth = last.lastDayOfMonth().tomorrow()
             return date.isAfter(dayBeforeFirstDayOfMonth) && date.isBefore(dayAfterLastDayOfMonth)
         }
     };*/






    abstract fun contains(date: LocalDate): Boolean
}