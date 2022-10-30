package eu.ways4.trackex.addeditexpense.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import eu.ways4.trackex.data.model.Tag

class AddEditExpenseActivityModel : ViewModel() {

    val selectedTags = MutableLiveData<List<Tag>>()

    fun selectTags(tags: List<Tag>) {
        selectedTags.value = tags
    }
}