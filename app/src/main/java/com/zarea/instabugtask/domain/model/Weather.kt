package com.zarea.instabugtask.domain.model

data class Weather(
    val location: String,
    val temperature: Double,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double,
    val date: String,
    val timezone: String
)