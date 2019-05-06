package com.lcabrales.simgas.ui.sensordetail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.lcabrales.simgas.BaseBackArrowActivity
import com.lcabrales.simgas.R
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.databinding.ActivitySensorDetailBinding
import com.lcabrales.simgas.model.readings.daily.DailyAverage
import com.lcabrales.simgas.model.sensors.Sensor



class SensorDetailActivity : BaseBackArrowActivity() {

    companion object {
        const val EXTRA_SENSOR_ID = "ExtraSensorId"
        const val EXTRA_TITLE = "ExtraTitle"
        const val EXTRA_SUBTITLE = "ExtraSubtitle"
    }

    private lateinit var viewModel: SensorDetailViewModel
    private lateinit var binding: ActivitySensorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor_detail)

        viewModel = ViewModelProviders.of(this).get(SensorDetailViewModel::class.java)

        setupToolbar(binding.includeAppBar.toolbar)
        subscribe()

        val sensorId = intent.getStringExtra(EXTRA_SENSOR_ID)
        viewModel.fetchData(sensorId)
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(this, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(this, Observer(this::showToast))
        viewModel.showContentsLiveData.observe(this, Observer(this::showContents))
        viewModel.sendSensorDataLiveData.observe(this, Observer(this::populateSensorData))
        viewModel.sendDailyAveragesDataLiveData.observe(this,
            Observer(this::populateDailyAverageData))
    }

    override fun setupToolbar(toolbar: Toolbar) {
        super.setupToolbar(toolbar)

        toolbar.title = intent.getStringExtra(EXTRA_TITLE)
        toolbar.subtitle = intent.getStringExtra(EXTRA_SUBTITLE)
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun showContents(show: Boolean) {
        binding.scrollView.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun populateSensorData(sensor: Sensor) {
        binding.tvDescriptionValue.text = sensor.shortDescription
    }

    @UiThread
    private fun populateDailyAverageData(list: List<DailyAverage>) {
        val chartEntries = ArrayList<Entry>()

        list.forEach {
            val timeStamp = Dates.getFormattedDate(it.createdDate, Dates.SERVER_FORMAT).time
            chartEntries.add(Entry(timeStamp.toFloat(), it.gasPpm!!.toFloat()))
        }

        binding.lineChart.xAxis.valueFormatter = DateValueFormatter()
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        val dataSet = LineDataSet(chartEntries, intent.getStringExtra(EXTRA_TITLE))
        dataSet.valueTextSize = 14F
        dataSet.valueTextColor = Color.WHITE
        dataSet.color = ContextCompat.getColor(this, R.color.color_primary)
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.color_secondary))
        dataSet.setDrawValues(true)

        val lineData = LineData(dataSet)

        binding.lineChart.data = lineData
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setNoDataTextColor(ContextCompat.getColor(this, R.color.color_primary))
        binding.lineChart.setNoDataText(getString(R.string.sensor_detail_activity_chart_no_data))

        binding.lineChart.invalidate()
    }
}