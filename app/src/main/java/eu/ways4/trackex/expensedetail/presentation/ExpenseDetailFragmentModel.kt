package eu.ways4.trackex.expensedetail.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.ways4.trackex.Application
import eu.ways4.trackex.R
import eu.ways4.trackex.data.model.Expense
import eu.ways4.trackex.data.model.Tag
import eu.ways4.trackex.expensedetail.domain.DeleteExpenseUseCase
import eu.ways4.trackex.expensedetail.domain.ObserveExpenseUseCase
import eu.ways4.trackex.util.READABLE_DATE_FORMAT
import eu.ways4.trackex.util.extensions.plusAssign
import eu.ways4.trackex.util.extensions.toString
import eu.ways4.trackex.util.reactive.DataEvent
import eu.ways4.trackex.util.reactive.Event
import eu.ways4.trackex.util.reactive.Variable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io

@SuppressLint("LongLogTag")
class ExpenseDetailFragmentModel(
    application: eu.ways4.trackex.Application,
    private val observeExpenseUseCase: ObserveExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private var expense: Expense
) : AndroidViewModel(application) {

    val amount = Variable("")
    val currency = Variable("")
    val title = Variable("")
    val tags = Variable(emptyList<Tag>())
    val date = Variable("")
    val notes = Variable("")

    val showEdit = DataEvent<Expense>()
    val finish = Event()

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        observeExpense()
    }

    private fun observeExpense() {
        disposables += observeExpenseUseCase(expense.id)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                expense = it; populateExpenseValues()
            }, { error ->
                Log.w(TAG, "Failed to observe expense: ($error).")
            })
    }

    private fun populateExpenseValues() {
        amount.value = "${"%.2f".format(expense.amount)} ${expense.currency.symbol}"
        currency.value = "(${expense.currency.title} • ${expense.currency.code})"
        title.value = expense.title
        tags.value = expense.tags
        date.value = expense.date.toString(READABLE_DATE_FORMAT)
        notes.value = makeNotes(expense)
    }

    private fun makeNotes(expense: Expense): String {
        val notes = expense.notes
        return if (notes.isNotEmpty()) notes
        else getApplication<eu.ways4.trackex.Application>().getString(R.string.no_notes)
    }

    // Lifecycle end

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    // Public

    fun edit() {
        showEdit.next(expense)
    }

    fun delete() {
        disposables += deleteExpenseUseCase(expense)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                finish.next()
            }, { error ->
                Log.w(TAG, "Failed to delete expense: ($error).")
            })
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: eu.ways4.trackex.Application, private val expense: Expense) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val dataStore = application.defaultDataStore

            val observeExpenseUseCase = ObserveExpenseUseCase(dataStore)
            val deleteExpenseUseCase = DeleteExpenseUseCase(dataStore)

            return ExpenseDetailFragmentModel(
                application,
                observeExpenseUseCase,
                deleteExpenseUseCase,
                expense
            ) as T
        }
    }

    companion object {
        private const val TAG = "ExpenseDetailFragmentModel"
    }
}