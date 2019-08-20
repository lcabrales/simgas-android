package com.lcabrales.simgas.ui.sensordetail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.lcabrales.simgas.model.filter.DaysAgoFilter
import com.lcabrales.simgas.model.readings.daily.DailyAverage
import com.lcabrales.simgas.model.sensors.Sensor
import com.lcabrales.simgas.ui.dialog.AirQualityDialogFragment
import com.lcabrales.simgas.ui.sensorreadings.SensorReadingsActivity

class SensorDetailActivity : BaseBackArrowActivity() {

    companion object {
        const val EXTRA_SENSOR_ID = "ExtraSensorId"
        const val EXTRA_TITLE = "ExtraTitle"
        const val EXTRA_SUBTITLE = "ExtraSubtitle"
        const val FRAGMENT_AIR_QUALITY_TAG = "AirQualityFragment"
    }

    private lateinit var viewModel: SensorDetailViewModel
    private lateinit var binding: ActivitySensorDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor_detail)

        viewModel = ViewModelProviders.of(this).get(SensorDetailViewModel::class.java)

        setupToolbar(binding.includeAppBar.toolbar)
        setOnClickListeners()
        setupChart()
        setupDaysAgoFilter()
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

    private fun setOnClickListeners() {
        binding.btnViewLastReadings.setOnClickListener { showLastReadingsActivity() }
    }

    private fun setupChart() {
        binding.lineChart.xAxis.valueFormatter = DateValueFormatter()
        binding.lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.description.isEnabled = false
        binding.lineChart.setNoDataTextColor(ContextCompat.getColor(this, R.color.color_primary))
        binding.lineChart.setNoDataText(getString(R.string.sensor_detail_activity_chart_no_data))
    }

    private fun setupDaysAgoFilter() {
        val filters = DaysAgoFilter.getDefaultFilters(this)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, filters)
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
    private fun showContents(show: Boolean) {
        binding.scrollView.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun populateSensorData(sensor: Sensor) {
        binding.tvDescriptionValue.text = sensor.shortDescription
    }

    @UiThread
    private fun populateDailyAverageData(list: List<DailyAverage>) {
        if (list.isNullOrEmpty()) {
            binding.lineChart.clear()
            return
        }

        val chartEntries = ArrayList<Entry>()

        list.forEach {
            val timeStamp = Dates.getFormattedDate(it.createdDate, Dates.SERVER_FORMAT).time
            chartEntries.add(Entry(timeStamp.toFloat(), it.gasPpm!!.toFloat()))
        }

        val dataSet = LineDataSet(chartEntries, intent.getStringExtra(EXTRA_TITLE))
        dataSet.valueTextSize = 14F
        dataSet.valueTextColor = Color.WHITE
        dataSet.color = ContextCompat.getColor(this, R.color.color_primary)
        dataSet.setCircleColor(ContextCompat.getColor(this, R.color.color_primary))
        dataSet.setDrawValues(true)

        val lineData = LineData(dataSet)

        binding.lineChart.data = lineData

        binding.lineChart.invalidate()
    }

    private fun showLastReadingsActivity() {
        val intent = Intent(this, SensorReadingsActivity::class.java)
        intent.putExtra(SensorReadingsActivity.EXTRA_SENSOR_ID,
            this.intent.getStringExtra(EXTRA_SENSOR_ID))
        intent.putExtra(SensorReadingsActivity.EXTRA_TITLE, this.intent.getStringExtra(EXTRA_TITLE))
        intent.putExtra(SensorReadingsActivity.EXTRA_SUBTITLE,
            this.intent.getStringExtra(EXTRA_SUBTITLE))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.sensor_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_info -> {
                val sensor = viewModel.getSensor()
                val fragment = sensor?.let { AirQualityDialogFragment.newInstance(it) }
                fragment?.show(supportFragmentManager, FRAGMENT_AIR_QUALITY_TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
