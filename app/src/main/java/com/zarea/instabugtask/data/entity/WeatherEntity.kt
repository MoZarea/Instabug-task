package com.zarea.instabugtask.data.entity

import org.json.JSONObject

data class WeatherEntity(
    val location: String,
    val temperature: Double,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double,
    val date: String,
    val timezone: String
) {
    fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("location", location)
        jsonObject.put("temperature", temperature)
        jsonObject.put("tempMax", tempMax)
        jsonObject.put("tempMin", tempMin)
        jsonObject.put("humidity", humidity)
        jsonObject.put("windSpeed", windSpeed)
        jsonObject.put("pressure", pressure)
        jsonObject.put("visibility", visibility)
        jsonObject.put("date", date)
        jsonObject.put("timezone", timezone)
        return jsonObject.toString()
    }

    companion object {
        fun fromJson(json: String): WeatherEntity {
            val jsonObject = JSONObject(json)
            return WeatherEntity(
                location = jsonObject.getString("location"),
                temperature = jsonObject.getDouble("temperature"),
                tempMax = jsonObject.getDouble("tempMax"),
                tempMin = jsonObject.getDouble("tempMin"),
                humidity = jsonObject.getDouble("humidity"),
                windSpeed = jsonObject.getDouble("windSpeed"),
                pressure = jsonObject.getDouble("pressure"),
                visibility = jsonObject.getDouble("visibility"),
                date = jsonObject.getString("date"),
                timezone = jsonObject.getString("timezone")
            )
        }
    }
}