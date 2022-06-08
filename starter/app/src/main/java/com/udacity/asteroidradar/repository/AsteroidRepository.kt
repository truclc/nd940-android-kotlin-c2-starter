package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.Constants.TAG
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PictureOfDayApi
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.asDomainModel

class AsteroidRepository(
    private val database: AsteroidDatabase,
    private val asteroidApi: AsteroidApi,
    private val pictureOfDayApi: PictureOfDayApi
) {
    private var today = ""

    private var next7Days = ""

    init {
        val dataFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance()
        today = dataFormat.format(calendar.time)

        calendar.add(Calendar.DAY_OF_YEAR, 7)
        next7Days = dataFormat.format(calendar.time)
    }

    suspend fun refreshAsteroids() {
        val jsonString = asteroidApi.getAsteroidNearEarth()
        Log.d(TAG, "refreshAsteroids: jsonString= " + jsonString)
        val asteroids = parseAsteroidsJsonResult(JSONObject(jsonString))
        Log.d(TAG, "refreshAsteroids: asteroids= " + asteroids.toString())
        database.dao.insertAll(*asteroids.asDatabaseModel())
    }

    suspend fun fetchPictureOfDay(): PictureOfDay {
        Log.d(TAG, "fetchPictureOfDay: getPictureOfDay= " + pictureOfDayApi.getPictureOfDay().url)
        return pictureOfDayApi.getPictureOfDay()
    }

    fun deleteAsteroidsFromPreviousDay() {
        val calendar = Calendar.getInstance()
        val dataFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dataFormat.format(calendar.time)
        database.dao.deleteAsteroidsPreviousDate(today)
    }

    val todayAsteroids = Transformations.map(database.dao.getAsteroidsForADay(today)) {
        it.asDomainModel()
    }

    val weeklyAsteroids = Transformations.map(database.dao.getAsteroidsInRange(today, next7Days)) {
        it.asDomainModel()
    }

    val allSavedAsteroids = Transformations.map(database.dao.getAllAsteroids()) {
        it.asDomainModel()
    }
}