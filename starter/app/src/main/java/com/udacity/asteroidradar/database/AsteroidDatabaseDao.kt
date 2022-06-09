package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Query("SELECT * FROM AsteroidDatabaseEntities ORDER BY date(closeApproachDate) ASC")
    fun getAllAsteroids(): LiveData<List<AsteroidDatabaseEntities>>

    @Query("SELECT * FROM AsteroidDatabaseEntities WHERE date(closeApproachDate)=date(:date)")
    fun getAsteroidsForADay(date: String): LiveData<List<AsteroidDatabaseEntities>>

    @Query("SELECT * FROM AsteroidDatabaseEntities WHERE date(closeApproachDate) BETWEEN date(:fromDate) AND date(:toDate) ORDER BY date(closeApproachDate) ASC")
    fun getAsteroidsInRange(fromDate: String, toDate: String): LiveData<List<AsteroidDatabaseEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg videos: AsteroidDatabaseEntities)

    @Query("DELETE FROM AsteroidDatabaseEntities WHERE date(closeApproachDate) < date(:date)")
    fun deleteAsteroidsPreviousDate(date: String)
}