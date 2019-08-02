package com.lcabrales.simgas.common

import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object Utils {

    fun getFormattedPercentValue(percent: Double): String {
        val df = DecimalFormat("#.0")
        df.roundingMode = RoundingMode.HALF_EVEN

        return String.format(Locale.US, "%s%%", df.format(percent))
    }
}