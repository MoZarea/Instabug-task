package com.zarea.instabugtask.data.mapper

import com.zarea.instabugtask.data.entity.LocationEntity
import com.zarea.instabugtask.domain.model.Location

class LocationMapper {
    fun fromEntityToDomain(entity: LocationEntity): Location {
        return Location(
            latitude = entity.latitude,
            longitude = entity.longitude
        )
    }
    
    fun fromDomainToEntity(domain: Location): LocationEntity {
        return LocationEntity(
            latitude = domain.latitude,
            longitude = domain.longitude
        )
    }
}