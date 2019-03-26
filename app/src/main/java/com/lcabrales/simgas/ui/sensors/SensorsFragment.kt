package com.lcabrales.simgas.ui.sensors

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lcabrales.simgas.R

class SensorsFragment : androidx.fragment.app.Fragment() {

    companion object {
        fun newInstance() = SensorsFragment()
    }

    private lateinit var viewModel: SensorsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.sensors_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SensorsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
