package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants.TAG
import com.udacity.asteroidradar.api.asteroidApi
import com.udacity.asteroidradar.api.pictureOfDayApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: ")
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(
            database,
            asteroidApi,
            pictureOfDayApi
        )
        return try {
            repository.refreshAsteroids()
            repository.deleteAsteroidsFromPreviousDay()
            Log.d(TAG, "doWork(): SUCCESS")
            Result.success()
        } catch (e: HttpException) {
            Log.d(TAG, "doWork(): RETRY")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
}