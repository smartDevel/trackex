package eu.ways4.trackex.addeditexpense.presentation.dateselection

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import java.util.*

/**
 * This class represents the date selection dialog fragment
 */
class DateSelectionDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    /**
     * The callback that will be invoked when a date is selected
     */
    var dateSelected: ((Int, Int, Int) -> Unit)? = null

    /**
     * Creates the dialog and returns it.
     */
    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Return a MaterialStyledDatePickerDialog with the current date as default selection
        return MaterialStyledDatePickerDialog(requireActivity(), this, year, month, dayOfMonth)
    }

    /**
     * This method is called when the date is set
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Invoke the dateSelected callback with the selected date
        // Note: Selected month is from 0 to 11.
        dateSelected?.invoke(year, month + 1, dayOfMonth)
    }

    companion object {

        /**
         * The tag used to identify this fragment
         */
        const val TAG = "DateSelectionDialogFragment"

        /**
         * Creates a new instance of the fragment
         */
        fun newInstance() = DateSelectionDialogFragment()
    }
}
