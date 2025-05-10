package com.zarea.instabugtask.data.mapper

import com.zarea.instabugtask.data.dto.WeatherResponse
import com.zarea.instabugtask.data.entity.WeatherEntity
import com.zarea.instabugtask.domain.model.Weather

class WeatherMapper {
    fun fromDtoToDomain(dto: WeatherResponse): Weather {
        val currentDay = dto.days.firstOrNull() ?: throw IllegalStateException("No weather data available")
        
        return Weather(
            location = dto.resolvedAddress,
            temperature = currentDay.temp,
            tempMax = currentDay.tempmax,
            tempMin = currentDay.tempmin,
            humidity = currentDay.humidity,
            windSpeed = currentDay.windspeed,
            pressure = currentDay.pressure,
            visibility = currentDay.visibility,
            date = currentDay.datetime,
            timezone = dto.timezone
        )
    }
    fun fromDtoToEntity(dto: WeatherResponse): WeatherEntity {
        val currentDay = dto.days.firstOrNull() ?: throw IllegalStateException("No weather data available")

        return WeatherEntity(
            location = dto.resolvedAddress,
            temperature = currentDay.temp,
            tempMax = currentDay.tempmax,
            tempMin = currentDay.tempmin,
            humidity = currentDay.humidity,
            windSpeed = currentDay.windspeed,
            pressure = currentDay.pressure,
            visibility = currentDay.visibility,
            date = currentDay.datetime,
            timezone = dto.timezone
        )
    }
    
    fun fromDomainToEntity(domain: Weather): WeatherEntity {
        return WeatherEntity(
            location = domain.location,
            temperature = domain.temperature,
            tempMax = domain.tempMax,
            tempMin = domain.tempMin,
            humidity = domain.humidity,
            windSpeed = domain.windSpeed,
            pressure = domain.pressure,
            visibility = domain.visibility,
            date = domain.date,
            timezone = domain.timezone
        )
    }
    
    fun fromEntityToDomain(entity: WeatherEntity): Weather {
        return Weather(
            location = entity.location,
            temperature = entity.temperature,
            tempMax = entity.tempMax,
            tempMin = entity.tempMin,
            humidity = entity.humidity,
            windSpeed = entity.windSpeed,
            pressure = entity.pressure,
            visibility = entity.visibility,
            date = entity.date,
            timezone = entity.timezone
        )
    }
}