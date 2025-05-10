package com.zarea.instabugtask.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.zarea.instabugtask.data.entity.ForecastEntity
import com.zarea.instabugtask.data.entity.LocationEntity
import com.zarea.instabugtask.data.entity.WeatherEntity

class SharedPreferencesDataSource(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    fun saveLocation(locationEntity: LocationEntity) {
        sharedPreferences.edit()
            .putString(KEY_LOCATION, locationEntity.toJson())
            .apply()
    }
    
    fun getLocation(): LocationEntity? {
        val locationJson = sharedPreferences.getString(KEY_LOCATION, null) ?: return null
        return LocationEntity.fromJson(locationJson)
    }
    
    fun saveCurrentWeather(weatherEntity: WeatherEntity) {
        sharedPreferences.edit()
            .putString(KEY_CURRENT_WEATHER, weatherEntity.toJson())
            .putLong(KEY_CURRENT_WEATHER_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }
    
    fun getCurrentWeather(): WeatherEntity? {
        val weatherJson = sharedPreferences.getString(KEY_CURRENT_WEATHER, null) ?: return null
        return WeatherEntity.fromJson(weatherJson)
    }
    
    fun getCurrentWeatherTimestamp(): Long {
        return sharedPreferences.getLong(KEY_CURRENT_WEATHER_TIMESTAMP, 0)
    }
    
    fun saveForecast(forecastEntity: ForecastEntity) {
        sharedPreferences.edit()
            .putString(KEY_FORECAST, forecastEntity.toJson())
            .putLong(KEY_FORECAST_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }
    
    fun getForecast(): ForecastEntity? {
        val forecastJson = sharedPreferences.getString(KEY_FORECAST, null) ?: return null
        return ForecastEntity.fromJson(forecastJson)
    }
    
    fun getForecastTimestamp(): Long {
        return sharedPreferences.getLong(KEY_FORECAST_TIMESTAMP, 0)
    }
    
    companion object {
        private const val PREFS_NAME = "weather_app_prefs"
        private const val KEY_LOCATION = "location"
        private const val KEY_CURRENT_WEATHER = "current_weather"
        private const val KEY_CURRENT_WEATHER_TIMESTAMP = "current_weather_timestamp"
        private const val KEY_FORECAST = "forecast"
        private const val KEY_FORECAST_TIMESTAMP = "forecast_timestamp"
    }
}