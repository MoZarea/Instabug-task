package com.zarea.instabugtask.data.location

interface LocationProvider {
    fun getLastKnownLocation(callback: LocationCallback)
    fun isLocationServiceEnabled(): Boolean
}

