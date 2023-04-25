package com.msaifurrijaal.submissiongithubuser.data.local

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: ResponseDetailUser)

    @Delete
    suspend fun deleteUser(user: ResponseDetailUser)

    @Query("SELECT * FROM userDetail ORDER BY login ASC")
    suspend fun getAllFavoriteUser(): List<ResponseDetailUser>

    @Query("SELECT * FROM userDetail WHERE login = :username")
    suspend fun getFavoriteUser(username: String): ResponseDetailUser?
}