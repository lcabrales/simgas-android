package com.lcabrales.simgas.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.FragmentAirQualityDialogBinding
import com.lcabrales.simgas.model.airquality.AirQuality
import com.lcabrales.simgas.model.sensors.Sensor
import com.lcabrales.simgas.ui.dialog.adapter.AirQualityAdapter

class AirQualityDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(sensor: Sensor): AirQualityDialogFragment {
            val fragment = AirQualityDialogFragment()
            fragment.sensor = sensor
            return fragment
        }
    }

    private lateinit var viewModel: AirQualityDialogViewModel
    private lateinit var binding: FragmentAirQualityDialogBinding
    private lateinit var sensor: Sensor
    private lateinit var adapter: AirQualityAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(R.string.sensor_detail_activity_dialog_info_title)
        builder.setPositiveButton(R.string.close) { _, _ -> dismiss() }

        binding = DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.fragment_air_quality_dialog, null,
            false)
        builder.setView(binding.root)

        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureView()
    }

    private fun configureView() {
        viewModel = ViewModelProviders.of(this).get(AirQualityDialogViewModel::class.java)
        subscribe()

        setupRecycler()
        viewModel.loadAirQualityValues(sensor.gas!!.gasId!!)
    }

    private fun subscribe() {
        viewModel.showLoadingLiveData.observe(viewLifecycleOwner, Observer(this::showLoading))
        viewModel.showToastLiveData.observe(viewLifecycleOwner, Observer(this::showToast))
        viewModel.dismissDialogLiveData.observe(viewLifecycleOwner, Observer(this::dismissDialog))
        viewModel.sendAirQualityListLiveData.observe(viewLifecycleOwner,
            Observer(this::populateAirQualityList))
    }

    private fun setupRecycler() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvAirQuality.layoutManager = layoutManager
        binding.rvAirQuality.addItemDecoration(
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.rvAirQuality.isNestedScrollingEnabled = false
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    fun showToast(@StringRes stringRes: Int) {
        Toast.makeText(context, getString(stringRes), Toast.LENGTH_SHORT).show()
    }

    @UiThread
    private fun dismissDialog(dismiss: Boolean) {
        if (!dismiss) return

        dismiss()
    }

    @UiThread
    private fun populateAirQualityList(list: List<AirQuality>) {
        adapter = AirQualityAdapter(list)
        binding.rvAirQuality.adapter = adapter
    }
}