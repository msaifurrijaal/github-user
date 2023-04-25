package com.msaifurrijaal.submissiongithubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertFavUser(user: ResponseDetailUser)

    @Delete
    fun deleteFavUser(user: ResponseDetailUser)

    @Query("SELECT * FROM userDetail ORDER BY login ASC")
    fun getAllFavUser() : List<ResponseDetailUser>

    @Query("SELECT * FROM userDetail WHERE login=:username")
    fun getFavUser(username: String) : ResponseDetailUser?
}