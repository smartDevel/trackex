package eu.ways4.trackex.addeditexpense.presentation

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.ways4.trackex.Application
import eu.ways4.trackex.data.model.Currency
import eu.ways4.trackex.data.model.Expense
import eu.ways4.trackex.data.model.Tag
import eu.ways4.trackex.data.preference.PreferenceDataSource
import eu.ways4.trackex.data.store.DataStore
import eu.ways4.trackex.util.extensions.plusAssign
import eu.ways4.trackex.util.reactive.Event
import eu.ways4.trackex.util.reactive.Variable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.io
import org.threeten.bp.LocalDate


/*
class AddEditExpenseFragmentModel

This class is the ViewModel for the Add/Edit Expense Fragment. It is responsible for managing the data and logic
related to the Add/Edit Expense feature.

Properties:
- amount: Double? = null: This is the amount of the expense.
- title: String = "" : This is the title of the expense.
- notes: String = "" : This is the additional notes related to the expense.
- selectedCurrency: Variable(Currency.USD) : This is the selected currency for the expense.
- selectedDate: Variable(LocalDate.now()) : This is the selected date for the expense.
- selectedTags: Variable(emptyList<Tag>()) : This is the selected tags for the expense.
- finish: Event(): This is an event that is triggered when the save operation is completed.
- disposables: CompositeDisposable : This is a disposable object that holds a collection of disposables.

Methods:
- init: This block sets the default currency for the expense if the expense is null, otherwise it populates the data
        with the given expense.

*/


class AddEditExpenseFragmentModel(
    application: eu.ways4.trackex.Application,
    private val dataStore: DataStore,
    private val preferenceDataSource: PreferenceDataSource,
    private val expense: Expense?
) : AndroidViewModel(application) {

    var amount: Double? = null
    var title: String = ""
    var notes: String = ""

    val selectedCurrency = Variable(Currency.USD)
    val selectedDate = Variable(LocalDate.now())
    val selectedTags = Variable(emptyList<Tag>())

    val finish = Event()

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        if (expense == null) {
            setDefaultCurrency()
        } else {
            populateData(expense)
        }
    }

    private fun setDefaultCurrency() {
        // Accessing the application context and setting the default currency value
        getApplication<eu.ways4.trackex.Application>().let {
            selectedCurrency.value = preferenceDataSource.getDefaultCurrency(it)
        }
    }

    private fun populateData(expense: Expense) {
        // Updating local properties with the values from the expense object
        amount = expense.amount
        title = expense.title
        notes = expense.notes

        selectedCurrency.value = expense.currency
        selectedDate.value = expense.date
        selectedTags.value = expense.tags
    }

// Lifecycle end

    override fun onCleared() {
        super.onCleared()
        // Clearing all disposable objects
        disposables.clear()
    }

// Selection

    fun selectCurrency(currency: Currency) {
        // Updating the selected currency value
        selectedCurrency.value = currency
    }

    fun selectTags(tags: List<Tag>) {
        // Updating the selected tags value
        selectedTags.value = tags
    }

    fun selectDate(year: Int, month: Int, day: Int) {
        // Updating the selected date value
        selectedDate.value = LocalDate.of(year, month, day)
    }

// Updating

    fun updateAmount(amount: Double) {
        // Updating the amount value
        this.amount = amount
    }

    fun updateTitle(title: String) {
        // Updating the title value
        this.title = title
    }

    fun updateNotes(notes: String) {
        // Updating the notes value
        this.notes = notes
    }

// Saving

    fun saveExpense() {
        // Checking if expense is new or existing
        if (expense == null) {
            createExpense()
        } else {
            updateExpense(expense)
        }
    }

    private fun createExpense() {
        // Preparing the expense object for insertion
        val expenseForInsertion = prepareExpenseForInsertion()

        // Inserting the expense into the database
        disposables += dataStore.insertExpense(expenseForInsertion)
            .subscribeOn(io())
            .observeOn(mainThread())
            .doOnTerminate { finish.next() }
            .subscribe({
                Log.d(TAG, "Expense insertion succeeded.")
            }, { error ->
                Log.e(TAG, "Expense insertion failed (${error.message}).")
            })
    }

    private fun prepareExpenseForInsertion(): Expense {
        // Creating an expense object with the updated values
        return Expense(
            "",
            amount ?: 0.0,
            selectedCurrency.value,
            title,
            selectedTags.value,
            selectedDate.value,
            notes,
            null
        )
    }


    private fun updateExpense(expense: Expense) {
        val expenseForUpdate = prepareExpenseForUpdate(expense)

        // Using `CompositeDisposable` to manage all disposable objects in the view model
        disposables += dataStore.updateExpense(expenseForUpdate)
            .subscribeOn(io()) // Running the update operation on the background thread
            .observeOn(mainThread()) // Observing the result on the main thread
            .doOnTerminate { finish.next() } // Notifying subscribers of the termination
            .subscribe({
                Log.d(TAG, "Expense update succeeded.")
            }, { error ->
                Log.d(TAG, "Expense update failed (${error.message}.")
            })
    }

    private fun prepareExpenseForUpdate(expense: Expense): Expense {
        // Creating a copy of the given expense object with updated properties
        return expense.copy(
            amount = amount ?: 0.0,
            currency = selectedCurrency.value,
            title = title,
            tags = selectedTags.value,
            date = selectedDate.value,
            notes = notes
        )
    }

    // Factory class for creating instances of the view model
    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val application: eu.ways4.trackex.Application,
        private val expense: Expense?
    ) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddEditExpenseFragmentModel(
                application,
                application.defaultDataStore,
                application.preferenceDataSource,
                expense
            ) as T
        }
    }

    companion object {
        private val TAG = AddEditExpenseFragmentModel::class.java.simpleName
    }

}