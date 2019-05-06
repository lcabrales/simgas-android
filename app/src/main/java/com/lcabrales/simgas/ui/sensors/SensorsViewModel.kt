package com.lcabrales.simgas.ui.sensors

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
import javax.inject.Inject

class SensorsViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "SensorsViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val sendSensorsDataLiveData: MutableLiveData<List<Sensor>> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    init {
        loadSensors()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    private fun loadSensors() {
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

        sendSensorsDataLiveData.value = response.data
    }

    private fun onRetrieveSensorListError() {
        showToastLiveData.value = R.string.network_error
    }
}
