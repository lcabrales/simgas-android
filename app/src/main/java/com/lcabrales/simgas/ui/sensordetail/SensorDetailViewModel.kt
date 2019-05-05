package com.lcabrales.simgas.ui.sensordetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.readings.daily.DailyAverage
import com.lcabrales.simgas.model.readings.daily.DailyAverageReadingResponse
import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import com.lcabrales.simgas.model.sensors.Sensor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SensorDetailViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "SensorDetailViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showContentsLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val sendSensorDataLiveData: MutableLiveData<Sensor> = MutableLiveData()
    val sendDailyAveragesDataLiveData: MutableLiveData<List<DailyAverage>> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var webCount = 0
    private var webCompleted = 0

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun fetchData(sensorId: String) {
        showLoadingLiveData.value = true
        showContentsLiveData.value = false

        fetchSensor(sensorId)
        webCount++
        fetchDailyAverages(sensorId)
        webCount++
    }

    //region Sensor
    private fun fetchSensor(sensorId: String) {
        val disposable = remoteApiInterface.getSensorDetail(sensorId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { onRetrieveDataFinish() }
            .subscribe(this::onRetrieveSensorSuccess) { onRetrieveSensorError() }
        disposables.add(disposable)
    }

    private fun onRetrieveSensorSuccess(response: GetSensorsResponse) {
        Log.d(TAG, "onRetrieveSensorSuccess: $response")

        sendSensorDataLiveData.value = response.data?.get(0)
    }

    private fun onRetrieveSensorError() {
        showToastLiveData.value = R.string.network_error
    }
    //endregion

    //region Sensor
    private fun fetchDailyAverages(sensorId: String) {
        val disposable = remoteApiInterface.getSensorDailyAverageReading(sensorId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { onRetrieveDataFinish() }
            .subscribe(this::onRetrieveDailyAveragesSuccess) { onRetrieveDailyAveragesError() }
        disposables.add(disposable)
    }

    private fun onRetrieveDailyAveragesSuccess(response: DailyAverageReadingResponse) {
        Log.d(TAG, "onRetrieveDailyAveragesSuccess: $response")

        sendDailyAveragesDataLiveData.value = response.data?.dailyAverages
    }

    private fun onRetrieveDailyAveragesError() {
        showToastLiveData.value = R.string.network_error
    }
    //endregion

    private fun onRetrieveDataFinish() {
        webCompleted++

        if (webCompleted >= webCount) {
            webCompleted = 0
            webCount = 0

            showLoadingLiveData.value = false
            showContentsLiveData.value = true
        }
    }
}
