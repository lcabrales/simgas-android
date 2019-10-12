package com.lcabrales.simgas.ui.readinglevels

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.lcabrales.simgas.BaseFragment
import com.lcabrales.simgas.R
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.common.Utils
import com.lcabrales.simgas.databinding.FragmentReadingLevelsBinding
import com.lcabrales.simgas.model.filter.DaysAgoFilter
import com.lcabrales.simgas.model.readings.daily.SensorDailyAverage
import com.lcabrales.simgas.ui.sensordetail.DateValueFormatter

class ReadingLevelsFragment : BaseFragment() {

    companion object {
        fun newInstance() = ReadingLevelsFragment()
    }

    private lateinit var viewModel: ReadingLevelsViewModel
    private lateinit var binding: FragmentReadingLevelsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reading_levels, container,
            false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReadingLevelsViewModel::class.java)

        setupLineChart()
        setupDaysAgoFilter()
        subscribe()
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(viewLifecycleOwner, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(viewLifecycleOwner, Observer(this::showToast))
        viewModel.showToastStringLiveData.observe(viewLifecycleOwner, Observer(this::showToast))
        viewModel.sendDailyAverageListLiveData.observe(viewLifecycleOwner,
            Observer(this::populateDailyAverageData))
    }

    private fun setupLineChart() {
        binding.lineChart.axisLeft.valueFormatter = PercentFormatter()
        binding.lineChart.axisRight.valueFormatter = PercentFormatter()
        binding.lineChart.xAxis.valueFormatter = DateValueFormatter()
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.legend.isEnabled = true
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setNoDataTextColor(
            ContextCompat.getColor(context!!, R.color.color_primary))
        binding.lineChart.setNoDataText(getString(R.string.reading_levels_fragment_chart_no_data))
    }

    private fun setupDaysAgoFilter() {
        val filters = DaysAgoFilter.getDefaultFilters(context!!)
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, filters)
        binding.spDaysFilter.adapter = adapter
        binding.spDaysFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int,
                                        id: Long) {
                viewModel.setSelectedFilter(filters[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun populateDailyAverageData(list: List<SensorDailyAverage>) {
        if (list.isNullOrEmpty()) {
            binding.lineChart.clear()
            return
        }

        val dataSetList = ArrayList<ILineDataSet>()
        val chartColors = Utils.getChartColors(context!!, list.size)

        list.forEachIndexed { index, sensorDailyAverage ->
            val chartEntries = ArrayList<Entry>()

            sensorDailyAverage.dailyAverages!!.forEach {
                val timeStamp = Dates.getFormattedDate(it.createdDate, Dates.SERVER_FORMAT).time
                var percentage = it.gasPercentage!!.toFloat() * 100
                if (percentage > 100) percentage = 100f
                chartEntries.add(Entry(timeStamp.toFloat(), percentage))
            }

            val dataSet = LineDataSet(chartEntries, sensorDailyAverage.sensor!!.name)
            dataSet.valueTextSize = 14F
            dataSet.valueTextColor = Color.WHITE

            val dataSetColor = chartColors[index]
            dataSet.color = dataSetColor
            dataSet.setCircleColor(dataSetColor)
            dataSet.setDrawValues(true)

            dataSetList.add(dataSet)
        }

        val lineData = LineData(dataSetList)

        binding.lineChart.data = lineData

        binding.lineChart.invalidate()
    }
}