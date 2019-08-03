package com.lcabrales.simgas.ui.readinglevels

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.lcabrales.simgas.BaseFragment
import com.lcabrales.simgas.R
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.common.Utils
import com.lcabrales.simgas.databinding.FragmentReadingLevelsBinding
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

        setupPieChart()
        setupLineChart()
        subscribe()
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(viewLifecycleOwner, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(viewLifecycleOwner, Observer(this::showToast))
        viewModel.sendDailyAverageListLiveData.observe(viewLifecycleOwner, Observer(this::populateChartData))
    }

    private fun setupPieChart() {
        val holeRadius = 40f
        binding.pieChart.setDrawEntryLabels(false)
        binding.pieChart.legend.isEnabled = true
        binding.pieChart.description.isEnabled = false
        binding.pieChart.holeRadius = holeRadius
        binding.pieChart.transparentCircleRadius = holeRadius + 5f
        binding.pieChart.setNoDataTextColor(
            ContextCompat.getColor(context!!, R.color.color_primary))
        binding.pieChart.setNoDataText(getString(R.string.reading_levels_fragment_chart_no_data))
    }

    private fun setupLineChart() {
        binding.lineChart.xAxis.valueFormatter = DateValueFormatter()
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.legend.isEnabled = true
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setNoDataTextColor(
            ContextCompat.getColor(context!!, R.color.color_primary))
        binding.lineChart.setNoDataText(getString(R.string.reading_levels_fragment_chart_no_data))
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun populateChartData(list: List<SensorDailyAverage>) {
        populateTodayData(list)
        populateDailyAverageData(list)
    }

    @UiThread
    private fun populateTodayData(list: List<SensorDailyAverage>) {
        val pieChartEntries = ArrayList<PieEntry>()
        val chartColors = Utils.getChartColors(context!!, list.size)

        list.forEach {
            pieChartEntries.add(PieEntry(it.dailyAverages!!.last().gasPercentage!!.toFloat() * 100, it.sensor!!.name))
        }

        val dataSet = PieDataSet(pieChartEntries, "")
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.WHITE
        dataSet.colors = chartColors
        dataSet.valueFormatter = PercentFormatter()

        val data = PieData(dataSet)
        binding.pieChart.data = data

        binding.pieChart.invalidate()
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
                chartEntries.add(Entry(timeStamp.toFloat(), it.gasPpm!!.toFloat()))
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