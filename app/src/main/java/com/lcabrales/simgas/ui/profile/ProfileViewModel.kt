package com.lcabrales.simgas.ui.profile

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.R
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.session.EditUserRequest
import com.lcabrales.simgas.model.session.User
import com.lcabrales.simgas.model.session.UserDao
import com.lcabrales.simgas.model.session.UserResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ProfileViewModel(private val userDao: UserDao) : BaseViewModel() {

    companion object {
        private const val TAG = "EditViewModel"
    }

    @Inject
    lateinit var remoteApiInterface: RemoteApiInterface

    val showLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val userLiveData: MutableLiveData<User> = MutableLiveData()
    val showToastLiveData: MutableLiveData<Int> = MutableLiveData()
    val enableSubmitButtonLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var user: User

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /**
     * Retrieves the logged user from the database.
     */
    fun fetchUser() {
        val disposable = Observable.fromCallable { userDao.getLoggedUser }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onFetchUserCompleted)
        disposables.add(disposable)
    }

    private fun onFetchUserCompleted(user: User) {
        this.user = user
        userLiveData.value = user
    }

    fun sendEditRequest(request: EditUserRequest) {
        if (TextUtils.isEmpty(request.firstName)
            || TextUtils.isEmpty(request.lastName)
            || TextUtils.isEmpty(request.email)) {
            showToastLiveData.value = R.string.profile_fragment_empty_fields
            return
        }

        request.userId = user.userId

        val disposable = remoteApiInterface.editUser(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onEditRequestStart() }
            .doOnTerminate { onEditRequestFinish() }
            .subscribe(
                { response -> onEditRequestSuccess(response) },
                { onEditRequestError() }
            )
        disposables.add(disposable)
    }

    private fun onEditRequestStart() {
        enableSubmitButtonLiveData.value = false
        showLoadingLiveData.value = true
    }

    private fun onEditRequestFinish() {
        enableSubmitButtonLiveData.value = true
        showLoadingLiveData.value = false
    }

    private fun onEditRequestSuccess(response: UserResponse) {
        Log.d(TAG, "onEditRequestSuccess: $response")

        when {
            response.user == null -> {
                showToastLiveData.value = R.string.network_error
            }
            else -> {
                showToastLiveData.value = R.string.profile_fragment_edit_success
                saveUserIntoDatabase(response.user!!)
            }
        }
    }

    private fun onEditRequestError() {
        showToastLiveData.value = R.string.network_error
    }

    /**
     * Saves into the database then send back the Edit completed action to the view.
     * @param user user to store
     */
    private fun saveUserIntoDatabase(user: User) {
        val disposable = Observable.fromCallable {
            userDao.insertLoggedUser(user)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { userLiveData.value = user }
        disposables.add(disposable)
    }
}
