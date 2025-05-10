package com.zarea.instabugtask.domain.usecase

import com.zarea.instabugtask.domain.model.Weather
import com.zarea.instabugtask.domain.repository.LocationRepository
import com.zarea.instabugtask.domain.repository.WeatherRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GetCurrentWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) {
    private val executor: Executor = Executors.newSingleThreadExecutor()

    interface Callback {
        fun onResult(result: Result<Weather>)
    }

    fun execute(callback: Callback) {
        try {
            executor.execute {
                locationRepository.fetchLocation(
                    onSuccess = { location ->
                        weatherRepository.getCurrentWeather(location).fold(
                            onSuccess = { weather ->
                                callback.onResult(Result.success(weather))
                            },
                            onFailure = {
                                callback.onResult(Result.failure(it))
                            }
                        )
                        println("Location fetched successfully: $location")
                    },
                    onError = {
                        callback.onResult(Result.failure(it))
                    }
                )
            }
        } catch (e: Exception) {
            callback.onResult(Result.failure(e))
        }
    }
}
