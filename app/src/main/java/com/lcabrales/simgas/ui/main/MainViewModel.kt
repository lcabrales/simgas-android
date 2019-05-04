package com.lcabrales.simgas.ui.main

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.session.User
import com.lcabrales.simgas.model.session.UserDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainViewModel(private val userDao: UserDao) : BaseViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    lateinit var remoteApiInterface: RemoteApiInterface

    val headerLiveData: MutableLiveData<String> = MutableLiveData()
    val logoutCompletedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

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
        val firstName = if (TextUtils.isEmpty(user.firstName)) "" else user.firstName
        val lastName = if (TextUtils.isEmpty(user.lastName)) "" else user.lastName

        val fullName = String.format(Locale.US, "%s %s", firstName, lastName).trim()
        headerLiveData.value = fullName
    }

    /**
     * Deletes the logged user from the database.
     */
    fun performUserLogout() {
        val disposable = Observable.fromCallable { userDao.deleteAll() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { logoutCompletedLiveData.value = true }
        disposables.add(disposable)
    }
}
