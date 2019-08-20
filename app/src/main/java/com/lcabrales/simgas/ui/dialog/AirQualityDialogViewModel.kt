package com.lcabrales.simgas.ui.dialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.airquality.AirQuality
import com.lcabrales.simgas.model.airquality.GetAirQualityResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AirQualityDialogViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "AirQualityDialogVM"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val sendAirQualityListLiveData: MutableLiveData<List<AirQuality>> = MutableLiveData()
    val dismissDialogLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun loadAirQualityValues(gasId: String) {
        val disposable = remoteApiInterface.getAirQualities(gasId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRetrieveAirQualityListStart() }
            .doOnTerminate { onRetrieveAirQualityListFinish() }
            .subscribe(
                { response -> onRetrieveAirQualityListSuccess(response) },
                { onRetrieveAirQualityListError() }
            )
        disposables.add(disposable)
    }

    private fun onRetrieveAirQualityListStart() {
        showLoadingLiveData.value = true
    }

    private fun onRetrieveAirQualityListFinish() {
        showLoadingLiveData.value = false
    }

    private fun onRetrieveAirQualityListSuccess(response: GetAirQualityResponse) {
        Log.d(TAG, "onRetrieveAirQualityListSuccess: $response")

        if (response.data.isNullOrEmpty()) {
            showLoadingLiveData.value = false
            return
        }

        sendAirQualityListLiveData.value = response.data
    }

    private fun onRetrieveAirQualityListError() {
        showToastLiveData.value = R.string.network_error
        dismissDialogLiveData.value = true
    }
}
