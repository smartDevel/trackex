package eu.ways4.trackex.settings.presentation

class SummaryActionSettingItemModel(val title: String, val summary: String) : SettingItemModel {
    var click: (() -> Unit)? = null
}