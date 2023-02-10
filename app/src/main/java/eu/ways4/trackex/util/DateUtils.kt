package eu.ways4.trackex.util

import org.threeten.bp.ZonedDateTime

const val READABLE_DATE_FORMAT = "EEEE, d. MMMM YYYY"

fun getCurrentTimestamp() = ZonedDateTime.now().toInstant().toEpochMilli()