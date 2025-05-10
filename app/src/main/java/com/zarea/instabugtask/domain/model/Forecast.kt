package com.zarea.instabugtask.domain.model

data class Forecast(
    val location: String,
    val timezone: String,
    val days: List<DayForecast>
)
data class DayForecast(
    val date: String,
    val tempMax: Double,
    val tempMin: Double,
    val avgTemp: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double,
    val hours: List<HourForecast>
)
data class HourForecast(
    val time: String,
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double
)