package com.lcabrales.simgas.ui.main

import androidx.lifecycle.MutableLiveData
import com.lcabrales.simgas.base.BaseViewModel
import com.lcabrales.simgas.data.RemoteApiInterface
import com.lcabrales.simgas.model.session.UserDao
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val userDao: UserDao) : BaseViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    lateinit var remoteApiInterface: RemoteApiInterface

    val logoutCompletedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    /**
     * Deletes the logged user from the database.
     */
    fun performUserLogout() {
        val disposable = Observable.fromCallable {
            userDao.deleteAll()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { logoutCompletedLiveData.value = true }
        disposables.add(disposable)
    }
}
