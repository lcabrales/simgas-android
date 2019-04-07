package com.lcabrales.simgas.ui.sensors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lcabrales.simgas.R
import com.lcabrales.simgas.databinding.FragmentSensorsBinding

class SensorsFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = SensorsFragment()
    }

    private lateinit var viewModel: SensorsViewModel
    private lateinit var binding: FragmentSensorsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensors, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SensorsViewModel::class.java)
        subscribe()
    }

    private fun subscribe() {
        viewModel.getObserverShowLoading().observe(viewLifecycleOwner, Observer(this::showLoading))
    }

    @UiThread
    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}