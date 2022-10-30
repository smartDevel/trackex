package eu.ways4.trackex.about

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.ways4.trackex.Application

class AboutActivityModel(application: eu.ways4.trackex.Application) : AndroidViewModel(application) {

    @Suppress("UNCHECKED_CAST")
    class Factory(private val application: eu.ways4.trackex.Application) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AboutActivityModel(application) as T
        }
    }
}