package com.lcabrales.simgas.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.lcabrales.simgas.model.database.AppDatabase
import com.lcabrales.simgas.ui.login.LoginViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java,
                "users").build()
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(db.userDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}