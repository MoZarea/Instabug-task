package com.zarea.instabugtask.ui.currentweather

import com.zarea.instabugtask.domain.model.Weather

data class CurrentWeatherUiState(
    val weather: Weather? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val locationPermissionRequired: Boolean = false,
    val locationServiceEnabledRequired: Boolean = false
)