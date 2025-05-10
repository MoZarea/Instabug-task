package com.zarea.instabugtask.data.repository

import com.zarea.instabugtask.data.location.LocationCallback
import com.zarea.instabugtask.data.location.LocationProvider
import com.zarea.instabugtask.data.mapper.LocationMapper
import com.zarea.instabugtask.data.source.local.SharedPreferencesDataSource
import com.zarea.instabugtask.domain.exception.LocationPermissionNotGranted
import com.zarea.instabugtask.domain.model.Location
import com.zarea.instabugtask.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val locationMapper: LocationMapper,
    private val sharedPrefsDataSource: SharedPreferencesDataSource,
    private val locationProvider: LocationProvider
) : LocationRepository {

    override fun fetchLocation(
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit
    ) {
        fetchUpdatedLocation(
            onSuccess = { location ->
                cacheLocation(location)
                onSuccess(location)
            },
            onError = {
                sharedPrefsDataSource.getLocation()?.let {
                    onSuccess(locationMapper.fromEntityToDomain(it))
                } ?: onError(it)
            }
        )
    }

    private fun fetchUpdatedLocation(
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit
    ) {
        locationProvider.getLastKnownLocation(object : LocationCallback {
            override fun onLocationResult(location: Location?) {
                if (location != null) {
                    onSuccess(Location(location.latitude, location.longitude))
                } else {
                    onError(LocationPermissionNotGranted())
                }
            }

            override fun onLocationError(error: Exception) {
                onError(error)
            }
        })
    }

    override fun cacheLocation(location: Location) {
        sharedPrefsDataSource.saveLocation(locationMapper.fromDomainToEntity(location))
    }
}