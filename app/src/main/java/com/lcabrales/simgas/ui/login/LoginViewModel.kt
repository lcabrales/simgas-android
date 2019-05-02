package com.lcabrales.simgas.ui.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.session.LoginRequest
import com.lcabrales.simgas.model.session.LoginResponse
import com.lcabrales.simgas.model.session.User
import com.lcabrales.simgas.model.session.UserDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoginViewModel(private val userDao: UserDao) : BaseViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val loadLoginActivityLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val loginCompletedLiveData: MutableLiveData<User> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val enableLoginButtonLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val clearUserFieldLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val clearPasswordFieldLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun checkForLoggedUser() {
        val disposable = Observable.fromCallable {
            userDao.getLoggedUser
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user -> userAlreadyLogged(user) },
                { userNotLogged() }
            )
        disposables.add(disposable)
    }

    private fun userAlreadyLogged(user: User) {
        loadLoginActivityLiveData.value = false
    }

    private fun userNotLogged() {
        loadLoginActivityLiveData.value = true
    }

    fun sendLoginRequest(username: String, password: String) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            showToastLiveData.value = R.string.login_activity_empty_fields
            return
        }

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
        enableLoginButtonLiveData.value = false
        showLoadingLiveData.value = true
    }

    private fun onLoginRequestFinish() {
        enableLoginButtonLiveData.value = true
        showLoadingLiveData.value = false
    }

    private fun onLoginRequestSuccess(response: LoginResponse) {
        Log.d(TAG, "onLoginRequestSuccess: $response")

        when {
            response.result?.code == 400 -> {
                //user does not exist
                showToastLiveData.value = R.string.login_activity_user_does_not_exist
                clearUserFieldLiveData.value = true
                clearPasswordFieldLiveData.value = true
            }
            response.result?.code == 401 -> {
                //invalid credentials
                showToastLiveData.value = R.string.login_activity_invalid_credentials
                clearPasswordFieldLiveData.value = true
            }
            response.user == null -> {
                showToastLiveData.value = R.string.network_error
                clearUserFieldLiveData.value = true
                clearPasswordFieldLiveData.value = true
            }
            else -> {
                saveUserIntoDatabase(response.user!!)
            }
        }
    }

    /**
     * Saves into the database then send back the login completed action to the view.
     * @param user user to store
     */
    private fun saveUserIntoDatabase(user: User) {
        val disposable = Observable.fromCallable {
            userDao.insertLoggedUser(user)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { loginCompletedLiveData.value = user }
        disposables.add(disposable)
    }

    private fun onLoginRequestError() {
        showToastLiveData.value = R.string.network_error
    }
}
