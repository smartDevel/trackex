package eu.ways4.trackex.addeditexpense.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.chip.Chip
import eu.ways4.trackex.R
import eu.ways4.trackex.addeditexpense.presentation.dateselection.DateSelectionDialogFragment
import eu.ways4.trackex.currencyselection.CurrencySelectionActivity
import eu.ways4.trackex.data.model.Currency
import eu.ways4.trackex.data.model.Tag
import eu.ways4.trackex.util.READABLE_DATE_FORMAT
import eu.ways4.trackex.util.extensions.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_edit_expense.*
import org.threeten.bp.LocalDate

class AddEditExpenseFragment : Fragment() {
    // View model for activity level data
    private lateinit var activityModel: AddEditExpenseActivityModel
    // View model for fragment level data
    private lateinit var model: AddEditExpenseFragmentModel

    private val disposables = CompositeDisposable()

    // Lifecycle start

    // Override the onCreateView method to inflate the fragment view
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_edit_expense, container, false)
    }

    // Override the onViewCreated method to setup the action bar, initialize and bind the view models, and add listeners to views
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBar()
        watchEditTexts()
        addListeners()
        initializeModels()
        bindModels()
        showKeyboard()
    }

    // Setup the action bar with title, back button, and home icon
    private fun setupActionBar() {
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar ?: return
        actionBar.title = ""
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_clear_24dp)
        setHasOptionsMenu(true)
    }

    // Observe changes to the edit texts and call the appropriate update method on the model
    private fun watchEditTexts() {
        // Observe changes to the amount edit text and update the model with the entered amount
        editTextAmount.afterTextChanged {
            // Convert the entered string to a double or default to 0.0 if the conversion fails
            val amount = it.toString().toDoubleOrNull() ?: 0.0
            model.updateAmount(amount)
        }
        // Observe changes to the title edit text and update the model with the entered title
        editTextTitle.afterTextChanged { model.updateTitle(it.toString()) }
        // Observe changes to the notes edit text and update the model with the entered notes
        editTextNotes.afterTextChanged { model.updateNotes(it.toString()) }
    }

    // Add click listeners to the text symbol, container tags, and text date
    private fun addListeners() {
        // Show the currency selection screen when the text symbol is clicked
        textSymbol.setOnClickListener { showCurrencySelection() }
        // Show the tag selection screen when the container tags is clicked
        containerTags.setOnClickListener { showTagSelection() }
        // Show the date selection dialog when the text date is clicked
        textDate.setOnClickListener { showDateSelection() }
    }

    // Show the currency selection screen
    private fun showCurrencySelection() {
        CurrencySelectionActivity.start(this, REQUEST_CODE_SELECT_CURRENCY)
    }

    // Show the tag selection screen
    private fun showTagSelection() {
        NavHostFragment.findNavController(this).navigate(R.id.action_tag_selection)
    }

    // Show the date selection dialog
    private fun showDateSelection() {
        // Create an instance of the date selection dialog fragment
        val dateSelectionDialogFragment = DateSelectionDialogFragment.newInstance()
        // Set the dateSelected lambda on the dialog fragment to update the model with the selected date
        dateSelectionDialogFragment.dateSelected = { y, m, d -> model.selectDate(y, m, d) }
        // Show the dialog
        dateSelectionDialogFragment.show(requireFragmentManager(), DateSelectionDialogFragment.TAG)
    }

    // Initialize the activity model and the fragment model
    private fun initializeModels() {
        // Get the AddEditExpenseActivityModel from the activity's ViewModelProviders
        activityModel = ViewModelProviders.of(requireActivity())
            .get(AddEditExpenseActivityModel::class.java)

        // Get the arguments for the fragment and use them to create a factory for the AddEditExpenseFragmentModel
        val args = arguments?.let { AddEditExpenseFragmentArgs.fromBundle(it) }
        val factory = AddEditExpenseFragmentModel.Factory(requireContext().application, args?.expense)

        // Get the AddEditExpenseFragmentModel from the ViewModelProviders using the factory
        model = ViewModelProviders.of(this, factory).get(AddEditExpenseFragmentModel::class.java)
    }


    private fun bindModels() {
        // Observe the selected tags from the activity model and update the local model's selected tags
        activityModel.selectedTags.observe(this, Observer { model.selectTags(it) })

        // Subscribe to changes in the selected currency, date and tags and update the respective text and layouts
        disposables += model.selectedCurrency
            .toObservable()
            .subscribe { updateCurrencyText(it) }
        disposables += model.selectedDate.toObservable().subscribe { updateDateText(it) }
        disposables += model.selectedTags.toObservable().subscribe { updateTagLayout(it) }

        // Observe the "finish" event from the model and finish the activity if it's triggered
        disposables += model.finish.toObservable().subscribe { finish() }

        // Initialize the amount, title, and notes fields with the respective data from the model
        editTextAmount.setText(makeEasilyEditableAmount(model.amount))
        editTextTitle.setText(model.title)
        editTextNotes.setText(model.notes)
    }

    // Converts the given amount to a string that is easy to edit
    private fun makeEasilyEditableAmount(amount: Double?): String {
        return when {
            amount == null -> ""
            amount - amount.toInt().toDouble() == 0.0 -> amount.toInt().toString()
            else -> amount.toString()
        }
    }

    // Updates the text of the currency field with the given currency
    private fun updateCurrencyText(currency: Currency) {
        val context = requireContext()
        val text = context.getString(
            R.string.currency_abbreviation,
            currency.flag,
            currency.code
        )
        textSymbol.text = text
    }

    // Updates the text of the date field with the given date
    private fun updateDateText(date: LocalDate) {
        textDate.text = date.toString(READABLE_DATE_FORMAT)
    }

    // Updates the tag layout with the given list of tags
    private fun updateTagLayout(tags: List<Tag>) {
        updateSelectTagsText(tags.isEmpty())
        updateChipGroup(tags)
    }

    private fun updateSelectTagsText(isVisible: Boolean) {
        // Show or hide the "Select Tags" text based on the given visibility value
        text_select_tags.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateChipGroup(tags: List<Tag>) {
        // Remove all chips from the chip group
        chip_group.removeAllViews()
        // Add chips to the group for each tag in the given list of tags
        tags.forEach { chip_group.addView(createChip(it.name)) }
    }

    private fun createChip(text: String): Chip {
        // Create a new chip with the given text
        val chip = Chip(context)
        chip.text = text
        // Disable clickability for the chip
        chip.isClickable = false
        return chip
    }

    private fun finish() {
        // Close the current activity by calling `onBackPressed()` on the parent activity
        requireActivity().onBackPressed()
    }

    private fun showKeyboard() {
        // Show the keyboard with a delay of `KEYBOARD_APPEARANCE_DELAY`
        showKeyboard(editTextAmount, KEYBOARD_APPEARANCE_DELAY)
    }

// Lifecycle end

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear all disposables and hide the keyboard
        clearDisposables()
        hideKeyboard()
    }

    private fun clearDisposables() {
        // Clear all disposables in the disposables composite disposable
        disposables.clear()
    }

    // Options
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_expense, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // Handle back button press
            android.R.id.home -> backSelected()
            // Handle save menu item press
            R.id.save -> saveSelected()
            // If not handled, delegate to superclass
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Go back to previous activity
    private fun backSelected(): Boolean {
        requireActivity().onBackPressed()
        return true
    }

    // Trigger model to save the expense
    private fun saveSelected(): Boolean {
        model.saveExpense()
        return true
    }

    // Handle activity result from the currency selection activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // If the result is not OK, do nothing
        if (resultCode != Activity.RESULT_OK) return

        when(requestCode) {
            REQUEST_CODE_SELECT_CURRENCY -> {
                // Retrieve the selected currency and pass it to the model
                val currency: Currency? =
                    data?.getParcelableExtra(CurrencySelectionActivity.EXTRA_CURRENCY)
                currency?.let { model.selectCurrency(it) }
            }
        }
    }

    companion object {
        // Request code for the currency selection activity
        private const val REQUEST_CODE_SELECT_CURRENCY = 1

        // Delay in milliseconds for the keyboard to appear
        private const val KEYBOARD_APPEARANCE_DELAY = 300L
    }

}
