package eu.ways4.trackex.util.extensions

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

// Parsing

fun String.toLocalDate(pattern: String): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern))
