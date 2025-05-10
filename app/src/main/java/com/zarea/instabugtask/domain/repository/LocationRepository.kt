package com.zarea.instabugtask.domain.repository

import com.zarea.instabugtask.domain.model.Location


interface LocationRepository {
    fun fetchLocation(
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit
    )
    fun cacheLocation(location: Location)
}
