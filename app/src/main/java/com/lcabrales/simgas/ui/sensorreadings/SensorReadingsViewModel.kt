package com.lcabrales.simgas.ui.sensorreadings

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.common.Dates
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.readings.GetSensorReadingsResponse
import com.lcabrales.simgas.model.readings.SensorReading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class SensorReadingsViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "SensorReadingsViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val sendSensorReadingsLiveData: MutableLiveData<List<SensorReading>> = MutableLiveData()
    val showRecyclerViewLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showEmptyViewLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun loadSensorReadings(sensorId: String) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -7)
        val dateString = Dates.getFormattedDateString(calendar.timeInMillis,
            Dates.SERVER_FORMAT_SHORT)

        val disposable = remoteApiInterface.getLatestSensorReadings(sensorId, dateString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveSensorReadingsStart() }
            .doOnTerminate { onRetrieveSensorReadingsFinish() }
            .subscribe(
                { response -> onRetrieveSensorReadingsSuccess(response) },
                { onRetrieveSensorReadingsError() }
            )
        disposables.add(disposable)
    }

    private fun onRetrieveSensorReadingsStart() {
        showLoadingLiveData.value = true
    }

    private fun onRetrieveSensorReadingsFinish() {
        showLoadingLiveData.value = false
    }

    private fun onRetrieveSensorReadingsSuccess(response: GetSensorReadingsResponse) {
        Log.d(TAG, "onRetrieveSensorReadingsSuccess: $response")

        val isEmpty = response.data.isNullOrEmpty()
        showEmptyViewLiveData.value = isEmpty
        showRecyclerViewLiveData.value = !isEmpty

        sendSensorReadingsLiveData.value = response.data
    }

    private fun onRetrieveSensorReadingsError() {
        showToastLiveData.value = R.string.network_error
    }
}
