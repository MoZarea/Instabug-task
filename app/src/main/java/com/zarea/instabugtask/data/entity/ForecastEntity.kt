package com.zarea.instabugtask.data.entity

import org.json.JSONArray
import org.json.JSONObject

data class ForecastEntity(
    val location: String,
    val timezone: String,
    val days: List<DayForecastEntity>
) {
    fun toJson(): String {
        val jsonObject = JSONObject()
        jsonObject.put("location", location)
        jsonObject.put("timezone", timezone)
        
        val daysArray = JSONArray()
        days.forEach { day ->
            val dayObject = JSONObject()
            dayObject.put("date", day.date)
            dayObject.put("tempMax", day.tempMax)
            dayObject.put("tempMin", day.tempMin)
            dayObject.put("avgTemp", day.avgTemp)
            dayObject.put("humidity", day.humidity)
            dayObject.put("windSpeed", day.windSpeed)
            dayObject.put("pressure", day.pressure)
            dayObject.put("visibility", day.visibility)
            
            val hoursArray = JSONArray()
            day.hours.forEach { hour ->
                val hourObject = JSONObject()
                hourObject.put("time", hour.time)
                hourObject.put("temperature", hour.temperature)
                hourObject.put("humidity", hour.humidity)
                hourObject.put("windSpeed", hour.windSpeed)
                hourObject.put("pressure", hour.pressure)
                hourObject.put("visibility", hour.visibility)
                hoursArray.put(hourObject)
            }
            
            dayObject.put("hours", hoursArray)
            daysArray.put(dayObject)
        }
        
        jsonObject.put("days", daysArray)
        return jsonObject.toString()
    }

    companion object {
        fun fromJson(json: String): ForecastEntity {
            val jsonObject = JSONObject(json)
            val days = mutableListOf<DayForecastEntity>()
            
            val daysArray = jsonObject.getJSONArray("days")
            for (i in 0 until daysArray.length()) {
                val dayObject = daysArray.getJSONObject(i)
                val hours = mutableListOf<HourForecastEntity>()
                
                val hoursArray = dayObject.getJSONArray("hours")
                for (j in 0 until hoursArray.length()) {
                    val hourObject = hoursArray.getJSONObject(j)
                    hours.add(
                        HourForecastEntity(
                            time = hourObject.getString("time"),
                            temperature = hourObject.getDouble("temperature"),
                            humidity = hourObject.getDouble("humidity"),
                            windSpeed = hourObject.getDouble("windSpeed"),
                            pressure = hourObject.getDouble("pressure"),
                            visibility = hourObject.getDouble("visibility")
                        )
                    )
                }
                
                days.add(
                    DayForecastEntity(
                        date = dayObject.getString("date"),
                        tempMax = dayObject.getDouble("tempMax"),
                        tempMin = dayObject.getDouble("tempMin"),
                        avgTemp = dayObject.getDouble("avgTemp"),
                        humidity = dayObject.getDouble("humidity"),
                        windSpeed = dayObject.getDouble("windSpeed"),
                        pressure = dayObject.getDouble("pressure"),
                        visibility = dayObject.getDouble("visibility"),
                        hours = hours
                    )
                )
            }
            
            return ForecastEntity(
                location = jsonObject.getString("location"),
                timezone = jsonObject.getString("timezone"),
                days = days
            )
        }
    }
}
data class DayForecastEntity(
    val date: String,
    val tempMax: Double,
    val tempMin: Double,
    val avgTemp: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double,
    val hours: List<HourForecastEntity>
)

data class HourForecastEntity(
    val time: String,
    val temperature: Double,
    val humidity: Double,
    val windSpeed: Double,
    val pressure: Double,
    val visibility: Double
)