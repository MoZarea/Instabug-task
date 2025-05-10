package com.zarea.instabugtask.data.location

import android.annotation.SuppressLint
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import com.zarea.instabugtask.domain.exception.LocationPermissionNotGranted
import com.zarea.instabugtask.domain.exception.LocationProviderDisabled
import com.zarea.instabugtask.domain.exception.LocationServiceNotEnabled
import com.zarea.instabugtask.domain.exception.NoLocationProvidersAvailable
import com.zarea.instabugtask.domain.model.Location

class LocationProviderImpl(private val locationManager: LocationManager) : LocationProvider {

    private var locationListener: LocationListener? = null

    private val NETWORK_PROVIDER = LocationManager.NETWORK_PROVIDER
    private val GPS_PROVIDER = LocationManager.GPS_PROVIDER



    override fun getLastKnownLocation(callback: LocationCallback) {

        if (!isLocationServiceEnabled()) {
            callback.onLocationError(LocationServiceNotEnabled())
            return
        }

        val networkLocation = getLocationFromProvider(NETWORK_PROVIDER)
        if (networkLocation != null) {
            callback.onLocationResult(networkLocation)
            return
        }

        val gpsLocation = getLocationFromProvider(GPS_PROVIDER)
        if (gpsLocation != null) {
            callback.onLocationResult(gpsLocation)
            return
        }

        requestLocationUpdates(callback)
    }

    override fun isLocationServiceEnabled(): Boolean {
        return try {
            locationManager.isProviderEnabled(NETWORK_PROVIDER) ||
                    locationManager.isProviderEnabled(GPS_PROVIDER)
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationFromProvider(provider: String): Location? {
        return try {
            if (locationManager.isProviderEnabled(provider)) {
                locationManager.getLastKnownLocation(provider)?.let { location ->
                    Location(location.latitude, location.longitude)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(callback: LocationCallback) {
        locationListener = object : LocationListener {

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {
                if (provider == NETWORK_PROVIDER) {
                    tryGpsProvider(callback)
                } else {
                    callback.onLocationError(LocationProviderDisabled())
                }
            }

            override fun onLocationChanged(location: android.location.Location) {
                callback.onLocationResult(location.let{Location(it.latitude, it.longitude)})
                removeLocationUpdates()
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
        }

        tryNetworkProvider(callback)
    }

    @SuppressLint("MissingPermission")
    private fun tryNetworkProvider(callback: LocationCallback) {
        try {
            if (locationManager.isProviderEnabled(NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    NETWORK_PROVIDER,
                    0,
                    0f,
                    locationListener!!,
                    Looper.getMainLooper()
                )
            } else {
                tryGpsProvider(callback)
            }
        } catch (e: Exception) {
            tryGpsProvider(callback)
        }
    }

    @SuppressLint("MissingPermission")
    private fun tryGpsProvider(callback: LocationCallback) {
        try {
            if (locationManager.isProviderEnabled(GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    GPS_PROVIDER,
                    0,
                    0f,
                    locationListener!!,
                    Looper.getMainLooper()
                )
            } else {
                callback.onLocationError(NoLocationProvidersAvailable())
                removeLocationUpdates()
            }
        } catch (e: Exception) {
            callback.onLocationError(LocationPermissionNotGranted())
            removeLocationUpdates()
        }
    }

    private fun removeLocationUpdates() {
        locationListener?.let {
            try {
                locationManager.removeUpdates(it)
            } catch (e: Exception) {
                Log.e("LocationProvider", "Error removing location updates", e)
            }
            locationListener = null
        }
    }
}