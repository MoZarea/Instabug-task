package com.zarea.instabugtask.ui.navigation

sealed class Screen(val route: String) {
    data object CurrentWeather: Screen("current_weather_screen")
    data object ForecastWeather: Screen("forecast_weather_screen")
}