package eu.ways4.trackex.addeditexpense.presentation.tagselection

import android.view.View

class AddTagItemHolder(itemView: View) : TagSelectionItemHolder(itemView) {

    fun bind(model: AddTagItemModel) {
        itemView.setOnClickListener { model.click?.invoke() }
    }

    fun recycle() {
        itemView.setOnClickListener(null)
    }
}