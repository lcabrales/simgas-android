package com.lcabrales.simgas.di.component

import com.lcabrales.simgas.di.module.NetworkModule
import com.lcabrales.simgas.ui.sensors.SensorsViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {
    /**
     * Injects required dependencies into the specified SensorsViewModel.
     * @param sensorsViewModel SensorsViewModel in which to inject the dependencies
     */
    fun inject(sensorsViewModel: SensorsViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}