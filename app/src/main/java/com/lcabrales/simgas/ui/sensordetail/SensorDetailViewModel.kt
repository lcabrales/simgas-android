package com.lcabrales.simgas.ui.sensordetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.filter.DaysAgoFilter
import com.lcabrales.simgas.model.readings.daily.DailyAverage
import com.lcabrales.simgas.model.readings.daily.DailyAverageReadingResponse
import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import com.lcabrales.simgas.model.sensors.Sensor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
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
    val showToastStringLiveData: MutableLiveData<String> = MutableLiveData()
    val sendSensorDataLiveData: MutableLiveData<Sensor> = MutableLiveData()
    val sendDailyAveragesDataLiveData: MutableLiveData<List<DailyAverage>> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var webCount = 0
    private var webCompleted = 0
    private lateinit var sensorId: String
    private lateinit var selectedDaysFilter: DaysAgoFilter
    private var sensor: Sensor? = null

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun fetchData(sensorId: String) {
        showLoadingLiveData.value = true
        showContentsLiveData.value = false

        this.sensorId = sensorId

        fetchSensor(sensorId)
        webCount++
    }

    fun getSensor(): Sensor? {
        return sensor
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

        if (response.result?.code != 200) {
            showLoadingLiveData.value = false
            showToastStringLiveData.value = response.result?.message
            return
        }

        sensor = response.data?.get(0)
        sendSensorDataLiveData.value = sensor
    }

    private fun onRetrieveSensorError() {
        showToastLiveData.value = R.string.network_error
    }
    //endregion

    //region Sensor
    fun setSelectedFilter(filter: DaysAgoFilter) {
        selectedDaysFilter = filter

        fetchDailyAverages(sensorId)
        webCount++
    }

    private fun fetchDailyAverages(sensorId: String) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -selectedDaysFilter.numberOfDaysAgo)
        val dateString = Dates.getFormattedDateString(calendar.timeInMillis,
            Dates.SERVER_FORMAT_SHORT)

        val disposable = remoteApiInterface.getSensorDailyAverageReading(sensorId, dateString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnTerminate { onRetrieveDataFinish() }
            .subscribe(this::onRetrieveDailyAveragesSuccess) { onRetrieveDailyAveragesError() }
        disposables.add(disposable)
    }

    private fun onRetrieveDailyAveragesSuccess(response: DailyAverageReadingResponse) {
        Log.d(TAG, "onRetrieveDailyAveragesSuccess: $response")

        if (response.result?.code != 200) {
            showLoadingLiveData.value = false
            showToastStringLiveData.value = response.result?.message
            return
        }

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
