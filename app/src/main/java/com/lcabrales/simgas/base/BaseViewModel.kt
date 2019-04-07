package com.lcabrales.simgas.base

import androidx.lifecycle.ViewModel
import com.lcabrales.simgas.di.component.DaggerViewModelInjector
import com.lcabrales.simgas.di.component.ViewModelInjector
import com.lcabrales.simgas.di.module.NetworkModule
import com.lcabrales.simgas.ui.sensors.SensorsViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
            .builder()
            .networkModule(NetworkModule)
            .build()

    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is SensorsViewModel -> injector.inject(this)
        }
    }
}