package eu.ways4.trackex.addeditexpense.presentation.tagselection

import eu.ways4.trackex.data.model.Tag

class TagItemModel(val tag: Tag):
    TagSelectionItemModel {

    var isChecked = false
    val name = tag.name

    var checkClick: (() -> Unit)? = null
    var deleteClick: (() -> Unit)? = null
}