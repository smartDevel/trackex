package eu.ways4.trackex.home.domain

import eu.ways4.trackex.data.model.Expense
import eu.ways4.trackex.home.presentation.DateRange
import eu.ways4.trackex.home.presentation.TagFilter

class FilterExpensesUseCase {

    operator fun invoke(
        expenses: List<Expense>,
        dateRange: DateRange,
        tagFilter: TagFilter?
    ): List<Expense> {
        return expenses.filter { expense ->
            dateRange.contains(expense.date) &&
                    tagFilter?.let { expense.tags.containsAll(it.tags) } ?: true
        }
    }
}