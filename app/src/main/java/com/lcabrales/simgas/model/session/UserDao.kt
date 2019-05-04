package com.lcabrales.simgas.model.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @get:Query("SELECT * FROM user LIMIT 1")
    val getLoggedUser: User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLoggedUser(vararg posts: User)

    @Query("DELETE FROM user")
    fun deleteAll()
}