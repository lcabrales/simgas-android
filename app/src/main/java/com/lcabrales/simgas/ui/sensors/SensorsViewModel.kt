package com.lcabrales.simgas.ui.sensors

import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.sensors.GetSensorsResponse
import com.lcabrales.simgas.model.sensors.Sensor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class SensorsViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "SensorsViewModel"
        private const val POLL_DELAY = 60000L
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val showAlertLiveData: MutableLiveData<String> = MutableLiveData()
    val sendSensorsDataLiveData: MutableLiveData<List<Sensor>> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val timer = Timer()

    init {
        pollForSensorsData()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
        timer.cancel()
    }

    private fun pollForSensorsData() {
        val handler = Handler()
        val pollTask = object : TimerTask() {
            override fun run() {
                handler.post {
                    loadSensors()
                }
            }
        }
        timer.schedule(pollTask, 0, POLL_DELAY) //execute periodically
    }

    fun loadSensors() {
        val disposable = remoteApiInterface.getSensors()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveSensorListStart() }
            .doOnTerminate { onRetrieveSensorListFinish() }
            .subscribe(
                { response -> onRetrieveSensorListSuccess(response) },
                { onRetrieveSensorListError() }
            )
        disposables.add(disposable)
    }

    private fun onRetrieveSensorListStart() {
        showLoadingLiveData.value = true
    }

    private fun onRetrieveSensorListFinish() {
        showLoadingLiveData.value = false
    }

    private fun onRetrieveSensorListSuccess(response: GetSensorsResponse) {
        Log.d(TAG, "onRetrieveSensorListSuccess: $response")

        if (response.result?.code != 200) {
            showAlertLiveData.value = response.result?.message
            return
        }

        sendSensorsDataLiveData.value = response.data
    }

    private fun onRetrieveSensorListError() {
        showLoadingLiveData.value = false

        showToastLiveData.value = R.string.network_error
    }
}
