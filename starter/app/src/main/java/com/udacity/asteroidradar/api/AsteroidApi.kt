package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApi {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidNearEarth(
        @Query("api_key") apiKey: String = API_KEY
    ): String
}