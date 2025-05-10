package com.zarea.instabugtask.ui.currentweather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zarea.instabugtask.domain.exception.*
import com.zarea.instabugtask.domain.model.Weather
import com.zarea.instabugtask.domain.usecase.GetCurrentWeatherUseCase

class CurrentWeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _state = MutableLiveData(CurrentWeatherUiState())
    val state: LiveData<CurrentWeatherUiState> = _state

    init {
        Log.d("CurrentWeatherViewModel", "init")
        loadWeather()
    }

    fun loadWeather() {
        _state.value = _state.value?.copy(isLoading = true, error = null)

        getCurrentWeatherUseCase.execute(object : GetCurrentWeatherUseCase.Callback {
            override fun onResult(result: Result<Weather>) {
                result.fold(
                    onSuccess = { weather ->
                        Log.d("CurrentWeatherViewModel", "onResult: $weather")
                        _state.postValue(_state.value?.copy(weather = weather, isLoading = false))
                    },
                    onFailure = { error ->
                        when (error) {
                            is LocationPermissionNotGranted -> {
                                _state.postValue(_state.value?.copy(locationPermissionRequired = true, isLoading = false))
                            }
                            is LocationServiceNotEnabled -> {
                                _state.postValue(_state.value?.copy(locationServiceEnabledRequired = true))
                            }
                            is ServerIsBusyException -> {
                                _state.postValue(_state.value?.copy(error = error.message, isLoading = false))
                            }
                            else -> {
                                _state.postValue(_state.value?.copy(error = error.message, isLoading = false))
                            }
                        }
                    }
                )
            }
        })
    }

    fun resetPermissionState() {
        _state.value = _state.value?.copy(locationPermissionRequired = false)
    }

    fun resetLocationServiceEnabledState() {
        _state.value = _state.value?.copy(locationServiceEnabledRequired = false)
    }
}
