package com.lcabrales.simgas.di.component

import com.lcabrales.simgas.di.module.NetworkModule
import com.lcabrales.simgas.ui.login.LoginViewModel
import com.lcabrales.simgas.ui.main.MainViewModel
import com.lcabrales.simgas.ui.profile.ProfileViewModel
import com.lcabrales.simgas.ui.register.RegisterViewModel
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
    fun inject(registerViewModel: RegisterViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(profileViewModel: ProfileViewModel)

    @Component.Builder
    interface Builder {

        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}