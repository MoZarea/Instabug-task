package com.zarea.instabugtask.data.repository

import android.text.format.DateUtils.isToday
import com.zarea.instabugtask.data.utils.getTodayDate
import com.zarea.instabugtask.data.mapper.ForecastMapper
import com.zarea.instabugtask.data.mapper.WeatherMapper
import com.zarea.instabugtask.data.source.local.SharedPreferencesDataSource
import com.zarea.instabugtask.data.source.remote.NetworkUtils
import com.zarea.instabugtask.data.source.remote.WeatherApi
import com.zarea.instabugtask.domain.exception.NoInternetConnectionAndNoCachedDataAvailable
import com.zarea.instabugtask.domain.model.Forecast
import com.zarea.instabugtask.domain.model.Location
import com.zarea.instabugtask.domain.model.Weather
import com.zarea.instabugtask.domain.repository.WeatherRepository
import java.time.LocalDate

class WeatherRepositoryImpl(
    private val weatherApi: WeatherApi,
    private val sharedPrefsDataSource: SharedPreferencesDataSource,
    private val networkUtils: NetworkUtils,
    private val weatherMapper: WeatherMapper,
    private val forecastMapper: ForecastMapper,
) : WeatherRepository {

    override fun getCurrentWeather(location: Location): Result<Weather> {
        if (networkUtils.isNetworkAvailable()) {
            return try {
                weatherApi.getCurrentWeather(location).let {
                    sharedPrefsDataSource.saveCurrentWeather(weatherMapper.fromDtoToEntity(it))
                    Result.success(weatherMapper.fromDtoToDomain(it))
                }
            }catch (e: Exception){
                Result.failure(e)
            }
        } else {
            val currentWeatherEntity = sharedPrefsDataSource.getCurrentWeather()
            val currentWeatherTimestamp = sharedPrefsDataSource.getCurrentWeatherTimestamp()

            if (currentWeatherEntity != null && isToday(currentWeatherTimestamp)) {
                return Result.success(weatherMapper.fromEntityToDomain(currentWeatherEntity))
            }

            val forecastEntity = sharedPrefsDataSource.getForecast()
            if (forecastEntity != null) {
                val todayForecast = forecastEntity.days.find { it.date == getTodayDate() }
                if (todayForecast != null) {
                    val weather = forecastMapper.fromDailyForecastToWeather(todayForecast, forecastEntity.location, forecastEntity.timezone)
                    return Result.success(weather)
                }
            }

            return Result.failure(NoInternetConnectionAndNoCachedDataAvailable())
        }
    }

    override fun getForecast(location: Location): Result<Forecast> {
        if (networkUtils.isNetworkAvailable()) {
            return try {
                val forecastResponse = weatherApi.getForecast(location)
                sharedPrefsDataSource.saveForecast(forecastMapper.fromDtoToEntity(forecastResponse))
                Result.success(forecastMapper.fromDtoToDomain(forecastResponse))
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val cachedForecast = sharedPrefsDataSource.getForecast()
            val currentForecastTimestamp = sharedPrefsDataSource.getForecastTimestamp()
            val now = System.currentTimeMillis()
            val fiveDaysInMillis = 5 * 24 * 60 * 60 * 1000L // 5 days in milliseconds

            if (cachedForecast != null && (now - currentForecastTimestamp) <= fiveDaysInMillis) {

                val currentDate = LocalDate.now()

                val filteredDays = cachedForecast.days.filter { day ->
                    try {
                        val forecastDate = LocalDate.parse(day.date)
                        !forecastDate.isBefore(currentDate) &&
                                forecastDate.isBefore(currentDate.plusDays(6))
                    } catch (e: Exception) {
                        false
                    }
                }

                if (filteredDays.isNotEmpty()) {
                    val validForecast = cachedForecast.copy(days = filteredDays)
                    return Result.success(forecastMapper.fromEntityToDomain(validForecast))
                }
            }

            return Result.failure(NoInternetConnectionAndNoCachedDataAvailable())
        }
    }


    override fun cacheCurrentWeather(weather: Weather) {
        val weatherEntity = weatherMapper.fromDomainToEntity(weather)
        sharedPrefsDataSource.saveCurrentWeather(weatherEntity)
    }

    override fun cacheForecast(forecast: Forecast) {
        val forecastEntity = forecastMapper.fromDomainToEntity(forecast)
        sharedPrefsDataSource.saveForecast(forecastEntity)
    }

    override fun getCachedCurrentWeather(): Weather? {
        val weatherEntity = sharedPrefsDataSource.getCurrentWeather() ?: return null
        return weatherMapper.fromEntityToDomain(weatherEntity)
    }

    override fun getCachedForecast(): Forecast? {
        val forecastEntity = sharedPrefsDataSource.getForecast() ?: return null
        return forecastMapper.fromEntityToDomain(forecastEntity)
    }

    override fun isNetworkAvailable(): Boolean {
        return networkUtils.isNetworkAvailable()
    }
}