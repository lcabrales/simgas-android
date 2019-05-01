package com.lcabrales.simgas.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.session.LoginRequest
import com.lcabrales.simgas.model.session.LoginResponse
import com.lcabrales.simgas.model.session.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    private val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val loginCompletedLiveData: MutableLiveData<User> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun sendLoginRequest(username: String, password: String) {
        val request = LoginRequest(username, password)

        val disposable = remoteApiInterface.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { onLoginRequestStart() }
                .doOnTerminate { onLoginRequestFinish() }
                .subscribe(
                        { response -> onLoginRequestSuccess(response) },
                        { onLoginRequestError() }
                )
        disposables.add(disposable)
    }

    private fun onLoginRequestStart() {
        showLoadingLiveData.value = true
    }

    private fun onLoginRequestFinish() {
        showLoadingLiveData.value = false
    }

    private fun onLoginRequestSuccess(response: LoginResponse) {
        Log.d(TAG, "onLoginRequestSuccess: $response")

        loginCompletedLiveData.value = response.user
    }

    private fun onLoginRequestError() {

    }

    fun getObservableShowLoading(): MutableLiveData<Boolean> {
        return showLoadingLiveData
    }

    fun getObservableLoginCompleted(): MutableLiveData<User> {
        return loginCompletedLiveData
    }
}
