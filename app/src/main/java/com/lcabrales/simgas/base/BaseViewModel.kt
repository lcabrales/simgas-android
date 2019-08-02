package com.lcabrales.simgas.base

import androidx.lifecycle.ViewModel
import com.lcabrales.simgas.di.component.DaggerViewModelInjector
import com.lcabrales.simgas.di.component.ViewModelInjector
import com.lcabrales.simgas.di.module.NetworkModule
import com.lcabrales.simgas.ui.readinglevels.ReadingLevelsViewModel
import com.lcabrales.simgas.ui.login.LoginViewModel
import com.lcabrales.simgas.ui.main.MainViewModel
import com.lcabrales.simgas.ui.profile.ProfileViewModel
import com.lcabrales.simgas.ui.register.RegisterViewModel
import com.lcabrales.simgas.ui.sensordetail.SensorDetailViewModel
import com.lcabrales.simgas.ui.sensorreadings.SensorReadingsViewModel
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
            is LoginViewModel -> injector.inject(this)
            is RegisterViewModel -> injector.inject(this)
            is MainViewModel -> injector.inject(this)
            is ProfileViewModel -> injector.inject(this)
            is SensorDetailViewModel -> injector.inject(this)
            is SensorReadingsViewModel -> injector.inject(this)
            is ReadingLevelsViewModel -> injector.inject(this)
        }
    }
}