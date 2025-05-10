package com.zarea.instabugtask.ui.currentweather

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zarea.instabugtask.domain.exception.LocationPermissionNotGranted
import com.zarea.instabugtask.domain.exception.LocationServiceNotEnabled
import com.zarea.instabugtask.domain.exception.NoInternetConnectionAndNoCachedDataAvailable
import com.zarea.instabugtask.domain.exception.ServerIsBusyException
import com.zarea.instabugtask.domain.model.Weather
import com.zarea.instabugtask.domain.usecase.GetCurrentWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CurrentWeatherViewModel(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CurrentWeatherUiState())
    val state = _state.asStateFlow()

    init {
        Log.d("CurrentWeatherViewModel", "init")
        loadWeather()
    }

    fun loadWeather() {
        _state.update {
            it.copy(isLoading = true, error = null)
        }

        getCurrentWeatherUseCase.execute(object : GetCurrentWeatherUseCase.Callback {
            override fun onResult(result: Result<Weather>) {
                result.fold(
                    onSuccess = {weather ->
                        Log.d("CurrentWeatherViewModel", "onResult: $weather")
                        _state.update {
                            it.copy(weather = weather, isLoading = false)
                        }
                    },
                    onFailure = {error ->
                        when(error){
                            is LocationPermissionNotGranted -> {
                                _state.update {
                                    it.copy(locationPermissionRequired = true)
                                }
                            }
                            is LocationServiceNotEnabled -> {
                                _state.update {
                                    it.copy(locationServiceEnabledRequired = true)
                                }
                            }
                            is NoInternetConnectionAndNoCachedDataAvailable -> {
                                _state.update {
                                    it.copy(error = error.message, isLoading = false)
                                }
                            }
                            is ServerIsBusyException -> {
                                _state.update {
                                    it.copy(error = error.message, isLoading = false)
                                }
                            }
                            else -> {
                                _state.update {
                                    it.copy(error = error.message, isLoading = false)
                                }
                            }
                        }
                    }
                )
            }
        })
    }

    fun resetPermissionState() {
        _state.update {
            it.copy(locationPermissionRequired = false)
        }
    }

    fun resetLocationServiceEnabledState() {
        _state.update {
            it.copy(locationServiceEnabledRequired = false)
        }
    }

}

