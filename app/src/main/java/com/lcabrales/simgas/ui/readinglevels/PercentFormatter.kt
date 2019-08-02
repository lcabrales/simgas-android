package com.lcabrales.simgas.ui.readinglevels

import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lcabrales.simgas.common.Utils

class PercentFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return Utils.getFormattedPercentValue(value.toDouble())
    }

    override fun getPieLabel(value: Float, pieEntry: PieEntry?): String {
        // Converted to percent
        return getFormattedValue(value)
    }
}
