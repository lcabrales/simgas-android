package com.lcabrales.simgas.common

import android.content.Context
import com.lcabrales.simgas.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

object Utils {

    fun getFormattedPercentValue(percent: Double): String {
        val df = DecimalFormat("#.0")
        df.roundingMode = RoundingMode.HALF_EVEN

        return String.format(Locale.US, "%s%%", df.format(percent))
    }

    fun getChartColors(context: Context, qty: Int): List<Int> {
        val retColors = ArrayList<Int>()

        val colors = context.resources.getIntArray(R.array.chart_colors).toList()
        if (qty > colors.size) throw IllegalArgumentException(
            "Quantity exceeds the number of colors")

        val step = if (qty < 2) colors.size else colors.size / (qty - 1)
        var currentIndex = 0

        var remaining = qty
        while (remaining > 0) {
            val color: Int = if (currentIndex >= colors.size) colors.last() else colors[currentIndex]

            retColors.add(color)

            currentIndex += step
            remaining--
        }

        return retColors
    }
}