package eu.ways4.trackex.util.extensions

import androidx.core.os.LocaleListCompat
import java.util.*

fun LocaleListCompat.default(): Locale? = get(0)