package com.lcabrales.simgas.di.component

import com.lcabrales.simgas.di.module.NetworkModule
import com.lcabrales.simgas.ui.login.LoginViewModel
import com.lcabrales.simgas.ui.sensors.SensorsViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(sensorsViewModel: SensorsViewModel)

    fun inject(loginViewModel: LoginViewModel)

    @Component.Builder
    interface Builder {

        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}