package com.zarea.instabugtask.data.entity

import org.json.JSONObject

data class LocationEntity(
    val latitude: Double,
    val longitude: Double
) {
    fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("latitude", latitude)
        jsonObject.put("longitude", longitude)
        return jsonObject.toString()
    }

    companion object {
        fun fromJson(json: String): LocationEntity {
            val jsonObject = JSONObject(json)
            return LocationEntity(
                latitude = jsonObject.getDouble("latitude"),
                longitude = jsonObject.getDouble("longitude")
            )
        }
    }
}