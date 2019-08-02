package com.lcabrales.simgas.ui.readinglevels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.readings.daily.DailyAverageReadingResponse
import com.lcabrales.simgas.model.readings.daily.SensorDailyAverage
import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class ReadingLevelsViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "ReadingLevelsViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val sendDailyAverageListLiveData: MutableLiveData<List<SensorDailyAverage>> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private var webCount = 0
    private var webCompleted = 0
    private lateinit var sensorDailyAverageList: MutableList<SensorDailyAverage>

    init {
        loadSensors()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    //region Sensors
    private fun loadSensors() {
        val disposable = remoteApiInterface.getSensors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveSensorListStart() }
            .subscribe(
                { response -> onRetrieveSensorListSuccess(response) },
                { onRetrieveSensorListError() }
            )
        disposables.add(disposable)
    }

    private fun onRetrieveSensorListStart() {
        showLoadingLiveData.value = true
    }

    private fun onRetrieveSensorListSuccess(response: GetSensorsResponse) {
        Log.d(TAG, "onRetrieveSensorListSuccess: $response")

        if (response.data.isNullOrEmpty()) {
            showLoadingLiveData.value = false
            return
        }

        sensorDailyAverageList = ArrayList()

        response.data!!.forEach {
            fetchDailyAverages(it.sensorId!!)
            webCount++
        }
    }

    private fun onRetrieveSensorListError() {
        showToastLiveData.value = R.string.network_error
    }
    //endregion

    private fun fetchDailyAverages(sensorId: String) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -180)
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

        if (response.data == null || response.data!!.dailyAverages.isNullOrEmpty()) return

        sensorDailyAverageList.add(response.data!!)
    }

    private fun onRetrieveDailyAveragesError() {
        showToastLiveData.value = R.string.network_error
    }

    private fun onRetrieveDataFinish() {
        webCompleted++

        if (webCompleted >= webCount) {
            webCompleted = 0
            webCount = 0

            showLoadingLiveData.value = false
            sendDailyAverageListLiveData.value = sensorDailyAverageList
        }
    }
}
