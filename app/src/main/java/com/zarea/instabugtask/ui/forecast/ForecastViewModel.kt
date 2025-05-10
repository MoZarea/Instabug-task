package com.zarea.instabugtask.ui.forecast

import android.util.Log
import androidx.lifecycle.ViewModel
import com.zarea.instabugtask.domain.exception.LocationPermissionNotGranted
import com.zarea.instabugtask.domain.exception.LocationServiceNotEnabled
import com.zarea.instabugtask.domain.exception.NoInternetConnectionAndNoCachedDataAvailable
import com.zarea.instabugtask.domain.exception.ServerIsBusyException
import com.zarea.instabugtask.domain.model.Forecast
import com.zarea.instabugtask.domain.usecase.GetForecastUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ForecastViewModel(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ForecastState())
    val state = _state.asStateFlow()

    init {
        loadWeather()
    }

    fun loadWeather() {
        _state.update {
            it.copy(isLoading = true, error = null)
        }

        getForecastUseCase.execute(object : GetForecastUseCase.Callback {
            override fun onResult(result: Result<Forecast>) {
                result.fold(
                    onSuccess = { forecast ->
                        _state.update {
                            it.copy(forecast = forecast, isLoading = false)
                        }
                    },
                    onFailure = { error ->
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


