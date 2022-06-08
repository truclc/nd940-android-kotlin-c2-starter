package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AsteroidRepository, application: Application) :
    AndroidViewModel(application) {

    private val _showLoading = MutableLiveData<Boolean>(false)
    val showLoading: LiveData<Boolean>
        get() = _showLoading

    private val _showErrorMsg = MutableLiveData<String?>()
    val showErrorMsg: LiveData<String?>
        get() = _showErrorMsg

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _navigateToDetailScreen = MutableLiveData<Asteroid?>()
    val navigateToDetailScreen: LiveData<Asteroid?>
        get() = _navigateToDetailScreen

    private val asteroidFilter = MutableLiveData<AsteroidApiFilter>(AsteroidApiFilter.SHOW_SAVED_ASTEROIDS)

    val asteroids = Transformations.switchMap(asteroidFilter) {
        when (it) {
            AsteroidApiFilter.SHOW_WEEK_ASTEROIDS -> repository.weeklyAsteroids
            AsteroidApiFilter.SHOW_TODAY_ASTEROIDS -> repository.todayAsteroids
            else -> repository.allSavedAsteroids
        }
    }

    init {
        viewModelScope.launch {
            try {
                _showLoading.value = true
                fetchPictureOfDayImageType()
                repository.refreshAsteroids()
            } catch (cause: Throwable) {
                cause.printStackTrace()
                _showErrorMsg.value = application.getString(R.string.data_error)
            } finally {
                _showLoading.value = false
            }
        }
    }

    private suspend fun fetchPictureOfDayImageType() {
        val result = repository.fetchPictureOfDay()
        if (result.mediaType == "image") {
            _pictureOfDay.value = repository.fetchPictureOfDay()
        }
    }

    fun updateFilter(asteroidApiFilter: AsteroidApiFilter) {
        asteroidFilter.value = asteroidApiFilter
    }

    fun onClickAsteroidItem(asteroid: Asteroid) {
        _navigateToDetailScreen.value = asteroid
    }

    fun onNavigateToDetailScreenCompleted() {
        _navigateToDetailScreen.value = null
    }

    fun onShowErrorMsgCompleted() {
        _showErrorMsg.value = null
    }
}

enum class AsteroidApiFilter {
    SHOW_WEEK_ASTEROIDS,
    SHOW_TODAY_ASTEROIDS,
    SHOW_SAVED_ASTEROIDS
}