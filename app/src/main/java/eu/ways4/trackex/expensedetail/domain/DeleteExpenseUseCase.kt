package eu.ways4.trackex.expensedetail.domain

import eu.ways4.trackex.data.model.Expense
import eu.ways4.trackex.data.store.DataStore
import io.reactivex.Completable

class DeleteExpenseUseCase(private val dataStore: DataStore) {

    operator fun invoke(expense: Expense): Completable {
        return dataStore.deleteExpense(expense)
    }
}