package com.lcabrales.simgas.common

import com.github.marlonlom.utilities.timeago.TimeAgo
import com.github.marlonlom.utilities.timeago.TimeAgoMessages
import java.text.SimpleDateFormat
import java.util.*

object Dates {

    const val SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val SERVER_FORMAT_SHORT = "yyyy-MM-dd"
    const val DISPLAY_FORMAT_LONG_DATE = "MMM dd, yyyy"
    const val DISPLAY_FORMAT_SHORT_DATE = "dd/MM/yyyy"
    const val DISPLAY_FORMAT_SHORT_DATE_DAILY = "dd-MMM"
    const val DISPLAY_FORMAT_TIME = "HH:mm:ss"

    fun getFormattedDateString(originalDateString: String?, inputFormat: String,
                               outputFormat: String): String {
        val originalFormat = SimpleDateFormat(inputFormat, Locale.US)
        val targetFormat = SimpleDateFormat(outputFormat, Locale.US)
        val date = originalFormat.parse(originalDateString)
        return targetFormat.format(date)
    }

    fun getFormattedDateString(timeInMillis: Long, outputFormat: String): String {
        val targetFormat = SimpleDateFormat(outputFormat, Locale.US)
        val date = Date(timeInMillis)
        return targetFormat.format(date)
    }

    fun getFormattedDate(dateString: String?, inputFormat: String) : Date {
        val format = SimpleDateFormat(inputFormat, Locale.US)
        return format.parse(dateString)
    }

    fun getRelativeTimeString(timeInMillis: Long, locale: Locale = Locale.getDefault()): String {
        val messages = TimeAgoMessages.Builder().withLocale(locale).build()
        return TimeAgo.using(timeInMillis, messages)
    }
}