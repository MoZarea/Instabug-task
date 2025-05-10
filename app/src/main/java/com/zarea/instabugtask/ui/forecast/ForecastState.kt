package com.zarea.instabugtask.ui.forecast

import com.zarea.instabugtask.domain.model.Forecast

data class ForecastState (
    val forecast: Forecast? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val locationPermissionRequired: Boolean = false,
    val locationServiceEnabledRequired: Boolean = false
)
