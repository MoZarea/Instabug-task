package com.zarea.instabugtask.data.source.remote

import com.zarea.instabugtask.data.dto.ForecastResponse
import com.zarea.instabugtask.data.dto.WeatherResponse
import com.zarea.instabugtask.domain.exception.ServerIsBusyException
import com.zarea.instabugtask.domain.model.Location
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherApi {
    private val BASE_URL =
        "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"
    private val API_KEY = "NNWEBVJWPRTVJK8SZWR6LNVWT"

    fun getCurrentWeather(location: Location): WeatherResponse {
        val url =
            "$BASE_URL/${location.latitude},${location.longitude}/today?unitGroup=metric&include=hours&key=$API_KEY&contentType=json&elements=tempmax,tempmin,temp,windspeed,visibility,pressure,humidity,datetime"
        try {
            val responseJson = makeApiCall(url)
            return WeatherResponse.fromJson(responseJson)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getForecast(location: Location): ForecastResponse {
        val url =
            "$BASE_URL/${location.latitude},${location.longitude}/next5days?unitGroup=metric&include=hours&key=$API_KEY&contentType=json&elements=tempmax,tempmin,temp,windspeed,visibility,pressure,humidity,datetime"
        try {
            val responseJson = makeApiCall(url)
            return ForecastResponse.fromJson(responseJson)
        } catch (e: Exception) {
            throw e
        }

    }

    private fun makeApiCall(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"

        try {
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                return response.toString()
            } else {
                throw ServerIsBusyException()
            }
        } catch (e: Exception) {
            throw ServerIsBusyException()
        } finally {
            connection.disconnect()
        }
    }
}