package eu.ways4.trackex.addeditexpense.presentation.tagselection

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import eu.ways4.trackex.R
import eu.ways4.trackex.data.model.Tag
import eu.ways4.trackex.util.extensions.afterTextChanged
import eu.ways4.trackex.util.extensions.showKeyboard
import eu.ways4.trackex.util.extensions.toggleKeyboard


class NewTagDialogFragment : DialogFragment() {

    var tagCreated: ((Tag) -> Unit)? = null

    private lateinit var editText: EditText
    private val text get() = editText.text.toString()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity())
            .setView(createView())
            .setPositiveButton(R.string.add) { _, _ -> tagCreated?.invoke(Tag("", text)) }
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .create()
            .apply { setOnShowListener { enableOrDisableEditText() } }
    }

    @SuppressLint("InflateParams")
    private fun createView(): View {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_new_tag, null)
        bindEditText(view)
        watchEditText()
        showKeyboard(editText)
        return view
    }

    private fun bindEditText(view: View) {
        editText = view.findViewById(R.id.edit_text)
    }

    private fun watchEditText() {
        editText.afterTextChanged { enableOrDisableEditText() }
    }

    private fun enableOrDisableEditText() {
        val dialog = dialog as AlertDialog
        val addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        addButton.isEnabled = text.isNotEmpty()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        toggleKeyboard()
    }

    companion object {
        fun newInstance() = NewTagDialogFragment()
    }
}