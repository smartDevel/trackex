package eu.ways4.trackex.addeditexpense.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.ways4.trackex.data.model.Tag

/**
 * The ViewModel class for the `AddEditExpenseActivity`.
 * Holds the selected tags data for the UI to observe.
 */
class AddEditExpenseActivityModel : ViewModel() {

    /**
     * A MutableLiveData holding the list of selected tags.
     */
    val selectedTags = MutableLiveData<List<Tag>>()

    /**
     * Selects the given list of tags.
     *
     * @param tags The list of tags to be selected.
     */
    fun selectTags(tags: List<Tag>) {
        selectedTags.value = tags
    }
}
