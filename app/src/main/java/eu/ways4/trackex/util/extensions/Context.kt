package eu.ways4.trackex.util.extensions

import android.content.Context
import eu.ways4.trackex.Application

val Context.application: eu.ways4.trackex.Application
    get() = applicationContext as eu.ways4.trackex.Application