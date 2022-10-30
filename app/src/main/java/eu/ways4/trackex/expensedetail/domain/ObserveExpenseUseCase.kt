package eu.ways4.trackex.expensedetail.domain

import eu.ways4.trackex.data.model.Expense
import eu.ways4.trackex.data.store.DataStore
import io.reactivex.Observable

class ObserveExpenseUseCase(private val dataStore: DataStore) {
    operator fun invoke(expenseId: String): Observable<Expense> {
        return dataStore.observeExpense(expenseId)
    }
}