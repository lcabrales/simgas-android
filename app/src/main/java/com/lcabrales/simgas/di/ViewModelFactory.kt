package com.lcabrales.simgas.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.lcabrales.simgas.model.database.AppDatabase
import com.lcabrales.simgas.ui.login.LoginViewModel
import com.lcabrales.simgas.ui.register.RegisterViewModel

class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(activity.applicationContext, AppDatabase::class.java,
            "users").build()

        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(db.userDao()) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(db.userDao()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}