package com.lcabrales.simgas.common

import java.text.SimpleDateFormat
import java.util.*

object Dates {

    const val SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val DISPLAY_FORMAT_LONG_DATE = "MMM dd, yyyy"
    const val DISPLAY_FORMAT_SHORT_DATE = "dd/MM/yyyy"
    const val DISPLAY_FORMAT_TIME = "HH:mm:ss"

    fun getFormattedDateString(originalDateString: String?, inputFormat: String,
                               outputFormat: String): String {
        val originalFormat = SimpleDateFormat(inputFormat, Locale.US)
        val targetFormat = SimpleDateFormat(outputFormat, Locale.US)
        val date = originalFormat.parse(originalDateString)
        return targetFormat.format(date)
    }
}