package com.lcabrales.simgas.ui.register

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.session.RegisterRequest
import com.lcabrales.simgas.model.session.User
import com.lcabrales.simgas.model.session.UserDao
import com.lcabrales.simgas.model.session.UserResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RegisterViewModel(private val userDao: UserDao) : BaseViewModel() {

    companion object {
        private const val TAG = "RegisterViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val registerCompletedLiveData: MutableLiveData<User> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val showAlertLiveData: MutableLiveData<String> = MutableLiveData()
    val enableRegisterButtonLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun sendRegisterRequest(request: RegisterRequest, confirmPassword: String) {
        if (TextUtils.isEmpty(request.username)
            || TextUtils.isEmpty(request.firstName)
            || TextUtils.isEmpty(request.lastName)
            || TextUtils.isEmpty(request.email)
            || TextUtils.isEmpty(request.password)) {
            showToastLiveData.value = R.string.register_activity_empty_fields
            return
        } else if (!TextUtils.equals(request.password, confirmPassword)) {
            showToastLiveData.value = R.string.register_activity_passwords_do_not_match
            return
        }

        val disposable = remoteApiInterface.register(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onRegisterRequestStart() }
            .doOnTerminate { onRegisterRequestFinish() }
            .subscribe(
                { response -> onRegisterRequestSuccess(response) },
                { onRegisterRequestError() }
            )
        disposables.add(disposable)
    }

    private fun onRegisterRequestStart() {
        enableRegisterButtonLiveData.value = false
        showLoadingLiveData.value = true
    }

    private fun onRegisterRequestFinish() {
        enableRegisterButtonLiveData.value = true
        showLoadingLiveData.value = false
    }

    private fun onRegisterRequestSuccess(response: UserResponse) {
        Log.d(TAG, "onRegisterRequestSuccess: $response")

        if (response.result?.code != 200) {
            showAlertLiveData.value = response.result?.message
            return
        }

        saveUserIntoDatabase(response.user!!)
    }

    /**
     * Saves into the database then send back the register completed action to the view.
     * @param user user to store
     */
    private fun saveUserIntoDatabase(user: User) {
        val disposable = Observable.fromCallable {
            userDao.insertLoggedUser(user)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { registerCompletedLiveData.value = user }
        disposables.add(disposable)
    }

    private fun onRegisterRequestError() {
        showToastLiveData.value = R.string.network_error
    }
}
