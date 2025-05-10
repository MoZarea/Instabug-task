package com.zarea.instabugtask.data.dto

import org.json.JSONObject

data class WeatherResponse(
    val queryCost: Int,
    val latitude: Double,
    val longitude: Double,
    val resolvedAddress: String,
    val address: String,
    val timezone: String,
    val tzoffset: Double,
    val days: List<DayResponse>
) {
    companion object {
        fun fromJson(json: String): WeatherResponse {
            val jsonObject = JSONObject(json)
            
            val days = mutableListOf<DayResponse>()
            val daysArray = jsonObject.getJSONArray("days")
            
            // Get the first day for current weather
            if (daysArray.length() > 0) {
                val dayObject = daysArray.getJSONObject(0)
                val hours = mutableListOf<HourResponse>()
                val hoursArray = dayObject.getJSONArray("hours")
                
                for (i in 0 until hoursArray.length()) {
                    val hourObject = hoursArray.getJSONObject(i)
                    hours.add(HourResponse(
                        datetime = hourObject.getString("datetime"),
                        temp = hourObject.getDouble("temp"),
                        humidity = hourObject.getDouble("humidity"),
                        windspeed = hourObject.getDouble("windspeed"),
                        pressure = hourObject.getDouble("pressure"),
                        visibility = hourObject.getDouble("visibility")
                    ))
                }
                
                days.add(DayResponse(
                    datetime = dayObject.getString("datetime"),
                    tempmax = dayObject.getDouble("tempmax"),
                    tempmin = dayObject.getDouble("tempmin"),
                    temp = dayObject.getDouble("temp"),
                    humidity = dayObject.getDouble("humidity"),
                    windspeed = dayObject.getDouble("windspeed"),
                    pressure = dayObject.getDouble("pressure"),
                    visibility = dayObject.getDouble("visibility"),
                    hours = hours
                ))
            }
            
            return WeatherResponse(
                queryCost = jsonObject.getInt("queryCost"),
                latitude = jsonObject.getDouble("latitude"),
                longitude = jsonObject.getDouble("longitude"),
                resolvedAddress = jsonObject.getString("resolvedAddress"),
                address = jsonObject.getString("address"),
                timezone = jsonObject.getString("timezone"),
                tzoffset = jsonObject.getDouble("tzoffset"),
                days = days
            )
        }
    }
}
