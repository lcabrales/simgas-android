package com.lcabrales.simgas.di.component

import com.lcabrales.simgas.di.module.NetworkModule
import com.lcabrales.simgas.ui.aboutus.AboutUsViewModel
import com.lcabrales.simgas.ui.dialog.AirQualityDialogViewModel
import com.lcabrales.simgas.ui.login.LoginViewModel
import com.lcabrales.simgas.ui.main.MainViewModel
import com.lcabrales.simgas.ui.profile.ProfileViewModel
import com.lcabrales.simgas.ui.readinglevels.ReadingLevelsViewModel
import com.lcabrales.simgas.ui.register.RegisterViewModel
import com.lcabrales.simgas.ui.sensordetail.SensorDetailViewModel
import com.lcabrales.simgas.ui.sensorreadings.SensorReadingsViewModel
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
    fun inject(sensorDetailViewModel: SensorDetailViewModel)
    fun inject(sensorReadingsViewModel: SensorReadingsViewModel)
    fun inject(readingLevelsViewModel: ReadingLevelsViewModel)
    fun inject(airQualityDialogViewModel: AirQualityDialogViewModel)
    fun inject(aboutUsViewModel: AboutUsViewModel)

    @Component.Builder
    interface Builder {

        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}