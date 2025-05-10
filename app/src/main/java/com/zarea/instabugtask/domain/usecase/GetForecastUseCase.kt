package com.zarea.instabugtask.domain.usecase


import com.zarea.instabugtask.domain.model.Forecast
import com.zarea.instabugtask.domain.model.Location
import com.zarea.instabugtask.domain.repository.LocationRepository
import com.zarea.instabugtask.domain.repository.WeatherRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class GetForecastUseCase(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    
    interface Callback {
        fun onResult(result: Result<Forecast>)
    }
    
    fun execute( callback: Callback) {
        executor.execute {
            try {
                executor.execute {
                    locationRepository.fetchLocation(
                        onSuccess = { location ->
                            weatherRepository.getForecast(location).fold(
                                onSuccess = { forecast ->
                                    callback.onResult(Result.success(forecast))
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
            }catch (e: Exception) {
                callback.onResult(Result.failure(e))
            }
        }
    }
}