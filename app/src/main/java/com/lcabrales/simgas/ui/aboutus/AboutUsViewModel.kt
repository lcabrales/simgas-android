package com.lcabrales.simgas.ui.aboutus

import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class AboutUsViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "AboutUsViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
