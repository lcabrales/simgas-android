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
        private const val PAGE_SIZE = 50
        private const val DAYS_AGO = 7
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val showAlertLiveData: MutableLiveData<String> = MutableLiveData()
    val sendSensorReadingsLiveData: MutableLiveData<ArrayList<SensorReading>> = MutableLiveData()
    val showRecyclerViewLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showEmptyViewLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showLoadMoreLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val enableLoadMoreLiveData: MutableLiveData<Boolean> = MutableLiveData()

    var pageNumber: Int = 0
    var isLoading = false
    lateinit var sensorId: String

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun loadSensorReadings(sensorId: String) {
        this.sensorId = sensorId

        loadMoreSensorReadings()
    }

    fun loadMoreSensorReadings() {
        pageNumber++

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, -DAYS_AGO)
        val dateString = Dates.getFormattedDateString(calendar.timeInMillis,
            Dates.SERVER_FORMAT_SHORT)

        val disposable = remoteApiInterface.getLatestSensorReadings(sensorId, dateString,
            pageNumber, PAGE_SIZE)
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
        isLoading = true
        showLoadingLiveData.value = true
        enableLoadMoreLiveData.value = false
    }

    private fun onRetrieveSensorReadingsFinish() {
        isLoading = false
        showLoadingLiveData.value = false
        enableLoadMoreLiveData.value = true
    }

    private fun onRetrieveSensorReadingsSuccess(response: GetSensorReadingsResponse) {
        Log.d(TAG, "onRetrieveSensorReadingsSuccess: $response")

        if (response.result?.code != 200) {
            showAlertLiveData.value = response.result?.message
        }

        val isAllEmpty = response.data.isNullOrEmpty() && pageNumber <= 1
        showEmptyViewLiveData.value = isAllEmpty
        showRecyclerViewLiveData.value = !isAllEmpty
        showLoadMoreLiveData.value = !response.data.isNullOrEmpty()

        sendSensorReadingsLiveData.value = response.data as ArrayList
    }

    private fun onRetrieveSensorReadingsError() {
        showToastLiveData.value = R.string.network_error
    }
}
