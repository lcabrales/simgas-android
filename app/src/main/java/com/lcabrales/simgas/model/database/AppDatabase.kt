package com.lcabrales.simgas.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lcabrales.simgas.model.session.User
import com.lcabrales.simgas.model.session.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}