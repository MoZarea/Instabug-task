package com.zarea.instabugtask.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zarea.instabugtask.ui.currentweather.CurrentWeatherScreen
import com.zarea.instabugtask.ui.forecast.ForecastWeatherScreen


@Composable
fun NavigationHost(
    onAskForLocationPermission: () -> Unit,
    onAskForEnableLocation: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.CurrentWeather.route) {
        composable(route = Screen.CurrentWeather.route) {
            CurrentWeatherScreen(
                onNavigateToForecast = { navController.navigate(Screen.ForecastWeather.route) },
                onRequestLocationPermission = onAskForLocationPermission,
                onRequestEnableLocation = onAskForEnableLocation
            )
        }
        composable(
            route = Screen.ForecastWeather.route
        ) {
            ForecastWeatherScreen(
                onNavigateToCurrentWeather = { navController.navigate(Screen.CurrentWeather.route) },
                onRequestLocationPermission = onAskForLocationPermission,
                onRequestEnableLocation = onAskForEnableLocation
            )
        }
    }
}