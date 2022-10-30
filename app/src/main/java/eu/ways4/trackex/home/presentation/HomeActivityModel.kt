package eu.ways4.trackex.home.presentation

import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkInfo
import eu.ways4.trackex.Application
import eu.ways4.trackex.R
import eu.ways4.trackex.authentication.AuthenticationManager
import eu.ways4.trackex.configuration.Configuration
import eu.ways4.trackex.settings.work.ExpenseExportWorker
import eu.ways4.trackex.settings.work.ExpenseImportWorker
import eu.ways4.trackex.util.reactive.DataEvent
import eu.ways4.trackex.util.reactive.Event
import eu.ways4.trackex.util.reactive.Variable
import java.util.*

class HomeActivityModel(
    application: eu.ways4.trackex.Application,
    private val authenticationManager: AuthenticationManager,
    private val configuration: Configuration
) : AndroidViewModel(application) {

    val isUserSignedIn: Variable<Boolean> by lazy {
        Variable(defaultValue = authenticationManager.isUserSignedIn())
    }
    val userName: Variable<String> by lazy {
        Variable(defaultValue = authenticationManager.getCurrentUserName() ?: "")
    }
    val userEmail: Variable<String> by lazy {
        Variable(defaultValue = authenticationManager.getCurrentUserEmail() ?: "")
    }
    val isBannerEnabled: Variable<Boolean> by lazy {
        Variable(defaultValue = configuration.getBoolean(Configuration.KEY_BANNER_ENABLED))
    }
    val bannerTitle: Variable<String> by lazy {
        Variable(defaultValue = configuration.getString(Configuration.KEY_BANNER_TITLE))
    }
    val bannerSubtitle: Variable<String> by lazy {
        Variable(defaultValue = configuration.getString(Configuration.KEY_BANNER_SUBTITLE))
    }

    val navigateToOnboarding = Event()
    val selectFileForImport = Event()
    val showExpenseImportFailureDialog = Event()
    val requestExportPermissions = Event()
    val showExpenseExportFailureDialog = Event()
    val navigateToSettings = Event()
    val navigateToSupport = Event()

    val showMessage = DataEvent<Int>()
    val showActivity = DataEvent<Uri>()
    val observeWorkInfo = DataEvent<UUID>()

    private var expenseImportId: UUID? = null
    private var expenseExportId: UUID? = null

    fun navigateToOnboardingRequested() {
        navigateToOnboarding.next()
    }

    fun importExpensesRequested() {
        selectFileForImport.next()
    }

    fun fileForImportSelected(fileUri: Uri) {
        if (expenseImportId != null) return

        val id = ExpenseImportWorker.enqueue(getApplication<eu.ways4.trackex.Application>(), fileUri)
        expenseImportId = id
        observeWorkInfo.next(id)
    }

    fun downloadTemplate() {
        showActivity.next(TEMPLATE_XLS_URI)
    }

    fun exportExpensesRequested() {
        requestExportPermissions.next()
    }

    fun exportPermissionsGranted() {
        if (expenseExportId != null) return

        val id = ExpenseExportWorker.enqueue(getApplication<eu.ways4.trackex.Application>())
        expenseExportId = id
        observeWorkInfo.next(id)
    }

    fun handleWorkInfo(workInfo: WorkInfo) {
        when (workInfo.id) {
            expenseImportId -> handleExpenseImportWorkInfo(workInfo)
            expenseExportId -> handleExpenseExportWorkInfo(workInfo)
        }
    }

    private fun handleExpenseImportWorkInfo(workInfo: WorkInfo) {
        expenseImportId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_import_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showExpenseImportFailureDialog.next()
                null
            }
            else -> return
        }
    }

    private fun handleExpenseExportWorkInfo(workInfo: WorkInfo) {
        expenseExportId = when (workInfo.state) {
            WorkInfo.State.SUCCEEDED -> {
                showMessage.next(R.string.expense_export_success_message)
                null
            }
            WorkInfo.State.FAILED -> {
                showExpenseExportFailureDialog.next()
                null
            }
            else -> return
        }
    }

    fun navigateToSettingsRequested() {
        navigateToSettings.next()
    }

    fun navigateToSupportRequested() {
        navigateToSupport.next()
    }

    fun performBannerActionRequested() {
        val bannerActionUrl = configuration.getString(Configuration.KEY_BANNER_ACTION_URL)
        showActivity.next(Uri.parse(bannerActionUrl))
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: eu.ways4.trackex.Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeActivityModel(
                application,
                application.authenticationManager,
                application.configuration
            ) as T
        }
    }

    companion object {
        private val TEMPLATE_XLS_URI =
            Uri.parse("https://raw.githubusercontent.com/nominalista/expenses/master/resources/template.xls")
    }
}