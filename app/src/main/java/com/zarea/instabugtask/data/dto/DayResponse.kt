package com.zarea.instabugtask.data.dto

data class DayResponse(
    val datetime: String,
    val tempmax: Double,
    val tempmin: Double,
    val temp: Double,
    val humidity: Double,
    val windspeed: Double,
    val pressure: Double,
    val visibility: Double,
    val hours: List<HourResponse>
)

data class HourResponse(
    val datetime: String,
    val temp: Double,
    val humidity: Double,
    val windspeed: Double,
    val pressure: Double,
    val visibility: Double
)