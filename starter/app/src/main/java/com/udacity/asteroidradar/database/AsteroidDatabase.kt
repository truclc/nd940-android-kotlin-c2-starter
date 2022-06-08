package com.udacity.asteroidradar.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Constants.TAG

@Database(entities = [AsteroidDatabaseEntities::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase(){
    abstract val dao: AsteroidDatabaseDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidDatabase

        fun getInstance(context: Context): AsteroidDatabase {
            Log.d(TAG, "getInstance: ")
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidDatabase::class.java, "asteroids").build()
                }
            }
            return INSTANCE
        }
    }
}