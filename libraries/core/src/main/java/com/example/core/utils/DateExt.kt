package com.example.core.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateFormat {
    companion object {
        final const val FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        final const val FORMAT_2 = "dd/MM/yyyy"
        final const val FORMAT_3 = "HH:mm"
        final const val FORMAT_4 = "dd/MM HH:mm"
    }
}

fun String.toDateWithFormatInputAndOutPut(
    formatInput: String,
    formatOutput: String
): String {
    return try {
        val apiDateFormat = SimpleDateFormat(formatInput, Locale.US)
        val dateTypeInput = apiDateFormat.parse(this)

        val desiredDateFormat = SimpleDateFormat(formatOutput, Locale.US)
        desiredDateFormat.format(dateTypeInput)
    } catch (e: Exception) {
        ""
    }
}

fun String.toDate(format: String): Date? {
    val dateFormatter = SimpleDateFormat(format, Locale.US)
    return try {
        dateFormatter.parse(this)
    } catch (e: ParseException) {
        null
    }
}

fun Date.toString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.US)
    return dateFormatter.format(this)
}

fun String.getCurrentDayName(): String {
    val cal = Calendar.getInstance()
    val date = toDate("yyyy/MM/dd")
    if (date != null) {
        cal.time = date
    }
    return when (cal[Calendar.DAY_OF_WEEK]) {
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        else -> "Sun"
    }
}