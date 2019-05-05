package com.lcabrales.simgas.ui.sensordetail

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lcabrales.simgas.common.Dates

class DateValueFormatter : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return Dates.getFormattedDateString(value.toLong(), Dates.DISPLAY_FORMAT_SHORT_DATE_DAILY)
    }
}