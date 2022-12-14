package eu.ways4.trackex.home.presentation

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
import eu.ways4.trackex.home.domain.FilterExpensesUseCase
import eu.ways4.trackex.home.domain.SortExpensesUseCase
import eu.ways4.trackex.home.domain.SortTagsUseCase
import eu.ways4.trackex.util.extensions.plusAssign
import eu.ways4.trackex.util.reactive.DataEvent
import eu.ways4.trackex.util.reactive.Event
import eu.ways4.trackex.util.reactive.Variable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers.computation
import io.reactivex.schedulers.Schedulers.io

class HomeFragmentModel(
    application: eu.ways4.trackex.Application,
    private val dataStore: DataStore,
    private val preferenceDataSource: PreferenceDataSource
) : AndroidViewModel(application) {

    val itemModels = Variable(emptyList<HomeItemModel>())
    val isLoading = Variable(false)
    val showExpenseDetail = DataEvent<Expense>()
    val showTagFiltering = Event()
    val showNoAddedTags = Event()
    val showDeleteAllExpensesConfirmation = Event()

    var expenses = emptyList<Expense>()
    var tags = emptyList<Tag>()

    private var dateRange: DateRange = DateRange.ALL_TIME
    private var tagFilter: TagFilter? = null

    private val disposables = CompositeDisposable()

    // Lifecycle start

    init {
        setDateRange()
        observeExpenses()
        observeTags()
        updateItemModels()
    }

    private fun setDateRange() {
        getApplication<eu.ways4.trackex.Application>().let {
            dateRange = preferenceDataSource.getDateRange(it)
        }
    }

    private fun observeExpenses() {
        disposables += dataStore.observeExpenses()
            .map { SortExpensesUseCase().invoke(it) }
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({ expenses ->
                this.expenses = expenses
                updateItemModels()
            }, { error ->
                Log.d(TAG, "Failed to observe expenses (${error.message}).")
            })
    }

    private fun observeTags() {
        disposables += dataStore.observeTags()
            .map { SortTagsUseCase().invoke(it) }
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({ tags ->
                this.tags = tags
                updateItemModels()
            }, { error ->
                Log.d(TAG, "Failed to observe tags (${error.message}).")
            })
    }

    private fun updateItemModels() {
        disposables += Observable.just(expenses)
            .map { FilterExpensesUseCase().invoke(it, dateRange, tagFilter) }
            .map { createSummarySection(it) + createExpenseSection(it) }
            .subscribeOn(computation())
            .observeOn(mainThread())
            .subscribe({ itemModels ->
                this.itemModels.value = itemModels
            }, { error ->
                Log.d(TAG, "Failed to update item models (${error.message}).")
            })
    }

    private fun createSummarySection(expenses: List<Expense>): List<HomeItemModel> {
        val summarySection = ArrayList<HomeItemModel>()
        summarySection.add(createSummaryItemModel(expenses))
        tagFilter?.let { summarySection.add(createTagFilterItemModel(it)) }
        return summarySection
    }

    private fun createTagFilterItemModel(tagFilter: TagFilter): TagFilterItemModel {
        val itemModel = TagFilterItemModel(tagFilter)
        itemModel.clearClick = { tagsFiltered(null) }
        return itemModel
    }

    private fun createSummaryItemModel(expenses: List<Expense>): SummaryItemModel {
        val context = getApplication<eu.ways4.trackex.Application>()
        val currencySummaries = createCurrencySummaries(expenses)
        val summaryItemModel = SummaryItemModel(context, currencySummaries, dateRange)
        summaryItemModel.dateRangeChange = { dateRangeSelected(it) }
        return summaryItemModel
    }

    private fun dateRangeSelected(dateRange: DateRange) {
        this.dateRange = dateRange

        getApplication<eu.ways4.trackex.Application>().let {
            preferenceDataSource.setDateRange(it, dateRange)
        }

        updateItemModels()
    }

    private fun createCurrencySummaries(expenses: List<Expense>): List<Pair<Currency, Double>> {
        return expenses
            .groupBy({ it.currency }, { it.amount })
            .map { Pair(it.key, it.value.sum()) }
            .sortedByDescending { it.second }
    }

    private fun createExpenseSection(expenses: List<Expense>): List<ExpenseItemModel> {
        return expenses.map { expense -> createExpenseItemModel(expense) }
    }

    private fun createExpenseItemModel(expense: Expense): ExpenseItemModel {
        val itemModel = ExpenseItemModel(expense)
        itemModel.click = { showExpenseDetail.next(expense) }
        return itemModel
    }

    // Lifecycle end

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    // Public

    fun filterTagsRequested() {
        if (tags.isEmpty()) showNoAddedTags.next()
        else showTagFiltering.next()
    }

    fun tagsFiltered(tagFilter: TagFilter?) {
        this.tagFilter = tagFilter
        updateItemModels()
    }

    fun deleteAllExpensesRequested() {
        showDeleteAllExpensesConfirmation.next()
    }

    fun deleteAllExpensesConfirmed() {
        disposables += dataStore.deleteAllExpenses()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: eu.ways4.trackex.Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeFragmentModel(
                application,
                application.defaultDataStore,
                application.preferenceDataSource
            ) as T
        }
    }

    companion object {
        private const val TAG = "HomeFragmentModel"
    }
}
