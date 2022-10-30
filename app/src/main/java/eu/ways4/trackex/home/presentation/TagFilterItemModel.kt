package eu.ways4.trackex.home.presentation

class TagFilterItemModel(val tagFilter: TagFilter): HomeItemModel {

    val chips = tagFilter.tags.map { it.name }
    var clearClick: (() -> Unit)? = null
}