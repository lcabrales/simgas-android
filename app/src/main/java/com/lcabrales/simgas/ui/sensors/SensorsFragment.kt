package com.lcabrales.simgas.ui.sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.FragmentSensorsBinding
import com.lcabrales.simgas.model.sensors.Sensor
import com.lcabrales.simgas.ui.sensors.adapter.SensorsAdapter

class SensorsFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = SensorsFragment()
    }

    private lateinit var viewModel: SensorsViewModel
    private lateinit var binding: FragmentSensorsBinding
    private lateinit var adapter: SensorsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensors, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SensorsViewModel::class.java)

        setupRecycler()
        subscribe()
    }

    private fun subscribe() {
        viewModel.getObservableShowLoading().observe(viewLifecycleOwner, Observer(this::showLoading))
        viewModel.getObservableSensorData().observe(viewLifecycleOwner, Observer(this::updateSensorsListUi))
    }

    private fun setupRecycler(){
        val layoutManager = LinearLayoutManager(context)
        binding.rvSensors.layoutManager = layoutManager
        binding.rvSensors.isNestedScrollingEnabled = false
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.pbLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    @UiThread
    private fun updateSensorsListUi(list: List<Sensor>){
        adapter = SensorsAdapter(list)
        binding.rvSensors.adapter = adapter
    }
}