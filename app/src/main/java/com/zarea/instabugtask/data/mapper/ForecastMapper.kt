package com.zarea.instabugtask.data.mapper

import com.zarea.instabugtask.data.dto.ForecastResponse
import com.zarea.instabugtask.data.entity.DayForecastEntity
import com.zarea.instabugtask.data.entity.ForecastEntity
import com.zarea.instabugtask.data.entity.HourForecastEntity
import com.zarea.instabugtask.domain.model.DayForecast
import com.zarea.instabugtask.domain.model.Forecast
import com.zarea.instabugtask.domain.model.HourForecast
import com.zarea.instabugtask.domain.model.Weather

class ForecastMapper {
    fun fromDtoToDomain(dto: ForecastResponse): Forecast {
        val days = dto.days.map { dayDto ->
            DayForecast(
                date = dayDto.datetime,
                tempMax = dayDto.tempmax,
                tempMin = dayDto.tempmin,
                avgTemp = dayDto.temp,
                humidity = dayDto.humidity,
                windSpeed = dayDto.windspeed,
                pressure = dayDto.pressure,
                visibility = dayDto.visibility,
                hours = dayDto.hours.map { hourDto ->
                    HourForecast(
                        time = hourDto.datetime,
                        temperature = hourDto.temp,
                        humidity = hourDto.humidity,
                        windSpeed = hourDto.windspeed,
                        pressure = hourDto.pressure,
                        visibility = hourDto.visibility
                    )
                }
            )
        }
        
        return Forecast(
            location = dto.resolvedAddress,
            timezone = dto.timezone,
            days = days
        )
    }
    fun fromDtoToEntity(dto: ForecastResponse): ForecastEntity {
        val days = dto.days.map { dayDto ->
            DayForecastEntity(
                date = dayDto.datetime,
                tempMax = dayDto.tempmax,
                tempMin = dayDto.tempmin,
                avgTemp = dayDto.temp,
                humidity = dayDto.humidity,
                windSpeed = dayDto.windspeed,
                pressure = dayDto.pressure,
                visibility = dayDto.visibility,
                hours = dayDto.hours.map { hourDto ->
                    HourForecastEntity(
                        time = hourDto.datetime,
                        temperature = hourDto.temp,
                        humidity = hourDto.humidity,
                        windSpeed = hourDto.windspeed,
                        pressure = hourDto.pressure,
                        visibility = hourDto.visibility
                    )
                }
            )
        }

        return ForecastEntity(
            location = dto.resolvedAddress,
            timezone = dto.timezone,
            days = days
        )
    }
    
    fun fromDomainToEntity(domain: Forecast): ForecastEntity {
        val days = domain.days.map { day ->
            DayForecastEntity(
                date = day.date,
                tempMax = day.tempMax,
                tempMin = day.tempMin,
                avgTemp = day.avgTemp,
                humidity = day.humidity,
                windSpeed = day.windSpeed,
                pressure = day.pressure,
                visibility = day.visibility,
                hours = day.hours.map { hour ->
                    HourForecastEntity(
                        time = hour.time,
                        temperature = hour.temperature,
                        humidity = hour.humidity,
                        windSpeed = hour.windSpeed,
                        pressure = hour.pressure,
                        visibility = hour.visibility
                    )
                }
            )
        }
        
        return ForecastEntity(
            location = domain.location,
            timezone = domain.timezone,
            days = days
        )
    }
    
    fun fromEntityToDomain(entity: ForecastEntity): Forecast {
        val days = entity.days.map { day ->
            DayForecast(
                date = day.date,
                tempMax = day.tempMax,
                tempMin = day.tempMin,
                avgTemp = day.avgTemp,
                humidity = day.humidity,
                windSpeed = day.windSpeed,
                pressure = day.pressure,
                visibility = day.visibility,
                hours = day.hours.map { hour ->
                    HourForecast(
                        time = hour.time,
                        temperature = hour.temperature,
                        humidity = hour.humidity,
                        windSpeed = hour.windSpeed,
                        pressure = hour.pressure,
                        visibility = hour.visibility
                    )
                }
            )
        }

        
        return Forecast(
            location = entity.location,
            timezone = entity.timezone,
            days = days
        )
    }
    fun fromDailyForecastToWeather(
        dayForecast: DayForecastEntity,
        location: String,
        timezone: String
    ): Weather {
        return Weather(
            location = location,
            temperature = dayForecast.avgTemp,
            tempMax = dayForecast.tempMax,
            tempMin = dayForecast.tempMin,
            humidity = dayForecast.humidity,
            windSpeed = dayForecast.windSpeed,
            pressure = dayForecast.pressure,
            visibility = dayForecast.visibility,
            date = dayForecast.date,
            timezone = timezone
        )
    }
}