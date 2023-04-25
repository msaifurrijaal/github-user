package com.msaifurrijaal.submissiongithubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.msaifurrijaal.submissiongithubuser.model.ResponseDetailUser

@Database(entities = [ResponseDetailUser::class], version = 1, exportSchema = false)
@TypeConverters(UserTypeConverter::class)
abstract class UserFavoriteDatabase: RoomDatabase() {

    abstract fun userFavDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: UserFavoriteDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): UserFavoriteDatabase {
            if (INSTANCE == null) {
                synchronized(UserFavoriteDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserFavoriteDatabase::class.java,
                        "User.db"
                    ).build()
                }
            }
            return INSTANCE as UserFavoriteDatabase
        }
    }
}