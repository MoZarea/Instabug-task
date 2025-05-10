package com.zarea.instabugtask.domain.repository

import com.zarea.instabugtask.domain.model.Forecast
import com.zarea.instabugtask.domain.model.Location
import com.zarea.instabugtask.domain.model.Weather

interface WeatherRepository {
    fun getCurrentWeather(location: Location): Result<Weather>

    fun getForecast(location: Location): Result<Forecast>

    fun cacheCurrentWeather(weather: Weather)

    fun cacheForecast(forecast: Forecast)

    fun getCachedCurrentWeather(): Weather?

    fun getCachedForecast(): Forecast?

    fun isNetworkAvailable(): Boolean
}