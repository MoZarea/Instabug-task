package com.zarea.instabugtask.data.location

import com.zarea.instabugtask.domain.model.Location

interface LocationCallback {
    fun onLocationResult(location: Location?)
    fun onLocationError(error: Exception)
}