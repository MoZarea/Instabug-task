package com.zarea.instabugtask.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zarea.instabugtask.domain.exception.*
import com.zarea.instabugtask.domain.model.Forecast
import com.zarea.instabugtask.domain.usecase.GetForecastUseCase

class ForecastViewModel(
    private val getForecastUseCase: GetForecastUseCase
) : ViewModel() {

    private val _state = MutableLiveData(ForecastState())
    val state: LiveData<ForecastState> = _state

    init {
        loadWeather()
    }

    fun loadWeather() {
        _state.value = _state.value?.copy(isLoading = true, error = null)

        getForecastUseCase.execute(object : GetForecastUseCase.Callback {
            override fun onResult(result: Result<Forecast>) {
                result.fold(
                    onSuccess = { forecast ->
                        _state.postValue(_state.value?.copy(forecast = forecast, isLoading = false))
                    },
                    onFailure = { error ->
                        when (error) {
                            is LocationPermissionNotGranted -> {
                                _state.postValue(_state.value?.copy(locationPermissionRequired = true, isLoading = false))
                            }
                            is LocationServiceNotEnabled -> {
                                _state.postValue(_state.value?.copy(locationServiceEnabledRequired = true, isLoading = false))
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
