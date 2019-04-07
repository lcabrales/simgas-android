package com.lcabrales.simgas.ui.sensors

import android.util.Log
import androidx.lifecycle.MutableLiveData
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

    private val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val sendSensorsDataLiveData: MutableLiveData<List<Sensor>> = MutableLiveData()

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
                .doOnSubscribe { onRetrievePostListStart() }
                .doOnTerminate { onRetrievePostListFinish() }
                .subscribe(
                        { response -> onRetrievePostListSuccess(response) },
                        { onRetrievePostListError() }
                )
        disposables.add(disposable)
    }

    private fun onRetrievePostListStart() {
        showLoadingLiveData.value = true
    }

    private fun onRetrievePostListFinish() {
        showLoadingLiveData.value = false
    }

    private fun onRetrievePostListSuccess(response: GetSensorsResponse) {
        Log.d(TAG, "onRetrievePostListSuccess: $response")

        sendSensorsDataLiveData.value = response.data
    }

    private fun onRetrievePostListError() {

    }

    fun getObservableShowLoading(): MutableLiveData<Boolean> {
        return showLoadingLiveData
    }

    fun getObservableSensorData(): MutableLiveData<List<Sensor>> {
        return sendSensorsDataLiveData
    }
}
