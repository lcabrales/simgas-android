package com.lcabrales.simgas.model.filter

import android.content.Context
import com.lcabrales.simgas.R

class DaysAgoFilter(
    var numberOfDaysAgo: Int,
    var displayText: String
) {

    companion object {
        fun getDefaultFilters(context: Context): MutableList<DaysAgoFilter> {
            val filters = ArrayList<DaysAgoFilter>()

            filters.add(DaysAgoFilter(7, context.getString(R.string.past_week)))
            filters.add(DaysAgoFilter(30, context.getString(R.string.past_month)))
            filters.add(DaysAgoFilter(90, context.getString(R.string.past_trimester)))
            filters.add(DaysAgoFilter(180,context.getString(R.string.past_semester)))
            filters.add(DaysAgoFilter(365, context.getString(R.string.past_year)))

            return filters
        }
    }

    override fun toString(): String {
        return displayText
    }
}