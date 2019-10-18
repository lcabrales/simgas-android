package com.lcabrales.simgas.ui.sensorreadings

import android.os.Bundle
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lcabrales.simgas.BaseBackArrowActivity
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.ActivitySensorReadingsBinding
import com.lcabrales.simgas.model.readings.SensorReading
import com.lcabrales.simgas.ui.sensorreadings.adapter.SensorReadingsAdapter

class SensorReadingsActivity : BaseBackArrowActivity() {

    companion object {
        const val EXTRA_SENSOR_ID = "ExtraSensorId"
        const val EXTRA_TITLE = "ExtraTitle"
    }

    private lateinit var viewModel: SensorReadingsViewModel
    private lateinit var binding: ActivitySensorReadingsBinding
    private lateinit var adapter: SensorReadingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sensor_readings)

        viewModel = ViewModelProviders.of(this).get(SensorReadingsViewModel::class.java)

        setupToolbar(binding.includeAppBar.toolbar)
        setupRecycler()
        setOnClickListeners()
        subscribe()

        val sensorId = intent.getStringExtra(EXTRA_SENSOR_ID)
        viewModel.loadSensorReadings(sensorId)
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(this, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(this, Observer(this::showToast))
        viewModel.showAlertLiveData.observe(this, Observer(this::showAlertDialog))
        viewModel.sendSensorReadingsLiveData.observe(this, Observer(this::updateSensorsListUi))
        viewModel.showEmptyViewLiveData.observe(this, Observer(this::showEmptyView))
        viewModel.showLoadMoreLiveData.observe(this, Observer(this::showLoadMore))
        viewModel.enableLoadMoreLiveData.observe(this, Observer(this::enableLoadMore))
        viewModel.showRecyclerViewLiveData.observe(this, Observer(this::showRecyclerView))
    }

    override fun setupToolbar(toolbar: Toolbar) {
        super.setupToolbar(toolbar)

        toolbar.title = intent.getStringExtra(EXTRA_TITLE)
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvLastReadings.layoutManager = layoutManager
        binding.rvLastReadings.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rvLastReadings.isNestedScrollingEnabled = false

        adapter = SensorReadingsAdapter(ArrayList())
        binding.rvLastReadings.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding.btnLoadMore.setOnClickListener { viewModel.loadMoreSensorReadings() }
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun showEmptyView(show: Boolean) {
        binding.tvEmptyView.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun showRecyclerView(show: Boolean) {
        binding.rvLastReadings.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun updateSensorsListUi(list: ArrayList<SensorReading>) {
        adapter.addData(list)
    }

    @UiThread
    private fun showLoadMore(show: Boolean) {
        binding.btnLoadMore.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun enableLoadMore(enable: Boolean) {
        binding.btnLoadMore.isEnabled = enable
    }
}
